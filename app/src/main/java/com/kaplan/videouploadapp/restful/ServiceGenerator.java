package com.kaplan.videouploadapp.restful;

import com.kaplan.videouploadapp.util.NetworkConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fatih Kaplan on 20/04/16.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = NetworkConstants.BASE_URL;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {


        final String basic =
                "Bearer 7fc0cf8dadac8586f327027ee22805178f369cb7";

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("authorization", "Bearer 7fc0cf8dadac8586f327027ee22805178f369cb7");
                requestBuilder.header("content-type", "multipart/form-data");
                requestBuilder.method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });


        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

}