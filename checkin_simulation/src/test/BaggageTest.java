package test;

import junit.framework.TestCase;
import main.Baggage;
import org.junit.*;

public class BaggageTest extends TestCase {

    private Baggage dummyBaggage;

    @Before
    public void setUp() {
        dummyBaggage = new Baggage(10,20,30,11);
    }

    @Test
    public void testVolume() {
        assertEquals(dummyBaggage.getVolume(),6);
    }
}
