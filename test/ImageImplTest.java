import org.junit.Test;

import controller.io.IImageLoader;
import controller.io.PPMImageLoader;
import model.IImage;
import model.IImageState;
import model.ImageImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test class for the IImageState and IImage interfaces and
 *     the ImageImpl class.
 */

public class ImageImplTest {

  private IImage testImageBasic;
  private IImageState testImageLoad;

  /**
   * This is a test case for the image constructor. Getters
   *     are also tested.
   */

  @Test
  public void testImageConstructor() {
    // construct a valid image manually
    testImageBasic = new ImageImpl(5,5);
    // confirm components are as expected
    assertEquals(5, testImageBasic.getHeight());
    assertEquals(5, testImageBasic.getWidth());
    assertEquals(255, testImageBasic.getMaxValue());

    // construct a valid image via load
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    // create the image object
    testImageLoad = imageLoader.run();
    // confirm components are as expected
    assertEquals(4, testImageLoad.getHeight());
    assertEquals(4, testImageLoad.getWidth());
    assertEquals(255, testImageLoad.getMaxValue());

    // test IllegalArgumentExceptions
    // check to see error message appears as expected - invalid height
    try {
      new ImageImpl(-1, 5);
    } catch (IllegalArgumentException e) {
      assertEquals("Cannot have negative height/width", e.getMessage());
    }
    // invalid height
    assertThrows(IllegalArgumentException.class, () -> {
      new ImageImpl(-10, 50);
    });
    // invalid width
    assertThrows(IllegalArgumentException.class, () -> {
      new ImageImpl(10, -1);
    });
  }

  /**
   * Test case for image getters and setters.
   */

  @Test
  public void testImageGettersAndSetters() {
    // construct a valid image manually
    testImageBasic = new ImageImpl(5,5);
    // set pixels to testImageBasic
    testImageBasic.setPixel(0,0,10,0,255);
    testImageBasic.setPixel(0,1,0,0,255);
    // confirm pixels were set as expected in correct location
    assertEquals("10 0 255", testImageBasic.getPixel(0,0).toString());
    assertEquals("0 0 255", testImageBasic.getPixel(0,1).toString());
    // confirm pixel values at given location are as expected
    assertEquals(10, testImageBasic.getRedChannel(0,0));
    assertEquals(0, testImageBasic.getGreenChannel(0,0));
    assertEquals(255, testImageBasic.getBlueChannel(0,0));

    // construct a valid image via load
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    // create the image object
    testImageLoad = imageLoader.run();
    // confirm pixels were loaded as expected in correct location
    assertEquals("255 0 0", testImageLoad.getPixel(0,0).toString());
    assertEquals("0 255 0", testImageLoad.getPixel(0,1).toString());
    // confirm pixel values at given location are as expected
    assertEquals(255, testImageLoad.getRedChannel(0,0));
    assertEquals(0, testImageLoad.getGreenChannel(0,0));
    assertEquals(0, testImageLoad.getBlueChannel(0,0));

    // test IllegalArgumentExceptions
    // check to see error message appears as expected - invalid height
    try {
      testImageLoad.getRedChannel(-1,2);
    } catch (IllegalArgumentException e) {
      assertEquals("row or col out of bounds", e.getMessage());
    }
    // get red channel - row < 0
    assertThrows(IllegalArgumentException.class, () -> {
      testImageLoad.getRedChannel(-1,2);
    });
    // get red channel - row >= height
    assertThrows(IllegalArgumentException.class, () -> {
      testImageLoad.getRedChannel(0,4);
    });
    // get green channel - row < 0
    assertThrows(IllegalArgumentException.class, () -> {
      testImageLoad.getGreenChannel(-2,2);
    });
    // get green channel - row >= height
    assertThrows(IllegalArgumentException.class, () -> {
      testImageLoad.getGreenChannel(0,5);
    });
    // get blue channel - row < 0
    assertThrows(IllegalArgumentException.class, () -> {
      testImageLoad.getBlueChannel(-5,2);
    });
    // get blue channel - row >= height
    assertThrows(IllegalArgumentException.class, () -> {
      testImageLoad.getBlueChannel(0,5);
    });
    // getPixel - row < 0
    assertThrows(IllegalArgumentException.class, () -> {
      testImageLoad.getPixel(-5,2);
    });
    // getPixel - row >= height
    assertThrows(IllegalArgumentException.class, () -> {
      testImageLoad.getPixel(0,5);
    });
    // setPixel - row < 0
    assertThrows(IllegalArgumentException.class, () -> {
      testImageBasic.setPixel(-5,2,10, 20, 255);
    });
    // setPixel - row >= height
    assertThrows(IllegalArgumentException.class, () -> {
      testImageBasic.setPixel(0,5,10, 20, 255);
    });
    // setPixel - invalid r
    assertThrows(IllegalArgumentException.class, () -> {
      testImageBasic.setPixel(0,2,-10, 20, 255);
    });
    // setPixel - invalid g
    assertThrows(IllegalArgumentException.class, () -> {
      testImageBasic.setPixel(0,2,10, 256, 255);
    });
    // setPixel - invalid b
    assertThrows(IllegalArgumentException.class, () -> {
      testImageBasic.setPixel(0,2,10, 20, -20);
    });

  }

}
