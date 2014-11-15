import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.javafx.iio.gif.GIFDescriptor;
import com.sun.javafx.iio.gif.GIFImageLoader2;
import javafx.animation.*;
import org.newdawn.slick.*;
import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.tiled.TiledMap;




public class Spudi extends Entity implements IControlable {

    public Spudi(float x, float y) {
        Acceleration = 0.4f;
        Speed = -3;
        OnEarth = false;
        this.x = x;
        this.y = y;

    }

    @Override
    public void onInit(World world) throws SlickException {


        CurrentWorld = world;
        CurrentWorld.GetBlocked();
        Image[] movementUp = getImages("data/SpriteJump.gif");//спрайты для разных положений
        Image[] movementDown = getImages("data/SpriteJump.gif");
        Image[] movementLeft =  getImages("data/SpriteAnimation.gif");
        Image[] movementRight =  getImages("data/SpriteAnimation.gif");
        SpriteSizeW = movementLeft[0].getWidth();//получаем параметры спрайта
        SpriteSizeH = movementLeft[0].getHeight();
     int duration=200;

        up = new Animation(movementUp, duration,false);
        down = new Animation(movementDown, duration, false);
        left = new Animation(movementLeft, duration, false);
        right = new Animation(movementRight, duration, false);
        // Спарйт смотрит вправо
        sprite = right;

        Rect=new Rectangle(0,0,SpriteSizeW,SpriteSizeW);

    }

    @Override
    //Внизу и далее мы можем увидеть материалы не для людей с неустойчивой психикой
    //гравитация
    public void onUpdate(int delta) {
        control(delta);
        if (CurrentWorld.isBlocked(x, y + SpriteSizeH + 2) && CurrentWorld.isBlocked(x + SpriteSizeW,y+SpriteSizeH + 2)) {
            OnEarth = true;
            sprite=down;
        } else  if (!CurrentWorld.isBlocked(x, y + SpriteSizeH + 2) && !CurrentWorld.isBlocked(x + SpriteSizeW,y+SpriteSizeH + 2)){
            OnEarth = false;
            sprite = down;
            sprite.update(delta);
            y += -Acceleration * Speed * 0.25;
        }
        if (!CurrentWorld.isBlocked(x, y + SpriteSizeH + 2) && !CurrentWorld.isBlocked(x + SpriteSizeW,y+SpriteSizeH + 2)) {

        }
    }

    public void onRender() {
        sprite.draw(CurrentWorld.CurrentContainer.getWidth() / 2, CurrentWorld.CurrentContainer.getHeight() / 2);
    }

    @Override
//TODO Исправить проникновение через блоки, слабонервным не смотреть

    public void control(int delta) {
        Input input = CurrentWorld.CurrentContainer.getInput();
        if (input.isKeyDown(Input.KEY_UP)) {
            if (JumpTimer < 130) {
                if (!CurrentWorld.isBlocked(x, y + (Acceleration * Speed))&&
                  ! CurrentWorld.isBlocked(x+SpriteSizeW, y + (Acceleration * Speed)) ) {
                    y += Acceleration * Speed;
                    JumpTimer++;
                }

            } else if (OnEarth) {
                JumpTimer = 0;
            }
        }
        if (input.isKeyDown(Input.KEY_LEFT)) {
            sprite = left;
            if (!CurrentWorld.isBlocked(x - delta * 0.1f, y)&&!CurrentWorld.isBlocked(x - delta * 0.1f, y+SpriteSizeH-1)) {
                sprite.update(delta);
                x -= delta * 0.1f;

            }
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            sprite = right;
            if  (!CurrentWorld.isBlocked(x + SpriteSizeW + delta * 0.1f, y) && !CurrentWorld.isBlocked(x + SpriteSizeW + delta * 0.1f, y + SpriteSizeH-4)) {
                sprite.update(delta);
                x += delta * 0.1f;
            }
        }

    }

    @Override
    public void interact(int delta) {
    }
    void onBlockCollide() {
        Block.OnCollide(CurrentWorld.CurrentMap,(int)x+32,(int)y+32);
    }
    void onEntityCollide()
    {

    }

}
    

