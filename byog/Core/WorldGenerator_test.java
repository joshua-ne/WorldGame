package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;

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
        availableDoors = new ArrayList<>();
        //pick a proper start position to start
        WorldGenerator.Position startPoint = pickStartPoint(world);

        Room firstRoom = addFirstRoom(startPoint, world);
        System.out.println(firstRoom.RoomAvailableDoors);
        for (int i = 0; i < firstRoom.RoomAvailableDoors.size(); i++){
            world[firstRoom.RoomAvailableDoors.get(i).x][firstRoom.RoomAvailableDoors.get(i).y] = Tileset.FLOWER;
        }


        // draws the world to the screen
        ter.renderFrame(world);
    }
}
