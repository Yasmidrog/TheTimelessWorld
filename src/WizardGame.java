import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.io.File;

class WizardGame extends BasicGame {


    private static AppGameContainer app;

    public WizardGame() {
        super("Wizard game");
    }

    private World world;
    private TiledMap grassMap;

    public static void main(String[] arguments) {
        try {
            try {

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


            app = new AppGameContainer(new WizardGame());
            app.setDisplayMode(1024, 750, false);
            app.setDefaultMouseCursor();
            app.setShowFPS(true);
            app.start();

        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        grassMap = new TiledMap("data/map.tmx");
        world = new World();
        world.init(grassMap, container);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        world.update(delta);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        world.render();
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
}


