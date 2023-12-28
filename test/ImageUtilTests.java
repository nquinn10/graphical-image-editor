import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import controller.ImageUtil;
import controller.io.IImageLoader;
import controller.io.PPMImageLoader;
import model.IImageState;
import model.IModel;
import model.ModelImpl;

/**
 * A JUnit test class for the ImageUtil class.
 */

public class ImageUtilTests {

  /**
   * Test case for getFileExtension method.
   */

  @Test
  public void testGetFileExtension() {
    // ppm file extension
    String ppmResult = ImageUtil.getFileExtension("res/fourbyfour.ppm");
    assertEquals("ppm", ppmResult);
    // jpeg file extension
    String jpegResult = ImageUtil.getFileExtension("res/logan.jpeg");
    assertEquals("jpeg", jpegResult);
    // png file extension
    String pngResult = ImageUtil.getFileExtension("res/bagel.png");
    assertEquals("png", pngResult);

    // test IllegalArgumentExceptions
    // check to see error message appears as expected - no file extension
    try {
      String invalidExtension = ImageUtil.getFileExtension("res/bagel");
    } catch (IllegalStateException e) {
      assertEquals("Invalid file type. File extension is missing\n", e.getMessage());
    }
    // null ImageID
    assertThrows(IllegalStateException.class, () -> {
      String invalidExtension = ImageUtil.getFileExtension("res/bagel");
    });
  }

  /**
   * Test case for createBufferedImage method.
   */

  @Test
  public void testCreateBufferedImage() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    BufferedImage bufferedImage = ImageUtil.createBufferedImage(image1);
    // confirm height and width
    assertEquals(4, bufferedImage.getHeight());
    assertEquals(4, bufferedImage.getWidth());

    // get RGB values of the pixel at (0, 0)
    int pixelRGB = bufferedImage.getRGB(0, 0);
    int red = (pixelRGB >> 16) & 0xFF;
    int green = (pixelRGB >> 8) & 0xFF;
    int blue = pixelRGB & 0xFF;

    // assert the individual RGB values
    assertEquals(255, red);
    assertEquals(0, green);
    assertEquals(0, blue);
  }
}
