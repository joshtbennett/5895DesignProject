package joshbennett.designproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LevelSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level_select);

        //LayoutParams params = new LinearLayout().LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ArrayList<ImageButton> buttons = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            final int j = i;
            ImageButton level = new ImageButton(this);
            level.setImageResource(R.drawable.penis);
            level.setScaleType(ImageView.ScaleType.FIT_XY);
            /*BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inJustDecodeBounds = true;
            Bitmap penis = BitmapFactory.decodeResource(getResources(), R.drawable.penis, dimensions);

            level.setScaleX(pxToDp(100)/dimensions.outWidth);
            level.setScaleY(pxToDp(100)/dimensions.outHeight); */
            level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LevelSelectActivity.this, ((CharSequence)("button" + j)), Toast.LENGTH_SHORT).show();
                }
            });
            buttons.add(level);
        }

        GridLayout grid = (GridLayout) findViewById(R.id.grid);

        for(int i = 0; i < 12; i++) {
            grid.addView(buttons.get(i));
        }
    }

    public static int dpToPx(int dp){
        return (int) (dp*Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px){
        return (int) (px/Resources.getSystem().getDisplayMetrics().density);
    }
}