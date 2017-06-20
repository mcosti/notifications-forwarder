package com.mihaicosti.notificationsforwarder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Costim on 20.06.2017.
 */

public class AppsAdapter extends ArrayAdapter<InstalledApp> {
    private ImageView appImage;
    private Switch appPackage;
    private TextView appName;

    public AppsAdapter(Context context, ArrayList<InstalledApp> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstalledApp app = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_app, parent, false);
        }
        // Lookup view for data population


        appImage = (ImageView) convertView.findViewById(R.id.appImage);
        appPackage = (Switch) convertView.findViewById(R.id.appSwitch);
        appName = (TextView) convertView.findViewById(R.id.appName);
        appImage.setImageDrawable(app.image);
        appPackage.setText(app.packageName);
        appName.setText(app.name);

        // Return the completed view to render on screen
        return convertView;
    }
}