package com.kaplan.videouploadapp.fragment;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import com.kaplan.videouploadapp.MainActivity;
import com.kaplan.videouploadapp.R;
import com.kaplan.videouploadapp.interfaces.OnMainFragmentListener;
import com.kaplan.videouploadapp.util.CameraPreview;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fatih Kaplan on 20/04/16.
 */
@EFragment(R.layout.camera_fragment)
public class CameraFragment extends BaseFragment implements MediaRecorder.OnInfoListener {

    private OnMainFragmentListener onMainFragmentListener;
    private static final String TAG = "MainActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public String filePath;
    boolean isRecording = false;

    @ViewById(R.id.button_capture)
    Button captureButton;

    @ViewById(R.id.camera_preview)
    FrameLayout preview;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onMainFragmentListener = (OnMainFragmentListener) activity;
    }


    @AfterViews
    protected void afterViews() {
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getActivity(), mCamera);
        preview.addView(mPreview);
    }


    @Click(R.id.button_capture)
    void captureButton() {
        if (isRecording) {

            // stop recording and release camera
            mMediaRecorder.stop();
            releaseMediaRecorder();
            mCamera.lock();
            captureButton.setText("Capture");
            isRecording = false;
            ((MainActivity) getActivity()).toThumbnailFragment(filePath);

        } else {
            if (prepareVideoRecorder()) {

                mMediaRecorder.start();

                captureButton.setText("Stop");
                isRecording = true;
            } else {
                releaseMediaRecorder();
            }
        }
    }


    private boolean prepareVideoRecorder() {

        if (mCamera == null) {
            mCamera = getCameraInstance();
        }

        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        filePath = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString();
        mMediaRecorder.setMaxDuration(10000);
        mMediaRecorder.setOnInfoListener(this);
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }


    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            c.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();
    }


    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
        }
    }


    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        mediaFile.getAbsolutePath();
        return mediaFile;
    }


    @Override
    public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            Log.v("VIDEOCAPTURE", "Maximum Duration Reached");
            captureButton.performClick();

        }
    }
}
