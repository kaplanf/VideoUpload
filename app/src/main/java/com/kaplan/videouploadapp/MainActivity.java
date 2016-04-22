package com.kaplan.videouploadapp;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaplan.videouploadapp.activity.BaseActivity;
import com.kaplan.videouploadapp.fragment.CameraFragment;
import com.kaplan.videouploadapp.fragment.CameraFragment_;
import com.kaplan.videouploadapp.fragment.ThumbNailPreviewFragment;
import com.kaplan.videouploadapp.fragment.ThumbNailPreviewFragment_;
import com.kaplan.videouploadapp.interfaces.OnMainFragmentListener;
import com.kaplan.videouploadapp.restful.RestClient;
import com.kaplan.videouploadapp.util.NetworkConstants;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements OnMainFragmentListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @RestService
    RestClient client;

    @AfterViews
    protected void afterViews() {
        toCameraFragment();
//        volleyGetRequest();
//        asyncAndroidRequest();
    }

    public void toCameraFragment() {
        CameraFragment cameraFragment = new CameraFragment_();
        replaceFragment(R.id.content_frame, cameraFragment, false);
    }

    public void toThumbnailFragment(String path) {
        ThumbNailPreviewFragment thumbNailPreviewFragment = new ThumbNailPreviewFragment_().builder().path(path).build();
        replaceFragment(R.id.content_frame, thumbNailPreviewFragment, true);
    }

    @Override
    public void onCloseFragment(String tag) {

    }

    @Override
    public void onStartFragment(String tag) {

    }

    @Override
    public void sendDataFromFragment(Bundle bundle) {

    }

    @Override
    public void backClicked(String fragmentName) {

    }

    @Background
    void volleyGetRequest() {
        JsonObjectRequest volleyTestRequest = new JsonObjectRequest(Request.Method.GET,
                NetworkConstants.BASE_URL + NetworkConstants.GET_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + "7fc0cf8dadac8586f327027ee22805178f369cb7");
                return headers;
            }
        };

        VideoUploadApplication.getInstance().addToRequestQueue(volleyTestRequest, TAG);
    }


    @Background
    void asyncAndroidRequest() {
        AsyncHttpGet asyncHttpGet = new AsyncHttpGet(NetworkConstants.BASE_URL + NetworkConstants.GET_URL);
        asyncHttpGet.addHeader("Authorization", "Bearer 7fc0cf8dadac8586f327027ee22805178f369cb7");
        AsyncHttpClient.getDefaultInstance().executeJSONObject(asyncHttpGet, new AsyncHttpClient.JSONObjectCallback() {
            // Callback is invoked with any exceptions/errors, and the result, if available.
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, JSONObject result) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                System.out.println("I got a JSONObject: " + result);
            }
        });
    }
}
