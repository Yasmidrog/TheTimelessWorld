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
    protected org.newdawn.slick.Image[] getImages(String adr) {

        try {
            ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();

            ImageInputStream in = ImageIO.createImageInputStream(new File(adr));
            reader.setInput(in);
            int count = reader.getNumImages(true);
            Texture[] textures = new Texture[count];
            Image images[]=new Image[count];
            for (int i = 0; i < count; i++) {
                BufferedImage image = reader.read(i);
                textures[i] =toT(image);
            }
            for (int i = 0; i < count; i++)
                images[i]=new Image(textures[i]);
            return images;
        } catch (IOException ex) {
            return null;
        }

    }
    private Texture  toT(BufferedImage image) {
try {

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(image, "gif", os);
    InputStream is = new ByteArrayInputStream(os.toByteArray());
    return TextureLoader.getTexture("GIF", is);
}catch(Exception ex) {
    ex.printStackTrace();
    return null;
}


    }
}
