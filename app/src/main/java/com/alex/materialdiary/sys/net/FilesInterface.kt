package com.alex.materialdiary.sys.net

import retrofit2.http.POST
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.HEAD
import retrofit2.http.Path

interface FilesInterface {
    @HEAD("file/download/{guid}")
    suspend fun getFile(@Path("guid") guid: String): Response<ResponseBody>
    @POST("file/download/{guid}")
    suspend fun getFileName(@Path("guid") guid: String): Response<ResponseBody>
}