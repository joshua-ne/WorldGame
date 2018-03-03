package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 30;
    public static final int AREA = WIDTH * HEIGHT;
    public static ArrayList<Position> availableDoors; // store the available positions for new rooms; updated whenever a new room is built
    public static int occupiedArea;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random();

    public static class Position {
        public int x, y;
        public Position(){}
        public Position (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //Room class with basic information
    public static class Room {
        public int roomWidth, roomHeight, roomArea;
        public Position leftTop, rightBottom, startingDoor;
        public ArrayList<Position> RoomAvailableDoors;
        public Room() {
            leftTop = new Position();
            rightBottom = new Position();
            startingDoor = new Position();
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
    public static Room buildARandomRoom(Position doorForNewRoomA, TETile[][] world){

        Room newRoom = new Room();
        //
        Position newRoomPosition = chooseAPositionForNewRoom(doorForNewRoomA, newRoom, world);
        
        //set the position of the new room
        newRoom.leftTop = newRoomPosition;
        newRoom.rightBottom.x = newRoom.leftTop.x + newRoom.roomWidth - 1;
        newRoom.rightBottom.y = newRoom.leftTop.y + newRoom.roomHeight - 1;

        //set the potential future doors
        newRoom.RoomAvailableDoors = setRandomAvailableDoors(newRoom);
        return newRoom;
    }


    // deploy the room to the word after built
    public static void deployRoom(Room newRoom, TETile[][] world) {

        for (int x = newRoom.leftTop.x; x < newRoom.leftTop.x + newRoom.roomWidth; x += 1) {
            for (int y = newRoom.leftTop.y; y < newRoom.leftTop.y + newRoom.roomHeight; y += 1) {

                if (x == newRoom.leftTop.x || x == newRoom.rightBottom.x || y == newRoom.leftTop.y || y == newRoom.rightBottom.y) {
                    world[x][y] = Tileset.WALL;
                } else {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }

        world[newRoom.startingDoor.x][newRoom.startingDoor.y] = Tileset.FLOOR;
    }

    public static ArrayList<Position> setRandomAvailableDoors(Room newRoom) {

        ArrayList<Position> newAvailableDoors = new ArrayList<>();
        int x1 = RANDOM.nextInt(newRoom.roomWidth - 2) + 1;
        int x2 = RANDOM.nextInt(newRoom.roomWidth - 2) + 1;
        int y1 = RANDOM.nextInt(newRoom.roomHeight - 2) + 1;
        int y2 = RANDOM.nextInt(newRoom.roomHeight - 2) + 1;

        Position[] doors = new Position[4];

        doors[0] = new Position(newRoom.leftTop.x + x1, newRoom.leftTop.y);
        doors[1] = new Position(newRoom.leftTop.x + x2, newRoom.rightBottom.y);
        doors[2] = new Position(newRoom.leftTop.x, newRoom.leftTop.y + y1);
        doors[3] = new Position(newRoom.rightBottom.x, newRoom.leftTop.y + y2);

        for (int i = 0; i < 4; i++) {
            if (!doors[i].equals(newRoom.startingDoor)) {
                newAvailableDoors.add(doors[i]);
            }
        }
        return newAvailableDoors;
    }

    public static void updateAvailableDoors(ArrayList<Position> newAvailableDoors) {
        availableDoors.addAll(newAvailableDoors);
    }

    //check the legitimacy of a room in the given world
    public static boolean checkLegitimacyOfRoom(Room newRoom, TETile[][] world){

        for (int x = newRoom.leftTop.x; x < newRoom.leftTop.x + newRoom.roomWidth; x += 1) {
            for (int y = newRoom.leftTop.y; y < newRoom.leftTop.y + newRoom.roomHeight; y += 1) {

                if (world[x][y] != Tileset.NOTHING) {
                    System.out.println("judged as false");
                    deployRoom(newRoom, world);

                    return false;
                }
            }
        }
        return true;
    }

    //choose a door for new room from the list of available doors; open the door; undate the available doors list
    public static Position chooseDoorForNewRoomAndOpenTheDoor(TETile[][] world) {

        //randomly choose a door
        int doorNum = RANDOM.nextInt(availableDoors.size());
        Position doorForNewRoomA = availableDoors.remove(doorNum);

        //open the door
        world[doorForNewRoomA.x][doorForNewRoomA.y] = Tileset.FLOOR;

        return doorForNewRoomA;
    }


    //choose a position for newRoom (currently, the newRoom only has its size available)
    public static Position chooseAPositionForNewRoom(Position doorForNewRoomA, Room newRoom, TETile[][] world){

        Position doorForNewRoomB = determineDoorForNewRoomB(doorForNewRoomA, world);
        newRoom.startingDoor = doorForNewRoomB;
        int sideOfNewRoom = determineTheSideOfNewRoom(doorForNewRoomA, world);
        int offsetX = RANDOM.nextInt(newRoom.roomWidth - 2);
        int offsetY = RANDOM.nextInt(newRoom.roomHeight - 2);

        Position newRoomPosition = new Position();
        switch (sideOfNewRoom) {
            case 1: {
                newRoomPosition.x = doorForNewRoomB.x - offsetX;
                newRoomPosition.y = doorForNewRoomB.y - newRoom.roomHeight + 1;
            } break;

            case 2: {
                newRoomPosition.x = doorForNewRoomB.x;
                newRoomPosition.y = doorForNewRoomB.y - offsetY;
            } break;

            case 3: {
                newRoomPosition.x = doorForNewRoomB.x - offsetX;
                newRoomPosition.y = doorForNewRoomB.y;
            } break;

            case 4: {
                newRoomPosition.x = doorForNewRoomB.x - newRoom.roomWidth + 1;
                newRoomPosition.y = doorForNewRoomB.y - offsetY;
            } break;
        }

        return newRoomPosition;
    }

    // determine the side of the newRoom relative to the doorForNewRoomA
    public static int determineTheSideOfNewRoom (Position doorForNewRoomA, TETile[][] world) {
        //determine the position of the used door, int side: 1:top, 2:right, 3:bottom, 4: left
        int side = 0;
        int x = doorForNewRoomA.x;
        int y = doorForNewRoomA.y;
        if (world[x + 1][y] == Tileset.NOTHING) side = 2;
        if (world[x - 1][y] == Tileset.NOTHING) side = 4;
        if (world[x][y + 1] == Tileset.NOTHING) side = 3;
        if (world[x][y - 1] == Tileset.NOTHING) side = 1;
        return side;

    }

    public static Position determineDoorForNewRoomB(Position doorForNewRoomA, TETile[][] world){

        Position doorForNewRoomB = new Position();

        //set the position of the door for new room the same as the door to use, will shift in the following according to the side of the doorForNewRoomA
        doorForNewRoomB.x = doorForNewRoomA.x;
        doorForNewRoomB.y = doorForNewRoomA.y;

        int side = determineTheSideOfNewRoom(doorForNewRoomA, world);
        //determine the position of the door for new room according to the door side
        switch (side) {
            case 1: {
                doorForNewRoomB.y -= 1;
            } break;

            case 2: {
                doorForNewRoomB.x += 1;
            } break;

            case 3: {
                doorForNewRoomB.y += 1;
            } break;

            case 4: {
                doorForNewRoomB.x -= 1;
            } break;
        }

        return doorForNewRoomB;
    }
    //putLockedDoor
    public static void putLockedDoor(Room firstRoom) {

    }

    //add first room
    public static Room addFirstRoom(Position startPoint, TETile[][] world){
        Room newRoom = new Room();
        Position newRoomPosition = startPoint;
        newRoom.leftTop = newRoomPosition;
        newRoom.rightBottom.x = newRoom.leftTop.x + newRoom.roomWidth - 1;
        newRoom.rightBottom.y = newRoom.leftTop.y + newRoom.roomHeight - 1;

        //set the potential future doors
        ArrayList<Position> newAvailableDoors = setRandomAvailableDoors(newRoom);
        newRoom.RoomAvailableDoors = newAvailableDoors;
        updateAvailableDoors(newAvailableDoors);

        deployRoom(newRoom,world);

        return newRoom;
    }
    //add one more room to the world
    public static Room addOneMoreRoom(Position doorForNewRoomA, TETile[][] world) {

        // build a room, check it legitimacy; continue to build new ones until legitimacy is true
        Room newRoom = buildARandomRoom(doorForNewRoomA, world);
        while (!checkLegitimacyOfRoom(newRoom, world)){
            //System.out.println("hhh");

            newRoom = buildARandomRoom(doorForNewRoomA, world);
            break;
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
        Room firstRoom = addFirstRoom(startPoint, world);
        putLockedDoor(firstRoom);
        ter.renderFrame(world);

        //when coverage < 0.8 add more rooms
        while (checkCoverage(world) < 0.8){
            Position doorForNewRoom = chooseDoorForNewRoomAndOpenTheDoor(world);
            addOneMoreRoom(doorForNewRoom, world);
            break;
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
