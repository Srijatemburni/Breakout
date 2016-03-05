package home.example.com.breakoutgame;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SensorEventListener {

    /*************
     *  This class MainActivity and its functions were created by Tapasya Gutta txg150730 and Srija Temburni sxt152930
     */
    // Custom View
    //GameView gameView;
    public final static String EXTRA_MESSAGE = "home.example.com.breakoutgame.MESSAGE";
    GameViewSurface gameView;
    float xPaddle, yPaddle;
    float ballXPos, ballYPos, xDif = 5, yDif = 5;
    int noOfLoops;
    int noOfCalls = 0;
    float collisionPointX, collisionPointY;
int radius;
    int numOfBricks = 24;
    Brick thisBrick[] = new Brick[numOfBricks];
    int colorOfBrick[] = new int[numOfBricks];

    Sensor accelerometer;
    SensorManager accelerometerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //gameView = new GameView(this);
        gameView = new GameViewSurface(this);
        gameView.setOnTouchListener(this);
        xPaddle = yPaddle = 0;
        setContentView(gameView);
        noOfLoops = 0;
        collisionPointX = collisionPointY = -1;

        accelerometerManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = accelerometerManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /*******************
     *
     *  This Function was created by Srija Temburni sxt152930
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xPaddle = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                xPaddle = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                xPaddle = event.getX();
                break;
        }
        return true;
    }

    /**** This  function was created by Tapasya Gutta txg150730
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        int changeInX = (int) (event.values[0]);
        if (changeInX > 7)
            changeInX = 7;
        else if (changeInX < -7)
            changeInX = -7;

        xDif += (changeInX * -1);    // to calculate the difference in the position of the ball because of accelerometer
        yDif += (changeInX * -1);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /********************************************
     * This class GameViewSurface and its functions were created by Tapasya Gutta txg150730
     */
    public class GameViewSurface extends SurfaceView implements Runnable {

        SurfaceHolder gameHolder;
        Thread gameThread = null;
        int time=0;
        Thread timerThread = null;
        boolean isRunning = false;

        public GameViewSurface(Context context) {
            super(context);
            gameHolder = getHolder();
        }

        // actions to perform if the app is paused
        public void pause() {
            isRunning = false;
            while (true) {
                try {
                    gameThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            gameThread = null;
        }

        // actions to perform if the app is resumed again
        public void resume() {
            isRunning = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        //checking if the ball is colliding with the paddle
        public boolean isCollidingWithPaddle(int screenW, int screenH, int r) {
            /* The equation to get the point of collision is
             * P = P1 + u(P2 - P1)
             * where P1 is the one point of line and P2 is another point of line
             *
             */
            boolean isColliding = false;
            float p1X = (xPaddle-screenW/12) - ballXPos;
            float p1Y = yPaddle - ballYPos;
            float p2X = (xPaddle + screenW/12) - ballYPos;
            float p2Y = (yPaddle + 50) - ballYPos;

            float p2Xdiffp1X = p2X - p1X;
            float p2Ydiffp1Y = p2Y- p1Y;

            float a = (p2Xdiffp1X * p2Xdiffp1X) + (p2Ydiffp1Y * p2Ydiffp1Y);
            float b = 2 * ((p2Xdiffp1X * p1X) + (p2Ydiffp1Y * p1Y));
            float c = (p1X * p1X) + (p1Y * p1Y) - (r * r);

            float delta = (b * b) - (4 * a * c);

            // checking for collision
            if (delta < 0)
                isColliding = false;
            else if ((delta >= 0)&&(xPaddle-screenW/12<ballXPos)&&(xPaddle+screenW/12>ballXPos)) {

                isColliding = true;
                System.out.println(" now now now Colliding");
            }


            return isColliding;
        }

        //checking if the ball is colliding with the paddle
        /* The equation to get the point of collision is
             * P = P1 + u(P2 - P1)
             * where P1 is the one point of line and P2 is another point of line
             *
             */
        public boolean isCollidingWithBrick(int left, int top, int right, int bottom, int r) {

            boolean isColliding = false;
            float p1X = left - ballXPos/*(ballXPos/2 + r)*/;
            float p1Y = bottom - ballYPos/*(ballYPos/2 + r)*/;
            float p2X = right - ballXPos/*(ballXPos/2 + r)*/;
            float p2Y = bottom -ballYPos/*(ballYPos/2 + r)*/;
            float u;

            float p2Xdiffp1X = p2X - p1X;
            float p2Ydiffp1Y = p2Y - p1Y;

            float a = (p2Xdiffp1X * p2Xdiffp1X) + (p2Ydiffp1Y * p2Ydiffp1Y);
            float b = 2 * ((p2Xdiffp1X * p1X) + (p2Ydiffp1Y * p1Y));
            float c = (p1X * p1X) + (p1Y * p1Y) - (r * r);

            float delta = (b * b) - (4 * a * c);

            // checking for collision
            if (delta < 0)
                isColliding = false;
            else if (delta >= 0) {
                isColliding = true;
                System.out.println("Colliding");
                u = -b / (2 * a);
                collisionPointX = p1X + (u * p2Xdiffp1X);
                collisionPointY = p1Y + (u * p2Ydiffp1Y);
                System.out.println(collisionPointX);
                System.out.println(collisionPointY);

            }
            return isColliding;
        }


        // method used to generate a random color for the brick
        public int generateColorOfBrick()
        {
            int range = 5; // // the number of colors possible. Derived using formula range = (max - min) + 1
            int color = (int) (Math.random() * range) + 1; // 1 is the minimum.
            return color;
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {

            while (isRunning) {

                try {
                    timerThread.sleep(1);
                    time += 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!gameHolder.getSurface().isValid())
                    continue;
                try {
                    gameThread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Canvas canvas = gameHolder.lockCanvas();
                canvas.drawRGB(128, 128, 128);
                int screenWidth = canvas.getWidth();
                int screenHeight = canvas.getHeight();
                radius = screenWidth / 24;



                /* The following code is to draw the bricks */

                // different paint objects for the bricks

                Paint paint_Fill_White=new Paint();
                paint_Fill_White.setColor(Color.WHITE);
                paint_Fill_White.setStyle(Paint.Style.FILL);
                Paint paint_Brick_Black=new Paint();
                paint_Brick_Black.setColor(Color.BLACK);
                paint_Brick_Black.setStyle(Paint.Style.FILL);
                Paint paint_Brick_Blue=new Paint();
                paint_Brick_Blue.setColor(Color.BLUE);
                paint_Brick_Blue.setStyle(Paint.Style.FILL);
                Paint paint_Brick_Green=new Paint();
                paint_Brick_Green.setColor(Color.GREEN);
                paint_Brick_Green.setStyle(Paint.Style.FILL);
                Paint paint_Brick_Red=new Paint();
                paint_Brick_Red.setColor(Color.RED);
                paint_Brick_Red.setStyle(Paint.Style.FILL);
                Paint paint_Brick_White=new Paint();
                paint_Brick_White.setColor(Color.WHITE);
                paint_Brick_White.setStyle(Paint.Style.FILL);

                int brickWidth1 = screenWidth / 9;
                int brickWidth2 = screenWidth / 8;
                int brickWidth3 = screenWidth / 7;
                int brickHeight = screenWidth / 12;

                // Build a wall of bricks
                int k = 0;
                int bricksNo;
                for (int row = 0; row < 3; row++) {
                    bricksNo = (row == 0) ? 9 : ((row == 1) ? 8 : 7);
                    for (int column = 0; column < bricksNo; column++) {
                        if (row == 0)
                            thisBrick[k] = new Brick(column, row, brickWidth1, brickHeight);
                        if (row == 1)
                            thisBrick[k] = new Brick(column, row, brickWidth2, brickHeight);
                        if (row == 2)
                            thisBrick[k] = new Brick(column, row, brickWidth3, brickHeight);
                        k++;
                    }
                }


                int thisBrickColor=0;
                int range = 5; // // the number of colors possible. Derived using formula range = (max - min) + 1
                int no = 0;
                //int i=0;
                // while(true){
                for (int i = 0; i < k; i++) {

                    boolean isMatching = false;
                    if (thisBrick[i].getVisibility()) {
                        if (noOfLoops == 1) {
                            do {

                                thisBrickColor = generateColorOfBrick(); // 1 is the minimum.
                                if(i!=0)
                                    isMatching = (thisBrickColor == thisBrick[i - 1].getColorOfBrick());
                                colorOfBrick[i] = thisBrickColor;
                                thisBrick[i].setNoOfHits(thisBrickColor);

                            }while (isMatching);
                        }
                        thisBrick[i].setColorOfBrick(thisBrickColor);
                        noOfCalls++;
                    } // to make sure that the brick colors are set only 24 times



                    if (/*thisBrick[i].getColorOfBrick()*/colorOfBrick[i] == 1)
                        canvas.drawRect(thisBrick[i].getRect(), paint_Brick_White);
                    else if (/*thisBrick[i].getColorOfBrick()*/colorOfBrick[i] == 2)
                        canvas.drawRect(thisBrick[i].getRect(), paint_Brick_Red);
                    else if (/*thisBrick[i].getColorOfBrick()*/colorOfBrick[i] == 3)
                        canvas.drawRect(thisBrick[i].getRect(), paint_Brick_Green);
                    else if (/*thisBrick[i].getColorOfBrick()*/colorOfBrick[i] == 4)
                        canvas.drawRect(thisBrick[i].getRect(), paint_Brick_Blue);
                    else if (/*thisBrick[i].getColorOfBrick()*/colorOfBrick[i] == 5)
                        canvas.drawRect(thisBrick[i].getRect(), paint_Brick_Black);
                    else if(colorOfBrick[i] == 0)
                        thisBrick[i].setInvisible();

                }
                //flag=false;
                // }
                //}


                /* The following code is to draw the paddle */
                yPaddle = screenHeight * 8 / 10;
                Paint paddleColor = new Paint();
                paddleColor.setColor(Color.CYAN);

                // we are matching the center of the paddle to the point of contact
                // this is the default position of the paddle;
                Rect paddle = new Rect();
                if (noOfLoops == 0) {
                    xPaddle = screenWidth / 2 - screenWidth / 12;
                }
                if (xPaddle != 0) {
                    // these positions move the center of the paddle to the point of contact of the finger touch
                    paddle.top = (int) yPaddle;
                    paddle.left = (int) (xPaddle - screenWidth / 12);
                    paddle.bottom = (int) (yPaddle + 50);
                    paddle.right = (int) (xPaddle + screenWidth / 12);

                    // conditions to make sure that paddle doesn't go off-screen
                    if (paddle.left <= 0) {
                        paddle.left = 0;
                        paddle.right = screenWidth / 6;
                    }
                    if (paddle.right > screenWidth) {
                        paddle.right = screenWidth;
                        paddle.left = screenWidth - (screenWidth / 6);
                    }

                    canvas.drawRect(paddle, paddleColor);
                }

                /* the following code is to draw the ball */
                if (noOfLoops == 0) {
                    ballXPos = screenWidth/5; /// 2;
                    ballYPos = screenHeight-700 ; /// 2;
                }

                // placing the ball
                //int radius = screenWidth / 24;
                int diameter = radius + radius;
                Paint ballColor = new Paint();
                ballColor.setColor(Color.YELLOW);

                if (ballXPos + diameter >= screenWidth) {
                    xDif = -30;
                }
                if (ballXPos <= 0) {
                    xDif = 30;
                }
                if (ballYPos + diameter >= screenHeight-200) {
                    // game over
                    Intent i = new Intent(this.getContext(), GetScore.class);
                    i.putExtra(EXTRA_MESSAGE,(int)(time/1000));

                    startActivity(i);
                   // yDif = -30;
                }
                if (ballYPos <= 0) {
                    yDif = 30;
                }

                ballXPos = ballXPos + xDif;
                ballYPos = ballYPos + yDif;

                // collision detection
                //if(isCollidingWithBrick()){
                collisionwithBrick();
               /* for(int i=0;i<k;i++) {
                    if(thisBrick[i].getVisibility())
                    {
                        if (isCollidingWithBrick(thisBrick[i].getLeftOfBrick(), thisBrick[i].getTopOfBrick(),
                                thisBrick[i].getRightOfBrick(), thisBrick[i].getBottomOfBrick(), radius)) {
                            colorOfBrick[i]--;
                            // System.out.println(i + " " + colorOfBrick[i] + " " + (thisBrick[i].getNoOfHits()));
                            // thisBrick[i].setNoOfHits(thisBrick[i].getNoOfHits()-1);
                            //if(collisionPointX)
                            reverseYVelocity();
                        }
                    }

                }*/

                if (isCollidingWithPaddle(screenWidth, screenHeight, radius))
                    reverseYVelocity();
                canvas.drawOval(ballXPos, ballYPos, ballXPos + (2 * radius), ballYPos + (2 * radius), ballColor);

                noOfLoops++;
                gameHolder.unlockCanvasAndPost(canvas);

            } // while of thread
        } // run()
       public void collisionwithBrick() {
           int k = 24;
           int bricksNos;
           for (int i = 0; i < k; i++) {
              // bricksNos = (row == 0) ? 9 : ((row == 1) ? 8 : 7);
               //for (int column = 0; column < bricksNos; column++) {
                  // if (row == 0) {
                       if (thisBrick[i].getVisibility()) {
                           if (isCollidingWithBrick(thisBrick[i].getLeftOfBrick(), thisBrick[i].getTopOfBrick(),
                                   thisBrick[i].getRightOfBrick(), thisBrick[i].getBottomOfBrick(), radius)) {
                               colorOfBrick[i]--;
                               // System.out.println(i + " " + colorOfBrick[i] + " " + (thisBrick[i].getNoOfHits()));
                               thisBrick[i].setNoOfHits(thisBrick[i].getNoOfHits() - 1);
                               //if(collisionPointX)
                               reverseYVelocity();
                           }
                       }
                   }
                   //k++;
               }


    } // GameView class

    // separate brick class

    /**********************************************
     * This class and its functions were created by Srija Temburni sxt152930
     */
    class Brick {

        private Rect rect;
        private boolean isVisible;
        int brickColor=-1;
        int noOfHits;

        int leftOfBrick;
        int rightOfBrick;
        int topOfBrick;
        int bottomOfBrick;

        public Brick(int column, int row, int width, int height) {

            isVisible = true;
            int padding = 1;
            leftOfBrick = column * width + padding;
            topOfBrick = row * height + padding;
            rightOfBrick = column * width + width - padding;
            bottomOfBrick = row * height + height - padding;
            rect = new Rect(leftOfBrick, topOfBrick, rightOfBrick, bottomOfBrick);
        }

        public Rect getRect() {
            return rect;
        }

        public void setInvisible() {
            isVisible = false;
        }

        public boolean getVisibility() {
            return isVisible;
        }

        public int getColorOfBrick() {
            return brickColor;
        }

        public void setColorOfBrick(int colorOfBrick) {
            this.brickColor = colorOfBrick;
        }

        public int getNoOfHits() {
            return noOfHits;
        }

        public void setNoOfHits(int noOfHits) {
            this.noOfHits = noOfHits;

            if(this.noOfHits <= 0)
                setInvisible();
        }
        public int getRightOfBrick() {
            return rightOfBrick;
        }

        public int getLeftOfBrick() {
            return leftOfBrick;
        }

        public int getTopOfBrick() {
            return topOfBrick;
        }

        public int getBottomOfBrick() {
            return bottomOfBrick;
        }
    }

    public void reverseYVelocity() {

        yDif = -yDif;
    }
}



