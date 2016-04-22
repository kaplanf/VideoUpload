package com.kaplan.videouploadapp.interfaces;


import android.os.Bundle;

public interface OnMainFragmentListener {


    void onCloseFragment(String tag);

    void onStartFragment(String tag);

    void sendDataFromFragment(Bundle bundle);

    void backClicked(String fragmentName);

}
