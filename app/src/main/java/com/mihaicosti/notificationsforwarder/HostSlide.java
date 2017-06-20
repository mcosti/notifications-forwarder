package com.mihaicosti.notificationsforwarder;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import agency.tango.materialintroscreen.SlideFragment;

public class HostSlide extends SlideFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_slide2, container, false);
        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.bg_screen2;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorPrimaryDark;
    }


    @Override
    public boolean canMoveFurther() {
        return true;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.slide_2_error);
    }


}