package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import static byog.Core.playWithInputString_helper.*;

public class Test_Game {
    //instantiate tools, may need to delete later
    public static TERenderer ter = new TERenderer();
    public static WorldGenerator wg = new WorldGenerator();


    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        //parse the input String
        int seed = extractSeed(input);
        boolean isNew = isNew(input);
        String moves = extractMoves(input);
        boolean save = saveOrNot(input);

        //initialize the world
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        wg.BuildAWorld(seed, finalWorldFrame);
        return finalWorldFrame;
    }

    public static void main(String[] args){
        Test_Game game = new Test_Game();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] worldState = game.playWithInputString("N12345");
        System.out.println(TETile.toString(worldState));
        ter.renderFrame(worldState);

    }
}
