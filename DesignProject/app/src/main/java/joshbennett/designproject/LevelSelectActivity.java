package joshbennett.designproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;

public class LevelSelectActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_level_select);
        ArrayList<Button> buttons = new ArrayList<>();

        for(int i = 0; i < 30; i++){
            final int j = i;
            final Button level = new Button(this);
            level.setEnabled(false);

            level.setText(Integer.toString(i+1));

            level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(level.getContext(), LevelActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("levelNum", j+1);
                    intent.putExtra("isTutorial", false);
                    startActivity(intent);
                }
            });
            buttons.add(level);
        }

        LevelDatabaseHelper mDbHelper = new LevelDatabaseHelper(this, 1);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int table = 1;
        while (mDbHelper.isTableExists(db, "level" + table)) {
            buttons.get(table-1).setEnabled(true);
            table++;
        }

        //create a grid to display the buttons
        GridLayout grid = (GridLayout) findViewById(R.id.grid);

        for(int i = 0; i < 30; i++) {
            //add all the buttons to the grid
            grid.addView(buttons.get(i));
        }
    }
}



