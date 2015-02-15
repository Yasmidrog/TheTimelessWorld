package TheTimeless.game;
import TheTimeless.gui.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;
import java.io.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
public class WizardGame extends BasicGame {
    private  Menu MainMenu;
    public int Level =1;//current level
    public World world;//current world
    private TiledMap GrassMap;//current map
    public WizardGame() {
       super("Timeless");
    }

    public static void main(String[] arguments)throws Exception {
            try {
                AppGameContainer app;
                setNatives();
            //set application parameters
            app = new AppGameContainer(new WizardGame());
            app.setTitle("The Timeless World");
            app.setDisplayMode(app.getScreenWidth()-50,app.getScreenHeight()-50 ,false);
            app.setUpdateOnlyWhenVisible(true);
            app.setMouseGrabbed(false);
            app.setDefaultMouseCursor();
            app.setTargetFrameRate(150);
            app.setIcon("data/icons/icon.png");
            app.start();
            setParams(app);

        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    public static void setParams(GameContainer cntr) {

        if(!ConfigReader.getConf("fps").equals("null"))
        cntr.setShowFPS(Boolean.valueOf(ConfigReader.getConf("fps")));
        if(!ConfigReader.getConf("updateonlyifvisible").equals("null"))
        cntr.setUpdateOnlyWhenVisible(Boolean.valueOf(ConfigReader.getConf("updateonlyifvisible")));
        if(!ConfigReader.getConf("smoothdeltas").equals("null"))
        cntr.setSmoothDeltas(Boolean.valueOf(ConfigReader.getConf("smoothdeltas")));
    }
    private static void  setNatives() {
        //set path to the natives
    try {
        //check the os
        if (isUnix())
            System.setProperty("java.library.path", new File("lib/natives-linux/").getAbsolutePath());
        else if (isMac())
            System.setProperty("java.library.path", new File("lib/natives-mac/").getAbsolutePath());
        else if (isWindows())
            System.setProperty("java.library.path", new File("lib/natives-windows/").getAbsolutePath());
        System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");

        java.lang.reflect.Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");

        fieldSysPath.setAccessible(true);
        try {
            fieldSysPath.set(null, null);
        } catch (Exception ex) {
            System.exit(1);
        }
    } catch (Exception ex) {
        System.exit(1);
    }
}
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        MainMenu=new Menu(this,container);
        GrassMap = new TiledMap("data/levels/"+ Level +"/"+"world.tmx");
        world = new World();
        world.init(GrassMap, container,Level);

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if(!container.isPaused()) {
            world.update(delta);
        }
        Input input = container.getInput();

        if(input.isKeyPressed(Input.KEY_P)){
            Date d = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("dd_MM_yyyy_hhmmss");
            save("data/saves/world_lev." + Level + "_" + format1.format(d)+"-fast"+".ttws");
        }

        if(input.isKeyPressed(Input.KEY_O)) {
            String[] saves=new File("data/saves").list();
            load("data/saves/"+saves[0], container);
        }

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
           if(!container.isPaused()){
               container.pause(); //pause or continue game
               MainMenu.setShown(true);
           }
            else if(container.isPaused()){
               container.setPaused(false);
               MainMenu.setShown(false);
           }
        }
        if(MainMenu.isShown()){
            if(container.isFullscreen()) {
                Mouse.setCursorPosition(0, 0);
            }
        }
        if(!world.IsExists) {
            try {
                container.pause();
                try {
                    Level++;
                    GrassMap = new TiledMap("data/levels/" + Level + "/" + "world.tmx");
                    world = new World();
                    world.init(GrassMap, container,Level);

                } catch (Exception e) {
                    GrassMap = new TiledMap("data/levels/" + 1+ "/" + "world.tmx");
                    world = new World();
                    world.init(GrassMap, container,1);
                }
                container.setPaused(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //try to load map for the next level
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        try {
            GL11.glColor3f(255, 255, 255);
            world.render();
        }catch(Exception e){
            GL11.glColor3f(255, 255, 255);
            e.printStackTrace();
        }
        MainMenu.render();
    }//render world&objects or menu

    /**
     * Save game
     */
    public void save(String adress)
    {
        try {
        ObjectOutputStream wldout = new ObjectOutputStream(new FileOutputStream(adress));
            Serializator ser=new Serializator();
            ser.Map="data/levels/"+Integer.toString(Level)+"/"+"world.tmx";
            ser.SCrts.add(0,new SerializableOne(world.SpMn));
            for(Entity ent:world.StaticObjects)
            {
                ser.SEnts.add(new SerializableOne(ent));
            }
            for(Creature ent:world.Creatures)
            {
                ser.SCrts.add(new SerializableOne(ent));
            }
            ser.level=Level;
        wldout.writeObject(ser);
        //Create SerializableOnes for each object and write to file
       }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Restore map and objects from serialization file
     */
    public void load(String adress,GameContainer cntr)
    {
        //I don't know anything about how it works
        cntr.pause();
          try {
          ObjectInputStream wldin = new ObjectInputStream(new FileInputStream(adress));
          Serializator s = (Serializator) wldin.readObject();//get serializator
              Level=s.level;
              GrassMap = new TiledMap(s.Map);//create map from string
              world = new World();
              world.init(GrassMap,cntr,Level);//init new world
              world.Creatures = new ArrayList<Creature>();//replace world new lists
              world.StaticObjects= new ArrayList<Entity>();
              for (int i=0;i<s.SCrts.size();i++)
              {
                  Constructor c = Class.forName(s.SCrts.get(i).Type).getConstructor(Float.TYPE, Float.TYPE);
                  Creature ent = (Creature) c.newInstance(s.SCrts.get(i).sx, s.SCrts.get(i).sy);
                  ent.Counters = s.SCrts.get(i).Cntrs;
                  ent.Health = s.SCrts.get(i).sHealth;
                  ent.Side = s.SCrts.get(i).Side;
                  ent.onInit(world);
                  world.Creatures.add(i, ent);
                  //get all parameters from serializableOne and add new creature to the  world's list
              }
              for (int i=0;i<s.SEnts.size();i++) {
                  if(s.SEnts.get(i).Type.equals("Table"))
                  {
                      Entity ent=new Table(s.SEnts.get(i).sx, s.SEnts.get(i).sy,s.SEnts.get(i).tableText);
                      ent.onInit(world);
                      world.StaticObjects.add(i, ent);
                  }else if(!(s.SEnts.get(i).Type.equals("Table"))) {
                      Constructor c = Class.forName(s.SEnts.get(i).Type).getConstructor(Float.TYPE, Float.TYPE);
                      Entity ent = (Entity) c.newInstance(s.SEnts.get(i).sx, s.SEnts.get(i).sy);
                      ent.onInit(world);
                      world.StaticObjects.add(i, ent);
                      //get all parameters from serializableOne and add new object to the  world's list
                  }
              }
              world.SpMn=(Spudi) world.Creatures.get(0);//get main hero
              for (Creature cr: world.Creatures)
              {
                  if((cr instanceof Spudi)&& world.SpMn!=cr)
                      world.Creatures.remove(cr);
              }//delete all other heroes
              cntr.setPaused(false);
           }

          catch (ConcurrentModificationException s){}
          catch(Exception e){
              e.printStackTrace();
          }
    }

}