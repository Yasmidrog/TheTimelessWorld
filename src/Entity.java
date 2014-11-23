import com.sun.glass.ui.Size;
import com.sun.javafx.iio.ImageStorage;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.*;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.geom.Shape;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.*;
import java.io.*;


public class Entity {
    public float  health;
    public Animation sprite, upleft,upright, left, right;//спрайты
    public float x ,y;
    public  boolean OnEarth;//на земле ли Сущность?
    public int Speed;
 protected  boolean blocked[][];
   public World CurrentWorld;
    protected float Acceleration;
    protected int SpriteSizeW;//парметры спрайта
    protected int SpriteSizeH;

   protected int JumpTimer=0;//регулирует высоту прыжка

    public int getSizeH(){return SpriteSizeH;}//параметры спрайта
    public int getSizeW(){return SpriteSizeW;}

    public float getAccelration(){return Acceleration;}

    public void onUpdate(int delta){}
    public void onInit(World world) throws SlickException {}
    public void onRender(){}
    public Shape Rect;//для столкновений

    protected void OnEntityCollide(Entity ent){}
    protected void OnBlockCollide(){}
    protected org.newdawn.slick.Image[] getImages(String adr) throws SlickException
    {
     String files[]=new File(adr).list();
     Image images[]=new Image[files.length];
            for(int i=0;i<files.length;i++)
                images[i]=new Image(adr+"/"+files[i]);
            return images;
    }
    protected boolean sideLocked(final String side){


        boolean blocked=false;

        if(side.toLowerCase()=="up") {

            for(int j=0;j<=SpriteSizeW;j+=60)
            {
                if (CurrentWorld.isBlocked(x+j, y + (Acceleration * Speed)))
                    blocked=true;
            }
            return blocked;
        }
        if(side.toLowerCase()=="left") {
            for(int j=2;j<=SpriteSizeH;j+=60)
            {

                if (CurrentWorld.isBlocked(x  + 1, y+j))
                    blocked=true;
            }
            return blocked;
        }
        if(side.toLowerCase()=="right") {
            for(int j=2;j<=SpriteSizeH;j+=60)
            {

                if (CurrentWorld.isBlocked(x + SpriteSizeW + 1, y+j))
                    blocked=true;
            }
            return blocked;
        }
        if(side.toLowerCase()=="down") {

            for(int j=4;j<=SpriteSizeW;j+=60)
            {
                if (CurrentWorld.isBlocked(x+j, y + SpriteSizeH + 2))
                    blocked=true;
            }
            return blocked;
        }

else {
        try {
                throw new Exception("Wrong side");
            }catch(Exception ex) {
                ex.printStackTrace();
            }
           return false;
        }
    }

}
