package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 30;
    public static final int AREA = WIDTH * HEIGHT;
    public static ArrayList<Position> availableDoors; // store the available positions for new rooms; updated whenever a new room is built
    public static int occupiedArea;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static class Position {
        public int x, y;
    }

    //Room class with basic information
    public static class Room {
        public int roomWidth, roomHeight, roomArea;
        public Position leftTop, rightBottom;
        public ArrayList<Position> availableDoors;
        public Room() {
            leftTop = new Position();
            rightBottom = new Position();
            roomWidth = RANDOM.nextInt(5) + 3;
            roomHeight = RANDOM.nextInt(5) + 3;
            roomArea = roomHeight * roomWidth;
        }
    }


    //pick a proper start position to start
    public static Position pickStartPoint(TETile[][] world){
        Position startPosition = new Position();
        startPosition.x = WIDTH/2;
        startPosition.y = HEIGHT/2;
        return startPosition;
    }

    //check the coverage of the world
    public static double checkCoverage(TETile[][] world) {
        return occupiedArea/(double)AREA;
    }

    //build a random room instance, determining its size, position, doors;
    public static Room buildARandomRoom(Position doorForNewRoom, TETile[][] world){

        Room newRoom = new Room();
        //
        Position newRoomPosition = chooseAPositionForNewRoom(doorForNewRoom, newRoom, world);
        
        //set the position of the new room
        newRoom.leftTop = newRoomPosition;
        newRoom.rightBottom.x = newRoom.leftTop.x + newRoom.roomWidth - 1;
        newRoom.rightBottom.y = newRoom.leftTop.y + newRoom.roomHeight - 1;
        return newRoom;
    }


    // deploy the room to the word after built
    public static void deployRoom(Room newRoom, TETile[][] world) {

        // fills in a block 14 tiles wide by 4 tiles tall
        for (int x = newRoom.leftTop.x; x < newRoom.leftTop.x + newRoom.roomWidth; x += 1) {
            for (int y = newRoom.leftTop.y; y < newRoom.leftTop.y + newRoom.roomHeight; y += 1) {

                if (x == newRoom.leftTop.x || x == newRoom.rightBottom.x || y == newRoom.leftTop.y || y == newRoom.rightBottom.y) {
                    world[x][y] = Tileset.WALL;
                } else {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    public static ArrayList<Position> setRandomAvailableDoors(Room newRoom) {
        return new ArrayList<Position>();
    }

    public static void updateAvailableDoors(ArrayList<Position> newAvailableDoors) {

    }

    //check the legitimacy of a room in the given world
    public static boolean checkLegitimacyOfRoom(Room aRoom, TETile[][] world){
        return true;
    }

    //choose a door for new room from the list of available doors; open the door; undate the available doors list
    public static Position chooseDoorForNewRoomAndOpenTheDoor(TETile[][] world) {

        //randomly choose a door
        int doorNum = RANDOM.nextInt(availableDoors.size());
        Position doorForNewRoom = availableDoors.remove(doorNum);

        //open the door
        world[doorForNewRoom.x][doorForNewRoom.y] = Tileset.FLOOR;

        return doorForNewRoom;
    }


    //choose a position for newRoom (currently, the newRoom only has its size available)
    public static Position chooseAPositionForNewRoom(Position useDoor, Room newRoom, TETile[][] world){

        Position doorForNewRoom = determineDoorForNewRoom(useDoor, world);
        int sideOfNewRoom = determineTheSideOfNewRoom(useDoor, world);
        int offsetX = RANDOM.nextInt(newRoom.roomWidth - 2);
        int offsetY = RANDOM.nextInt(newRoom.roomHeight - 2);

        Position newRoomPosition = new Position();
        switch (sideOfNewRoom) {
            case 1: {
                newRoomPosition.x = doorForNewRoom.x - offsetX;
                newRoomPosition.y = doorForNewRoom.y - newRoom.roomHeight + 1;
            } break;

            case 2: {
                newRoomPosition.x = doorForNewRoom.x;
                newRoomPosition.y = doorForNewRoom.y - offsetY;
            } break;

            case 3: {
                newRoomPosition.x = doorForNewRoom.x - offsetX;
                newRoomPosition.y = doorForNewRoom.y;
            } break;

            case 4: {
                newRoomPosition.x = doorForNewRoom.x - newRoom.roomWidth + 1;
                newRoomPosition.y = doorForNewRoom.y - offsetY;
            } break;
        }

        return newRoomPosition;
    }

    // determine the side of the newRoom relative to the useDoor
    public static int determineTheSideOfNewRoom (Position useDoor, TETile[][] world) {
        //determine the position of the used door, int side: 1:top, 2:right, 3:bottom, 4: left
        int side = 0;
        int x = useDoor.x;
        int y = useDoor.y;
        if (world[x + 1][y] == Tileset.NOTHING) side = 2;
        if (world[x - 1][y] == Tileset.NOTHING) side = 4;
        if (world[x][y + 1] == Tileset.NOTHING) side = 3;
        if (world[x][y - 1] == Tileset.NOTHING) side = 1;
        return side;

    }

    public static Position determineDoorForNewRoom (Position useDoor, TETile[][] world){

        Position doorForNewRoom = new Position();

        //set the position of the door for new room the same as the door to use, will shift in the following according to the side of the useDoor
        doorForNewRoom.x = useDoor.x;
        doorForNewRoom.y = useDoor.y;

        int side = determineTheSideOfNewRoom(useDoor, world);
        //determine the position of the door for new room according to the door side
        switch (side) {
            case 1: {
                doorForNewRoom.y -= 1;
            } break;

            case 2: {
                doorForNewRoom.x += 1;
            } break;

            case 3: {
                doorForNewRoom.y += 1;
            } break;

            case 4: {
                doorForNewRoom.x -= 1;
            } break;
        }

        return doorForNewRoom;
    }
    //putLockedDoor
    public static void putLockedDoor(Room firstRoom) {

    }

    //add one more room to the world
    public static Room addOneMoreRoom(Position doorForNewRoom, TETile[][] world) {

        // build a room, check it legitimacy; continue to build new ones until legitimacy is true
        Room newRoom = buildARandomRoom(doorForNewRoom, world);
        while (!checkLegitimacyOfRoom(newRoom, world)){
            newRoom = buildARandomRoom(doorForNewRoom, world);
        }

        //deploy the room to the world
        deployRoom(newRoom,world);


        ArrayList<Position> newAvailableDoors = setRandomAvailableDoors(newRoom);
        updateAvailableDoors(newAvailableDoors);
        return newRoom;
    }



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

        //Initialize occupied area and available doors
        occupiedArea = 0;
        availableDoors = new ArrayList<Position>();

        //pick a proper start position to start
        Position startPoint = pickStartPoint(world);

        //place the first room at the start position and put the locked door
        Room firstRoom = addOneMoreRoom(startPoint, world);
        putLockedDoor(firstRoom);

        //when coverage < 0.8 add more rooms
        while (checkCoverage(world) < 0.8){
            Position doorForNewRoom = chooseDoorForNewRoomAndOpenTheDoor(world);
            addOneMoreRoom(doorForNewRoom, world);
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
