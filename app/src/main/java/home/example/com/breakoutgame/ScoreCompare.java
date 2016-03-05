package home.example.com.breakoutgame;

import android.text.format.Time;
import android.util.TimeUtils;

import java.util.Comparator;

/**
 * Created by sxt152930 on 11/30/2015.

public class ScoreCompare {
    private String name;
    private String score;
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }



    public ScoreCompare()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScoreCompare( String name, String score) {
        setName(name);
        setScore(score);
    }
    class ComparatorId implements Comparator<ScoreCompare>
    {

        @Override
        public int compare(ScoreCompare obj1, ScoreCompare obj2) {
            String p1 = obj1.getScore();
            String p2 = obj2.getScore();

            if (p1.equals(p2)) {
                return 1;
            } else if (p1 < p2){
                return -1;
            } else {
                return 0;
            }
        }
}
*/