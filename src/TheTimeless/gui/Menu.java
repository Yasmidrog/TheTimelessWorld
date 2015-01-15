package TheTimeless.gui;

import TheTimeless.game.ConfigReader;
import TheTimeless.game.WizardGame;
import TheTimeless.game.World;
import org.newdawn.slick.GameContainer;

/**
 * Created by yasmidrog on 1/15/15.
 */
public class Menu {
    private  guiContainer gui,params;
    private GameContainer app;
    private VTextRender mainFont;
    private WizardGame Game;
    public boolean Shown=false;
    public  Menu(WizardGame game,GameContainer cntr){
       mainFont =new VTextRender(46,"Sans");
        app=cntr;
        Game=game;
        setGui();
    }
    public void render()
    {
        if(Shown) {
            gui.render();
            params.render();
        }
    }
    private void setGui(){
        gui =new guiContainer(app);
        gui.setShown(true);
        params =new guiContainer(app);
        params.setShown(false);
        params.setSound(World.ResLoader.getSound("click"));
        gui.setSound(World.ResLoader.getSound("click"));
        gui.add(new guiButton(app,"Resume", mainFont,app.getWidth()/2-  mainFont.getWidth("Resume")/2,
                app.getHeight()/2-  mainFont.getHeight()/2-150){
            @Override
            public void onClicked(){
               Shown=false;
                World.ResLoader.playSound("click",1,1,false);
            }
        });

        gui.add(new guiButton(app,"Parameters",  mainFont,app.getWidth()/2-  mainFont.getWidth("Parameters")/2,
                app.getHeight()/2-  mainFont.getHeight()/2){
            @Override
            public void onClicked(){
                gui.setShown(false);
                params.setShown(true);
                World.ResLoader.playSound("click",1,1,false);
            }
        });


        gui.add(new guiButton(app,"Exit", mainFont,app.getWidth()/2-  mainFont.getWidth("Exit")/2,
                app.getHeight()/2-  mainFont.getHeight()/2+150){
            @Override
            public void onClicked(){
                app.exit();
                World.ResLoader.playSound("click",1,1,false);
            }
        });
        params.add(new guiCheckBox(app,"FPS", mainFont,app.getWidth()/2-  mainFont.getWidth("FPS")/2,
                app.getHeight()/2-  mainFont.getHeight()/2-150,app.isShowingFPS()){
            @Override
            public void onClicked (){
                if (active) {
                    active=false;
                    ConfigReader.setConfig("fps", "false");
                    Game.setParams(app);
                }else {
                    active = true;
                    ConfigReader.setConfig("fps", "true");
                    Game.setParams(app);
                }
                World.ResLoader.playSound("click",1,1,false);
            }
        });
        params.add(new guiButton(app,"Return", mainFont,app.getWidth()/2-  mainFont.getWidth("Return")/2,
                app.getHeight()/2-  mainFont.getHeight()/2- mainFont.getHeight()/2-10){
            @Override
            public void onClicked(){
                params.setShown(false);
                gui.setShown(true);
                World.ResLoader.playSound("click",1,1,false);
            }
        });
    }
    public void setShown(boolean show){
        Shown=show;
    }
    public boolean isShown(){
        return Shown;
    }
}
