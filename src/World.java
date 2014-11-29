import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class World {
    public Spudi SpiderMan;

    public GameContainer CurrentContainer;//текущее игровое окно
    public TiledMap CurrentMap;//карта, на которой играем

    public ArrayList<Bullet> Bullets = new ArrayList<Bullet>();
    public ArrayList<Entity> Entities = new ArrayList<Entity>();//все сущности в мире
    private boolean[][] blocked;
    Map<String, Animation> Animations = new HashMap<String, Animation>();

    public void init(TiledMap Map, GameContainer Cont) throws SlickException {
        setSprites();
        SpiderMan = new Spudi(-80, 1900);//наш основной герой
        Entities.add(SpiderMan);

        CurrentMap = Map;
        CurrentContainer = Cont;
        Entities.add(new DrOctopus(200, 1900));

        for (Entity ent : Entities) {
            ent.onInit(this);//для всех сущнстей вызываем их версию методов
        }
    }

    public void update(final int delta) throws SlickException {

        for (Entity ent : Entities) {
            ent.onUpdate(delta);
            if (ent.health <= 0) {
                Entities.remove(ent);
                System.gc();
                DrOctopus oct = new DrOctopus(120, 1800);
                oct.onInit(this);
                Entities.add(oct);
            }
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    for (Bullet ent : Bullets) {

                        ent.onUpdate(delta);
                        if(!ent.exists)
                            Bullets.remove(ent);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }.start();
    }

    public void render() throws SlickException {
        //игрока рисуем на месте, а все остальное двигаем вокруг
        CurrentMap.render(0 - (int) SpiderMan.x, 0 - (int) SpiderMan.y);

        for (Entity ent : Entities)
            ent.onRender();

        for (Bullet ent : Bullets)
            ent.onRender();

    }

    protected void GetBlocked() {//получаем непрходиме блоки
        blocked = new boolean[CurrentMap.getWidth()][CurrentMap.getHeight()];//массив с адресами блоков

        for (int xAxis = 0; xAxis < CurrentMap.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < CurrentMap.getHeight(); yAxis++) {
                int tileID = CurrentMap.getTileId(xAxis, yAxis, 0);
                String value = CurrentMap.getTileProperty(tileID, "blocked", "false");
                if ("true".equals(value)) {
                    blocked[xAxis][yAxis] = true;
                }
            }
        }
    }

    //проверяет, проходим ли блок в определенной точке+часть окна, которую мы отодвинули, чтоб игрок был в центре
    public boolean isBlocked(float x, float y) {
        int xBlock = (int) (x + CurrentContainer.getWidth() / 2) / 64;
        int yBlock = (int) (y + CurrentContainer.getHeight() / 2) / 64;
        return blocked[xBlock][yBlock];
    }

    private void setSprites() {
        try {
            Animation lazerSprite =
                    new Animation(new Entity().getImages("data/Lazer"), 30, true);
            Animations.put("Lazer", lazerSprite);
            ////
            Animation DrOctUp =
                    new Animation(new Entity().getImages("data/BADSprites/BADSpriteJump"), 30, true);
            Animations.put("DrOctUp", DrOctUp);
            Animation DrOctUpLeft =
                    new Animation(new Entity().getImages("data/BADSprites/BADSpriteJump"), 30, true);
            Animations.put("DrOctUpLeft", DrOctUpLeft);
            Animation DrOctLeft =
                    new Animation(new Entity().getImages("data/BADSprites/BADSpriteStaticREV"), 30, true);
            Animations.put("DrOctLeft", DrOctLeft);
            Animation DrOctRight =
                    new Animation(new Entity().getImages("data/BADSprites/BADSpriteStatic"), 30, true);
            Animations.put("DrOctRight", DrOctRight);
            ////
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
