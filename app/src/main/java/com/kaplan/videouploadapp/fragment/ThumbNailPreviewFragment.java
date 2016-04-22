package com.kaplan.videouploadapp.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaplan.videouploadapp.R;
import com.kaplan.videouploadapp.VideoUploadApplication;
import com.kaplan.videouploadapp.interfaces.OnMainFragmentListener;
import com.kaplan.videouploadapp.restful.FileUploadService;
import com.kaplan.videouploadapp.restful.RestClient;
import com.kaplan.videouploadapp.restful.ServiceGenerator;
import com.kaplan.videouploadapp.restful.model.VideoUploadModel;
import com.kaplan.videouploadapp.util.NetworkConstants;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.callback.HttpConnectCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Fatih Kaplan on 20/04/16.
 */
@EFragment(R.layout.thumbnail_preview_fragment)
public class ThumbNailPreviewFragment extends BaseFragment {

    private final String TAG = getFramentTag();

    @RestService
    RestClient restClient;

    @FragmentArg
    String path;

    private OnMainFragmentListener onMainFragmentListener;

    @ViewById(R.id.thumbnailImageview)
    ImageView thumbnailImageview;

    private byte[] data;
    private String bitmapString;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onMainFragmentListener = (OnMainFragmentListener) activity;

    }

    @AfterViews
    protected void afterViews() {
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
        thumbnailImageview.setImageBitmap(thumbnail);
        bitmapString = getThumbnailString(thumbnail);
        try {
            data = readBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getThumbnailString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] photo = byteArrayOutputStream.toByteArray();

        String baseString = Base64.encodeToString(photo, Base64.NO_WRAP);
        return baseString;
    }

    public byte[] readBytes(String path) throws IOException {

        InputStream inputStream = new FileInputStream(path);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public InputStream getInput(String path) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return inputStream;

    }


    @Click
    void thumbnailImageview() {
        uploadVideo(bitmapString, data);
    }

    @Background
    void uploadVideo(String baseString, byte[] data) {

        retrofitPostRequest(path);

//        asyncAndroidPostRequest(null,null);


//        volleyPostRequest(null);


//        try {
//            okHttpPostRequest();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("test");
    }

    private void retrofitPostRequest(String path) {
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        File file = new File(path);

        RequestBody media =
                RequestBody.create(
                        MediaType.parse("video/mp4"), path);

        MultipartBody.Part videoBody =
                MultipartBody.Part.createFormData("media", file.getName(), media);

        VideoUploadModel model = new VideoUploadModel();
        model.file = file;
        model.baseString = bitmapString;

        Call<ResponseBody> call = service.upload(videoBody, bitmapString);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }


    @Background
    void volleyPostRequest(MultiValueMap<String, Object> multiValueMap) {
        JsonObjectRequest volleyTestRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                NetworkConstants.BASE_URL + NetworkConstants.UPLOAD_URL, null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                    }
                }, new com.android.volley.Response.ErrorListener() {

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
    void asyncAndroidPostRequest(String string, File file) {
        AsyncHttpPost post = new AsyncHttpPost(NetworkConstants.BASE_URL + NetworkConstants.UPLOAD_URL);

        MultiValueMap dataMultiPart = new LinkedMultiValueMap<>();

        dataMultiPart.add("thumbnail", bitmapString);
        dataMultiPart.add("media", file);

        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addFilePart("media", file);
        body.addStringPart("thumbnail", string);
        post.setHeader("Content-Type", "application/json charset=UTF-8");
        post.setBody(body);
        post.addHeader("Authorization", "Bearer 7fc0cf8dadac8586f327027ee22805178f369cb7");
        AsyncHttpClient.getDefaultInstance().execute(post, new HttpConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, AsyncHttpResponse res) {
                Log.i(TAG, "Uploaded");
            }
        });
    }


    public String okHttpPostRequest() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("video/mp4");
        Map<String, Object> stringObjectMap = new LinkedHashMap<>();
        stringObjectMap.put("thumbnail", bitmapString);
        stringObjectMap.put("media", new File(path));

        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        multipartBody.addFormDataPart("thumbnail", bitmapString);
        multipartBody.addFormDataPart("media", "media", RequestBody.create(mediaType, new File(path)));

        RequestBody req = multipartBody.build();


        Request request = new Request.Builder().url(NetworkConstants.BASE_URL + NetworkConstants.UPLOAD_URL).
                addHeader("authorization", "Bearer 7fc0cf8dadac8586f327027ee22805178f369cb7").
                addHeader("content-type", "multipart/form-data").post(req).build();
        okhttp3.Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body().string();
    }
}
