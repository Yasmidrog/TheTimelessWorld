package TheTimeless.game;

import TheTimeless.gui.VTextRender;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.tiled.TiledMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 *A world with map,objects, creatures etc.
 */
public class World implements Serializable {
    public Spudi SpMn;
    public boolean IsExists =true;

    private javax.swing.Timer EntityTimer;
    private javax.swing.Timer BulletTimer;
    public int delta=1;
    private boolean[][] HardBlocks;
    static  final long serialVersionUID=1488228l;
    transient private Image BackgroundImage;
    transient public GameContainer CrCntr;//текущее игровое окно
    public int CrLvl;
    transient public TiledMap CurrentMap;//карта, на которой играем
    transient public static Loader ResLoader=new Loader();
    public ArrayList<Bullet> Bullets ;
    public ArrayList<Creature> Creatures ;
    public ArrayList<Entity> StaticObjects ;
    private static enum  states{FIGHTING,SPEAKING};
    private states state;
    private int dialognumber=0;

    public void init(TiledMap Map, GameContainer Cont,int level) throws SlickException {
        try {
            ResLoader=new Loader();
            Bullets = new ArrayList<Bullet>();
            Creatures = new ArrayList<Creature>();
            StaticObjects = new ArrayList<Entity>();
            try{
               BackgroundImage= new Image("data/Backgroundold.png");
            }
            catch (Exception ex){
             BackgroundImage = new Image("data/Background.png").getScaledCopy(Display.getWidth(),
                                                                              Display.getHeight());
            }
            CurrentMap = Map;
            CrCntr = Cont;
            Creatures.addAll(ResLoader.getCreatures(CurrentMap));
            StaticObjects.addAll(ResLoader.getObjects(CurrentMap));
            for (Entity ent : StaticObjects)
                ent.onInit(this);
            for (Creature ent : Creatures) {
                ent.onInit(this);//для всех сущнстей вызываем их версию методов
            }
            CrLvl=level;
            checkSpudies();
            GetBlocked();
            startTimers();
        }
   catch(Exception e){
        e.printStackTrace();
    }
    }

public void startTimers() {

     EntityTimer = new javax.swing.Timer(8, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int fps = CrCntr.getFPS();
                if (!CrCntr.isPaused()) {
                    if (fps > 0)
                        updateEntities(delta);
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    });
  BulletTimer = new javax.swing.Timer(4, new ActionListener() {
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

    public javax.swing.Timer GetBulletTimer(){
        return BulletTimer;
    }
    public javax.swing.Timer GetEntityTimer(){
        return EntityTimer;
    }

    public void update(final int Delta) throws SlickException {
        Input in=CrCntr.getInput();
        delta=Delta;
        try {
            if (state == states.SPEAKING) {
                EntityTimer.stop();
                if (in.isKeyPressed(Input.KEY_TAB)) {
                    dialognumber++;
                }
                if (ResLoader.getString(dialognumber)==null||
                             in.isKeyDown(Input.KEY_ESCAPE)||
                        ResLoader.getString(dialognumber).isEmpty()) {
                    state = states.FIGHTING;
                    startTimers();
                    return;
                }
            }
        }catch (Exception e){}

}

    public void render() throws SlickException {
          BackgroundImage.draw(
         0,0,
          new Color(Color.red.getRed(), Color.red.getGreen(),
          Color.red.getBlue(), 110));
            CurrentMap.render(-(int) SpMn.x - SpMn.SzW / 2, -(int) SpMn.y - SpMn.SzH / 2,1);

            renderEntities();
            renderBullets();
        if(state==states.SPEAKING)
        {
            ResLoader.renderString(dialognumber);
        }
    }
    //rendering
    private void renderEntities()
    {
        Creature[] ents=new Creature[Creatures.size()];
        System.arraycopy(Creatures.toArray(),0,ents,0, Creatures.size());

       Entity [] entz=new Entity[StaticObjects.size()];
        System.arraycopy(StaticObjects.toArray(), 0, entz, 0, StaticObjects.size());
        for(Entity ent:entz) {
            if(!(   SpMn.x - ent.x > ent.SzW + Display.getWidth()/2
                    || ent.x - SpMn.x > ent.SzW + Display.getWidth()/2
                    ||SpMn.y - ent.y > ent.SzH + Display.getHeight()/2
                    ||ent.y - SpMn.y > ent.SzH + Display.getHeight()/2
            ))
            ent.onRender();
        }
        for (Creature ent : ents) {
            if(!(   SpMn.x - ent.x > ent.SzW + Display.getWidth()/2
                    || ent.x - SpMn.x > ent.SzW + Display.getWidth()/2
                    ||SpMn.y - ent.y > ent.SzH + Display.getHeight()/2
                    ||ent.y - SpMn.y > ent.SzH + Display.getHeight()/2
            ))
            ent.onRender();
        }
        for(Entity ent:entz) {
            if(ent instanceof Table)
                ((Table)ent).drawTable();
        }
    }
    private void renderBullets()
    {
        try {
            Bullet[] ents = new Bullet[Bullets.size()];
            System.arraycopy(Bullets.toArray(), 0, ents, 0, Bullets.size());
            for (Creature ent : ents) {
                if(!(   SpMn.x - ent.x > ent.SzW + Display.getWidth()/2
                        || ent.x - SpMn.x > ent.SzW + Display.getWidth()/2
                        ||SpMn.y - ent.y > ent.SzH + Display.getHeight()/2
                        ||ent.y - SpMn.y > ent.SzH + Display.getHeight()/2
                ))
                ent.onRender();
            }
        }catch(Exception e){
            Bullets=new ArrayList<Bullet>();
        }
    }
    private void updateEntities(int delta) {
        try {
            Creature[] ents=new Creature[Creatures.size()];
            System.arraycopy(Creatures.toArray(), 0, ents, 0, Creatures.size());

            for (Creature ent : ents) {
                ent.onUpdate(delta);
            }
            Entity [] entz=new Entity[StaticObjects.size()];
            System.arraycopy(StaticObjects.toArray(),0,entz,0,StaticObjects.size());
            for(Entity ent:entz)
                ent.onUpdate(delta);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void updateBullets(int delta)
    {
        try {
            Bullet[] ents=new Bullet[Bullets.size()];
            System.arraycopy(Bullets.toArray(), 0, ents, 0, Bullets.size());

            for (Bullet ent : ents) {
                ent.onUpdate(delta);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void GetBlocked() {//получаем непрходиме блоки
        HardBlocks = new boolean[CurrentMap.getWidth()+1][CurrentMap.getHeight()+1];//массив с адресами блоков

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
  public void startSpeaking(int dialogIndex){
    dialognumber=dialogIndex;
    state=states.SPEAKING;
}
    //проверяет, проходим ли блок в определенной точке+часть окна, которую мы отодвинули, чтоб игрок был в центре
    public boolean isBlocked(float x, float y) {
        boolean blocked;
        try {
            int xBlock = (int) (x + CrCntr.getWidth() / 2) / 64;
            int yBlock = (int) (y + CrCntr.getHeight() / 2) / 64;
            blocked=HardBlocks[xBlock][yBlock];
        }catch(Exception e){
            blocked=true;
        }
        return blocked;
    }
    private void checkSpudies(){
   try {
    for (Creature cr: Creatures)
    {
        if ((cr instanceof Spudi) && SpMn != cr)
            throw new Exception("It seems that there are more than one HeroSpawner on map");
      }
}catch(Exception e){
    e.printStackTrace();
}
}

}
