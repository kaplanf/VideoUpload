package com.kaplan.videouploadapp.restful.model;

import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by Fatih Kaplan on 20/04/16.
 */
public class VideoUploadModel {

    @SerializedName("media")
    public File file;

    @SerializedName("thumbnail")
    public String baseString;


}
