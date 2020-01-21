package traf1.ganotrakrish.audioapp;

        import android.content.ActivityNotFoundException;
        import android.content.Intent;
        import android.provider.ContactsContract;
        import android.speech.RecognizerIntent;
        import android.os.Bundle;
        import android.speech.tts.TextToSpeech;
        import android.util.Log;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;



public class IntroActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private final int REQ_CODE = 100;
    TextView textView;
    public FloatingActionButton fab;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public TextToSpeech tts;
    public FloatingActionButton btnSpeak;
    int count = 0;
    ArrayList<String> texts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        textView = findViewById(R.id.text);
        ImageView speak = findViewById(R.id.speak);
        fab = findViewById(R.id.save);
        fab.setOnClickListener(fabListener);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        texts = new ArrayList<String>();

        tts = new TextToSpeech(this, this);
        btnSpeak = findViewById(R.id.speakOut);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                speakOut();
            }

        });


        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                texts.clear();
                count = 1;
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    texts.add(ds.getValue().toString());
                    count++;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textView.setText(textView.getText().toString() + " " + result.get(0).toString());
                }
                break;
            }
        }
    }

    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(textView.getText().toString().length() != 0) {
                myRef = database.getReference(count+"");
                myRef.setValue(textView.getText().toString());
            }
            Toast.makeText(getApplicationContext(),
                    "Text saved",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                System.out.println("This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);
                speakOut();
            }

        } else {
            System.out.println("Initilization Failed!");
        }

    }

    private void speakOut() {

        String text = textView.getText().toString();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}

