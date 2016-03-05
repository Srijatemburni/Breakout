package home.example.com.breakoutgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
// this is a sample of how it should work.
public class HighScores extends AppCompatActivity {
TextView score;
    String scoreitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
       List<String> lists = new ArrayList<>();
        score = (TextView) findViewById(R.id.text1);
        //get these and split bby \n
        //lists.add(new ArrayList<String>());
        //access via Iterator
        String result;
        Bundle extras = getIntent().getExtras();
        result = extras.getString("EXTRA_MESSAGEs");
        System.out.print(result);
        lists.add("60"+"\t"+"peack"+"\n");

        lists.add("20"+"\t"+"peack"+"\n");

        lists.add("30"+"\t"+"peack"+"\n");

        lists.add("40"+"\t"+"peack"+"\n");
       ;
        lists.add("50" + "\t" + "peack"+"\n");
//lists.add(result);
        Iterator iterator = lists.iterator();
        while(iterator.hasNext()){
            scoreitem = (String) iterator.next();
            score.append(scoreitem);
        }
        //Collections.sor
       // Collections.sort(lists, Collections.reverseOrder());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_scores, menu);
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
