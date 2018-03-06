package byog.Core.tests;

import byog.Core.InputParser;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class inputParser_test {

    @Test
    public void test() {

        //test on isNew, saveOrNot, seed, moves
        String input = "N2948938592WASDDWSASDWSAW";
        InputParser p = new InputParser(input);

        assertTrue(p.isNew);
        assertFalse(p.saveOrNot);
        assertEquals(p.seed, 2948938592L);
        assertEquals(p.moves, "WASDDWSASDWSAW");


    }
}
