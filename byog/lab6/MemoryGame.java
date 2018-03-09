package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private int seed;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        MemoryGame game = new MemoryGame(40, 40);
        game.seed = Integer.parseInt(args[0]);
        game.startGame();
    }

    public MemoryGame(){}
    
    public MemoryGame(int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        char[] stringList = new char[n];
        for (int i = 0; i < n; i++) {
            stringList[i] = CHARACTERS[rand.nextInt(26)];
        }
        return new String(stringList);
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(width/2, height/2, s);

        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.textLeft(0,height-1,String.format("Round: %d", round));
        StdDraw.textRight(width,height-1,ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        if (playerTurn) {
            StdDraw.text(width/2, height-1, "Type!");
        } else {
            StdDraw.text(width/2, height-1, "Watch!");
        }
        StdDraw.line(0,height*9.5/10, width, height*9.5/10);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        //System.out.print(round);

        for(int i = 0; i < letters.length(); i++) {
            drawFrame(Character.toString(letters.charAt(i)));
            StdDraw.pause(1000);
            StdDraw.clear(Color.black);
            drawFrame(" ");
            StdDraw.pause(500);

        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        char[] str = new char[n];
        int i = 0;
        // clear the keyboard cache
        while (StdDraw.hasNextKeyTyped()) StdDraw.nextKeyTyped();
        //System.out.print(round);

        while (true) {

            if (i == n) break;
            if (StdDraw.hasNextKeyTyped()) {
                str[i] = StdDraw.nextKeyTyped();
                i++;
                drawFrame(new String(str));
            }
        }
        StdDraw.pause(500);
        return new String(str);
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        MemoryGame game = new MemoryGame(40, 40);
        round = 1;
        gameOver = false;
        seed = 123;

        //TODO: Establish Game loop
        while (gameOver == false) {
            playerTurn = false;
            drawFrame(String.format("Round %d", round));
            StdDraw.pause(1000);
            String gameString = game.generateRandomString(round);
            flashSequence(gameString);

            playerTurn = true;
            String user_input = solicitNCharsInput(round);
            if (user_input.equals(gameString)) {
                round += 1;
            } else {
                gameOver = true;
            }
        }

        drawFrame("Wrong Answer. Game ended!");
    }

}
