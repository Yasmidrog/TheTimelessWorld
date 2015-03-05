package TheTimeless.game;
/**
 * A box which increases current health of hero
 */
public class HealthBox extends Increaser{
    public HealthBox(float x, float y){
        this.x = x;
        this.y = y;
        Name = "HB";
    }
    @Override
    public void setColorAndSprite(){
        IntColor= 14680064;
        sprite=World.ResLoader.getSprite("HealthBox");
    }
    @Override
    public  void  onPick(){
        if(CrWld.SpMn.Health!=CrWld.SpMn.MAXHEALTH) {
            Amount = CrWld.SpMn.changeHealth(20);
            exists = false;
        }
    }

}

