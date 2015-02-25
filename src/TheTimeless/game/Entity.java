package TheTimeless.game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

import java.io.File;
import java.io.Serializable;

public class Entity implements Serializable {
    transient public World CrWld;//current world which the object situated in
    transient public Animation sprite;//the animation of the objects
    transient protected int SzW;//width of sprite
    transient protected int SzH;//height of sprite
    transient public Shape Rect;//collider
    protected float weight;
    public String Name;
    public float x, y;//coords
    public int getSizeH() {
        return SzH;
    }
    public static enum sides{LEFT,RIGHT,UP,DOWN}
    public int getSizeW() {
        return SzW;
    }
    /**
     * @param delta
     * called on every timer tick
     */
    public void onUpdate(int delta) {}
    /**
     * called on initialization of object
     */
    public void onInit(World world) throws SlickException {}
    /**
     * render method
     */
    public void onRender() {}
}
