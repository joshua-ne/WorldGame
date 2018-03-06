package byog.Core.tests;

import byog.Core.InputParser;
import byog.Core.WorldGenerator;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

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
        InputParser p = new InputParser(input);

        //initialize the world
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        wg.BuildAWorld(p.seed, finalWorldFrame);
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
