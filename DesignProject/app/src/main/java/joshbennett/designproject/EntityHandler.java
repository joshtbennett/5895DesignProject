package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class EntityHandler {

    public void addEntity(Level level, ColorableEntity entity, int position) {
        ArrayList<ColorableEntity> entities = level.getEntities();
        for (ColorableEntity i : entities) {
            if (i.getPosition() == position)
                return;
        }
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
        if (newposition < length - 1 || newposition > (length * length) - length - 1 || newposition % length == 0 || newposition % length == length-1) {
            //the next  cell is out of bounds
            return;
        }


        if (entity != null) {
            if (entity.getColor() == beam.getColor()) {
                entity.setReceived(true);
                return;
            }
            if(entity.getColor() != beam.getColor()){
                entity.setReceived(false);
                return;
            }
        } else if (mirror != null) {
            ArrayList<Beam> reflectedBeams = new ArrayList<>();
            ArrayList<Beam> passedBeams = new ArrayList<>();

            for (String color : deconstructBeam(beam)) {
                if (isComponent(color, mirror.getColor())) {
                    newDirection = reflect(beam.getDirection(), mirror.getAngle());
                    reflectedBeams.add(new Beam(newDirection, color, newposition));
                } else {
                    newDirection = beam.getDirection();
                    passedBeams.add(new Beam(newDirection, color, newposition));
                }
            }

            if (!reflectedBeams.isEmpty()) {
                Beam reflectedBeam = reflectedBeams.get(0);
                for (int i = 0; i < reflectedBeams.size() - 1; i++) {
                    reflectedBeam.setColor(combineColors(reflectedBeam.getColor(), reflectedBeams.get(i+1).getColor()));
                }
                level.getBeams().add(reflectedBeam);
                moveBeam(level, reflectedBeam, reflectedBeam.getPosition());
            }

            if (!passedBeams.isEmpty()) {
                Beam passedBeam = passedBeams.get(0);
                for (int i = 0; i < passedBeams.size() - 1; i++) {
                    passedBeam.setColor(combineColors(passedBeam.getColor(), passedBeams.get(i+1).getColor()));
                }
                level.getBeams().add(passedBeam);
                moveBeam(level, passedBeam, passedBeam.getPosition());
            }
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
        //red green and blue are not combinations of colors and there for have no components other than themselves
        if(color2 == "red"){
            if(color1 == "red")
                return true;
            else
                return false;
        }
        if(color2 == "green"){
            if(color1 == "green")
                return true;
            else
                return false;
        }
        if(color2 == "blue"){
            if(color1 == "blue")
                return true;
            else
                return false;
        }

        //cyan is a combination of blue and green
        if(color2 == "cyan"){
            if(color1 == "blue" || color1 == "green" || color1 == "cyan")
                return true;
            else
                return false;
        }

        //yellow is a combination of red and green
        if(color2 == "yellow"){
            if(color1 == "red" || color1 == "green" || color1 == "yellow")
                return true;
            else
                return false;

        }

        //magenta is a combination of red and blue
        if(color2 == "magenta"){
            if(color1 == "blue" || color1 == "red" || color1 == "magenta"){
                return true;
            }
            else
                return false;
        }

        //white is a combination of all colors
        return true;
    }

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

    String combineColors(String color1, String color2){
        if(color1 == color2)
            return color1;
        if(color1 == "red"){
            if(color2 == "green"){
                return "yellow";
            }
            else if(color2 == "blue"){
                return "magenta";
            }
            else if(color2 == "cyan"){
                return "white";
            }
            else{
                return color2;
            }
        }
        else if(color1 == "green") {
            if (color2 == "red") {
                return "yellow";
            } else if (color2 == "blue") {
                return "cyan";
            } else if (color2 == "magenta") {
                return "white";
            } else {
                return color2;
            }
        }
        else if(color1 == "blue") {
            if (color2 == "green") {
                return "cyan";
            } else if (color2 == "red") {
                return "magenta";
            } else if (color2 == "yellow") {
                return "white";
            } else {
                return color2;
            }
        }
        return "white";
    }



}