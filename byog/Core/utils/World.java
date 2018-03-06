package byog.Core.utils;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class World implements Serializable{

    public TETile[][] worldMap;
    public WorldGenerator.Position playerPosition;
    public WorldGenerator.Position lockedDoorPosition;

    public World(){}

    public World(int WIDTH, int HEIGHT){
        worldMap = new TETile[WIDTH][HEIGHT];
        playerPosition = new WorldGenerator.Position();
        lockedDoorPosition = new WorldGenerator.Position();
    }

}
