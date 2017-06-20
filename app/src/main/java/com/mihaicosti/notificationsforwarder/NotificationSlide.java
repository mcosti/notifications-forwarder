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

public class NotificationSlide extends SlideFragment {
    private Button btn_activate;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_slide1, container, false);
        Button button = (Button) view.findViewById(R.id.btn_activate);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
            }
        });

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.bg_screen1;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorPrimaryDark;
    }


    @Override
    public boolean canMoveFurther() {
        return isNotificationServiceEnabled();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.slide_1_error);
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getActivity().getPackageName();
        final String flat = Settings.Secure.getString(getActivity().getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}