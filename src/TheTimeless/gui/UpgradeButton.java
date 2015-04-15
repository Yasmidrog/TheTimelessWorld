package TheTimeless.gui;

import TheTimeless.game.Fonts;
import TheTimeless.game.Upgrade;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class UpgradeButton extends guiButton{
    private Image pic;
    private Upgrade upgrade;
    private Shop CrShop;
    public UpgradeButton(Upgrade upgr, Image picture, int x, int y,GameContainer cntr,Shop shop){
        try {
            upgrade=upgr;
            pic = picture;
            CrShop=shop;
            this.container = cntr;
            shown=true;
            input = cntr.getInput();
            rect=new Rectangle(x,y,300,70);
            setLocation(x, y);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void render(){
        try{
            Graphics g=container.getGraphics();
            g.setColor(Color.gray);
            g.drawRect(getX(),getY(),300,70);
            g.setColor(new Color(0,0,0,80));
            g.fillRect(getX(), getY(), 300, 70);
            pic.draw(getX()-2,getY()-2);
            Fonts.RegularText.drawString(upgrade.Name, (int) getX() + 67, (int) getY() + 2, Color.white);
            String desc=Fonts.SmallText.splitString(upgrade.Description,300-67,true);
            Fonts.MediumText.drawString(desc, (int) getX() + 67, (int) getY() + 30, Color.white);
            Fonts.SmallText.drawString("Price: " + upgrade.Price, (int) getX() + 30,
                                                     (int) getY() + 67, Color.white);
            if(upgrade.getType()== Upgrade.UpgradeType.DEVIL){
             Fonts.SmallText.drawString("Evilness: " + (int)(upgrade.Price*0.3),
                            (int) getX() + 200, (int) getY() + 52, Color.white);
            }
            update();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected  void update(){
        if(mouseClicked()&&active) {
            onClicked();
            if(upgrade.getType()== Upgrade.UpgradeType.DEVIL)
                CrShop.getDevilUpgrades().remove(this);
            else if(upgrade.getType()== Upgrade.UpgradeType.GOD)
            CrShop.getGodUpgrades().remove(this);
        }
    }
    @Override
    public void onClicked() {
        CrShop.Hero.applyAndRemoveAbility(upgrade.Name);
    }
}
