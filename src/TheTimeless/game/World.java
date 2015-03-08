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
    transient public static Loader ResLoader = new Loader();//loader of resources
    public Spudi SpMn;
    public boolean exsists = true;
    public int delta = 1;

    transient public GameContainer CrCntr;//current window with game
    public int CrLvl;//level of the game
    transient public TiledMap CurrentMap;//map that you playing on
    public ArrayList<Bullet> Bullets;
    public ArrayList<Creature> Creatures;
    public ArrayList<Entity> StaticObjects;
    javax.swing.Timer EntityTimer;
    javax.swing.Timer BulletTimer;
    private boolean[][] HardBlocks;//solid blocks
    transient private Image BackgroundImage;
    public states state;
    public int dialognumber = 0;

    public static enum states {FIGHTING, SPEAKING}//states of the game

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
            HardBlocks = GetBlocked(CurrentMap);//get hard blocks
            for (Creature e : Creatures) {
                if (e instanceof Spudi)
                    SpMn = (Spudi) e;
            }
            checkSpudies();//check if there are more thn one hero on the screen
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
        BulletTimer = new javax.swing.Timer(6, new ActionListener() {
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
        try {
            if(CrCntr.getFPS()>0) {
                CurrentMap.render(-(int) SpMn.x - SpMn.SzW / 2, -(int) SpMn.y - SpMn.SzH / 2, 1);
                renderBullets();
                renderEntities();
                if (state == states.SPEAKING) {
                    ResLoader.renderString(dialognumber);//if the state is SPEAKING,
                                                        // render current string from dialog
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
            ) && ent.renderBehind)
                ent.onRender();
        }
        for (Creature ent : ents) {
            if (!(SpMn.x - ent.x > ent.SzW + Display.getWidth()
                    || ent.x - SpMn.x > ent.SzW + Display.getWidth()
                    || SpMn.y - ent.y > ent.SzH + Display.getHeight()
                    || ent.y - SpMn.y > ent.SzH + Display.getHeight()
            ))
                ent.onRender();
        }
        for (Entity ent : entz) {
            if (!(SpMn.x - ent.x > ent.SzW + Display.getWidth()
                    || ent.x - SpMn.x > ent.SzW + Display.getWidth()
                    || SpMn.y - ent.y > ent.SzH + Display.getHeight()
                    || ent.y - SpMn.y > ent.SzH + Display.getHeight()
            ) && !ent.renderBehind)
                ent.onRender();
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
    public static boolean[][] GetBlocked(TiledMap Map) {
        boolean blocks[][] = new boolean[Map.getWidth() + 1][Map.getHeight() + 1];

        for (int xAxis = 0; xAxis < Map.getWidth(); xAxis++) {
            for (int yAxis = 0; yAxis < Map.getHeight(); yAxis++) {
                int tileID = Map.getTileId(xAxis, yAxis, 0);
                String value = Map.getTileProperty(tileID, "blocked", "false");
                if ("true".equals(value)) {
                    blocks[xAxis][yAxis] = true;
                }
            }
        }
        return blocks;
    }

    public void setBlocked(boolean[][] blocks) {
        HardBlocks = blocks;
    }

    public void startSpeaking(int dialogIndex) {
        dialognumber = dialogIndex;
        state = states.SPEAKING;
        //set state to speaking and start to render string
    }
    public void startSpeaking() {
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
        try {
            Input in = CrCntr.getInput();
            EntityTimer.stop();
            if (in.isKeyPressed(Input.KEY_TAB)) {
                if (ResLoader.getDialogString(dialognumber + 1) != null &&
                        !(ResLoader.getDialogString(dialognumber + 1).isEmpty()))
                    dialognumber++;
                else {
                    if (ResLoader.getDialogString(dialognumber + 1) != null)
                        dialognumber += 2;//move to the next block
                    state = states.FIGHTING;
                    startTimers();
                    return;
                }
            }//stop all timers and start to render the string under dialognumber
            if (in.isKeyDown(Input.KEY_GRAVE)) {
                state = states.FIGHTING;
                startTimers();
            }
        }catch (IndexOutOfBoundsException e) {
            //if there are no lines below
            state = states.FIGHTING;
            startTimers();
        }catch (Exception e){
            e.printStackTrace();
            state = states.FIGHTING;
            startTimers();
        }
    }

    /**
    check if there are more than hero on the map
    */
    public void checkSpudies() {
        try {
            for (Creature cr : Creatures) {
                if ((cr instanceof Spudi) && SpMn != cr)
                    throw new Exception("It seems that there are more than one HeroSpawner on map");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     spawn new Entity
     */
    public void spawn(Entity ent) {
        try {
            if (!(ent instanceof Creature)) {
                ent.onInit(this);
                StaticObjects.add(ent);
            } else {
                throw new Exception("Wrong type of the object to spawn\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     spawn new Crearure
     */
    public void spawn(Creature ent) {
        try {
            if (ent instanceof Creature) {
                ent.onInit(this);
                Creatures.add(ent);
            }
            else {
                throw new Exception("Wrong type of the object to spawn\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


