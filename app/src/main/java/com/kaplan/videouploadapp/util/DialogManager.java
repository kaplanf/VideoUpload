package com.kaplan.videouploadapp.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.kaplan.videouploadapp.R;


/**
 * Created by kaplanfatt on 08/09/15.
 */
public class DialogManager {


    private static DialogManager instance;

    private DialogManager() {
    }

    public synchronized static DialogManager getInstance() {
        if (instance == null) instance = new DialogManager();
        return instance;
    }

    public ProgressDialog getProgressDialog(Context context, int messageResId) {
        ProgressDialog progress = new ProgressDialog(context);
//        progress.setMessage(context.getString(messageResId));
        progress.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.drawable.progress_spin));
//        progress.setProgressStyle(R.style.ProgressDialogStyle);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.getWindow().setGravity(Gravity.CENTER);
//        progress.setContentView(R.layout.progress_layout);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        return progress;
    }

    public Dialog getSimpleDialog(Context context, String title, String message) {
        Dialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
