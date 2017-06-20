package com.mihaicosti.notificationsforwarder;

import android.graphics.drawable.Drawable;
import android.media.Image;

/**
 * Created by Costim on 20.06.2017.
 */

public class InstalledApp {
    public String name;
    public String packageName;
    public Drawable image;

    public InstalledApp(String name, String packageName, Drawable image) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
    }
}
