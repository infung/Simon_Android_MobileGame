package ca.uwaterloo.cs349.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{
    Model mModel;
    Button button_play;
    Button button_setting;
    TextView title;
    TextView introduction;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mModel = Model.getInstance();
        mModel.addObserver(this);

        title = (TextView) findViewById(R.id.title);
        introduction = (TextView) findViewById(R.id.introdution);
        iv = (ImageView) findViewById(R.id.welcome);
        // Get button reference.
        button_play = (Button) findViewById(R.id.button_play);

        // Create controller to increment counter
        button_play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGameActivity();
            }
        });

        button_setting = (Button) findViewById(R.id.button_setting);

        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View v){
               openSettingActivity();
            }
        });

        // Init observers
        mModel.initObservers();
    }

    public void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void openSettingActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
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
    public void update(Observable observable, Object o) {

    }
}
