package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldGenerator_test_2 extends WorldGenerator {
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        WIDTH = 120/2;
        HEIGHT = 60/2;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        //Initialize occupied area and available doors
        occupiedArea = 0;
        availableDoors = new HashMap<>();

        //pick a proper start position to start
        Position startPoint = pickStartPoint(world);

        //place the first room at the start position and put the locked door
        Room firstRoom = addFirstRoom(startPoint, world);
        putLockedDoor(world);
        ter.renderFrame(world);

        //when coverage < 0.8 add more rooms
        int i = 0;
        while (i<8){
            addOneMoreRoom(world);
            ter.renderFrame(world);

            i += 1;
            //break;
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
