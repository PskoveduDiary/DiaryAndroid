package com.alex.materialdiary.sys.net

import com.alex.materialdiary.MainActivity
import com.alex.materialdiary.sys.net.database.CacheRepository
import com.alex.materialdiary.sys.net.database.CachedData
import com.alex.materialdiary.sys.net.models.ClassicBody
import com.alex.materialdiary.sys.net.models.diary_day.DiaryDay
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.Period
import org.joda.time.format.ISODateTimeFormat.dateTime
import java.io.IOException
import java.util.Calendar


internal class CacheInterceptor(repository: CacheRepository?) : Interceptor {
    val repository = repository

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val buffer = Buffer()
        request.body()?.writeTo(buffer)
        val requestBody = buffer.readUtf8()
        if (repository == null) {
            println("repository is null")
            return chain.proceed(request)
        }
        val response = try {
            chain.proceed(request)
        } catch (_: IOException) {
            null
        }

        try {
            val data = Gson().fromJson(requestBody, ClassicBody::class.java)
            val responseBody = try {
                response?.peekBody(Long.MAX_VALUE)?.string();
            } catch (_: IOException) {
                null
            }

            if (response?.isSuccessful == true && responseBody != null) {

                with(request.url().toString()) {
                    when {
                        contains("diaryday") -> {
                            if (data.date == "") return@with
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val day = Gson().fromJson(responseBody, DiaryDay::class.java)
                                    if (day.success == true) {
                                        repository.insertNewCacheRecord(
                                            CachedData(
                                                id = 0,
                                                req_type = "day",
                                                optional = data.guid + "_" + data.date,
                                                actuality = Calendar.getInstance().time.time,
                                                response = responseBody
                                            )
                                        )
                                    }
                                }
                                catch (_: JsonSyntaxException){

                                }
                            }
                        }

                        contains("marksbyperiod") -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                repository.insertNewCacheRecord(
                                    CachedData(
                                        id = 0,
                                        req_type = "marksbyperiod",
                                        optional = data.guid + "_" + data.from + "_" + data.to,
                                        actuality = Calendar.getInstance().getTime().time,
                                        response = responseBody
                                    )
                                )
                            }
                        }

                        contains("periodmarks") -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                repository.insertNewCacheRecord(
                                    CachedData(
                                        id = 0,
                                        req_type = "periodmarks",
                                        optional = data.guid,
                                        actuality = Calendar.getInstance().getTime().time,
                                        response = responseBody
                                    )
                                )
                            }
                        }

                        else -> {}
                    }
                }
            } else {
                var cacheBody: ResponseBody? = null
                var timestamp: Long = System.currentTimeMillis()
                with(request.url().toString()) {
                    when {
                        contains("diaryday") -> {
                            if (data.guid == null || data.date == null) return@with
                            runBlocking(Dispatchers.IO) {
                                if (data.date != "") {
                                    val cacheResponseData =
                                        repository.getCacheByTypeAndOptional(
                                            "day",
                                            data.guid + "_" + data.date
                                        )
                                    if (cacheResponseData.isNotEmpty()) {
                                        timestamp = cacheResponseData.last().actuality
                                        cacheBody = ResponseBody.create(
                                            MediaType.get("application/json; charset=UTF-8"),
                                            cacheResponseData.last().response
                                        )
                                    }
                                }
                            }
                        }

                        contains("marksbyperiod") -> {
                            if (data.guid == null || data.from == null || data.to == null) return@with
                            runBlocking(Dispatchers.IO) {
                                val cacheResponseData =
                                    repository.getCacheByTypeAndOptional(
                                        "marksbyperiod",
                                        data.guid + "_" + data.from + "_" + data.to
                                    )
                                if (cacheResponseData.isNotEmpty()) {
                                    timestamp = cacheResponseData.last().actuality
                                    cacheBody = ResponseBody.create(
                                        MediaType.get("application/json; charset=UTF-8"),
                                        cacheResponseData.last().response
                                    )
                                }
                            }
                        }

                        contains("periodmarks") -> {
                            if (data.guid == null) return@with
                            runBlocking(Dispatchers.IO) {
                                val cacheResponseData =
                                    repository.getCacheByTypeAndOptional(
                                        "periodmarks",
                                        data.guid.toString()
                                    )
                                if (cacheResponseData.isNotEmpty()) {
                                    timestamp = cacheResponseData.last().actuality
                                    cacheBody = ResponseBody.create(
                                        MediaType.get("application/json; charset=UTF-8"),
                                        cacheResponseData.last().response
                                    )
                                }
                            }
                        }

                        else -> {}
                    }
                }
                if (cacheBody != null) {
                    val now = DateTime()
                    val period = Period(timestamp, now.millis)
                    var periodString = ""
                    if (period.days == 0) periodString = LocalTime(timestamp).toString("HH:mm")
                    else periodString = DateTime(timestamp).toString("EEEE, HH:mm")
                    MainActivity.showSnack("Нет доступа к серверу, последнее обновление данных в ${periodString}")
                    return Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(202)
                        .message("FromCache").body(cacheBody).build()
                }
            }
        } catch (_: JsonSyntaxException) {
        }
        if (response != null) return response
        return chain.proceed(request)
    }
}