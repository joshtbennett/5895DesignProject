package joshbennett.designproject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MainMenuActivity extends AppCompatActivity {

    private ViewGroup mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_main_menu);

        mRootView = (ViewGroup)findViewById(R.id.activity_main_menu);
    }

    void LevelSelectButton(View v) {
        Intent i = new Intent(this, LevelSelectActivity.class);
        startActivity(i);
    }

    void TutorialButton(View v) {
        Intent i = new Intent(this, Level.class);
        startActivity(i);
    }
}