package home.example.com.breakoutgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetScore extends AppCompatActivity {
    public final static String EXTRA_MESSAGEs = "home.example.com.breakoutgame.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_score);
        final String result, score;
        EditText gamescore = (EditText) findViewById(R.id.editText);
        score = "\t" + gamescore.toString() + "\n";
        Bundle extras = getIntent().getExtras();
        result = extras.getString("EXTRA_MESSAGE");
        final Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HighScores.class);
                i.putExtra(EXTRA_MESSAGEs, result);
                i.putExtra(EXTRA_MESSAGEs, score);
                startActivity(i);
            }
        });

    }}