package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class EntityHandler {

    /*
    * Places an entity on the game board at a given location if its currently empty
    * */
    public void addEntity(Level level, LevelEntity entity, int position) {
        ArrayList<LevelEntity> entities = level.getEntities();
        for (LevelEntity i : entities) {
            if (i.getPosition() == position)
                return;
        }
        entities.add(entity);
        level.setEntities(entities);
    }

    /*
    * Places a mirror on the game board
    * */
    public void addMirror(Level level, Mirror entity) {
        ArrayList<LevelEntity> entities = level.getEntities();
        entities.add(entity);
        level.setEntities(entities);
    }

    /*
    * Removes a mirror from a give position on the game board if one is there
    * */
    public void removeMirror(Level level, int position) {
        ArrayList<LevelEntity> entities = level.getEntities();
        for (int i = 0; i < entities.size(); i++)
            if (entities.get(i).getPosition() == position)
                entities.remove(i);
        level.setEntities(entities);
    }

    /*
    * Flips the mirrors orientation in the given position
    * */
    public void flipMirror(Level level, int position) {
        ArrayList<LevelEntity> entities = level.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getPosition() == position) {
                if(entities.get(i) instanceof Mirror) {
                    if (((Mirror)entities.get(i)).getAngle() == 45) {
                        ((Mirror)entities.get(i)).setAngle(135);
                    }
                    else if (((Mirror)entities.get(i)).getAngle() == 135) {
                        ((Mirror)entities.get(i)).setAngle(45);
                    }
                }
            }
            level.setEntities(entities);
        }
    }

    /*
    * Recursively creates beams following their paths from the emitters until theyve all hit a collector, wall, or the game border
    * */
    public void moveBeam(Level level, Beam beam, int pos) {
        int length = level.getSideLength();
        int newposition;
        char currentDirection = beam.getDirection();
        char newDirection = currentDirection;

        if (currentDirection == 'u') {
            newposition = pos - length;
        } else if (currentDirection == 'd') {
            newposition = pos + length;
        } else if (currentDirection == 'l') {
            newposition = pos - 1;
        } else {
            newposition = pos + 1;
        }

        for(int k = 0; k < level.getBeams().size(); k++){
            for(int j = 0; j< level.getBeams().size(); j++){
                if (level.getBeams().get(k).getPosition() == level.getBeams().get(j).getPosition()) {
                    if(level.getBeams().get(k).getDirection() == level.getBeams().get(j).getDirection()) {
                        String color = combineColors(level.getBeams().get(k).getColor(), level.getBeams().get(j).getColor());
                        level.getBeams().get(k).setColor(color);
                        level.getBeams().get(j).setColor(color);
                    }
                }
            }
        }

        Beam newBeam;
        LevelEntity entity = null;

        for (int i = 0; i < level.getEntities().size(); i++) {
            if (level.getEntities().get(i).getPosition() == newposition) {
                if((level.getEntities().get(i) instanceof Wall) || (level.getEntities().get(i) instanceof Emitter)) {
                    return;
                }
                else
                    entity = level.getEntities().get(i);
            }
        }

        if (newposition < length - 1 || newposition > (length * length) - length - 1 || newposition % length == 0 || newposition % length == length-1) {
            //the next cell is out of bounds
            return;
        }

        if (entity != null) {
            if (entity instanceof Collector) {
                if (((ColorableEntity) entity).getColor().equals(beam.getColor())) {
                    ((Collector) entity).setReceived(true);
                    return;
                } else {
                    ((Collector) entity).setReceived(false);
                    return;
                }
            }
            else {
                ArrayList<Beam> reflectedBeams = new ArrayList<>();
                ArrayList<Beam> passedBeams = new ArrayList<>();

                for (String color : deconstructBeam(beam)) {
                    if (isComponent(color, ((Mirror)entity).getColor())) {
                        newDirection = reflect(currentDirection, ((Mirror)entity).getAngle());
                        reflectedBeams.add(new Beam(newDirection, color, newposition));
                    }
                    else {
                        newDirection = currentDirection;
                        passedBeams.add(new Beam(newDirection, color, newposition));
                    }
                }

                if (!reflectedBeams.isEmpty()) {
                    Beam reflectedBeam = reflectedBeams.get(0);
                    for (int i = 0; i < reflectedBeams.size() - 1; i++) {
                        reflectedBeam.setColor(combineColors(reflectedBeam.getColor(), reflectedBeams.get(i + 1).getColor()));
                    }
                    level.getBeams().add(reflectedBeam);
                    moveBeam(level, reflectedBeam, reflectedBeam.getPosition());
                }

                if (!passedBeams.isEmpty()) {
                    Beam passedBeam = passedBeams.get(0);
                    for (int i = 0; i < passedBeams.size() - 1; i++) {
                        passedBeam.setColor(combineColors(passedBeam.getColor(), passedBeams.get(i + 1).getColor()));
                    }
                    level.getBeams().add(passedBeam);
                    moveBeam(level, passedBeam, passedBeam.getPosition());
                }
            }
        }
        else {
            //empty cell ahead
            newBeam = new Beam(newDirection, beam.getColor(), newposition);
            level.getBeams().add(newBeam);
            moveBeam(level, newBeam, newposition);
        }
    }

    /*
    * Calculates the new direction a beam will go based on the current direction and the angle of the mirror
    * */
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

    /*
    * Returns true if color1 is a component of color2
    * */
    public boolean isComponent(String color1, String color2){
        //red green and blue are not combinations of colors and there for have no components other than themselves
        if(color2.equals("red")){
            if(color1.equals("red"))
                return true;
            else
                return false;
        }
        if(color2.equals("green")){
            if(color1.equals("green"))
                return true;
            else
                return false;
        }
        if(color2.equals("blue")){
            if(color1.equals("blue"))
                return true;
            else
                return false;
        }

        //cyan is a combination of blue and green
        if(color2.equals("cyan")){
            if(color1.equals("blue") || color1.equals("green") || color1.equals("cyan"))
                return true;
            else
                return false;
        }

        //yellow is a combination of red and green
        if(color2.equals("yellow")){
            if(color1.equals("red") || color1.equals("green") || color1.equals("yellow"))
                return true;
            else
                return false;

        }

        //magenta is a combination of red and blue
        if(color2.equals("magenta")){
            if(color1.equals("blue") || color1.equals("red") || color1.equals("magenta")){
                return true;
            }
            else
                return false;
        }

        //white is a combination of all colors
        return true;
    }

    /*
    * Breaks a beam into the multiple beams with the colors of the originals base components
    * */
    private String[] deconstructBeam(Beam beam) {
        switch (beam.getColor()) {
            case "red":
                return new String[] {"red"};
            case "blue":
                return new String[] {"blue"};
            case "green":
                return new String[] {"green"};
            case "magenta":
                return new String[] {"red", "blue"};
            case "yellow":
                return new String[] {"red", "green"};
            case "cyan":
                return new String[] {"blue", "green"};
            case "white":
                return new String[] {"red", "green", "blue"};
            default:
                return null;
        }
    }

    /*
    * Combines the 2 colors passed in
    * */
    String combineColors(String color1, String color2){
        if(color1.equals(color2))
            return color1;
        if(color1.equals("red")){
            if(color2.equals("green"))
                return "yellow";
            else if(color2.equals("blue"))
                return "magenta";
            else if(color2.equals("cyan"))
                return "white";
            else
                return color2;
        }
        else if(color1.equals("green")) {
            if (color2.equals("red"))
                return "yellow";
            else if (color2.equals("blue"))
                return "cyan";
            else if (color2.equals("magenta"))
                return "white";
            else
                return color2;
        }
        else if(color1.equals("blue")) {
            if (color2.equals("green"))
                return "cyan";
            else if (color2.equals("red"))
                return "magenta";
            else if (color2.equals("yellow"))
                return "white";
            else
                return color2;
        }
        else if(color1.equals("cyan")){
            if(color2.equals("cyan") || color2.equals("blue") || color2.equals("cyan"))
                return "cyan";
            else
                return "white";
        }

        else if(color1.equals("yellow")){
            if(color2.equals("yellow") || color2.equals("red") || color2.equals("green"))
                return "cyan";
            else
                return "white";
        }

        else if(color1.equals("magenta")){
            if(color2.equals("magenta") || color2.equals("blue") || color2.equals("red"))
                return "cyan";
            else
                return "white";
        }
        else
            return "white";
    }
}