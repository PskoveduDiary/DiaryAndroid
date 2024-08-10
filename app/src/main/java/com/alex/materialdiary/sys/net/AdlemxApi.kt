package com.alex.materialdiary.sys.net

import android.util.Log
import com.alex.materialdiary.MainActivity
import com.alex.materialdiary.sys.net.models.ClassicBody
import com.alex.materialdiary.sys.net.models.diary_day.DiaryDay
import com.alex.materialdiary.sys.net.models.notes.Note
import com.alex.materialdiary.sys.net.models.notes.NoteRequest
import com.google.firebase.crashlytics.FirebaseCrashlytics
import retrofit2.HttpException
import java.net.ConnectException

class AdlemxApi {
    private val endpoints: AdlemxEndpoints = AdlemxClient.getEndpoints()
    var guid = ""

    companion object {

        var i: AdlemxApi? = null

        fun getInstance(): AdlemxApi{
            if (i == null) return AdlemxApi()
            return i as AdlemxApi
        }
    }
    init {
        i = this
        guid = PskoveduApi.getInstance().guid
    }
    fun HttpError(){
        MainActivity.showSnack("Невозможно получить данные, попробуйте позже")
    }
    fun ConnectError(){
        MainActivity.showSnack("Невозможно получить данные, проверьте подключение к интернету")
    }

    fun UnknownError(){
        MainActivity.showSnack("Произошла ошибка, мы уже работаем над решением проблемы")
    }
    suspend fun createNote(lessonGuid: String, text: String, public: Boolean): Note? {
        if (guid === "") return null
        val body = NoteRequest(lessonGuid , public, text)
        try {
            val note = endpoints.create_note(guid, body)
            return note
        }
        catch (e: HttpException){
            e.printStackTrace()
            HttpError()
        }
        catch (e: ConnectException){
            ConnectError()
        }
        catch (e: Exception){
            e.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(e)
            UnknownError()
        }
        return null
    }
    suspend fun updateNote(lessonGuid: String, text: String, public: Boolean): Note? {
        if (guid === "") return null
        val body = NoteRequest(text, public)
        try {
            val note = endpoints.update_note(guid, lessonGuid, body)
            return note
        }
        catch (e: HttpException){
            e.printStackTrace()
            HttpError()
        }
        catch (e: ConnectException){
            ConnectError()
        }
        catch (e: Exception){
            e.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(e)
            UnknownError()
        }
        return null
    }
}