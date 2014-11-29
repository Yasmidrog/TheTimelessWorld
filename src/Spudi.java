import org.newdawn.slick.*;

import java.util.Timer;


public class Spudi extends Entity implements IControlable {
    private Timer time = new Timer();
    public boolean invincible;
    private int shoottimer = 0;

    Graphics HealthBack = new org.newdawn.slick.Graphics();
    Graphics HealthFore = new org.newdawn.slick.Graphics();

    public Spudi(float x, float y) {
        Acceleration = 0.4f;
        Speed = -5;
        OnEarth = false;
        this.x = x;
        this.y = y;
        health = 100;
        Name = "Spudi";
    }

    @Override
    public void onInit(World world) {

        try {
            CurrentWorld = world;
            CurrentWorld.GetBlocked();

            Image[] movementUpLeft = getImages("data/SpriteJumpREV");//спрайты для разных положений
            Image[] movementUpRight = getImages("data/SpriteJump");

            Image[] movementLeft = getImages("data/SpriteStaticREV");
            Image[] movementRight = getImages("data/SpriteStatic");
            Image[] shootLeft = getImages("data/SpriteShootREV");
            Image[] shootRight = getImages("data/SpriteShoot");
            SpriteSizeW = movementLeft[0].getWidth();//получаем параметры спрайта
            SpriteSizeH = movementLeft[0].getHeight();
            int duration = 120;
            shootleft = new Animation(shootLeft, duration, true);
            shootright = new Animation(shootRight, 100, true);
            upleft = new Animation(movementUpLeft, duration, true);
            upright = new Animation(movementUpRight, duration, true);
            left = new Animation(movementLeft, duration, true);
            right = new Animation(movementRight, duration, true);
            // Спарйт смотрит вправо
            sprite = right;
            Rect = new org.newdawn.slick.geom.Rectangle(x, y, SpriteSizeW, SpriteSizeH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    //Внизу и далее мы можем увидеть материалы не для людей с неустойчивой психикой
    //гравитация
    public void onUpdate(int delta) {
        shoottimer--;
        if (health < 0) {
            CurrentWorld.CurrentContainer.exit();
            System.out.print("********Health=0********");
        }
        Rect.setX(x);
        Rect.setY(y);

        OnEarth = sideLocked("down");
        if (OnEarth) {

            if (side == "left")
                sprite = left;
            else if (side == "right")
                sprite = right;

        } else if (!OnEarth) {

            if (side == "left")
                sprite = upleft;
            else if (side == "right")
                sprite = upright;
            y += -Acceleration * Speed * 0.25;
        }
        control(delta);
    }

    public void onRender() {
        sprite.draw(CurrentWorld.CurrentContainer.getWidth() / 2, CurrentWorld.CurrentContainer.getHeight() / 2);
        HealthBack.setColor(Color.cyan);
        HealthBack.fillRect(10, 50, 150, 10);
        HealthFore.setColor(Color.red);
        HealthFore.fillRect(10, 50, 150 * health / 100, 10);
    }

    @Override
//TODO Исправить проникновение через блоки, слабонервным не смотреть

    public void control(int delta) {
        Input input = CurrentWorld.CurrentContainer.getInput();
        if (side == "right")
            sprite = upright;
        else if (side == "left") sprite = upleft;

        if (input.isKeyDown(Input.KEY_SPACE)) {
            if (JumpTimer < 217) {
                if (!sideLocked("up")) {
                    y += Acceleration * Speed;
                    JumpTimer++;
                }
            } else if (OnEarth) {
                JumpTimer = 0;
            }
        }

        if (input.isKeyDown(Input.KEY_A)) {
            if (OnEarth) sprite = left;
            else sprite = upleft;
            side = "left";
            if (!sideLocked(side)) {

                x -= delta * 0.2;
            }
        } else if (input.isKeyDown(Input.KEY_D)) {
            if (OnEarth) sprite = right;
            else sprite = upright;

            side = "right";
            if (!sideLocked(side)) {
                x += delta * 0.2;
            }
        }

        if (input.isKeyDown(Input.KEY_W)) {

            if (shoottimer < 0) {
                Bullet bullet = null;

                if (side == "right") {
                    bullet = new Bullet(x + SpriteSizeW-15, y , this, 0.4f, -5);
                } else if (side == "left")
                    bullet = new Bullet(x-30, y, this, 0.4f, 5);

                bullet.onInit(CurrentWorld);
                CurrentWorld.Bullets.add(bullet);

                shoottimer = 200;
            }
            if (side == "right")
                sprite = shootright;

            if (side == "left")
                sprite = shootleft;
        }
    }

    @Override
    public void interact(int delta) {
    }

    void onBlockCollide() {
        Block.OnCollide(CurrentWorld.CurrentMap, (int) x + 64, (int) y + 64);
    }

    void onEntityCollide() {

    }

}
    

