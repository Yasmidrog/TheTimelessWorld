package TheTimeless.gui;

import TheTimeless.game.Fonts;
import TheTimeless.game.WizardGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.Audio;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yasmidrog on 1/15/15.
 */
public class SavesList extends guiContainer {
    private ArrayList<guiButton> buttons=new ArrayList<guiButton>();
    private  int elem;

    private VTextRender Font=Fonts.regulartext;
    private WizardGame WGame;
    public SavesList(GameContainer container,WizardGame game,final Menu menu){
        int y=container.getHeight()/2-100;
        input=container.getInput();
        listener=null;
        WGame=game;
        elem=0;
        final  SavesList list=this;
        for(final String str:new File("data/saves").list()){
            buttons.add(new guiButton(container, str, Font, container.getWidth() / 2 - Font.getWidth(str) / 2, y) {
                @Override
                public void onClicked() {
                    WGame.load("data/saves/" + str, container);
                    menu.setShown(false);
                    menu.gui.setShown(true);
                    container.setPaused(false);
                    list.shown=false;
                }
            });
            y+=30;
        }
    }
    public void render() {
        if (shown) {
            if (input.isKeyPressed(Input.KEY_UP)) {

                if (elementNumber != 0) {
                    elementNumber--;
                } else {
                    elementNumber = buttons.size() - 1;
                    elem = buttons.size() - 1 - 24;
                }
                if (elementNumber < elem)
                    elem--;
                if (mus != null)
                    mus.playAsMusic(1, 1, false);
            }
            if (input.isKeyPressed(Input.KEY_DOWN)) {
                if (elementNumber < buttons.size() - 1) {
                    elementNumber++;
                } else {
                    elementNumber = 0;
                }
                elem = 0;
                if (mus != null)
                    mus.playAsMusic(1, 1, false);
            }
            if (input.isKeyPressed(Input.KEY_ENTER)) {
                buttons.get(elementNumber).onClicked();
            }

            guiElement e = buttons.get(elementNumber);
            guiElement f = buttons.get(buttons.size() - 1);
            graphics.setColor(Color.darkGray);

            graphics.setColor(new Color(0, 0, 0, 85));
            graphics.fillRect(buttons.get(0).getX() - 120, buttons.get(0).getY() - 30, buttons.get(0).getWidth() + 240, f.getY()
                    - buttons.get(0).getY() + f.getHeight() + 60);
            graphics.setColor(Color.white);
            graphics.drawRect(e.getX() - 3, e.getY() - 3, e.getWidth() + 6, e.getHeight() + 6);

            if (buttons.size() < 24)
                for (guiElement element : buttons) {
                    try {
                        if (element.getShown())
                            element.render();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            else {
                for (guiElement element : buttons.subList(elem, 23)) {
                    try {
                        if (element.getShown())
                            element.render();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    public void setSound(Audio a){
        mus=a;
    }
    public void remove(guiElement e) {
        buttons.remove(e);
    }

}
