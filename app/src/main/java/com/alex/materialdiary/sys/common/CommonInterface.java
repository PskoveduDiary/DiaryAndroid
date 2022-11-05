package com.alex.materialdiary.sys.common;

import com.alex.materialdiary.sys.common.models.ClassicBody;
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods;
import com.alex.materialdiary.sys.common.models.diary_day.DiaryDay;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarks;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CommonInterface {
    @POST("journals/diaryday")
    Call<DiaryDay> getDiaryDay(@Body ClassicBody body);

    @POST("journals/allmarks")
    Call<PeriodMarks> getAllMarks(@Body ClassicBody body);

    @POST("journals/marksbyperiod")
    Call<PeriodMarks> getPeriodMarks(@Body ClassicBody body);

    @POST("journals/allperiods")
    Call<AllPeriods> getAllPeriods(@Body ClassicBody body);
}
