package test;

import junit.framework.TestCase;
import main.Baggage;
import main.exceptions.NegativeDimensionException;
import main.exceptions.NullDimensionException;
import org.junit.Before;
import org.junit.Test;

public class BaggageTest extends TestCase {

    private Baggage dummyBaggage;

    /**
     *
     */
    @Before
    public void setUp() throws NullDimensionException {
        try {
            dummyBaggage = new Baggage(10,20,30,11);
        } catch (NegativeDimensionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    @Test
    public void testVolume() {
        assertEquals(dummyBaggage.getVolume(),6);
    }

    @Test
    public void testInitializationRaisesException() {
        try{
            Baggage nullBaggage = new Baggage(0,0,0,1);
        } catch (NullDimensionException | NegativeDimensionException e) {
            assertEquals("Attributes cannot be 0",e.getMessage());
        }

    }
}
