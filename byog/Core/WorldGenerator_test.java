package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class WorldGenerator_test extends WorldGenerator{

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        //pick a proper start position to start
        WorldGenerator.Position startPoint = pickStartPoint(world);

        System.out.println(startPoint.x);
        System.out.println(startPoint.y);


        addOneMoreRoom(startPoint, world);


        // draws the world to the screen
        ter.renderFrame(world);
    }
}
