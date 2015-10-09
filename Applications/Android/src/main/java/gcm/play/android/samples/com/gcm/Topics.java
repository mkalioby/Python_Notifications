package gcm.play.android.samples.com.gcm;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import gcm.play.android.samples.com.gcm.R;

public class Topics extends AppCompatActivity {
    Intent intent;

    public String readFile(String filename) throws FileNotFoundException,IOException
    {
        FileInputStream in = openFileInput(filename);
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        byte[] data = new byte[in.available()];
        in.read(data, 0, in.available());
        in.close();
        return new String(data, "UTF-8");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        EditText topicsEditText = (EditText) findViewById(R.id.topicEditText);
        try {
            topicsEditText.setText(readFile("topics.txt"));
            MyGcmListenerService.defaultSoundUri=Uri.parse(readFile("ringtone.txt"));
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

    private void writeFile(String filename, String text) throws FileNotFoundException,IOException
    {

        FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();

    }

    public void register(View view) {
        EditText topicsEditText = (EditText) findViewById(R.id.topicEditText);
        //EditText clientID= (EditText) findViewById(R.id.clientID);

        String[] topics = topicsEditText.getText().toString().split(",");
        //RegistrationIntentService.ClientID=clientID.getText().toString();
        RegistrationIntentService.setTopics(topics, true);
        try {
            writeFile("topics.txt", topicsEditText.getText().toString());
            /*fos = openFileOutput("clientID.txt", Context.MODE_PRIVATE);
            fos.write(clientID.getText().toString().getBytes());
            fos.close();*/

            Toast.makeText(getApplicationContext(), "Saved Successfully", 5).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("mode", "register");
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


    public void changeTone(View view)
    {
        intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, MyGcmListenerService.defaultSoundUri);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                MyGcmListenerService.defaultSoundUri=uri;
                try {
                    writeFile("ringtone.txt", uri.toString());
                }
                catch (Exception exp)
                {

                }
            }
        }
    }
}