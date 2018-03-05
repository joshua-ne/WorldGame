package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldGenerator_test extends WorldGenerator{

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        WIDTH = 60;
        HEIGHT = 30;
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

        //set a counter for the number of rooms
        int roomNum = 1;

        while (availableDoors.size() > 0 ){
            Room newRoom = addOneMoreRoom(world);
            //System.out.println(newRoom);
            //ter.renderFrame(world);
            roomNum += 1;
            System.out.println("number of available doors: " + availableDoors.size());
        }

        putLockedDoor(world);

        System.out.println(roomNum + " rooms added to the world!");
        // draws the world to the screen
        ter.renderFrame(world);
    }
}
