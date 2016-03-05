package home.example.com.breakoutgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {
    /********************************************
     * This class and its functions were created by Srija Temburni
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Button button = (Button) findViewById(R.id.play);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent i = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(i);

            }
        });
        final Button button1 = (Button) findViewById(R.id.instructions);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent i = new Intent(getApplicationContext(), Instructions.class);

                startActivity(i);

            }
        });
        final Button button2 = (Button) findViewById(R.id.highscores);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent i = new Intent(getApplicationContext(), HighScores.class);

                startActivity(i);

            }
        });
    }
    /*public void startPlay()
   {
       Intent i = new Intent(this, MainActivity.class);

       startActivity(i);
   }
    /*  public void goScore()
       {
           Intent i = new Intent(this, Scores.class);

           startActivity(i);
       }
       public void goInstructions()
       {
           Intent i = new Intent(this, Instructions.class);

           startActivity(i);
       }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
