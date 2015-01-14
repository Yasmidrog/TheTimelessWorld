package TheTimeless.game;

import TheTimeless.gui.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;

import java.awt.*;
import java.io.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
class WizardGame extends BasicGame {
    private  guiContainer gui,params;
    private boolean ShowMenu;//is the menu shown
    private static AppGameContainer app;
    transient public VTextRender mainFont;//font to draw menu
    public int Level =1;//current level
    public World World;//current world
    private TiledMap GrassMap;//current map
    public WizardGame() {
       super("Timeless");
    }

    public static void main(String[] arguments)throws Exception {
            try {
                setNatives();
            //set application parameters
            app = new AppGameContainer(new WizardGame());
            app.setTitle("The Timeless");
            app.setDisplayMode(1000, 1000,false);
            app.setUpdateOnlyWhenVisible(true);
            app.setMouseGrabbed(false);
            app.setDefaultMouseCursor();
            app.setTargetFrameRate(150);
            app.start();
            setParams();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    private static void setParams() {
        if(!ConfigReader.getConf("fps").equals("null"))
        app.setShowFPS(Boolean.valueOf(ConfigReader.getConf("fps")));
        if(!ConfigReader.getConf("updateonlyifvisible").equals("null"))
        app.setUpdateOnlyWhenVisible(Boolean.valueOf(ConfigReader.getConf("updateonlyifvisible")));
        if(!ConfigReader.getConf("smoothdeltas").equals("null"))
        app.setSmoothDeltas(Boolean.valueOf(ConfigReader.getConf("smoothdeltas")));

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

        mainFont=new VTextRender(46,"Sans");
        setGui();
        GrassMap = new TiledMap("data/levels/"+ Level +"/"+"world.tmx");
        World = new World();
        World.init(GrassMap, container);

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if(!app.isPaused()) {
            World.update(delta);
        }
        Input input = app.getInput();

        if(input.isKeyPressed(Input.KEY_P))
            save("world.sss");
        if(input.isKeyPressed(Input.KEY_O))
            load("world.sss");

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
           if(!app.isPaused()){
               app.pause(); ShowMenu =true;//pause or continue game
               gui.setShown(true);
           }
            else if(app.isPaused()){
               app.setPaused(false);
               ShowMenu =false;
               gui.setShown(false);
               params.setShown(false);
           }
        }
        if(!ShowMenu){
            if(app.isFullscreen()) {
                Mouse.setCursorPosition(0, 0);
            }
            gui.setShown(false);
            params.setShown(false);
        }
        if(!World.IsExists) {
            try {
                app.pause();
                try {
                    Level++;
                    GrassMap = new TiledMap("data/levels/" + Level + "/" + "world.tmx");
                    World = new World();
                    World.init(GrassMap, container);

                } catch (Exception e) {

                    GrassMap = new TiledMap("data/levels/" + 1+ "/" + "world.tmx");
                    World = new World();
                    World.init(GrassMap, container);
                }
                app.pause();
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
            World.render();
            gui.render();
            params.render();
        }catch(Exception e){
            GL11.glColor3f(255, 255, 255);
            gui.render();
            e.printStackTrace();
        }
    }//render world&objects or menu

    /**
     * Save game
     */
    private void save(String adress)
    {
        try {
        ObjectOutputStream wldout = new ObjectOutputStream(new FileOutputStream(adress));
        wldout.writeObject(new Serializator(World, Level));
        //Create SerializableOnes for each object and write to file
       }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Restore map and objects from serialization file
     */
    private void load(String adress)
    {
        //i don't know how it works
        app.pause();
          try {
          ObjectInputStream wldin = new ObjectInputStream(new FileInputStream(adress));
          Serializator s = (Serializator) wldin.readObject();//get serializator
              GrassMap = new TiledMap(s.Map);//create map from string
              World = new World();
              World.init(GrassMap, app);//init new world
              World.Creatures = new ArrayList<Creature>();//replace world new lists
              World.StaticObjects= new ArrayList<Entity>();
              for (int i=0;i<s.SCrts.size();i++)
              {
                  Constructor c = Class.forName(s.SCrts.get(i).Type).getConstructor(Float.TYPE, Float.TYPE);
                  Creature ent = (Creature) c.newInstance(s.SCrts.get(i).sx, s.SCrts.get(i).sy);
                  ent.Counters = s.SCrts.get(i).Cntrs;
                  ent.Health = s.SCrts.get(i).sHealth;
                  ent.Side = s.SCrts.get(i).Side;
                  ent.onInit(World);
                  World.Creatures.add(i, ent);
                  //get all parameters from serializableOne and add new creature to the  world's list
              }
              for (int i=0;i<s.SEnts.size();i++) {
                  if(s.SEnts.get(i).Type.equals("Table"))
                  {
                      Entity ent=new Table(s.SEnts.get(i).sx, s.SEnts.get(i).sy,s.SEnts.get(i).tableText);
                      ent.onInit(World);
                      World.StaticObjects.add(i, ent);
                  }else if(!(s.SEnts.get(i).Type.equals("Table"))) {
                      Constructor c = Class.forName(s.SEnts.get(i).Type).getConstructor(Float.TYPE, Float.TYPE);
                      Entity ent = (Entity) c.newInstance(s.SEnts.get(i).sx, s.SEnts.get(i).sy);
                      ent.onInit(World);
                      World.StaticObjects.add(i, ent);
                      //get all parameters from serializableOne and add new object to the  world's list
                  }
              }
              World.SpMn=(Spudi) World.Creatures.get(0);//get main hero
              for (Creature cr: World.Creatures)
              {
                  if((cr instanceof Spudi)&& World.SpMn!=cr)
                      World.Creatures.remove(cr);
              }//delete all other heroes
              app.setPaused(false);
           }
          catch (ConcurrentModificationException s){}
          catch(Exception e){
              e.printStackTrace();
          }
    }
    public void setGui(){
        gui =new guiContainer(app);
        gui.setShown(false);
        params =new guiContainer(app);
        params.setShown(false);

        gui.add(new guiButton(app,"Resume", mainFont,app.getWidth()/2-  mainFont.getWidth("Resume")/2,
                app.getHeight()/2-  mainFont.getHeight()/2-150){
            @Override
            public void onClicked(){
                app.setPaused(false);
                ShowMenu =false;
                gui.setShown(false);
            }
        });

        gui.add(new guiButton(app,"Parameters",  mainFont,app.getWidth()/2-  mainFont.getWidth("Parameters")/2,
                app.getHeight()/2-  mainFont.getHeight()/2){
            @Override
            public void onClicked(){
gui.setShown(false);
                params.setShown(true);
            }
        });


        gui.add(new guiButton(app,"Exit", mainFont,app.getWidth()/2-  mainFont.getWidth("Exit")/2,
                app.getHeight()/2-  mainFont.getHeight()/2+150){
            @Override
            public void onClicked(){
                app.exit();
            }
        });
        params.add(new guiCheckBox(app,"FPS", mainFont,app.getWidth()/2-  mainFont.getWidth("FPS")/2,
                app.getHeight()/2-  mainFont.getHeight()/2-150,app.isShowingFPS()){
            @Override
                public void onClicked (){
                if (active) {
                  active=false;
                    ConfigReader.setConfig("fps","false");
                    setParams();
                }else {
                    active = true;
                    ConfigReader.setConfig("fps", "true");
                    setParams();
                }
            }
        });
    }
}