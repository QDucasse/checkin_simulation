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
     * Before each test, an environment is created. Here, a simple baggage is instanciated.
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
     * Checks the volume of the baggage.
     */
    @Test
    public void testVolume() {
        assertEquals(dummyBaggage.getVolume(),6);
    }

    /**
     * Checks that a baggage with null dimensions cannot be created
     */
    @Test
    public void testNullInitializationRaisesException() {
        try{
            Baggage nullBaggage = new Baggage(0,0,0,1);
        } catch (NullDimensionException | NegativeDimensionException e) {
            assertEquals("Attributes cannot be 0",e.getMessage());
        }
    }

    /**
     * Checks that a baggage with negative dimensions cannot be created
     */
    @Test
    public void testNegativeInitializationRaisesException() {
        try{
            Baggage nullBaggage = new Baggage(-4,-1,-2,1);
        } catch (NullDimensionException | NegativeDimensionException e) {
            assertEquals("Attributes cannot be negative",e.getMessage());
        }
    }
}
