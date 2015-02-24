package TheTimeless.game;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A world with map,objects, creatures etc.
 */
public class World implements Serializable {
    static final long serialVersionUID = 1488228l;
    transient public static Loader ResLoader = new Loader();//loader of resources
    public Spudi SpMn;
    public boolean IsExists = true;
    public int delta = 1;
    transient public GameContainer CrCntr;//current window with game
    public int CrLvl;//level of the game
    transient public TiledMap CurrentMap;//map that you playing on
    public ArrayList<Bullet> Bullets;
    public ArrayList<Creature> Creatures;
    public ArrayList<Entity> StaticObjects;
    private javax.swing.Timer EntityTimer;
    private javax.swing.Timer BulletTimer;
    private boolean[][] HardBlocks;//solid blocks
    transient private Image BackgroundImage;
    private states state;
    private int dialognumber = 0;

    public void init(TiledMap Map, GameContainer Cont, int level) throws SlickException {
        try {
            ResLoader = new Loader();
            Bullets = new ArrayList<Bullet>();
            Creatures = new ArrayList<Creature>();
            StaticObjects = new ArrayList<Entity>();
            try {
                BackgroundImage = new Image("data/Backgroundold.png");
            } catch (Exception ex) {
                BackgroundImage = new Image("data/Background.png").getScaledCopy(Display.getWidth(),
                        Display.getHeight());
            }
            //try to set bckground image
            CurrentMap = Map;
            CrCntr = Cont;
            Creatures.addAll(ResLoader.getCreatures(CurrentMap));//add al objects and creatures from map
            StaticObjects.addAll(ResLoader.getObjects(CurrentMap));
            for (Entity ent : StaticObjects)
                ent.onInit(this);
            for (Creature ent : Creatures) {
                ent.onInit(this);
            }
            CrLvl = level;
            checkSpudies();//check if there are more thn one hero on the screen
            GetBlocked();//get hard blocks
            startTimers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startTimers() {

        EntityTimer = new javax.swing.Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int fps = CrCntr.getFPS();
                    if (!CrCntr.isPaused()) {
                        if (fps > 0)
                            updateEntities(delta);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        BulletTimer = new javax.swing.Timer(9, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int fps = CrCntr.getFPS();
                    if (!CrCntr.isPaused()) {
                        if (fps > 0)
                            updateBullets(delta);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        EntityTimer.restart();
        BulletTimer.restart();
    }

    public javax.swing.Timer GetBulletTimer() {
        return BulletTimer;
    }

    public javax.swing.Timer GetEntityTimer() {
        return EntityTimer;
    }

    public void update(final int Delta) throws SlickException {

        delta = Delta;
        if (state == states.SPEAKING) {
            try {
                HandleSpeaking();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void render() throws SlickException {
        if (BackgroundImage != null) {
            BackgroundImage.draw(
                    0, 0,
                    new Color(Color.red.getRed(), Color.red.getGreen(),
                            Color.red.getBlue(),
                            110));
        }
        CurrentMap.render(-(int) SpMn.x - SpMn.SzW / 2, -(int) SpMn.y - SpMn.SzH / 2, 1);
        renderEntities();
        renderBullets();
        if (state == states.SPEAKING) {
            ResLoader.renderString(dialognumber);//if the state is SPEAKING, render current string from dialog
        }
    }

    //rendering
    private void renderEntities() {
        Creature[] ents = new Creature[Creatures.size()];
        System.arraycopy(Creatures.toArray(), 0, ents, 0, Creatures.size());
//copy to the local list to avoid ConcurrentModificationException
        Entity[] entz = new Entity[StaticObjects.size()];
        System.arraycopy(StaticObjects.toArray(), 0, entz, 0, StaticObjects.size());
        for (Entity ent : entz) {
            if (!(SpMn.x - ent.x > ent.SzW + Display.getWidth()
                    || ent.x - SpMn.x > ent.SzW + Display.getWidth()
                    || SpMn.y - ent.y > ent.SzH + Display.getHeight()
                    || ent.y - SpMn.y > ent.SzH + Display.getHeight()
            ))
                ent.onRender();
        }//render if the object is on the string
        for (Creature ent : ents) {
            if (!(SpMn.x - ent.x > ent.SzW + Display.getWidth()
                    || ent.x - SpMn.x > ent.SzW + Display.getWidth()
                    || SpMn.y - ent.y > ent.SzH + Display.getHeight()
                    || ent.y - SpMn.y > ent.SzH + Display.getHeight()
            ))
                ent.onRender();
        }
        for (Entity ent : entz) {
            if (ent instanceof Table)
                ((Table) ent).drawTable();
        }
    }

    private void renderBullets() {
        try {
            Bullet[] ents = new Bullet[Bullets.size()];
            //copy to the local list to avoid ConcurrentModificationException
            System.arraycopy(Bullets.toArray(), 0, ents, 0, Bullets.size());
            for (Creature ent : ents) {
                if (!(SpMn.x - ent.x > ent.SzW + Display.getWidth()
                        || ent.x - SpMn.x > ent.SzW + Display.getWidth()
                        || SpMn.y - ent.y > ent.SzH + Display.getHeight()
                        || ent.y - SpMn.y > ent.SzH + Display.getHeight()
                ))
                    ent.onRender();//render if the object is on the string
            }
        } catch (Exception e) {
            Bullets = new ArrayList<Bullet>();
        }
    }

    private void updateEntities(int delta) {
        try {
            Creature[] ents = new Creature[Creatures.size()];
            System.arraycopy(Creatures.toArray(), 0, ents, 0, Creatures.size());

            for (Creature ent : ents) {
                ent.onUpdate(delta);
            }
            Entity[] entz = new Entity[StaticObjects.size()];
            System.arraycopy(StaticObjects.toArray(), 0, entz, 0, StaticObjects.size());
            for (Entity ent : entz)
                ent.onUpdate(delta);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateBullets(int delta) {
        try {
            Bullet[] ents = new Bullet[Bullets.size()];
            System.arraycopy(Bullets.toArray(), 0, ents, 0, Bullets.size());

            for (Bullet ent : ents) {
                ent.onUpdate(delta);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
    get solid blocks array
     */
    private void GetBlocked() {
        HardBlocks = new boolean[CurrentMap.getWidth() + 1][CurrentMap.getHeight() + 1];//массив с адресами блоков

        for (int xAxis = 0; xAxis < CurrentMap.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < CurrentMap.getHeight(); yAxis++) {
                int tileID = CurrentMap.getTileId(xAxis, yAxis, 0);
                String value = CurrentMap.getTileProperty(tileID, "blocked", "false");
                if ("true".equals(value)) {
                    HardBlocks[xAxis][yAxis] = true;
                }
            }
        }
    }

    public void startSpeaking(int dialogIndex) {
        dialognumber = dialogIndex;
        state = states.SPEAKING;
        //set state to speaking and start to render string
    }

    public boolean isBlocked(float x, float y) {
        boolean blocked;
        try {
            int xBlock = (int) (x + CrCntr.getWidth() / 2) / 64;
            int yBlock = (int) (y + CrCntr.getHeight() / 2) / 64;
            blocked = HardBlocks[xBlock][yBlock];
        } catch (Exception e) {
            blocked = true;
        }
        return blocked;
    }

    private void HandleSpeaking() {
        Input in = CrCntr.getInput();
        EntityTimer.stop();
        if (in.isKeyPressed(Input.KEY_TAB)) {
            dialognumber++;
        }//stop all timers and start to render the string under dialognumber
        if (ResLoader.getString(dialognumber) == null ||
                in.isKeyDown(Input.KEY_ESCAPE) ||
                ResLoader.getString(dialognumber).isEmpty()) {
            state = states.FIGHTING;
            startTimers();
        }
    }

    /*
    check if there are more than hero on the map
    */
    private void checkSpudies() {
        try {
            for (Creature cr : Creatures) {
                if ((cr instanceof Spudi) && SpMn != cr)
                    throw new Exception("It seems that there are more than one HeroSpawner on map");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static enum states {FIGHTING, SPEAKING}//states of the game

}
