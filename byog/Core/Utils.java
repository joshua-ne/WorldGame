package byog.Core;

import byog.TileEngine.TETile;

import java.io.*;

public class Utils {

    public static void Move (char direction, World world) {

        switch (direction){

            case('W'): {

                break;
            }

            case('A'): {

                break;
            }

            case('S'): {

                break;
            }

            case('D'): {

                break;
            }
        }

    }

    public static void playerMove (String moves, World world){


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
