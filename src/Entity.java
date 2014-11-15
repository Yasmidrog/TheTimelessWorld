import com.sun.glass.ui.Size;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.geom.Shape;

public class Entity {
    public Animation sprite, up, down, left, right;//спрайты
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

}
