import org.newdawn.slick.tiled.TiledMap;

public class Block {
    static void OnCollide(TiledMap Map, int x, int y) {
        int TileID = Map.getTileId(x, y, 0);
        if (Map.getTileProperty(TileID, "Type", "Ground").equals("Ground")) {
            System.out.print("fff");
        }
    }
}
