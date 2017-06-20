package com.mihaicosti.notificationsforwarder;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import agency.tango.materialintroscreen.SlideFragment;

public class AppsSlide extends SlideFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_slide3, container, false);
        ArrayList<InstalledApp> arrayOfApps = getApps();
        AppsAdapter adapter = new AppsAdapter(getContext(), arrayOfApps);
        ListView lv = (ListView) view.findViewById(R.id.appsList);
        lv.setAdapter(adapter);
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

    public ArrayList<InstalledApp> getApps() {
        ArrayList<InstalledApp> arrayOfApps = new ArrayList<InstalledApp>();

        final PackageManager pm = getActivity().getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("d", "App name: " + packageInfo.loadLabel(getActivity().getPackageManager()));
            Log.d("d", "Installed package :" + packageInfo.packageName);
            Log.d("d", "App image : " + packageInfo.loadIcon(getActivity().getPackageManager()));
            Log.d("d", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));

            String name = packageInfo.loadLabel(getActivity().getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            Drawable image = getResources().getDrawable(R.drawable.ic_next);
            try {
                image = packageInfo.loadIcon(getActivity().getPackageManager());
            }
            catch(Exception e) {
                Log.d("d", "Did not find an image");
            }

            String activity = "";
            try {
                activity = pm.getLaunchIntentForPackage(packageInfo.packageName).toString();
            }
            catch(Exception e) {
                Log.d("d", "No intent. Moving on");
            }

            if(!activity.isEmpty()) {
                InstalledApp app = new InstalledApp(name, packageName, image);
                arrayOfApps.add(app);
            }
        }

        return arrayOfApps;
    }




}