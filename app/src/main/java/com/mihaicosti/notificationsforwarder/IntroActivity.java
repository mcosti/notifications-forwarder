package com.mihaicosti.notificationsforwarder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);


        addSlide(new NotificationSlide());
        addSlide(new HostSlide());
        addSlide(new AppsSlide());

    }

    @Override
    public void onFinish() {
        super.onFinish();
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this, "Try this library in your project! :)", Toast.LENGTH_SHORT).show();
    }
}
