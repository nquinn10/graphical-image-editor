import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;

import controller.ControllerImpl;
import controller.IController;
import controller.io.IImageLoader;
import controller.io.IImageSaver;
import controller.io.PPMImageLoader;
import controller.io.PPMImageSaver;
import model.IImageState;
import model.IModel;
import model.ModelImpl;
import model.kernel.IKernel;
import model.kernel.KernelImpl;
import model.transformations.ColorTransformation;
import model.transformations.ITransformation;
import view.ImageView;
import view.ImageTextView;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for the bonus features -
 * colorTransformation, SepiaMatrixCommand, and GreyscaleMatrixCommand.
 */

public class BonusFeatures {

  /**
   * Test case for sepia image color transformation.
   */

  @Test
  public void testModelSepiaTransformImage() {
    IModel model1 = new ModelImpl();
    // load twobytwo ppm
    IImageLoader imageLoader = new PPMImageLoader("res/twobytwo.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/twobytwo.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "2 2\n"
                    + "255\n"
                    + "185 137 58 146 188 219 \n"
                    + "187 137 219 185 137 58 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());
    // create sepia matrix filter
    double[][] sepiaFilter = {
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    };
    // create kernel object and pass to transformation call
    IKernel sepiaMatrix = new KernelImpl(sepiaFilter);
    // blur transform
    ITransformation sepiaMatrixTransform = new ColorTransformation(sepiaMatrix);
    IImageState sepiaTransformImage = sepiaMatrixTransform.run(image1);

    Appendable appendable = new StringBuilder();
    // save image to appendable
    IImageSaver saveImage = new PPMImageSaver("res/twobytwoSepia.ppm",
            sepiaTransformImage, appendable);
    saveImage.run();
    // expected string of image PPM AFTER transform
    // used to verify image was loaded and sepia was calculated correctly
    String expectedOutput =
            "P3\n"
                    + "2 2\n"
                    + "255\n"
                    + "189 168 131 243 217 169 \n"
                    + "220 196 153 189 168 131 \n";
    assertEquals(expectedOutput, appendable.toString());
  }

  /**
   * Test case for greyscale image color transformation.
   */

  @Test
  public void testModelGreyscaleTransformImage() {
    IModel model1 = new ModelImpl();
    // load twobytwo ppm
    IImageLoader imageLoader = new PPMImageLoader("res/twobytwo.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/twobytwo.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "2 2\n"
                    + "255\n"
                    + "185 137 58 146 188 219 \n"
                    + "187 137 219 185 137 58 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());
    // create greyscale matrix filter
    double[][] greyscaleFilter = {
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    };
    // create kernel object and pass to transformation call
    IKernel greyscaleMatrix = new KernelImpl(greyscaleFilter);
    // blur transform
    ITransformation greyscaleMatrixTransform = new ColorTransformation(greyscaleMatrix);
    IImageState greyscaleTransformImage = greyscaleMatrixTransform.run(image1);

    Appendable appendable = new StringBuilder();
    // save image to appendable
    IImageSaver saveImage = new PPMImageSaver("res/twobytwoSepia.ppm",
            greyscaleTransformImage, appendable);
    saveImage.run();
    // expected string of image PPM AFTER transform
    // used to verify image was loaded and sepia was calculated correctly
    String expectedOutput =
            "P3\n"
                    + "2 2\n"
                    + "255\n"
                    + "142 142 142 181 181 181 \n"
                    + "154 154 154 142 142 142 \n";
    assertEquals(expectedOutput, appendable.toString());
  }

  /**
   * Test case for sepia image command.
   */

  @Test
  public void testSepiaCommand() {
    IModel model = new ModelImpl();
    // load + value component command
    Readable readable = new StringReader("load res/twobytwo.ppm twobytwo "
            + "sepia twobytwo twobytwoSepia");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("twobytwoSepia");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("twobytwoSepia.ppm",
            loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 5, lines.length));
    // confirm value PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "2 2\n"
                    + "255\n"
                    + "189 168 131 243 217 169 \n"
                    + "220 196 153 189 168 131 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for greyscale image command.
   */

  @Test
  public void testGreyscaleCommand() {
    IModel model = new ModelImpl();
    // load + value component command
    Readable readable = new StringReader("load res/twobytwo.ppm twobytwo "
            + "greyscale twobytwo twobytwoGreyscale");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("twobytwoGreyscale");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("twobytwoGreyscale.ppm",
            loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 5, lines.length));
    // confirm value PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "2 2\n"
                    + "255\n"
                    + "142 142 142 181 181 181 \n"
                    + "154 154 154 142 142 142 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

}
