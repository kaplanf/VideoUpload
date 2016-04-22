package com.kaplan.videouploadapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.kaplan.videouploadapp.R;
import com.kaplan.videouploadapp.interfaces.OnMainFragmentListener;
import com.kaplan.videouploadapp.util.DialogManager;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;


@EFragment
public abstract class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private OnMainFragmentListener listener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnMainFragmentListener) activity;
        } catch (ClassCastException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listener != null) listener.onCloseFragment(this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null) listener.onStartFragment(this.getClass().getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @UiThread
    protected void fragmentTransaction(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @UiThread
    protected void showProgressDialog() {
        if (getActivity() != null) {
            if (progressDialog == null)
                progressDialog = DialogManager.getInstance().getProgressDialog(getActivity(), R.string.loading);
            progressDialog.show();
        }
    }


    @UiThread
    protected void hideProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }


    protected boolean isFMVisibleFragment(String fragmentTag) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (fragment != null && fragment.isVisible()) return true;
        else return false;
    }

    protected boolean isChildFMVisibleFragment(String fragmentTag) {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(fragmentTag);
        if (fragment != null && fragment.isVisible()) return true;
        else return false;
    }

    protected SharedPreferences getPreferences() {
        if (preferences == null)
            preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return preferences;
    }

    public String getFramentTag() {
        String tag = this.getClass().getSimpleName();
        return tag;
    }

}