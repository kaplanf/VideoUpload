package com.kaplan.videouploadapp.restful;

import com.kaplan.videouploadapp.util.NetworkConstants;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Fatih Kaplan on 20/04/16.
 */
public interface FileUploadService {
    @Multipart
    @POST(NetworkConstants.UPLOAD_URL)
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file, @Part("thumbnail") String description);

}