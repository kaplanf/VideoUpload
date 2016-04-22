package com.kaplan.videouploadapp.restful;

import com.kaplan.videouploadapp.restful.model.VideoUploadResponse;
import com.kaplan.videouploadapp.util.NetworkConstants;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresAuthentication;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.MultiValueMap;


/**
 * Created by kaplanfatt on 08/09/15.
 */
@Rest(rootUrl = NetworkConstants.BASE_URL, converters = {
        GsonHttpMessageConverter.class, StringHttpMessageConverter.class}, interceptors = HeadersRequestInterceptor.class)
public interface RestClient extends RestClientHeaders {

    @RequiresAuthentication
    @Post(NetworkConstants.UPLOAD_URL)
    VideoUploadResponse uploadVideo(@Body MultiValueMap<String, Object> data);

    @RequiresAuthentication
    @Get(NetworkConstants.GET_URL)
    Object getVideo();

    @Override
    void setBearerAuth(String token);
//
//    @Override
//    void setHeader(String name, String value);

    //    Object uploadVideo(@Part("thumbnail") String thumbnail, @Part("media") File media);


}


