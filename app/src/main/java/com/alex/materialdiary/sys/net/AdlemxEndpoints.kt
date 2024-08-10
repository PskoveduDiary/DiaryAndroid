package com.alex.materialdiary.sys.net

import com.alex.materialdiary.sys.net.models.ClassicBody
import com.alex.materialdiary.sys.net.models.pda.PDAnswer
import com.alex.materialdiary.sys.net.models.pda.PDBody
import com.alex.materialdiary.sys.net.models.get_user.UserInfoRequest
import com.alex.materialdiary.sys.net.models.all_periods.AllPeriods
import com.alex.materialdiary.sys.net.models.check_list.CheckList
import com.alex.materialdiary.sys.net.models.check_list.Lesson
import com.alex.materialdiary.sys.net.models.diary_day.DiaryDay
import com.alex.materialdiary.sys.net.models.get_user.UserInfo
import com.alex.materialdiary.sys.net.models.notes.Note
import com.alex.materialdiary.sys.net.models.notes.NoteRequest
import com.alex.materialdiary.sys.net.models.period_marks.PeriodMarks
import com.alex.materialdiary.sys.net.models.periods.Periods
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AdlemxEndpoints {
    @GET("checklist/{guid}")
    suspend fun get_checklist(@Path("guid") guid: String, @Query("date") date: String): CheckList

    @POST("checklist/{guid}")
    suspend fun set_checklist(@Path("guid") guid: String, @Query("date") date: String, @Body body: List<Lesson>): Response<JsonObject>

    @GET("/notes/{lesson_uuid}")
    suspend fun get_notes(@Header("DiaryId") diaryId: String, @Path("lesson_uuid") guid: String): List<Note>
    @PUT("/notes/{lesson_uuid}")
    suspend fun update_note(@Header("DiaryId") diaryId: String, @Path("lesson_uuid") guid: String, @Body body: NoteRequest): Note
    @POST("/notes/create")
    suspend fun create_note(@Header("DiaryId") diaryId: String, @Body body: NoteRequest): Note
}