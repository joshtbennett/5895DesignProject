package joshbennett.designproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class LevelSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level_select);

        //LayoutParams params = new LinearLayout().LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ArrayList<ImageButton> buttons = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            final int j = i;
            ImageButton level = new ImageButton(this);
            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            Bitmap testimage = BitmapFactory.decodeResource(getResources(), R.drawable.testimage, dimensions);

            int newDimensions = dpToPx(100);

            testimage = android.graphics.Bitmap.createScaledBitmap(testimage, newDimensions, newDimensions, true);

            level.setImageBitmap(testimage);
            level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LevelSelectActivity.this, ((CharSequence)("button" + j)), Toast.LENGTH_SHORT).show();
                }
            });
            buttons.add(level);
        }

        GridLayout grid = (GridLayout) findViewById(R.id.grid);

        for(int i = 0; i < 30; i++) {
            grid.addView(buttons.get(i));
        }
    }

    public int dpToPx(int dp){
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        return (int) ((dp * metrics.density) + 0.5);
    }

    public int pxToDp(int px){
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        return (int) ((px/metrics.density) + 0.5);
    }
}



