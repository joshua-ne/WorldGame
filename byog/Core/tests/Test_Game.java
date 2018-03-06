package byog.Core.tests;

import byog.Core.Game;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.io.IOException;


public class Test_Game extends Game{

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        TERenderer ter = new TERenderer();
        Test_Game game = new Test_Game();
        ter.initialize(WIDTH, HEIGHT);
        //TETile[][] worldState = game.playWithInputString("N294899038592DDS:Q");
        TETile[][] worldState = game.playWithInputString("LDDDDDDD");
        //System.out.println(TETile.toString(worldState));
        ter.renderFrame(worldState);
    }
}
