package TheTimeless.gui;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.openal.Audio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class guiContainer extends guiElement{
    private ArrayList<guiElement> ve = new ArrayList<guiElement>();
    public void add(guiElement e) {
        ve.add(e);
    }
    private int elementNumber=0;
    private Audio mus;
public guiContainer(GameContainer container){
    input=container.getInput();
    listener=null;
}
    @Override
    public void render() {
        if(shown) {
            if (input.isKeyPressed(Input.KEY_UP)) {
                if(elementNumber!=0)
                    elementNumber--;
                else elementNumber=ve.size()-1;
                if(mus!=null)
                    mus.playAsMusic(1,1,false);
            }
            if (input.isKeyPressed(Input.KEY_DOWN)) {
                if(elementNumber<ve.size()-1)
                    elementNumber++;
                else elementNumber=0;
                if(mus!=null)
                    mus.playAsMusic(1,1,false);
            }
            if(input.isKeyPressed(Input.KEY_ENTER))
            {
                ve.get(elementNumber).onClicked();
            }

            guiElement e=ve.get(elementNumber);
            guiElement f=ve.get(ve.size()-1);
            graphics.setColor(Color.darkGray);

            graphics.setColor(new Color(0,0,0,85));
            graphics.fillRect(ve.get(0).getX() - 50, ve.get(0).getY() - 30, ve.get(0).getWidth() + 100, f.getY()
                    - ve.get(0).getY() + f.getHeight() + 60);
            graphics.setColor(Color.white);
            graphics.drawRect(e.getX()-3,e.getY()-3,e.getWidth()+6,e.getHeight()+6);
            for (guiElement element : ve) {
                try {
                    if(element.getShown())
                        element.render();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public void setSound(Audio a){
        mus=a;
    }
    public void remove(guiElement e) {
        ve.remove(e);
    }

}
