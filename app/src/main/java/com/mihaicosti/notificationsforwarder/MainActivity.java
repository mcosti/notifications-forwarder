package com.mihaicosti.notificationsforwarder;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MIT License
 *
 *  Copyright (c) 2016 Fábio Alves Martins Pereira (Chagall)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class MainActivity extends AppCompatActivity {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private TextChangeBroadcastReceiver textChangeBroadcastReceiver;
    private AlertDialog enableNotificationListenerAlertDialog;
    private TextView interceptedNotificationCode;
    private EditText editHost;
    private EditText editPort;
    private EditText editAuth;
    private EditText editPath;
    private Button socketSaveButton;
    private SharedPreferences savedSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("d", "Got here");

        // Here we get a reference to the image we will modify when a notification is received


        interceptedNotificationCode = (TextView) this.findViewById(R.id.notifCode);
        editHost = (EditText) this.findViewById(R.id.editHost);
        editPort = (EditText) this.findViewById(R.id.editPort);
        editAuth = (EditText) this.findViewById(R.id.editAuth);
        editPath = (EditText) this.findViewById(R.id.editPath);




        savedSocket = getSharedPreferences("socketInfo", MODE_PRIVATE);
        editHost.setText(savedSocket.getString("host", "1.2.3.4"));
        Log.d("d", "Host initialized: " + savedSocket.getString("host", ""));
        editPort.setText(savedSocket.getString("port", "80"));
        Log.d("d", "Port initialized: " + savedSocket.getString("port", "80"));

        editPath.setText(savedSocket.getString("path", ""));
        Log.d("d", "Path initialized: " + savedSocket.getString("path", ""));


        editAuth.setText(savedSocket.getString("auth", ""));
        Log.d("d", "Auth initialized: " + savedSocket.getString("auth", ""));


        socketSaveButton = (Button) this.findViewById(R.id.socketSave);
        socketSaveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SharedPreferences.Editor socketEditor = savedSocket.edit();
                socketEditor.clear();
                socketEditor.putString("host", editHost.getText().toString());
                Log.d("d", "Saved host: " +  editHost.getText().toString());

                socketEditor.putString("path", editPath.getText().toString());
                Log.d("d", "Saved path: " + editPath.getText().toString());

                socketEditor.putString("port", editPort.getText().toString());
                Log.d("d", "Saved port: " + editPort.getText().toString());

                socketEditor.putString("auth", editAuth.getText().toString());
                Log.d("d", "Saved auth: " + editAuth.getText().toString());

                socketEditor.apply();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        });



        // If the user did not turn the notification listener service on we prompt him to do so
        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        // Finally we register a receiver to tell the MainActivity when a notification has been received
        textChangeBroadcastReceiver = new TextChangeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mihaicosti.notificationsforwarder");

        registerReceiver(textChangeBroadcastReceiver, intentFilter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(textChangeBroadcastReceiver);
    }




    private void changeInterceptedNotificationText(int notificationCode) {
        Log.d("d", "Change to code: " + notificationCode);
        interceptedNotificationCode.setText("Code received: " + notificationCode);
    }

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if eanbled, false otherwise.
     */
    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
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


    /**
     * Image Change Broadcast Receiver.
     * We use this Broadcast Receiver to notify the Main Activity when
     * a new notification has arrived, so it can properly change the
     * notification image
     * */


    public class TextChangeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int receivedNotificationCode = intent.getIntExtra("Notification Code", -1);
            Log.d("d", "Text broadcast" + receivedNotificationCode);
            changeInterceptedNotificationText(receivedNotificationCode);
        }
    }


    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }
}
