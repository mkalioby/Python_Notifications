package gcm.play.android.samples.com.gcm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
    EditText senderIDEditText;
    EditText topicsEditText;
    TextView historyView;
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
        historyView= (TextView) findViewById(R.id.history);
        senderIDEditText=(EditText)findViewById(R.id.senderID);
        try {
            topicsEditText.setText(readFile("topics.txt"));
            MyGcmListenerService.defaultSoundUri=Uri.parse(readFile("ringtone.txt"));
            senderIDEditText.setText(readFile("senderID.txt"));
            /*String[] hist=readFile("history.txt").split("\n");
            StringBuilder histText=new StringBuilder();
            for (int j=hist.length-1;j<0;j--)
            {
                histText.append(hist[j]+"\n");
            }
            historyView.setText(histText.toString());
*/
            historyView.setText(readFile("history.txt"));
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

    public void checkSenderID()
    {
        if (senderIDEditText.getText().toString().length()<10) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("You have to enter a sender ID")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
            register2();
    }

    public void register(View view) {
        topicsEditText = (EditText) findViewById(R.id.topicEditText);
        senderIDEditText = (EditText) findViewById(R.id.senderID);
        checkSenderID();
    }
    public  void register2()
    {
        String[] topics = topicsEditText.getText().toString().split(",");
        RegistrationIntentService.setTopics(topics, true);
        String senderID=senderIDEditText.getText().toString();
        RegistrationIntentService.senderID=senderID;
        try {
            writeFile("topics.txt", topicsEditText.getText().toString());
            writeFile("senderID.txt",senderID);

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
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
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

    public void clearHist(View view)
    {
        try{
            writeFile("history.txt","");
            historyView= (TextView) findViewById(R.id.history);

            historyView.setText(readFile("history.txt"));
        }
        catch (Exception exp)
        {

        }

    }
}