package TheTimeless.game;
import TheTimeless.gui.*;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;
import java.io.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import com.thoughtworks.xstream.*;


public class WizardGame extends BasicGame {
    private Menu MainMenu;
    public int Level = 1;//current level
    public World world;//current world
    private AppGameContainer app;
    public boolean loaded;
    private Image back;
    public WizardGame() {
        super("Timeless");
    }

    public static void main(String[] arguments) throws Exception {
        try {
            AppGameContainer app;
            setNatives();
            //set application parameters
            app = new AppGameContainer(new WizardGame());
            app.setTitle("The Timeless World");
            app.setDisplayMode(app.getScreenWidth() - 50, app.getScreenHeight() - 50, false);
            app.setUpdateOnlyWhenVisible(true);
            app.setMouseGrabbed(false);
            app.setDefaultMouseCursor();
            app.setIcon("data/icons/icon.png");
            app.start();
            setParams(app);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public static void setParams(GameContainer cntr) {
        try {
            if (!ConfigReader.getConf("fps").equals("null"))
                cntr.setShowFPS(Boolean.valueOf(ConfigReader.getConf("fps")));
            if (!ConfigReader.getConf("updateonlyifvisible").equals("null"))
                cntr.setUpdateOnlyWhenVisible(Boolean.valueOf(ConfigReader.getConf("updateonlyifvisible")));
            if (!ConfigReader.getConf("smoothdeltas").equals("null"))
                cntr.setSmoothDeltas(Boolean.valueOf(ConfigReader.getConf("smoothdeltas")));
            if (!ConfigReader.getConf("width").equals("null") && !ConfigReader.getConf("height").equals("null")) {
                int width = Integer.valueOf(ConfigReader.getConf("width"));
                int height = Integer.valueOf(ConfigReader.getConf("height"));
                Display.setDisplayMode(new DisplayMode(width, height));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setNatives() {
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
        return (os.contains("win"));
    }
    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("mac"));
    }
    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("nix") || os.contains("nux"));
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        int swidth=Display.getDesktopDisplayMode().getWidth();
        int sheight=Display.getDesktopDisplayMode().getHeight();
        back=new Image("data/icons/twlogo.png").getScaledCopy(swidth-(int)(swidth/2.5),
                                                                sheight-(int)(sheight/3.5));
        MainMenu = new Menu(this, container);
        MainMenu.setShown(true);
        newGame(container);
        container.pause();
    }
public void newGame(GameContainer container)throws SlickException{
    TiledMap grassMap = new TiledMap("data/levels/" + 1 + "/" + "world.tmx");
    world = new World();
    world.init(grassMap, container, 1);
    this.app=(AppGameContainer)container;
    try {

        Display.setResizable(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        if (!loaded)
            app.pause();
            if (!container.isPaused()) {
                world.update(delta);
                Buttons(container);
                if(!world.exsists){
                    LoadWorld();
                    world.exsists=true;
                }
                if(world.SpMn.Health<=0){
                    boolean b=fastLoad();
                    if (!b) {
                        Level--;
                        world.SpMn.Health=world.SpMn.MAXHEALTH;
                        LoadWorld();
                    }
                }
            }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        if(loaded) {
            g.scale(1, 1);
            GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
            try {
                GL11.glColor3f(255, 255, 255);
                world.render();
            } catch (Exception e) {
                GL11.glColor3f(255, 255, 255);
                e.printStackTrace();
            }
        }else {
            back.draw(Display.getDesktopDisplayMode().getWidth()-back.getWidth()-100
                    ,Display.getDesktopDisplayMode().getHeight()/13.5f);
        }
        MainMenu.render();
    }//render world&objects or menu

    /**
     * Save game
     */
    public void save(String adress) {
        app.pause();
        try {
            PrintWriter writer = new PrintWriter(adress, "UTF-8");
            XStream stream=new XStream(new DomDriver());
            String str=stream.toXML(new Serializator(Level, world.Creatures, world.StaticObjects));
            writer.print(str);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(loaded) {
            app.resume();
        }
    }

    /**
     * Restore map and objects from serialization file
     */
    public void load(String adress) {
        try {
            XStream stream=new XStream(new DomDriver());
            Serializator s=(Serializator)stream.fromXML(new File(adress));
            TiledMap d=s.getMap();
            world.setBlocked(World.GetBlocked(d));
            world.CurrentMap=d;
            Level=s.getLevel();
            ArrayList<Creature> crts=s.getCreatures();
            ArrayList<Entity> ents=s.getEntities();
            world.Creatures=crts;
            for(Creature cr:world.Creatures){
                cr.onInit(world);
                if(cr instanceof Spudi)
                    world.SpMn=(Spudi)cr;
            }
            world.checkSpudies();

            world.StaticObjects=ents;
            for(Entity ent:world.StaticObjects){
                ent.onInit(world);
            }

        } catch (ConcurrentModificationException s) {
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void LoadWorld() {
        app.pause();
        try{

        Level++;
            XStream x=new XStream(new DomDriver());
            String sp=x.toXML(world.SpMn);

        TiledMap d= new TiledMap("data/levels/" + (Level) + "/" + "world.tmx");
        System.out.println("Loaded level: "+Level+": data/levels/" + (Level) + "/world.tmx");
        world = new World();
        world.init(d, app, Level);
        System.gc();
            float X=world.SpMn.x;
            float Y=world.SpMn.y;
            Spudi spmn=(Spudi)x.fromXML(sp);
            spmn.x=X;
            spmn.y=Y;
            world.Creatures.remove(world.SpMn);
            world.SpMn=spmn;
            world.Creatures.add(world.SpMn);
            world.SpMn.onInit(world);
            app.resume();
    } catch (Exception e) {
       e.printStackTrace();
    }

        //try to load map for the next level

    }
    private void Buttons(GameContainer container)
    {
        Input input = container.getInput();

        if (input.isKeyPressed(Input.KEY_P)) {
           fastSave();
        }

        if (input.isKeyPressed(Input.KEY_O)) {
          fastLoad();
        }

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            if (!container.isPaused()) {
                container.pause();
                MainMenu.setShown(true);
            } else {
                container.setPaused(false);
                MainMenu.setShown(false);
            }
        }
        if (MainMenu.isShown()) {
            if (container.isFullscreen()) {
                Mouse.setCursorPosition(0, 0);
            }
        }
    }
    private boolean fastLoad(){
        try {
            File[] saves = new File("data/saves").listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if(pathname.getName().contains(".ttws"))
                        return true;
                    else return false;
                }
            });
            if(! (saves==null)&&!(saves.length==0)) {
                Arrays.sort(saves, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                            return -1;
                        } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                            return +1;
                        } else {
                            return 0;
                        }
                    }
                });
                load(saves[0].getAbsolutePath());
                return true;
            }else return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private  void fastSave(){
        try {
            Date d = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("dd_MM_yyyy_hhmmss");
            save("data/saves/world_lev." + Level + "_" + format1.format(d) + "-fast" + ".ttws");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
public GameContainer getContainer(){
    return  app;
}
}