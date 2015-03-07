package TheTimeless.gui;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

public class guiCheckBox extends guiElement {
    public boolean active=false;
    public guiCheckBox(GameContainer cntr, String str, VTextRender font, int x, int y,boolean active) {
        this.container = cntr;
        string=str;
        this.render=font;
        input = container.getInput();
        rect=new Rectangle(x,y,font.getWidth(str),font.getHeight());
        setLocation(x, y);
        shown=true;
        this.active=active;
    }


    public void render() throws SlickException{
        if(shown) {
            super.update();
            if (active) {
                render.drawString( string, (int) rect.getX(), (int) rect.getY(),Color.yellow);
            }else{
                render.drawString( string, (int) rect.getX(), (int) rect.getY(),Color.white);
            }
        }
    }

    public void setLocation(int x, int y){
        rect.setX(x);
        rect.setY(y);
    }




}