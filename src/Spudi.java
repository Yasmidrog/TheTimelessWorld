import org.newdawn.slick.*;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;





public class Spudi extends Entity implements IControlable {

    public boolean invincible;
    Graphics HealthBack=new org.newdawn.slick.Graphics();
    Graphics HealthFore=new org.newdawn.slick.Graphics();
    public Spudi(float x, float y) {
        Acceleration = 0.4f;
        Speed = -5;
        OnEarth = false;
        this.x = x;
        this.y = y;
        health=100;
    }

    @Override
    public void onInit(World world) throws SlickException {


        CurrentWorld = world;
        CurrentWorld.GetBlocked();

        Image[] movementUpLeft = getImages("data/SpriteJumpREV");//спрайты для разных положений
        Image[] movementUpRight = getImages("data/SpriteJump");

        Image[] movementLeft =  getImages("data/SpriteStaticREV");
        Image[] movementRight =  getImages("data/SpriteStatic");
        SpriteSizeW = movementLeft[0].getWidth();//получаем параметры спрайта
        SpriteSizeH = movementLeft[0].getHeight();
     int duration=30;

        upleft = new Animation(movementUpLeft, duration,true);
        upright = new Animation(movementUpRight, duration, true);
        left = new Animation(movementLeft, duration, true);
        right = new Animation(movementRight, duration, true);
        // Спарйт смотрит вправо
        sprite = right;

        Rect=new org.newdawn.slick.geom.Rectangle(x,y,SpriteSizeW,SpriteSizeW);

    }

    @Override
    //Внизу и далее мы можем увидеть материалы не для людей с неустойчивой психикой
    //гравитация
    public void onUpdate(int delta) {

if(health<0)
    CurrentWorld.CurrentContainer.exit();
        Rect.setX(x);
        Rect.setY(y);
        control(delta);
        OnEarth=sideLocked("down");
        if (OnEarth) {

            if(sprite==left||sprite==upleft)
            sprite=left;
            else  if(sprite==right||sprite==right)
                sprite=right;

        } else  if (!OnEarth){

            if(sprite==left||sprite==upleft)
                sprite=upleft;
            else  if(sprite==right||sprite==right)
                sprite=upright;
            y += -Acceleration * Speed * 0.25;
        }

    }

    public void onRender() {
        sprite.draw(CurrentWorld.CurrentContainer.getWidth() / 2, CurrentWorld.CurrentContainer.getHeight() / 2);
        HealthBack.setColor(Color.cyan);
        HealthBack.fillRect(10, 50, 50, 10);
        HealthFore.setColor(Color.red);
        HealthFore.fillRect(10, 50, 50 * health / 100, 10);
    }

    @Override
//TODO Исправить проникновение через блоки, слабонервным не смотреть

    public void control(int delta) {
        Input input = CurrentWorld.CurrentContainer.getInput();
        if (input.isKeyDown(Input.KEY_SPACE)) {
            if (JumpTimer < 217) {
if(!sideLocked("up")) {
    y += Acceleration * Speed;
    JumpTimer++;
}
            } else if (OnEarth) {
                JumpTimer = 0;
            }
        }
        if (input.isKeyDown(Input.KEY_W)) {

        }

        if (input.isKeyDown(Input.KEY_A)) {
            if(OnEarth) sprite=left;
            else sprite=upleft;
            if (!sideLocked("left")) {

                x -= delta * 0.2;

            }
        } else if (input.isKeyDown(Input.KEY_D)) {
            if(OnEarth) sprite=right;
            else sprite=upright;

            if  (!sideLocked("right")) {

                x += delta * 0.2;

            }
        }

    }

    @Override
    public void interact(int delta) {
    }
    void onBlockCollide() {
        Block.OnCollide(CurrentWorld.CurrentMap,(int)x+64,(int)y+64);
    }
    void onEntityCollide()
    {

    }

}
    

