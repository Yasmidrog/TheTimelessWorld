import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
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
        Image[] movementUp = {new Image("data/gg.png"), new Image("data/gg.png")};//спрайты для разных положений
        Image[] movementDown = {new Image("data/gg.png"), new Image("data/gg.png")};
        Image[] movementLeft = {new Image("data/gg.png"), new Image("data/gg.png")};
        Image[] movementRight = {new Image("data/gg.png"), new Image("data/gg.png")};
        SpriteSizeW = movementLeft[0].getWidth();//получаем параметры спрайта
        SpriteSizeH = movementLeft[0].getHeight();
        int[] duration = {300, 300};
        up = new Animation(movementUp, duration, false);
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
        if (CurrentWorld.isBlocked(x, y + SpriteSizeH + 1)) {
            OnEarth = true;
        } else {
            OnEarth = false;
        }
        if (!CurrentWorld.isBlocked(x, y + SpriteSizeH +1)) {
            sprite = down;
            sprite.update(delta);
            y += -Acceleration * Speed * 0.25;
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
            if (!CurrentWorld.isBlocked(x - delta * 0.1f, y)) {
                sprite.update(delta);
                x -= delta * 0.1f;

            }
        } else if (input.isKeyDown(Input.KEY_RIGHT)) {
            sprite = right;
            if  (!CurrentWorld.isBlocked(x + SpriteSizeW + delta * 0.1f, y) && !CurrentWorld.isBlocked(x + SpriteSizeW + delta * 0.1f, y + SpriteSizeH-1)) {
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
    

