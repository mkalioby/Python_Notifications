/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gcm.play.android.samples.com.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.FileOutputStream;

import gcm.play.android.samples.com.gcm.R;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private BroadcastReceiver mUnRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private Boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        Toast.makeText(getApplicationContext(),mode,2);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mInformationTextView = (TextView) findViewById(R.id.informationTextView);
        if (mode.equals("register")) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        String st=getString(R.string.gcm_send_message);
                        StringBuilder sb=new StringBuilder();
                        String[] topics=RegistrationIntentService.getTopics();
                        for (int i=0;i<topics.length;i++)
                            sb.append(topics[i]+", ");

                        mInformationTextView.setText(st+" Currently listening to "+ sb.toString().substring(0,sb.length()-2));
                    } else {
                        mInformationTextView.setText(getString(R.string.token_error_message));
                    }
                }
            };


            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                try {
                    intent = new Intent(this, RegistrationIntentService.class);
                    startService(intent);

                } catch (Exception exp) {

                }

            }
        }
        else
        {
            mInformationTextView.setText("Unregistering in progress....");
            mUnRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.e("MAIN", "Boardcast Received");
                     mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    StringBuilder sb = new StringBuilder();
                    String[] topics=UnRegisterIntentService.getTopics();
                    for (int i=0;i<topics.length;i++)
                        sb.append(topics[i]+", ");
                    String removedTopics=sb.toString().substring(0,sb.length()-2);
                    String[] RegTopics= RegistrationIntentService.getTopics();
                    String[] NewTopics=new String[RegTopics.length-topics.length];
                    Log.d("UnRegister","Array Length: "+ (RegTopics.length-topics.length));
                    String cTopics=null;
                    if (NewTopics.length>0) {
                        int z = 0;
                        for (int i = 0; i < RegTopics.length; i++) {
                            Boolean found = false;
                            for (int j = 0; j < topics.length; j++) {

                                if (topics[j].equals(RegTopics[i]))
                                    found = true;
                            }
                            if (!found) {
                                NewTopics[z] = RegTopics[i];
                                z++;
                            }
                        }
                        sb = new StringBuilder();
                        for (int i = 0; i < NewTopics.length; i++)
                            sb.append(NewTopics[i] + ", ");
                        cTopics = sb.toString().substring(0, sb.length() - 2);
                    }
                    if (cTopics==null)
                        cTopics="'  '";
                    RegistrationIntentService.setTopics(NewTopics,false);
                    try {
                        FileOutputStream fos = openFileOutput("topics.txt", Context.MODE_PRIVATE);
                        fos.write(cTopics.replaceAll(", ",",").getBytes());
                        fos.close();
                    }
                    catch (Exception exp)
                    {}
                    mInformationTextView.setText("Unregistered Succesfully from "+removedTopics+". Currently Listening to "+cTopics);


                }};
            Log.i("Main:","Starting unregisteration Intent");
            Intent unregintent = new Intent(this, UnRegisterIntentService.class);
            startService(unregintent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mUnRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.UNREGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUnRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}