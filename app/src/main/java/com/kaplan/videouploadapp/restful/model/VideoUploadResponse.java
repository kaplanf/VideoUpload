package com.kaplan.videouploadapp.restful.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fatih Kaplan on 20/04/16.
 */
public class VideoUploadResponse {

    @SerializedName("id")
    public int id;

    @SerializedName("thumbnail")
    public String thumbnail;

    @SerializedName("media")
    public String media;
}
