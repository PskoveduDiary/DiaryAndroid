package com.alex.materialdiary.sys.common

import retrofit2.http.POST
import com.alex.materialdiary.sys.common.models.ClassicBody
import com.alex.materialdiary.sys.common.models.diary_day.DiaryDay
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarks
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods
import com.alex.materialdiary.sys.common.models.periods.Periods
import com.alex.materialdiary.sys.common.models.PDBody
import com.alex.materialdiary.sys.common.models.PDAnswer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path
import java.io.File

interface FilesInterface {
    @POST("file/download/{guid}")
    suspend fun getFile(@Path("guid") guid: String): Response<ResponseBody>
}