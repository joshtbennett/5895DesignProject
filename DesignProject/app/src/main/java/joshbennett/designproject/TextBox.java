package joshbennett.designproject;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class TextBox {

    private String message;

    public TextBox(String message) {
        this.message = message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
