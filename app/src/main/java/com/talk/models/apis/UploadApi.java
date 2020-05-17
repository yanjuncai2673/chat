package com.talk.models.apis;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApi {
    //上传文件  上传头像接口
    @POST("file_upload.php")
    @Multipart
    Call<ResponseBody>uploadFile(@Part("key") RequestBody key, @Part MultipartBody.Part file);

}
