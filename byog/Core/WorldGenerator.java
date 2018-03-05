package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;


public class WorldGenerator {
    public static  int WIDTH;
    public static  int HEIGHT;
    public static  int AREA = WIDTH * HEIGHT;
    public static HashMap<Room, ArrayList<Position>> availableDoors = new HashMap<>();
    public static int occupiedArea;
    private static Random RANDOM = new Random();


    public void BuildAWorld (int seed, TETile[][] world){
        WIDTH = world.length;
        HEIGHT = world[0].length;
        RANDOM = new Random(seed);

        // initialize tiles
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
        addFirstRoom(startPoint, world);

        while (availableDoors.size() > 0 ){
            addOneMoreRoom(world);
        }
        putLockedDoor(world);
        putPlayer(world);

    }

    public static class Position {
        public int x, y;
        public Position(){}
        public Position (int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {

            String strPosition = "(" + x + ", " + y + ")";
            return strPosition;
        }
    }

    //Room class with basic information
    public static class Room {
        public int roomWidth, roomHeight, roomArea;
        public Position leftTop, rightBottom, startingDoor;
        public ArrayList<Position> RoomAvailableDoors;
        public boolean isLegit;
        public Room() {
            leftTop = new Position();
            rightBottom = new Position();
            startingDoor = new Position();
            roomWidth = RANDOM.nextInt(5) + 3;
            roomHeight = RANDOM.nextInt(5) + 3;
            roomArea = roomHeight * roomWidth;
            isLegit = false;
        }

        @Override
        public String toString() {
            String roomString = "LeftTop: (" + this.leftTop.x +" "+ this.leftTop.y+")" + "; RightBottom: (" + this.rightBottom.x + " "+this.rightBottom.y+")"+"; size: " + this.roomHeight + " x " + this.roomWidth;
            return roomString;
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

    //choose a door for new room from the list of available doors; open the door; undate the available doors list
    public static Room chooseRoomToBeConnected (TETile[][] world) {

        //randomly choose a room to connect

        //System.out.println("the number of rooms available to be connected: " + availableDoors.size());
        ArrayList<Room> roomsCanBeConnected = new ArrayList<Room>(availableDoors.keySet());
        int roomNum = RANDOM.nextInt(roomsCanBeConnected.size());
        Room roomToBeConnected = roomsCanBeConnected.get(roomNum);
        return roomToBeConnected;
    }

    public static Position chooseDoorForNewRoom (Room roomToBeConnected, TETile[][] world) {
        ArrayList<Position> availableDoorsForA = availableDoors.get(roomToBeConnected);
        //System.out.println(availableDoorsForA);
        int doorNum = RANDOM.nextInt(availableDoorsForA.size());

        Position doorForNewRoomA = new Position();
        doorForNewRoomA = availableDoorsForA.remove(doorNum);

        if (availableDoorsForA.size() == 0) availableDoors.remove(roomToBeConnected);

        return doorForNewRoomA;
    }

    // determine the side of the newRoom relative to the doorForNewRoomA
    public static int determineTheSideOfNewRoom (Room roomToBeConnected, Position doorForNewRoomA, TETile[][] world) {
        //determine the position of the used door, int side: 1:top, 2:right, 3:bottom, 4: left

        int side = 0;
        int x = doorForNewRoomA.x;
        int y = doorForNewRoomA.y;

        if (y == roomToBeConnected.leftTop.y) side = 1;
        if (y == roomToBeConnected.rightBottom.y) side = 3;
        if (x == roomToBeConnected.leftTop.x) side = 4;
        if (x == roomToBeConnected.rightBottom.x) side = 2;

        if (side == 0) {
            System.out.println("Warning: Door to be connected is not on the room to be connected. Check determineTheSideOfNewRoom function!!!");
        }
        return side;
    }

    public static Position determineDoorForNewRoomB(Room roomToBeConnected, Position doorForNewRoomA, TETile[][] world){

        Position doorForNewRoomB = new Position();

        //set the position of the door for new room the same as the door to use, will shift in the following according to the side of the doorForNewRoomA
        doorForNewRoomB.x = doorForNewRoomA.x;
        doorForNewRoomB.y = doorForNewRoomA.y;

        int side = determineTheSideOfNewRoom(roomToBeConnected, doorForNewRoomA, world);
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

    //choose a position for newRoom (currently, the newRoom only has its size available)
    public static Position chooseAPositionForNewRoom(Room roomToBeConnected, Position doorForNewRoomA, Room newRoom, TETile[][] world){

        Position doorForNewRoomB = determineDoorForNewRoomB(roomToBeConnected, doorForNewRoomA, world);
        newRoom.startingDoor = doorForNewRoomB;
        int sideOfNewRoom = determineTheSideOfNewRoom(roomToBeConnected, doorForNewRoomA, world);
        int offsetX = RANDOM.nextInt(newRoom.roomWidth - 2)+ 1;
        int offsetY = RANDOM.nextInt(newRoom.roomHeight - 2)+ 1;

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

    //build a random room instance, determining its size, position, doors;
    public static Room buildARandomRoom(Room roomToBeConnected, Position doorForNewRoomA, TETile[][] world){

        Room newRoom = new Room();
        //
        Position newRoomPosition = chooseAPositionForNewRoom(roomToBeConnected, doorForNewRoomA, newRoom, world);
        
        //set the position of the new room
        newRoom.leftTop = newRoomPosition;
        newRoom.rightBottom.x = newRoom.leftTop.x + newRoom.roomWidth - 1;
        newRoom.rightBottom.y = newRoom.leftTop.y + newRoom.roomHeight - 1;

        //set the potential future doors
        //newRoom.RoomAvailableDoors = setRandomAvailableDoors(newRoom);
        return newRoom;
    }

    //check the legitimacy of a room in the given world
    public static boolean checkLegitimacyOfRoom(Room newRoom, TETile[][] world){

        for (int x = newRoom.leftTop.x; x < newRoom.leftTop.x + newRoom.roomWidth; x += 1) {
            for (int y = newRoom.leftTop.y; y < newRoom.leftTop.y + newRoom.roomHeight; y += 1) {

                try {
                    TETile ti = world[x][y];

                } catch (ArrayIndexOutOfBoundsException e) {
                    //System.out.println("x = " + x);
                    //System.out.println("y = " + y);
                    return false;
                }

                if (world[x][y] != Tileset.NOTHING) {
                    //System.out.println("judged as false");

                    return false;
                }
            }
        }
        return true;
    }

    // deploy the room to the word after built
    public static void deployRoom(Room newRoom, TETile[][] world) {

        //System.out.println(newRoom);
        for (int x = newRoom.leftTop.x; x < newRoom.leftTop.x + newRoom.roomWidth; x += 1) {
            for (int y = newRoom.leftTop.y; y < newRoom.leftTop.y + newRoom.roomHeight; y += 1) {



                if (x == newRoom.leftTop.x || x == newRoom.rightBottom.x || y == newRoom.leftTop.y || y == newRoom.rightBottom.y) {
                    world[x][y] = Tileset.WALL;
                } else {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
        //world[newRoom.startingDoor.x][newRoom.startingDoor.y] = Tileset.FLOOR;
        //world[newRoom.leftTop.x][newRoom.leftTop.y] = Tileset.WATER;
        //world[newRoom.rightBottom.x][newRoom.rightBottom.y] = Tileset.TREE;



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
        //System.out.println("check newAvailableDoors' adding:" + newAvailableDoors);
        return newAvailableDoors;
    }

    public static void updateAvailableDoors(Room newRoom, ArrayList<Position> newAvailableDoors) {
        //availableDoors.addAll(newAvailableDoors);
        availableDoors.put(newRoom, newAvailableDoors);
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
        updateAvailableDoors(newRoom, newAvailableDoors);
        //newRoom.startingDoor.x = newRoom.leftTop.x+1;
        //newRoom.startingDoor.y = newRoom.leftTop.y+1;

        deployRoom(newRoom,world);
        //System.out.println(newRoom);

        //ter.renderFrame(world);

        occupiedArea += newRoom.roomArea;


        return newRoom;
    }

    //add one more room to the world
    public static Room addOneMoreRoom(TETile[][] world) {

        // build a room, check it legitimacy; continue to build new ones until legitimacy is true
        Room roomToBeConnected = chooseRoomToBeConnected(world);
        Position doorForNewRoomA = chooseDoorForNewRoom(roomToBeConnected, world);

        Room newRoom = buildARandomRoom(roomToBeConnected, doorForNewRoomA, world);
        newRoom.isLegit = checkLegitimacyOfRoom(newRoom,world);
        while (availableDoors.size()>0 && !newRoom.isLegit){
            //System.out.println("hhh");
            roomToBeConnected = chooseRoomToBeConnected(world);

            doorForNewRoomA = chooseDoorForNewRoom(roomToBeConnected, world);
            newRoom = buildARandomRoom(roomToBeConnected, doorForNewRoomA, world);
            newRoom.isLegit = checkLegitimacyOfRoom(newRoom,world);
            //break;

        }

        // 确保最后一次因为availableDoors.size() == 0 的情况退出来的时候，不要对newRoom进行deploy
        if (newRoom.isLegit) {
            //open doorA
            world[doorForNewRoomA.x][doorForNewRoomA.y] = Tileset.FLOOR;

            //deploy the room to the world
            deployRoom(newRoom,world);

            //open doorB
            world[newRoom.startingDoor.x][newRoom.startingDoor.y] = Tileset.FLOOR;

            ArrayList<Position> newAvailableDoors = setRandomAvailableDoors(newRoom);
            updateAvailableDoors(newRoom, newAvailableDoors);
            occupiedArea += newRoom.roomArea;
            return newRoom;
        }

        return null;
    }

    //putLockedDoor
    public static void putLockedDoor(TETile[][] world) {

        while (true) {
            int x = RANDOM.nextInt(WIDTH-2)+1;
            int y = RANDOM.nextInt(HEIGHT-2)+1;
            if (world[x][y] != Tileset.WALL) continue;
            Boolean accessible = (world[x+1][y] == Tileset.FLOOR || world[x-1][y] == Tileset.FLOOR ||world[x][y+1] == Tileset.FLOOR ||world[x][y-1] == Tileset.FLOOR);
            if (accessible) {
                world[x][y] = Tileset.LOCKED_DOOR;
                break;
            }
        }
    }

    public static void putPlayer(TETile[][] world){
        while (true) {
            int x = RANDOM.nextInt(WIDTH-2)+1;
            int y = RANDOM.nextInt(HEIGHT-2)+1;
            if (world[x][y] != Tileset.WALL) continue;
            Boolean accessible = (world[x+1][y] == Tileset.FLOOR || world[x-1][y] == Tileset.FLOOR ||world[x][y+1] == Tileset.FLOOR ||world[x][y-1] == Tileset.FLOOR);
            if (accessible) {
                world[x][y] = Tileset.PLAYER;
                break;
            }
        }
    }
}
