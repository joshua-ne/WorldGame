package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import javax.swing.text.Position;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static  int WIDTH = 60;
    private static  int HEIGHT = 30;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);


    private static class Position {
        public int x, y;
    }


    private static Position rowPosition (Position p, int n, int row) {
        Position rowP = new Position();
        if (row <= n) {
            rowP.x = p.x - (row - 1);
            rowP.y = p.y - (row - 1);
        } else {
            rowP.x = p.x - (n - 1) + row - n - 1;
            rowP.y = p.y -(row - 1);
        }

        return rowP;
    }

    private static int numToDraw (int n, int row) {
        if (row > n) {
            return 3*n-2-2*(row-n-1);
        } else {
            return n + 2 * (row -1);
        }
    }

    private static void drawLine (TETile [][] world, Position p, int x, TETile t){
        for (int i = 0; i < x; i++){
            world[p.x + i][p.y] = t;
        }
    }


    public static void addHexagon (TETile [][] world, Position p, int width, TETile t) {
        for (int i = 1; i < 2*width + 1; i++){
            drawLine(world, rowPosition(p,width,i), numToDraw(width,i), t);
        }

    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(8);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.TREE;
            case 4: return Tileset.SAND;
            case 5: return Tileset.MOUNTAIN;
            case 6: return Tileset.FLOOR;
            case 7: return Tileset.LOCKED_DOOR;
            default: return Tileset.WATER;
        }
    }

    private static int[] calSize (int width) {
        int[] size = new int[2];
        size[0] = 10 * width;
        size[1] = 2*width + 3 * (3*width - 2);
        return size;
    }

    private static Position calFirstPoint(TETile[][] world, int width) {
        int[] size = calSize(width);
        Position firstPoint = new Position();
        firstPoint.x = (world[0].length - size[1]) / 2;
        firstPoint.y = (world.length + size[0]) / 2;
        return firstPoint;

    }


    private static Position positionOfHexAbove(Position p, int nAbove, int width) {
        Position pAbove = new Position();
        pAbove.x = p.x;
        pAbove.y = p.y - (width * 2) * nAbove;
        return pAbove;
    }

    private static Position columnStartPosition (Position firstPoint, int column, int width) {

        Position startPosition = new Position();
        startPosition.x = firstPoint.x + (2*width - 1) * column;
        switch (column) {
            case 0: startPosition.y = firstPoint.y; break;
            case 1: startPosition.y = firstPoint.y + width;break;
            case 2: startPosition.y = firstPoint.y + width*2;break;
            case 3: startPosition.y = firstPoint.y + width;break;
            default: startPosition.y = firstPoint.y;break;
        }
        return startPosition;
    }

    private static void drawColumn(TETile[][] world, Position startP, int numToDraw, int width){
        for (int i = 0; i < numToDraw; i++) {
            addHexagon(world, positionOfHexAbove(startP, i, width), width, randomTile());
        }

    }


    public static void main (String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        int width = 4;

        WIDTH = calSize(width)[1] *2;
        HEIGHT = calSize(width)[0] * 2;

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);


        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        Position firstPoint = calFirstPoint(world,width);


        int numToDraw;
        //add Hexagons to the world
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0: numToDraw = 3;break;
                case 1: numToDraw = 4;break;
                case 2: numToDraw = 5;break;
                case 3: numToDraw = 4;break;
                default: numToDraw = 3;break;
            }
            drawColumn(world,columnStartPosition(firstPoint,i, width), numToDraw, width);
        }


        // draws the world to the screen
        ter.renderFrame(world);
    }


}
