package TheTimeless.game;

public class HeroSpawner extends Entity {
    public HeroSpawner(float x, float y){

        this.x = x;
        this.y = y;
        Name = "HS";
    }

    @Override
    public void onInit(World world) {
        try {
        CrWld = world;

                CrWld.SpMn = new Spudi(x, y);
                CrWld.Creatures.add(0, CrWld.SpMn);//add a hero on the coords and remove the spawner from objects

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override

    public void onUpdate(int delta) {
        CrWld.StaticObjects.remove(this);
    }
    public void onRender() {}
}
