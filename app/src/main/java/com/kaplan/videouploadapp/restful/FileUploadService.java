package com.kaplan.videouploadapp.restful;

import com.kaplan.videouploadapp.restful.model.VideoUploadResponse;
import com.kaplan.videouploadapp.util.NetworkConstants;

import okhttp3.MultipartBody;
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
    Call<VideoUploadResponse> upload(
            @Part MultipartBody.Part file, @Part("thumbnail") String description);

}