package byog.Core.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class InputParser {

    public long seed;
    public String moves;
    public boolean isNew, saveOrNot;

    public InputParser (String input) {
        seed = extractSeed(input);
        moves = extractMoves(input);
        isNew = isNew(input);
        saveOrNot = saveOrNot(input);
    }

    //extract the int seed inside the input string
    public static long extractSeed (String input){
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m =p.matcher(input);
        if (m.find()) return Long.parseLong(m.group(0));
        else if (!isNew(input)) return 0;
        else throw new RuntimeException("Wrong input argument: no valid seed found!");
    }

    // if the input string start with an "N" return true (means starting a new game)
    // if the input string start with a "L", return false (means loading previous game)
    // else: optional: throw exception, waring that the input argument is not legal
    public static boolean isNew (String input){
        char ch = input.charAt(0);
        if (ch == 'N') return true;
        else if (ch == 'L') return false;
        else throw new RuntimeException("Wrong input argument");
    }

    public static String extractMoves(String input){
        Pattern p = Pattern.compile("[WASD]+");
        Matcher m =p.matcher(input);
        if (m.find()) return m.group(0);
        else throw new RuntimeException("Wrong input argument: no valid moves found!");
    }

    public static boolean saveOrNot(String input){
        Pattern p = Pattern.compile(":Q");
        Matcher m =p.matcher(input);
        if (m.find()) return true;
        else return false;
    }



}
