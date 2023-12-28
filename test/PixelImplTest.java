import org.junit.Test;

import model.IPixel;
import model.PixelImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test class for the IPixelState and IPixel interfaces and
 *     the PixelImpl class.
 */

public class PixelImplTest {

  /**
   * This is a test case for the pixel methods. The constructor,
   *     getters, setters, and toString are all tested.
   */

  @Test
  public void testPixelMethods() {
    // construct a valid pixel
    IPixel pixel = new PixelImpl(10,0,255);
    // confirm values are as expected
    assertEquals(10,pixel.getR());
    assertEquals(0,pixel.getG());
    assertEquals(255,pixel.getB());
    assertEquals("10 0 255", pixel.toString());

    // set new pixel values
    pixel.setR(50);
    // confirm value was set
    assertEquals(50,pixel.getR());
    pixel.setG(255);
    // confirm value was set
    assertEquals(255,pixel.getG());
    pixel.setB(0);
    // confirm value was set
    assertEquals(0,pixel.getB());
    // confirm new pixel is as expected
    assertEquals("50 255 0", pixel.toString());

    // test IllegalArgumentExceptions
    // check to see error message appears as expected - invalid pixel
    try {
      new PixelImpl(-1, 50,255);
    } catch (IllegalArgumentException e) {
      assertEquals("Component values cannot be less than 0 or exceed 255", e.getMessage());
    }
    // invalid r
    assertThrows(IllegalArgumentException.class, () -> {
      new PixelImpl(-1, 50,255);
    });
    // invalid g
    assertThrows(IllegalArgumentException.class, () -> {
      new PixelImpl(10, 256,255);
    });
    // invalid b
    assertThrows(IllegalArgumentException.class, () -> {
      new PixelImpl(10, 255,260);
    });
    // check to see error message appears as expected - invalid set
    try {
      pixel.setR(-1);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid channel value", e.getMessage());
    }
    // invalid set r
    assertThrows(IllegalArgumentException.class, () -> {
      pixel.setR(256);
    });
    // invalid set g
    assertThrows(IllegalArgumentException.class, () -> {
      pixel.setG(-10);
    });
    // invalid set b
    assertThrows(IllegalArgumentException.class, () -> {
      pixel.setB(-1);
    });
  }
}
