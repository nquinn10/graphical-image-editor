import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import controller.ControllerImpl;
import controller.IController;
import controller.io.IImageLoader;
import controller.io.IImageSaver;
import controller.io.OtherImageTypeLoader;
import controller.io.OtherImageTypeSaver;
import controller.io.PPMImageLoader;
import controller.io.PPMImageSaver;
import model.IImageState;
import model.IModel;
import model.ModelImpl;
import view.ImageView;
import view.ImageTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test class for the IController interface and other methods in controller package.
 */
public class ControllerTest {

  /**
   * This is a test for the ControllerImpl constructor.
   */

  @Test
  public void testControllerImplConstructor() {
    IModel model = new ModelImpl();
    Readable readable = new StringReader("");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);

    // test IllegalArgumentExceptions
    // null model
    IModel nullModel = null;
    assertThrows(IllegalArgumentException.class, () -> {
      new ControllerImpl(readable, nullModel, view);
    });
    // null view
    ImageView nullView = null;
    assertThrows(IllegalArgumentException.class, () -> {
      new ControllerImpl(readable, model, nullView);
    });
    // null readable
    Readable nullReadable = null;
    assertThrows(IllegalArgumentException.class, () -> {
      new ControllerImpl(nullReadable, model, view);
    });
  }

  /**
   * Test case for invalid command.
   */

  @Test
  public void testInvalidCommand() {
    IModel model = new ModelImpl();
    // incorrect load command argument
    Readable readable = new StringReader("loa res/fourby.ppm fourbyfour");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm correct error message comes up when command is invalid
    String expectedOutput = "Invalid command. Please enter a valid command.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // incorrect brighten command argument
    Readable readable2 = new StringReader("brightenimage res/fourbyfour.ppm");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm correct error message comes up when command is invalid
    String expectedOutput2 = "Invalid command. Please enter a valid command.\n";
    assertEquals(expectedOutput2, appendable2.toString());
  }


  /**
   * Test case for null PPMImageLoader arguments.
   */

  @Test
  public void testNullPPMImageLoaderArguments() {
    // null file path
    assertThrows(IllegalArgumentException.class, () -> {
      new PPMImageLoader(null);
    });

  }

  /**
   * Test case for null PPMImageSaver arguments.
   */

  @Test
  public void testNullPPMImageSaverArguments() {
    IModel model1 = new ModelImpl();
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendable = new StringBuilder();
    // null path to save
    assertThrows(IllegalArgumentException.class, () -> {
      new PPMImageSaver(null, image1, appendable);
    });
    // null image
    assertThrows(IllegalArgumentException.class, () -> {
      new PPMImageSaver("res/fourbyfour.ppm", null, appendable);
    });

  }

  /**
   * Test case for null OtherImageTypeLoader arguments.
   */

  @Test
  public void testNullOtherImageTypeLoaderArguments() {
    // null file path
    assertThrows(IllegalArgumentException.class, () -> {
      new OtherImageTypeLoader(null);
    });

  }

  /**
   * Test case for null OtherImageTypeSaver arguments.
   */

  @Test
  public void testNullOtherImageTypeSaverArguments() {
    IModel model1 = new ModelImpl();
    IImageLoader imageLoader = new OtherImageTypeLoader("res/fourbyfour.png");
    IImageState image1 = imageLoader.run();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    // null path to save
    assertThrows(IllegalArgumentException.class, () -> {
      new OtherImageTypeSaver(null, image1, output);
    });
    // null image
    assertThrows(IllegalArgumentException.class, () -> {
      new OtherImageTypeSaver("res/fourbyfour.ppm", null, output);
    });
    // null output stream
    assertThrows(IllegalArgumentException.class, () -> {
      new OtherImageTypeSaver("res/fourbyfour.ppm", image1, null);
    });

  }

  /**
   * Test case for load PPM image command.
   */

  @Test
  public void testLoadPPMImageCommand() {
    IModel model = new ModelImpl();
    // load command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the loaded image from the model
    IImageState loadedImage = model.getImage("fourbyfour");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("fourbyfour.ppm",loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm loaded PPM file contents are as expected
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for load PNG image command.
   */

  @Test
  public void testLoadPNGImageCommand() {
    IModel model = new ModelImpl();
    // load command for png image
    Readable readable = new StringReader("load res/fourbyfour.png fourbyfour");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm that the confirmation message was written to view
    String expectedOutput1 =
            "Command completed successfully\n";
    assertEquals(expectedOutput1, appendable.toString());

    // get PNG image from model
    IImageState loadedImage = model.getImage("fourbyfour");
    Appendable appendable2 = new StringBuilder();
    // save PNG as PPM
    IImageSaver saveImage = new PPMImageSaver("res/fourbyfourPNGPPM.ppm",
            loadedImage, appendable2);
    saveImage.run();
    // expected string of PNG to PPM file
    // used to verify PNG image was loaded correctly
    String expectedOutput2 =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutput2, appendable2.toString());
  }

  /**
   * Test case for load JPEG image command.
   */

  @Test
  public void testLoadJPEGImageCommand() {
    IModel model = new ModelImpl();
    // load command for jpeg image
    Readable readable = new StringReader("load res/fourbyfour.jpeg fourbyfour");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm that the confirmation message was written to view
    String expectedOutput1 =
            "Command completed successfully\n";
    assertEquals(expectedOutput1, appendable.toString());
    // get jpeg image from model
    IImageState loadedImage = model.getImage("fourbyfour");
    // confirm file was loaded via command by checking file height and width
    // unable to convert to ppm to compare contents due to jpegs using lossy compression
    assertEquals(4, loadedImage.getHeight());
    assertEquals(4, loadedImage.getWidth());
  }

  /**
   * Test case for load image invalid commands.
   */

  @Test
  public void testLoadImageCommandInvalid() {
    IModel model = new ModelImpl();
    // load command with nonexistent file path
    Readable readable = new StringReader("load res/fourby.ppm fourbyfour");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm correct error message comes up when file path is invalid
    String expectedOutput = "Image with specified ID does not exist.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // load command with no dest id
    Readable readable2 = new StringReader("load res/fourbyfour.ppm");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm correct error message comes up when no image id is provided
    String expectedOutput2 = "Third argument must be destination id.\n";
    assertEquals(expectedOutput2, appendable2.toString());

    IModel model3 = new ModelImpl();
    // load command with no file extension
    Readable readable3 = new StringReader("load res/fourbyfour fourbyfour\n");
    Appendable appendable3 = new StringBuilder();
    ImageView view3 = new ImageTextView(model3, appendable3);
    // load command entered into controller
    IController controller3 = new ControllerImpl(readable3, model3, view3);
    controller3.run();
    // confirm correct error message comes up when no image id is provided
    String expectedOutput3 = "Invalid file type. File extension is missing\n";
    assertEquals(expectedOutput3, appendable3.toString());
  }

  /**
   * Test case for save PPM image command.
   */

  @Test
  public void testSavePPMImageCommand() {
    IModel model = new ModelImpl();
    // load and save image
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "save res/fourbyfourSAVETEST.ppm fourbyfour");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();

    IModel model2 = new ModelImpl();
    // load saved image back in
    Readable readable2 = new StringReader("load res/fourbyfourSAVETEST.ppm fourbyfourTEST");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();

    // get the loaded image from the second model
    IImageState loadedImage = model2.getImage("fourbyfourTEST");
    // save the image to StringBuilder appendable
    IImageSaver saveImage =
            new PPMImageSaver("res/fourbyfourSAVETEST.ppm",loadedImage, appendable2);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable2.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm loaded PPM file contents are as expected
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for brightness image command.
   */

  @Test
  public void testEditBrightnessCommand() {
    IModel model = new ModelImpl();
    // load + brighten command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "brighten 10 fourbyfour fourbyfourbrighten");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load + brighten command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("fourbyfourbrighten");
    // save the image to StringBuilder appendable
    IImageSaver saveImage =
            new PPMImageSaver("fourbyfourbrighten.ppm",loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm brightened PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 10 10 10 255 10 10 10 255 255 255 255 \n"
                    + "10 10 10 255 10 10 10 255 10 10 10 255 \n"
                    + "255 10 255 10 10 10 255 10 10 10 255 10 \n"
                    + "10 255 255 255 10 255 10 10 10 255 10 10 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for brighten image invalid commands.
   */

  @Test
  public void testBrightenImageCommandInvalid() {
    IModel model = new ModelImpl();
    // no brighten value
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "brighten fourbyfour fourbyfourbrighten");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput = "Command completed successfully\n"
            + "Second argument must be an int.\n"
            + "Invalid command. Please enter a valid command.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // missing source id
    Readable readable2 = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "brighten 10 ");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput2 = "Command completed successfully\n"
            + "Third argument must be image id.\n";
    assertEquals(expectedOutput2, appendable2.toString());

    IModel model3 = new ModelImpl();
    // load command with no image id
    Readable readable3 = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "brighten 10 fourby fourbyfourbrighten");
    Appendable appendable3 = new StringBuilder();
    ImageView view3 = new ImageTextView(model3, appendable3);
    // load command entered into controller
    IController controller3 = new ControllerImpl(readable3, model3, view3);
    controller3.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput3 = "Command completed successfully\n"
            + "Image with specified ID does not exist.\n";
    assertEquals(expectedOutput3, appendable3.toString());
  }

  /**
   * Test case for darken image command.
   */

  @Test
  public void testEditDarkenCommand() {
    IModel model = new ModelImpl();
    // load + brighten command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "brighten -10 fourbyfour fourbyfourdarken");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("fourbyfourdarken");
    // save the image to StringBuilder appendable
    IImageSaver saveImage =
            new PPMImageSaver("fourbyfourdarken.ppm",loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm darkened PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "245 0 0 0 245 0 0 0 245 245 245 245 \n"
                    + "0 0 0 245 0 0 0 245 0 0 0 245 \n"
                    + "245 0 245 0 0 0 245 0 0 0 245 0 \n"
                    + "0 245 245 245 0 245 0 0 0 245 0 0 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for red component image command.
   */

  @Test
  public void testRedComponentGreyscaleCommand() {
    IModel model = new ModelImpl();
    // load + red component command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "red-component fourbyfour fourbyfourRed");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("fourbyfourRed");
    // save the image to StringBuilder appendable
    IImageSaver saveImage =
            new PPMImageSaver("fourbyfourRed.ppm",loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm red component PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 255 255 0 0 0 0 0 0 255 255 255 \n"
                    + "0 0 0 255 255 255 0 0 0 0 0 0 \n"
                    + "255 255 255 0 0 0 255 255 255 0 0 0 \n"
                    + "0 0 0 255 255 255 0 0 0 255 255 255 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for green component image command.
   */

  @Test
  public void testGreenComponentGreyscaleCommand() {
    IModel model = new ModelImpl();
    // load + green component command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "green-component fourbyfour fourbyfourGreen");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("fourbyfourGreen");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("fourbyfourGreen.ppm",
            loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm green component PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "0 0 0 255 255 255 0 0 0 255 255 255 \n"
                    + "0 0 0 0 0 0 255 255 255 0 0 0 \n"
                    + "0 0 0 0 0 0 0 0 0 255 255 255 \n"
                    + "255 255 255 0 0 0 0 0 0 0 0 0 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for blue component image command.
   */

  @Test
  public void testBlueComponentGreyscaleCommand() {
    IModel model = new ModelImpl();
    // load + blue component command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "blue-component fourbyfour fourbyfourBlue");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("fourbyfourBlue");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("fourbyfourBlue.ppm",
            loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm blue component PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "0 0 0 0 0 0 255 255 255 255 255 255 \n"
                    + "0 0 0 0 0 0 0 0 0 255 255 255 \n"
                    + "255 255 255 0 0 0 0 0 0 0 0 0 \n"
                    + "255 255 255 255 255 255 0 0 0 0 0 0 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for red value component image invalid commands.
   */

  @Test
  public void testRedComponentGreyscaleCommandInvalid() {
    IModel model = new ModelImpl();
    // image does not exist
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "red-component fourby fourbyfourRed");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput = "Command completed successfully\n"
            + "Image with specified ID does not exist.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // missing source id
    Readable readable2 = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "red-component ");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput2 = "Command completed successfully\n"
            + "Second argument must be image id.\n";
    assertEquals(expectedOutput2, appendable2.toString());
  }

  /**
   * Test case for luma greyscale image command.
   */

  @Test
  public void testLumaGreyscaleCommand() {
    IModel model = new ModelImpl();
    // load + luma component command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "luma fourbyfour fourbyfourLuma");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("fourbyfourLuma");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("fourbyfourLuma.ppm",
            loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm luma PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "54 54 54 182 182 182 18 18 18 255 255 255 \n"
                    + "0 0 0 54 54 54 182 182 182 18 18 18 \n"
                    + "73 73 73 0 0 0 54 54 54 182 182 182 \n"
                    + "201 201 201 73 73 73 0 0 0 54 54 54 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for luma image invalid commands.
   */

  @Test
  public void testLumaGreyscaleCommandInvalid() {
    IModel model = new ModelImpl();
    // image does not exist
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "luma fourby fourbyfourRed");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput = "Command completed successfully\n"
            + "Image with specified ID does not exist.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // missing source id
    Readable readable2 = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "luma ");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput2 = "Command completed successfully\n"
            + "Second argument must be image id.\n";
    assertEquals(expectedOutput2, appendable2.toString());
  }

  /**
   * Test case for intensity greyscale image command.
   */

  @Test
  public void testIntensityGreyscaleCommand() {
    IModel model = new ModelImpl();
    // load + intensity component command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "intensity fourbyfour fourbyfourIntensity");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("fourbyfourIntensity");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("fourbyfourIntensity.ppm",
            loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm intensity PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "85 85 85 85 85 85 85 85 85 255 255 255 \n"
                    + "0 0 0 85 85 85 85 85 85 85 85 85 \n"
                    + "170 170 170 0 0 0 85 85 85 85 85 85 \n"
                    + "170 170 170 170 170 170 0 0 0 85 85 85 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for intensity image invalid commands.
   */

  @Test
  public void testIntensityGreyscaleCommandInvalid() {
    IModel model = new ModelImpl();
    // image does not exist
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "intensity fourby fourbyfourRed");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput = "Command completed successfully\n"
            + "Image with specified ID does not exist.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // missing source id
    Readable readable2 = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "intensity ");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput2 = "Command completed successfully\n"
            + "Second argument must be image id.\n";
    assertEquals(expectedOutput2, appendable2.toString());
  }

  /**
   * Test case for value greyscale image command.
   */

  @Test
  public void testValueGreyscaleCommand() {
    IModel model = new ModelImpl();
    // load + value component command
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "value fourbyfour fourbyfourValue");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("fourbyfourValue");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("fourbyfourValue.ppm",
            loadedImage, appendable);
    saveImage.run();
    // split the output into array of lines
    String[] lines = appendable.toString().split("\n");
    // actual PPM string output
    String ppmString = String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 7, lines.length));
    // confirm value PPM file contents are as expected with values updated
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 255 255 255 255 255 255 255 255 255 255 255 \n"
                    + "0 0 0 255 255 255 255 255 255 255 255 255 \n"
                    + "255 255 255 0 0 0 255 255 255 255 255 255 \n"
                    + "255 255 255 255 255 255 0 0 0 255 255 255 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for value image invalid commands.
   */

  @Test
  public void testValueGreyscaleCommandInvalid() {
    IModel model = new ModelImpl();
    // image does not exist
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "value fourby fourbyfourRed");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput = "Command completed successfully\n"
            + "Image with specified ID does not exist.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // missing source id
    Readable readable2 = new StringReader("load res/fourbyfour.ppm fourbyfour "
            + "value ");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput2 = "Command completed successfully\n"
            + "Second argument must be image id.\n";
    assertEquals(expectedOutput2, appendable2.toString());
  }

  /**
   * Test case for blur image command.
   */

  @Test
  public void testBlurCommand() {
    IModel model = new ModelImpl();
    // load + value component command
    Readable readable = new StringReader("load res/twobytwo.ppm twobytwo "
            + "blur twobytwo twobytwoBlur");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("twobytwoBlur");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("twobytwoBlur.ppm",
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
                    + "99 83 73 94 90 83 \n"
                    + "102 80 83 99 83 73 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for blur image invalid commands.
   */

  @Test
  public void testBlurCommandInvalid() {
    IModel model = new ModelImpl();
    // no brighten value
    Readable readable = new StringReader("load res/twobytwo.ppm twobytwo "
            + "blur fourby twobytwoBlur");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput = "Command completed successfully\n"
            + "Image with specified ID does not exist.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // missing source id
    Readable readable2 = new StringReader("load res/twobytwo.ppm twobytwo "
            + "blur ");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput2 = "Command completed successfully\n"
            + "Second argument must be image id.\n";
    assertEquals(expectedOutput2, appendable2.toString());
  }

  /**
   * Test case for sharpen image command.
   */

  @Test
  public void testSharpenCommand() {
    IModel model = new ModelImpl();
    // load + value component command
    Readable readable = new StringReader("load res/twobytwo.ppm twobytwo "
            + "sharpen twobytwo twobytwoSharpen");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // get the image from the model
    IImageState loadedImage = model.getImage("twobytwoSharpen");
    // save the image to StringBuilder appendable
    IImageSaver saveImage = new PPMImageSaver("twobytwoSharpen.ppm",
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
                    + "255 253 182 255 255 255 \n"
                    + "255 253 255 255 253 182 ";
    assertEquals(expectedOutput, ppmString);
    // confirm that the confirmation message was written to view
    String successfulLoadMessage = lines[0];
    assertEquals("Command completed successfully", successfulLoadMessage);
  }

  /**
   * Test case for sharpen image invalid commands.
   */

  @Test
  public void testSharpenCommandInvalid() {
    IModel model = new ModelImpl();
    // no brighten value
    Readable readable = new StringReader("load res/twobytwo.ppm twobytwo "
            + "blur twoby twobytwoSharpen");
    Appendable appendable = new StringBuilder();
    ImageView view = new ImageTextView(model, appendable);
    // load command entered into controller
    IController controller = new ControllerImpl(readable, model, view);
    controller.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput = "Command completed successfully\n"
            + "Image with specified ID does not exist.\n";
    assertEquals(expectedOutput, appendable.toString());

    IModel model2 = new ModelImpl();
    // missing source id
    Readable readable2 = new StringReader("load res/twobytwo.ppm twobytwo "
            + "sharpen ");
    Appendable appendable2 = new StringBuilder();
    ImageView view2 = new ImageTextView(model2, appendable2);
    // load command entered into controller
    IController controller2 = new ControllerImpl(readable2, model2, view2);
    controller2.run();
    // confirm image loaded and brighten error message displays when image doesn't exist
    String expectedOutput2 = "Command completed successfully\n"
            + "Second argument must be image id.\n";
    assertEquals(expectedOutput2, appendable2.toString());
  }

  /**
   * Test case for IOException/IllegalStateException.
   */

  @Test
  public void testIOExceptionController() {
    IModel model = new ModelImpl();
    Readable readable = new StringReader("load res/fourbyfour.ppm fourbyfour");
    ImageView view = new ImageTextView(model, new FailingAppendable());
    IController controller = new ControllerImpl(readable, model, view);

    // check to ensure view throws IOException with failing appendable
    try {
      view.renderMessage("test");
    } catch (IOException e) {
      assertNotNull(e.getMessage());
    }

    // check to ensure controller throws IllegalStateException with failing appendable
    try {
      controller.run();
    } catch (IllegalStateException e) {
      assertNotNull(e.getMessage());
    }

    // test to check IllegalStateException with real appendable
    Readable readable2 = new StringReader("load res/fourbyfour.ppm");
    // using real appendable object
    Appendable appendable = new StringBuilder();
    ImageView view2 = new ImageTextView(model, appendable);
    IController controller2 = new ControllerImpl(readable2, model, view2);

    try {
      // check to ensure controller throws IllegalStateException
      // when runs out of input
      controller2.run();
    } catch (IllegalStateException e) {
      assertNotNull(e.getMessage());
    }

    Readable readable3 = new StringReader("");
    IController controller3 = new ControllerImpl(readable3, model, view2);
    try {
      // check to ensure controller throws IllegalStateException
      // when runs out of input
      controller3.run();
    } catch (IllegalStateException e) {
      assertNotNull(e.getMessage());
    }
  }
}
