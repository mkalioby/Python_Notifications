package gcm.play.android.samples.com.gcm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import gcm.play.android.samples.com.gcm.R;

public class Topics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        EditText topicsEditText = (EditText) findViewById(R.id.topicEditText);
        try {
            FileInputStream in = openFileInput("topics.txt");

            InputStreamReader inputStreamReader = new InputStreamReader(in);
            byte[] data = new byte[in.available()];
            in.read(data, 0, in.available());
            topicsEditText.setText(new String(data, "UTF-8"));
            in.close();
/*
            in = openFileInput("clientID.txt");
            inputStreamReader = new InputStreamReader(in);
            data = new byte[in.available()];
            in.read(data, 0, in.available());
            topicsEditText.setText(new String(data, "UTF-8"));
            in.close();*/
        } catch (Exception exp) {
            Toast.makeText(getApplicationContext(), "Failed to open: " + exp.getMessage(), 5).show();

        }


    }

    public void register(View view) {
        EditText topicsEditText = (EditText) findViewById(R.id.topicEditText);
        //EditText clientID= (EditText) findViewById(R.id.clientID);

        String[] topics = topicsEditText.getText().toString().split(",");
        //RegistrationIntentService.ClientID=clientID.getText().toString();
        RegistrationIntentService.setTopics(topics,true);
        try {
            FileOutputStream fos = openFileOutput("topics.txt", Context.MODE_PRIVATE);
            fos.write(topicsEditText.getText().toString().getBytes());
            fos.close();
            /*fos = openFileOutput("clientID.txt", Context.MODE_PRIVATE);
            fos.write(clientID.getText().toString().getBytes());
            fos.close();*/

            Toast.makeText(getApplicationContext(), "Saved Successfully", 5).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("mode","register");
            startActivity(intent);

        } catch (Exception exp) {
            Toast.makeText(getApplicationContext(), "Failed to save: " + exp.getMessage(), 5).show();
        }
        //Toast.makeText(getApplicationContext(),"After Starting Service",2).show();

    }

    public void unSubscribeTopic(String topic) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        pubSub.unsubscribe(RegistrationIntentService.token, "/topics/" + topic);
    }
    public void unregister(View view) {
        EditText topicsEditText = (EditText) findViewById(R.id.topicEditText);
        String[] topics = topicsEditText.getText().toString().split(",");
        UnRegisterIntentService.setTopics(topics);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode","unregister");
        startActivity(intent);

    }
}