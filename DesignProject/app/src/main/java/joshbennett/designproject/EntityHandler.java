package joshbennett.designproject;

import android.content.EntityIterator;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class EntityHandler {

    public void addEntity(Level level, ColorableEntity entity, int position) {
        ArrayList<ColorableEntity> entities = level.getEntities();
        entities.add(entity);
        level.setEntities(entities);
    }

    public void addWall(Level level, Wall entity, int position) {
        ArrayList<Wall> walls = level.getWalls();
        walls.add(entity);
        level.setWalls(walls);
    }

    public void addMirror(Level level, Mirror entity, int position) {
        ArrayList<Mirror> mirrors = level.getMirrors();
        mirrors.add(entity);
        level.setMirrors(mirrors);
    }

    public void removeMirror(Level level, int position) {
        ArrayList<Mirror> mirrors = level.getMirrors();
        for (int i = 0; i < mirrors.size(); i++)
            if (mirrors.get(i).getPosition() == position)
                mirrors.remove(i);
        level.setMirrors(mirrors);
    }

    public void flipMirror(Level level, Mirror mirror, int position) {

        ArrayList<Mirror> mirrors = level.getMirrors();
        for (int i = 0; i < mirrors.size(); i++) {
            if (mirrors.get(i).getPosition() == position) {
                if (mirrors.get(i).getAngle() == 45) {
                    mirrors.get(i).setAngle(135);
                } else if (mirrors.get(i).getAngle() == 135) {
                    mirrors.get(i).setAngle(45);
                }
            }
            level.setMirrors(mirrors);
        }
    }

    public void moveBeam(Level level, Beam beam, int pos) {
        int length = level.getSideLength();
        int newposition;
        char newDirection = beam.getDirection();

        if (beam.getDirection() == 'u') {
            newposition = pos - length;
        } else if (beam.getDirection() == 'd') {
            newposition = pos + length;
        } else if (beam.getDirection() == 'l') {
            newposition = pos - 1;
        } else {
            newposition = pos + 1;
        }

        Beam newBeam;
        Mirror mirror = null;
        boolean wall = false;
        ColorableEntity entity = null;

        for (int i = 0; i < level.getMirrors().size(); i++) {
            if (level.getMirrors().get(i).getPosition() == newposition) {
                //theres a mirror in the next cell
                mirror = level.getMirrors().get(i);
            }
        }
        for (int i = 0; i < level.getWalls().size(); i++) {
            if (level.getWalls().get(i).getPosition() == newposition) {
                //theres a wall in the next cell
                wall = true;
            }
        }

        if (wall)
            return;

        for (int i = 0; i < level.getEntities().size(); i++) {
            if (level.getEntities().get(i).getPosition() == newposition && level.getEntities().get(i).getIdentifier() == 'c') {
                //collector in the next cell
                entity = level.getEntities().get(i);
            } else if (level.getEntities().get(i).getPosition() == newposition && level.getEntities().get(i).getIdentifier() == 'e') {
                //emitter in the next cell
                return;
            }
        }
        if (newposition < length - 1 || newposition > (length * length) - length - 1 || newposition % length == 0 || newposition % length == 9) {
            //the next  cell is out of bounds
            return;
        }


        if (entity != null) {
            if (entity.getColor() == beam.getColor()) {
                entity.setReceived();
                return;
            }
        } else if (mirror != null) {
            if (isComponent(beam.getColor(), mirror.getColor())){
                newDirection = reflect(beam.getDirection(), mirror.getAngle());
            }
            else
               newDirection = beam.getDirection();

            newBeam = new Beam(newDirection, beam.getColor(), newposition);
            level.getBeams().add(newBeam);
            moveBeam(level, newBeam, newposition);

        } else {
            //empty cell ahead
            newBeam = new Beam(newDirection, beam.getColor(), newposition);
            level.getBeams().add(newBeam);
            moveBeam(level, newBeam, newposition);
        }
    }

    public char reflect(char direction, int angle){
        char newDirection;
        if (angle == 135) {
            if (direction == 'u') {
                newDirection = 'r';
            } else if (direction == 'd') {
                newDirection = 'l';
            } else if (direction == 'l') {
                newDirection = 'd';
            } else {
                newDirection = 'u';
            }
        } else {
            if (direction == 'u') {
                newDirection = 'l';
            } else if (direction == 'd') {
                newDirection = 'r';
            } else if (direction == 'l') {
                newDirection = 'u';
            } else {
                newDirection = 'd';
            }
        }
        return newDirection;
    }

    public boolean isComponent(String color1, String color2){
        //Red Green and Blue are not combinations of colors and there for have no components other than themselves
        if(color2 == "Red"){
            if(color1 == "Red")
                return true;
            else
                return false;
        }
        if(color2 == "Green"){
            if(color1 == "Green")
                return true;
            else
                return false;
        }
        if(color2 == "Blue"){
            if(color1 == "Blue")
                return true;
            else
                return false;
        }

        //cyan is a combination of blue and green
        if(color2 == "Cyan"){
            if(color1 == "Blue" || color1 == "Green" || color1 == "Cyan")
                return true;
            else
                return false;
        }

        //yellow is a combination of red and green
        if(color2 == "Yellow"){
            if(color1 == "Red" || color1 == "Green" || color1 == "Yellow")
                return true;
            else
                return false;

        }

        //magenta is a combination of red and blue
        if(color2 == "Magenta"){
            if(color1 == "Blue" || color1 == "Red" || color1 == "Magenta"){
                return true;
            }
            else
                return false;
        }

        //White is a combination of all colors
        return true;
    }

    String combineColors(String color1, String color2){
        if(color1 == color2)
            return color1;
        if(color1 == "Red"){
            if(color2 == "Green"){
                return "Yellow";
            }
            else if(color2 == "Blue"){
                return "Magenta";
            }
            else if(color2 == "Cyan"){
                return "White";
            }
            else{
                return color2;
            }
        }
        else if(color1 == "Green") {
            if (color2 == "Red") {
                return "Yellow";
            } else if (color2 == "Blue") {
                return "Cyan";
            } else if (color2 == "Magenta") {
                return "White";
            } else {
                return color2;
            }
        }
        else if(color1 == "Blue") {
            if (color2 == "Green") {
                return "Cyan";
            } else if (color2 == "Red") {
                return "Magenta";
            } else if (color2 == "Yellow") {
                return "White";
            } else {
                return color2;
            }
        }
        return "White";
    }
}