package com.alex.materialdiary.sys.common

import retrofit2.http.POST
import com.alex.materialdiary.sys.common.models.ClassicBody
import com.alex.materialdiary.sys.common.models.diary_day.DiaryDay
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarks
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods
import com.alex.materialdiary.sys.common.models.periods.Periods
import com.alex.materialdiary.sys.common.models.PDBody
import com.alex.materialdiary.sys.common.models.PDAnswer
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body

interface CommonInterface {
    @POST("journals/diaryday")
    fun getDiaryDay(@Body body: ClassicBody?): Call<DiaryDay?>?

    @POST("journals/allmarks")
    fun getAllMarks(@Body body: ClassicBody?): Call<PeriodMarks?>?

    @POST("journals/marksbyperiod")
    fun getPeriodMarks(@Body body: ClassicBody?): Call<PeriodMarks?>?

    @POST("journals/marksbyperiod")
    suspend fun getPeriodMarksCoroutine(@Body body: ClassicBody?): PeriodMarks?

    @POST("journals/allperiods")
    fun getAllPeriods(@Body body: ClassicBody?): Call<AllPeriods?>?

    @POST("journals/periodmarks")
    fun getPeriods(@Body body: ClassicBody?): Call<Periods?>?

    @POST("pda/setpdakey")
    fun setPda(@Body body: PDBody?): Call<PDAnswer?>?

    @POST("pda/getpdakey")
    fun getPda(@Body body: PDBody?): Call<PDAnswer?>?
}