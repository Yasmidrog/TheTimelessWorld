
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

//TODO Код-сраное говно, а я нуб
class WizardGame extends BasicGame {


    private static AppGameContainer app;

    public WizardGame() {
        super("Wizard game");
    }

    private World world;
    private TiledMap grassMap;

    public static void main(String[] arguments) {
        try {
            app = new AppGameContainer(new WizardGame());
            app.setDisplayMode(800, 800, false);
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

}


