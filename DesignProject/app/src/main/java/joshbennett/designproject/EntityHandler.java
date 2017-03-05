package joshbennett.designproject;

import java.util.ArrayList;

/**
 * Created by Josh Bennett on 2/16/2017.
 */

public class EntityHandler {

    boolean checkCollision() {
        return false;
    }

    public void addEntity(Level level, ColorableEntity entity, int position) {
        ArrayList<ColorableEntity> entities = level.getEntities();
        entities.set(position, entity);
        level.setEntities(entities);
    }

    public void addWall(Level level, Wall entity, int position) {
        ArrayList<Wall> walls = level.getWalls();
        walls.set(position, entity);
        level.setWalls(walls);
    }

    public void addMirror(Level level, Mirror entity, int position) {
        ArrayList<Mirror> mirrors = level.getMirrors();
        mirrors.set(position, entity);
        level.setMirrors(mirrors);
        int x = 44;
    }

    public void removeMirror(Level level, int position) {
        ArrayList<Mirror> mirrors = level.getMirrors();
        mirrors.set(position, null);
        level.setMirrors(mirrors);
    }

    public void flipMirror(Level level, Mirror mirror, int position){

        ArrayList<Mirror> mirrors = level.getMirrors();
        if(mirror.getAngle() == 45) {
            mirror.setAngle(135);
            mirrors.set(position, mirror);
        }
        if(level.getMirrors().get(position).getAngle() == 135) {
            mirror.setAngle(45);
            mirrors.set(position, mirror);
        }
    }
}
