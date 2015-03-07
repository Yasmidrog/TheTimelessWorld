package TheTimeless.gui;
import org.newdawn.slick.*;
import org.newdawn.slick.openal.Audio;

import java.util.ArrayList;

public class guiContainer extends guiElement{
    public ArrayList<guiElement> ve = new ArrayList<guiElement>();
    public void add(guiElement e) {
        ve.add(e);
    }
    protected int elementNumber=0;
    protected Sound clickSound;
    protected guiContainer(){}
public guiContainer(GameContainer container){
    input=container.getInput();
}
    @Override
    public void render() {
        if(shown) {
           update();
            int w=0;
            for(guiElement el:ve){
                if(el.getWidth()+110>w)
                    w=(int)el.getWidth()+110;

            }
            guiElement e=ve.get(elementNumber);
            guiElement f=ve.get(ve.size()-1);
            graphics.setColor(Color.darkGray);

            graphics.setColor(new Color(0,0,0,85));
            graphics.fillRect(ve.get(0).getX()-20, ve.get(0).getY() - 30, w, f.getY()
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
    public void setSound(Sound a){
        clickSound =a;
    }
    public void remove(guiElement e) {
        ve.remove(e);
    }
@Override
    public void update(){
    if (input.isKeyPressed(Input.KEY_UP)) {
        if(elementNumber!=0)
            elementNumber--;
        else elementNumber=ve.size()-1;
        if(clickSound !=null)
            clickSound.play(1,1);
    }
    if (input.isKeyPressed(Input.KEY_DOWN)) {
        if(elementNumber<ve.size()-1)
            elementNumber++;
        else elementNumber=0;
        if(clickSound !=null)
            clickSound.play(1,1);
    }
    if(input.isKeyPressed(Input.KEY_ENTER))
    {
        ve.get(elementNumber).onClicked();
    }
}
}
