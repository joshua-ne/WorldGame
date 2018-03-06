package byog.Core.tests;

import byog.Core.utils.InputParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class inputParser_test {

    @Test
    public void test() {

        //test on isNew, saveOrNot, seed, moves
        String input = "LWASDDWSASDWSAW";
        InputParser p = new InputParser(input);

        assertFalse(p.isNew);
        assertFalse(p.saveOrNot);
        assertEquals(p.seed, 0);
        assertEquals(p.moves, "WASDDWSASDWSAW");


    }
}
