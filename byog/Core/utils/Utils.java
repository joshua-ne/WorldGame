package byog.Core.utils;

import byog.Core.utils.WorldGenerator.Position;
import byog.TileEngine.Tileset;

import java.io.*;

public class Utils {


    // update the position of player in the world; direction: 1: W,up,y-=1; 2:
    public static void updatePlayerPosition(Position currPlayerPosition, int direction, World world) {

    }

    public static void Move (char direction, World world) {

        Position currPlayerPosition = world.playerPosition;
        int x = currPlayerPosition.x;
        int y = currPlayerPosition.y;

        switch (direction){

            case('W'): {
                if (y != 0 && world.worldMap[x][y-1].equals(Tileset.FLOOR)) {
                    //over-write origin position
                    world.worldMap[x][y] = Tileset.FLOOR;
                    //write new position
                    world.worldMap[x][y-1] = Tileset.PLAYER;
                    //update position information in the world
                    world.playerPosition.y -= 1;
                    }
                break;
            }

            case('A'): {
                if (x != 0 && world.worldMap[x-1][y].equals(Tileset.FLOOR)){
                    //over-write origin position
                    world.worldMap[x][y] = Tileset.FLOOR;
                    //write new position
                    world.worldMap[x-1][y] = Tileset.PLAYER;
                    //update position information in the world
                    world.playerPosition.x -= 1;
                }
                break;
            }

            case('S'): {
                if (y != world.worldMap[0].length && world.worldMap[x][y+1].equals(Tileset.FLOOR)) {
                    //over-write origin position
                    world.worldMap[x][y] = Tileset.FLOOR;
                    //write new position
                    world.worldMap[x][y+1] = Tileset.PLAYER;
                    //update position information in the world
                    world.playerPosition.y += 1;
                }
                break;
            }

            case('D'): {
                //System.out.println("trying to move right!");
                //System.out.println(world.worldMap[x+1][y].description());
                if (x != world.worldMap.length && world.worldMap[x+1][y].equals(Tileset.FLOOR)) {
                    //over-write origin position
                    world.worldMap[x][y] = Tileset.FLOOR;
                    //write new position
                    world.worldMap[x+1][y] = Tileset.PLAYER;
                    //update position information in the world
                    world.playerPosition.x += 1;
                    //System.out.println("moved right!");
                }
                break;
            }
        }

    }

    public static void playerMove (String moves, World world){
        //System.out.println(moves);
        for (int i = 0; i < moves.length(); i++) {
            Move(moves.charAt(i), world);
        }

    }

    public static void saveCurrentWorld (World newWorld) throws IOException {
        FileOutputStream fos = new FileOutputStream("byog/Data/savedGame.data");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(newWorld);
        oos.close();
    }

    public static World loadSavedWorld() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("byog/Data/savedGame.data");
        ObjectInputStream ois = new ObjectInputStream(fis);
        World savedWorld = (World) ois.readObject();
        ois.close();
        return savedWorld;
    }
}
