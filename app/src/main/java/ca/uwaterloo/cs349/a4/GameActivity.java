package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;


public class GameActivity extends AppCompatActivity implements Observer{
    Model mModel;
    ImageButton backhome;
    Button startbutton;
    TextView SCORE;
    TextView score;
    TextView message;
    TextView message2;
    TextView finalMessage;
    ImageView finalView;
    ImageButton gohome;
    ImageButton restart;
    ImageButton continuelar;
    TextView finalScore;
    ConstraintLayout layout;
    ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
    int height;
    int width;
    static final int[] images = {
            R.drawable.ic_image1,
            R.drawable.ic_image2,
            R.drawable.ic_image3,
            R.drawable.ic_image4,
            R.drawable.ic_image5,
            R.drawable.ic_image6,
            R.drawable.ic_image7,
            R.drawable.ic_image8,
            R.drawable.ic_image9,
            R.drawable.ic_image10,
            R.drawable.ic_image11,
            R.drawable.ic_image12,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        layout = (ConstraintLayout)findViewById(R.id.game);

        mModel = Model.getInstance();
        mModel.addObserver(this);

        finalMessage = (TextView) findViewById(R.id.finaltext);
        finalView = (ImageView) findViewById(R.id.finalview);
        gohome = (ImageButton) findViewById(R.id.gohome);
        gohome.setClickable(false);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.setScore(0);
                mModel.reset();
                openMainActivity();
            }
        });
        restart = (ImageButton) findViewById(R.id.restart);
        restart.setClickable(false);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.setScore(0);
                mModel.reset();
                openGameActivity();
            }
        });
        continuelar = (ImageButton) findViewById(R.id.tocontinue);
        continuelar.setClickable(false);
        continuelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalView.setVisibility(View.INVISIBLE);
                finalMessage.setVisibility(View.INVISIBLE);
                gohome.setVisibility(View.INVISIBLE);
                restart.setVisibility(View.INVISIBLE);
                continuelar.setVisibility(View.INVISIBLE);
                finalScore.setVisibility(View.INVISIBLE);
                backhome.setVisibility(View.VISIBLE);
                backhome.setClickable(true);
                SCORE.setVisibility(View.VISIBLE);
                score.setVisibility(View.VISIBLE);
                score.setText(Integer.toString(mModel.getScore()));
                mModel.initObservers();
                for(ImageButton ib: buttons) {
                    ib.setVisibility(View.VISIBLE);
                }
                startGame();
            }
        });
        finalScore = (TextView) findViewById(R.id.finalscore);
        finalView.setVisibility(View.INVISIBLE);
        finalMessage.setVisibility(View.INVISIBLE);
        gohome.setVisibility(View.INVISIBLE);
        restart.setVisibility(View.INVISIBLE);
        continuelar.setVisibility(View.INVISIBLE);
        finalScore.setVisibility(View.INVISIBLE);

        SCORE = (TextView) findViewById(R.id.SCORE);
        score = (TextView) findViewById(R.id.score);
        score.setText(Integer.toString(mModel.getScore()));
        backhome = (ImageButton) findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.setScore(0);
                openMainActivity();
            }
        });

        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        width = Resources.getSystem().getDisplayMetrics().widthPixels - 250;
        int num = mModel.getButtonsNum();
        double d = Math.PI*2 / num;
        double angle = 0;
        double x = 0;
        double y = 0;
        //System.out.printf("height: %d, width: %d \n", height, width);
        for(int i = 0; i < num; i++) {
            x = 350 * Math.cos(angle) + width/2;
            y = 350 * Math.sin(angle) + height/2;
            //System.out.printf("x: %f, y: %f \n", x, y);
            angle += d;
            ImageButton b = new ImageButton(this);
            b.setBackgroundColor(Color.TRANSPARENT);
            b.setImageResource(images[i+6]);
            if(num == 1) {
                b.setX((float)(width/2) - 30);
                b.setY((float)(y - 250));
            } else {
                b.setX((float) x - 30);
                b.setY((float) y - 250);
            }
            layout.addView(b);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = buttons.indexOf(b);
                    flash(index, b);
                    Timer t = new Timer(false);
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if(mModel.simon.getState() == Simon.State.HUMAN) {
                                        if(mModel.simon.verifyButton(index) == false) {
                                            message.setVisibility(View.INVISIBLE);
                                            message2.setVisibility(View.INVISIBLE);
                                            message.setVisibility(View.GONE);
                                            message2.setVisibility(View.GONE);
                                            backhome.setVisibility(View.INVISIBLE);
                                            backhome.setClickable(false);
                                            SCORE.setVisibility(View.INVISIBLE);
                                            score.setVisibility(View.INVISIBLE);
                                            finalView.setVisibility(View.VISIBLE);
                                            finalMessage.setVisibility(View.VISIBLE);
                                            finalScore.setVisibility(View.VISIBLE);
                                            mModel.setSystemMode(true);
                                            for(ImageButton ib: buttons) {
                                                ib.setVisibility(View.INVISIBLE);
                                            }
                                            System.out.print("you lose lar\n");
                                            finalMessage.setText("GAME OVER");
                                            finalScore.setText("YOUR SCORE: " + Integer.toString(mModel.getScore()));
                                            gohome.setVisibility(View.VISIBLE);
                                            gohome.setClickable(true);
                                            restart.setVisibility(View.VISIBLE);
                                            restart.setClickable(true);
                                        }else {
                                            System.out.print("you r correct lar\n");
                                        }
                                    }
                                    if(mModel.simon.getState() == Simon.State.WIN) {
                                        message.setVisibility(View.INVISIBLE);
                                        message2.setVisibility(View.INVISIBLE);
                                        message.setVisibility(View.GONE);
                                        message2.setVisibility(View.GONE);
                                        backhome.setVisibility(View.INVISIBLE);
                                        backhome.setClickable(false);
                                        SCORE.setVisibility(View.INVISIBLE);
                                        score.setVisibility(View.INVISIBLE);
                                        finalView.setVisibility(View.VISIBLE);
                                        finalMessage.setVisibility(View.VISIBLE);
                                        finalScore.setVisibility(View.VISIBLE);
                                        mModel.setSystemMode(true);
                                        for(ImageButton ib: buttons) {
                                            ib.setVisibility(View.INVISIBLE);
                                        }
                                        continuelar.setVisibility(View.VISIBLE);
                                        mModel.setScore(mModel.getScore()+1);
                                        finalMessage.setText("YOU WON!");
                                        finalScore.setText("YOUR SCORE: " + Integer.toString(mModel.getScore()));
                                        continuelar.setClickable(true);
                                    }
                                }
                            });
                        }
                    }, 500);

                }
            });
            buttons.add(b);
        }

        startbutton = (Button) findViewById(R.id.startbutton);
        startbutton.setVisibility(View.VISIBLE);
        startbutton.setClickable(true);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        backhome.setVisibility(View.VISIBLE);
        backhome.setClickable(true);
        SCORE.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);

        mModel.initObservers();
    }

    private void updateMessage(String s1, String s2) {
        message = new TextView(this);
        message.setText(s1);
        message.setX((float)(width/2));
        message.setY(height/2 - 200);
        message.setTextSize(28);
        message.setTextColor(Color.WHITE);
        message.setTypeface(null, Typeface.BOLD);
        layout.addView(message);
        message2 = new TextView(this);
        message2.setText(s2);
        message2.setX((float)(width/2) - 80);
        message2.setY(height/2 - 100);
        message2.setTextSize(28);
        message2.setTextColor(Color.WHITE);
        message2.setTypeface(null, Typeface.BOLD);
        layout.addView(message2);
    }

    private void flash(int index, ImageButton b) {
        Drawable[] layers = new Drawable[2];
        layers[0] = getResources().getDrawable(images[index]);
        layers[1] = getResources().getDrawable(images[index+6]);
        TransitionDrawable transition = new TransitionDrawable(layers);
        transition.setCrossFadeEnabled(true);
        b.setImageDrawable(transition);
        transition.startTransition(1800 - 800*mModel.getLevels());
    }

    int count;

    private void startGame() {
        startbutton.setVisibility(View.INVISIBLE);
        startbutton.setClickable(false);
        mModel.setSystemMode(true);
        mModel.simon.newRound();
        updateMessage("WATCH", "WHAT I DO");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mModel.simon.getState() == Simon.State.COMPUTER) {
                            int next = mModel.simon.nextButton();
                            ImageButton b = buttons.get(next);
                            flash(next, b);
                            handler.postDelayed(this, 1800 - 800*mModel.getLevels());
                        }
                    }
                });
            }
        }, 0);
        /*while(mModel.simon.getState() == Simon.State.COMPUTER) {
            int next = mModel.simon.nextButton();
            ImageButton b = buttons.get(next);
            flash(next, b);
            count++;
        }*/
        System.out.printf("count: %d \n", mModel.simon.length);
        Timer t = new Timer(false);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        message.setVisibility(View.INVISIBLE);
                        message2.setVisibility(View.INVISIBLE);
                        message.setVisibility(View.GONE);
                        message2.setVisibility(View.GONE);
                        updateMessage(" NOW ", "YOUR TURN");
                        mModel.setSystemMode(false);
                    }
                });
            }
        }, mModel.simon.length * (1800 - 800 * mModel.getLevels()));
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        if(mModel.getMode()) {
            for(ImageButton b: buttons) {
                b.setClickable(false);
            }
        } else {
            for(ImageButton b: buttons) {
                b.setClickable(true);
            }
        }
    }
}
