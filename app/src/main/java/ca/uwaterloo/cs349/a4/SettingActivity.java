package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.SeekBar;
import java.util.Observable;
import java.util.Observer;

public class SettingActivity extends AppCompatActivity implements Observer {
    Model mModel;
    SeekBar buttomSlider;
    SeekBar levelSlider;
    TextView buttonText;
    TextView levelText;
    TextView num_buttom;
    TextView level;
    ImageButton goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mModel = Model.getInstance();
        mModel.addObserver(this);

        buttonText = (TextView) findViewById(R.id.buttons);
        levelText = (TextView) findViewById(R.id.level);
        num_buttom = (TextView) findViewById(R.id.textView);
        num_buttom.setText(Integer.toString(mModel.getButtonsNum()));
        level = (TextView) findViewById(R.id.textView2);
        if(mModel.getLevels() == 0) {
            level.setText("EASY");
        } else if(mModel.getLevels() == 1) {
            level.setText("NORMAL");
        } else {
            level.setText("HARD");
        }

        buttomSlider = (SeekBar) findViewById(R.id.seekBarButton);
        buttomSlider.setProgress(mModel.getButtonsNum() - 1);
        buttomSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                num_buttom.setText(Integer.toString(progress+1));
                mModel.setButtonsNum(progress+1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        levelSlider = (SeekBar) findViewById(R.id.seekBarLevel);
        levelSlider.setProgress(mModel.getLevels());
        levelSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0) {
                    level.setText("EASY");
                } else if(progress == 1) {
                    level.setText("NORMAL");
                } else {
                    level.setText("HARD");
                }
                mModel.setLevels(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        goback = (ImageButton) findViewById(R.id.back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        mModel.initObservers();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
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

    }
}
