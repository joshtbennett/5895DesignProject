package joshbennett.designproject;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class TextBox {

    private String message;

    public TextBox(String message) {
        this.message = message;
    }

    /*
    * Returns the text to be displayed in the text box
    * */
    public String getMessage(){
        return message;
    }
}
