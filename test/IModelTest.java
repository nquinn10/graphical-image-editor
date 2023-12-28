import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import controller.io.IImageSaver;
import controller.io.OtherImageTypeLoader;
import controller.io.OtherImageTypeSaver;
import controller.io.PPMImageLoader;
import controller.io.PPMImageSaver;
import model.IImageState;
import model.IModel;
import model.ModelImpl;
import controller.io.IImageLoader;
import model.GreyscaleComponent;
import model.kernel.IKernel;
import model.kernel.KernelImpl;
import model.transformations.BrightenTransformation;
import model.transformations.FilterTransformation;
import model.transformations.ITransformation;
import model.transformations.IntensityTransformation;
import model.transformations.LumaTransformation;
import model.transformations.ValueComponentTransformation;
import model.transformations.ValueTransformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test class for the IModel interface and
 *     the ModelImpl class.
 */

public class IModelTest {

  /**
   * Test case for add image and get image methods.
   */

  @Test
  public void testModelAddImage() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("res/fourbyfour.ppm",image1, appendable);
    saveImage.run();
    model1.addImage("fourbyfour", image1);
    // expected string of image PPM
    // used to verify image was loaded correctly and verifying getting elements from the model
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutput, appendable.toString());
    // confirm image was added correctly by getting image from model and verifying pixel values
    assertEquals(4, model1.getImage("fourbyfour").getHeight());
    assertEquals(4, model1.getImage("fourbyfour").getWidth());
    assertEquals(255, model1.getImage("fourbyfour").getPixel(0,0).getR());
    assertEquals(0, model1.getImage("fourbyfour").getPixel(0,0).getG());
    assertEquals(0, model1.getImage("fourbyfour").getPixel(0,0).getB());
    assertEquals(0, model1.getImage("fourbyfour").getPixel(0,1).getR());
    assertEquals(255, model1.getImage("fourbyfour").getPixel(0,1).getG());
    assertEquals(0, model1.getImage("fourbyfour").getPixel(0,1).getB());
    assertEquals(255, model1.getImage("fourbyfour").getPixel(3,1).getR());
    assertEquals(0, model1.getImage("fourbyfour").getPixel(3,1).getG());
    assertEquals(255, model1.getImage("fourbyfour").getPixel(3,1).getB());

    // test IllegalArgumentExceptions
    // check to see error message appears as expected - null imageID
    try {
      model1.addImage(null, image1);
    } catch (IllegalArgumentException e) {
      assertEquals("ID or Image cannot be null", e.getMessage());
    }
    // null ImageID
    assertThrows(IllegalArgumentException.class, () -> {
      model1.addImage(null, image1);
    });
    // null image object
    assertThrows(IllegalArgumentException.class, () -> {
      model1.addImage("fourbyfour", null);
    });
  }

  /**
   * Test case for load PNG image.
   */

  @Test
  public void testLoadPNGImage() {
    IModel model1 = new ModelImpl();
    // load png image
    IImageLoader imageLoader = new OtherImageTypeLoader("res/fourbyfour.png");
    IImageState image1 = imageLoader.run();
    model1.addImage("fourbyfourPNG", image1);
    // check to make sure image height/width are as expected
    assertEquals(4, model1.getImage("fourbyfourPNG").getHeight());
    assertEquals(4, model1.getImage("fourbyfourPNG").getWidth());
    // check to make sure image is not null after being loaded
    assertNotNull(image1);
  }

  /**
   * Test case for load JPEG image.
   */

  @Test
  public void testLoadJPEGImage() {
    IModel model1 = new ModelImpl();
    // load jpeg image
    IImageLoader imageLoader = new OtherImageTypeLoader("res/fourbyfour.jpeg");
    IImageState image1 = imageLoader.run();
    model1.addImage("fourbyfourJPEG", image1);
    // check to make sure image height/width are as expected
    assertEquals(4, model1.getImage("fourbyfourJPEG").getHeight());
    assertEquals(4, model1.getImage("fourbyfourJPEG").getWidth());
    // check to make sure image is not null after being loaded
    assertNotNull(image1);
  }

  /**
   * Test case for save PNG image - saving an actual PNG to res/.
   */

  @Test
  public void testSavePNGImage() {
    IModel model1 = new ModelImpl();
    // load ppm image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    model1.addImage("fourbyfour", image1);

    // save image as PNG
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    IImageSaver savePNGImage =
            new OtherImageTypeSaver("res/fourbyfour.png", image1, output);
    // run - can check res folder for saved image
    savePNGImage.run();
    // load saved PNG and add to model
    IImageLoader imageLoader2 = new OtherImageTypeLoader("res/fourbyfour.png");
    IImageState image2 = imageLoader2.run();
    model1.addImage("fourbyfourPNG", image2);
    // check to make sure image height/width are as expected
    assertEquals(4, model1.getImage("fourbyfourPNG").getHeight());
    assertEquals(4, model1.getImage("fourbyfourPNG").getWidth());
    // check to make sure image is not null after being loaded
    assertNotNull(image2);
  }

  /**
   * Test case for save PNG image - verifying image contents.
   */

  @Test
  public void testSavePPMtoPNGImage() {
    IModel model1 = new ModelImpl();
    // load ppm image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    model1.addImage("fourbyfour", image1);

    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM
    // used to verify image was loaded correctly and compare to PPM to PNG file output
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // save PPM image as PNG
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    IImageSaver savePNGImage =
            new OtherImageTypeSaver("res/fourbyfour.png", image1, output);
    savePNGImage.run();

    // load saved PNG and add to model
    IImageLoader imageLoader2 = new OtherImageTypeLoader("res/fourbyfour.png");
    IImageState image2 = imageLoader2.run();
    model1.addImage("fourbyfourPNG", image2);

    // get PNG image from model
    IImageState image3 = model1.getImage("fourbyfourPNG");
    Appendable appendable = new StringBuilder();

    // save PNG as PPM
    IImageSaver saveImage = new PPMImageSaver("res/fourbyfourPNGPPM.ppm",
            image3, appendable);
    saveImage.run();
    // expected string of PNG to PPM file
    // used to verify PNG image was loaded and saved correctly
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutput, appendable.toString());
  }


  /**
   * Test case for save JPEG image - verifying image contents.
   */

  @Test
  public void testSavePPMtoJPEGImage() {
    IModel model1 = new ModelImpl();
    // load ppm image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    model1.addImage("fourbyfour", image1);

    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM
    // used to verify image was loaded correctly and compare to PPM to JPEG file output
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // save PPM image as JPEG
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    IImageSaver savePNGImage =
            new OtherImageTypeSaver("res/fourbyfour.jpeg", image1, output);
    savePNGImage.run();

    // load saved jpeg and add to model
    IImageLoader imageLoader2 = new OtherImageTypeLoader("res/fourbyfour.jpeg");
    IImageState image2 = imageLoader2.run();
    model1.addImage("fourbyfourJPEG", image2);

    // due to lossy compression of JPEGs cannot compare contents directly
    // checking to be sure image size remains the same and JPEG image exists
    // meaning it was saved as a jpeg correctly
    assertEquals(4, image2.getHeight());
    assertEquals(4, image2.getWidth());
    assertNotNull(image2);
  }

  /**
   * Test case for brighten image transformation.
   */

  @Test
  public void testModelBrightenImage() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // brighten image
    ITransformation brightenTransform = new BrightenTransformation(10);
    IImageState brightenedImage = brightenTransform.run(image1);
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("res/brightenedImage.ppm",
            brightenedImage, appendable);
    saveImage.run();
    // add image to model
    model1.addImage("brightenedImage.ppm", brightenedImage);
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 10 10 10 255 10 10 10 255 255 255 255 \n"
                    + "10 10 10 255 10 10 10 255 10 10 10 255 \n"
                    + "255 10 255 10 10 10 255 10 10 10 255 10 \n"
                    + "10 255 255 255 10 255 10 10 10 255 10 10 \n";
    // confirm ppm of transformed image is as expected
    assertEquals(expectedOutput, appendable.toString());
    // confirm image and pixel values are equal to expected
    assertEquals(4, model1.getImage("brightenedImage.ppm").getHeight());
    assertEquals(4, model1.getImage("brightenedImage.ppm").getWidth());
    assertEquals(10, model1.getImage("brightenedImage.ppm")
            .getPixel(0,1).getR());
    // confirm that pixel channel value does not exceed 255 even hen 10 is added
    // confirm clamp works correctly
    assertEquals(255, model1.getImage("brightenedImage.ppm")
            .getPixel(0,1).getG());
    assertEquals(10, model1.getImage("brightenedImage.ppm")
            .getPixel(0,1).getB());
  }

  /**
   * Test case for darken image transformation.
   */

  @Test
  public void testModelDarkenImage() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // darken image
    ITransformation brightenTransform = new BrightenTransformation(-10);
    IImageState darkenedImage = brightenTransform.run(image1);
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("darkenedImage.ppm",
            darkenedImage, appendable);
    saveImage.run();
    // add image to model
    model1.addImage("darkenedImage.ppm", darkenedImage);
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "245 0 0 0 245 0 0 0 245 245 245 245 \n"
                    + "0 0 0 245 0 0 0 245 0 0 0 245 \n"
                    + "245 0 245 0 0 0 245 0 0 0 245 0 \n"
                    + "0 245 245 245 0 245 0 0 0 245 0 0 \n";
    // confirm ppm of transformed image is as expected
    assertEquals(expectedOutput, appendable.toString());
    // confirm image and pixel values are equal to expected
    assertEquals(4, model1.getImage("darkenedImage.ppm").getHeight());
    assertEquals(4, model1.getImage("darkenedImage.ppm").getWidth());
    // confirm that pixel channel value does not run below 0 even hen 10 is subtracted
    // confirm clamp works correctly
    assertEquals(0, model1.getImage("darkenedImage.ppm")
            .getPixel(0,1).getR());
    assertEquals(245, model1.getImage("darkenedImage.ppm")
            .getPixel(0,1).getG());
    assertEquals(0, model1.getImage("darkenedImage.ppm")
            .getPixel(0,1).getB());
  }

  /**
   * Test case for red component image transformation.
   */

  @Test
  public void testModelRedComponentGreyscaleImage() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // red-component image
    ITransformation redComponentTransformation =
            new ValueComponentTransformation(GreyscaleComponent.RED);
    IImageState redComponentGreyscaleImage = redComponentTransformation.run(image1);
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("redComponentGreyscaleImage.ppm",
            redComponentGreyscaleImage, appendable);
    saveImage.run();
    // add image to model
    model1.addImage("redComponentGreyscaleImage.ppm", redComponentGreyscaleImage);
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 255 255 0 0 0 0 0 0 255 255 255 \n"
                    + "0 0 0 255 255 255 0 0 0 0 0 0 \n"
                    + "255 255 255 0 0 0 255 255 255 0 0 0 \n"
                    + "0 0 0 255 255 255 0 0 0 255 255 255 \n";
    // confirm ppm of transformed image is as expected
    assertEquals(expectedOutput, appendable.toString());
    // confirm image and pixel values are equal to expected
    assertEquals(4, model1.getImage("redComponentGreyscaleImage.ppm").getHeight());
    assertEquals(4, model1.getImage("redComponentGreyscaleImage.ppm").getWidth());
    assertEquals(0, model1.getImage("redComponentGreyscaleImage.ppm")
            .getPixel(0,1).getR());
    assertEquals(0, model1.getImage("redComponentGreyscaleImage.ppm")
            .getPixel(0,1).getG());
    assertEquals(0, model1.getImage("redComponentGreyscaleImage.ppm")
            .getPixel(0,1).getB());
  }

  /**
   * Test case for green component image transformation.
   */

  @Test
  public void testModelGreenComponentGreyscaleImage() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // green-component
    ITransformation greenComponentTransformation =
            new ValueComponentTransformation(GreyscaleComponent.GREEN);
    IImageState greenComponentGreyscaleImage = greenComponentTransformation.run(image1);
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("greenComponentGreyscaleImage.ppm",
            greenComponentGreyscaleImage, appendable);
    saveImage.run();
    // add image to model
    model1.addImage("greenComponentGreyscaleImage.ppm", greenComponentGreyscaleImage);
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "0 0 0 255 255 255 0 0 0 255 255 255 \n"
                    + "0 0 0 0 0 0 255 255 255 0 0 0 \n"
                    + "0 0 0 0 0 0 0 0 0 255 255 255 \n"
                    + "255 255 255 0 0 0 0 0 0 0 0 0 \n";
    // confirm ppm of transformed image is as expected
    assertEquals(expectedOutput, appendable.toString());
    // confirm image and pixel values are equal to expected
    assertEquals(4, model1.getImage("greenComponentGreyscaleImage.ppm")
            .getHeight());
    assertEquals(4, model1.getImage("greenComponentGreyscaleImage.ppm")
            .getWidth());
    assertEquals(255, model1.getImage("greenComponentGreyscaleImage.ppm")
            .getPixel(0,1).getR());
    assertEquals(255, model1.getImage("greenComponentGreyscaleImage.ppm")
            .getPixel(0,1).getG());
    assertEquals(255, model1.getImage("greenComponentGreyscaleImage.ppm")
            .getPixel(0,1).getB());
  }

  /**
   * Test case for blue component image transformation.
   */

  @Test
  public void testModelBlueComponentGreyscaleImage() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // blue-component
    ITransformation blueComponentTransformation =
            new ValueComponentTransformation(GreyscaleComponent.BLUE);
    IImageState blueComponentGreyscaleImage = blueComponentTransformation.run(image1);
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("blueComponentGreyscaleImage.ppm",
            blueComponentGreyscaleImage, appendable);
    saveImage.run();
    // add image to model
    model1.addImage("blueComponentGreyscaleImage.ppm", blueComponentGreyscaleImage);
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "0 0 0 0 0 0 255 255 255 255 255 255 \n"
                    + "0 0 0 0 0 0 0 0 0 255 255 255 \n"
                    + "255 255 255 0 0 0 0 0 0 0 0 0 \n"
                    + "255 255 255 255 255 255 0 0 0 0 0 0 \n";
    // confirm ppm of transformed image is as expected
    assertEquals(expectedOutput, appendable.toString());
    // confirm image and pixel values are equal to expected
    assertEquals(4, model1.getImage("blueComponentGreyscaleImage.ppm")
            .getHeight());
    assertEquals(4, model1.getImage("blueComponentGreyscaleImage.ppm")
            .getWidth());
    assertEquals(0, model1.getImage("blueComponentGreyscaleImage.ppm")
            .getPixel(0,1).getR());
    assertEquals(0, model1.getImage("blueComponentGreyscaleImage.ppm")
            .getPixel(0,1).getG());
    assertEquals(0, model1.getImage("blueComponentGreyscaleImage.ppm")
            .getPixel(0,1).getB());
  }

  /**
   * Test case for value greyscale image transformation.
   */

  @Test
  public void testModelValueTransformImage() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // value transform
    ITransformation valueTransformation = new ValueTransformation();
    IImageState valueTransformImage = valueTransformation.run(image1);
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("valueTransformImage.ppm",
            valueTransformImage, appendable);
    saveImage.run();
    // add image to model
    model1.addImage("valueTransformImage.ppm", valueTransformImage);
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 255 255 255 255 255 255 255 255 255 255 255 \n"
                    + "0 0 0 255 255 255 255 255 255 255 255 255 \n"
                    + "255 255 255 0 0 0 255 255 255 255 255 255 \n"
                    + "255 255 255 255 255 255 0 0 0 255 255 255 \n";
    // confirm ppm of transformed image is as expected
    assertEquals(expectedOutput, appendable.toString());
    // confirm image and pixel values are equal to expected
    assertEquals(4, model1.getImage("valueTransformImage.ppm").getHeight());
    assertEquals(4, model1.getImage("valueTransformImage.ppm").getWidth());
    assertEquals(255, model1.getImage("valueTransformImage.ppm")
            .getPixel(0,1).getR());
    assertEquals(255, model1.getImage("valueTransformImage.ppm")
            .getPixel(0,1).getG());
    assertEquals(255, model1.getImage("valueTransformImage.ppm")
            .getPixel(0,1).getB());
  }

  /**
   * Test case for intensity greyscale image transformation.
   */

  @Test
  public void testModelIntensityTransformImage() {
    IModel model1 = new ModelImpl();
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // intensity transform
    ITransformation intensityTransformation = new IntensityTransformation();
    IImageState intensityTransformImage = intensityTransformation.run(image1);
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("intensityTransformImage.ppm",
            intensityTransformImage, appendable);
    saveImage.run();
    // add image to model
    model1.addImage("intensityTransformImage.ppm", intensityTransformImage);
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "85 85 85 85 85 85 85 85 85 255 255 255 \n"
                    + "0 0 0 85 85 85 85 85 85 85 85 85 \n"
                    + "170 170 170 0 0 0 85 85 85 85 85 85 \n"
                    + "170 170 170 170 170 170 0 0 0 85 85 85 \n";
    // confirm ppm of transformed image is as expected
    assertEquals(expectedOutput, appendable.toString());
    // confirm image and pixel values are equal to expected
    assertEquals(4, model1.getImage("intensityTransformImage.ppm").getHeight());
    assertEquals(4, model1.getImage("intensityTransformImage.ppm").getWidth());
    assertEquals(85, model1.getImage("intensityTransformImage.ppm")
            .getPixel(0,1).getR());
    assertEquals(85, model1.getImage("intensityTransformImage.ppm")
            .getPixel(0,1).getG());
    assertEquals(85, model1.getImage("intensityTransformImage.ppm")
            .getPixel(0,1).getB());
  }

  /**
   * Test case for luma greyscale image transformation.
   */

  @Test
  public void testModelLumaTransformImage() {
    IModel model1 = new ModelImpl();
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // luma transform
    ITransformation lumaTransformation = new LumaTransformation();
    IImageState lumaTransformImage = lumaTransformation.run(image1);
    Appendable appendable = new StringBuilder();
    // save image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("lumaTransformImage.ppm",
            lumaTransformImage, appendable);
    saveImage.run();
    // add image to model
    model1.addImage("lumaTransformImage.ppm", lumaTransformImage);
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "54 54 54 182 182 182 18 18 18 255 255 255 \n"
                    + "0 0 0 54 54 54 182 182 182 18 18 18 \n"
                    + "73 73 73 0 0 0 54 54 54 182 182 182 \n"
                    + "201 201 201 73 73 73 0 0 0 54 54 54 \n";
    // confirm ppm of transformed image is as expected
    assertEquals(expectedOutput, appendable.toString());
    // confirm image and pixel values are equal to expected
    assertEquals(4, model1.getImage("lumaTransformImage.ppm").getHeight());
    assertEquals(4, model1.getImage("lumaTransformImage.ppm").getWidth());
    assertEquals(182, model1.getImage("lumaTransformImage.ppm")
            .getPixel(0,1).getR());
    assertEquals(182, model1.getImage("lumaTransformImage.ppm")
            .getPixel(0,1).getG());
    assertEquals(182, model1.getImage("lumaTransformImage.ppm")
            .getPixel(0,1).getB());
  }

  /**
   * Test case for blur image transformation and save to file path.
   */

  @Test
  public void testModelBlurTransformImageAndSave() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // create Gaussian blur filter
    double[][] blurFilter = {
            {1.0 / 16, 1.0 / 8, 1.0 / 16},
            {1.0 / 8, 1.0 / 4, 1.0 / 8},
            {1.0 / 16, 1.0 / 8, 1.0 / 16}
    };
    // manually create blur filter kernel
    IKernel blurKernel = new KernelImpl(blurFilter);
    // blur transform
    ITransformation blurTransform = new FilterTransformation(blurKernel);
    IImageState blurTransformImage = blurTransform.run(image1);
    model1.addImage("blurTransform", blurTransformImage);

    // confirm blur image height/width are unchanged
    assertEquals(4, model1.getImage("blurTransform").getHeight());
    assertEquals(4, model1.getImage("blurTransform").getWidth());
    // confirm calculated channel values for pixel at 0,0 are as expected
    assertEquals(80, model1.getImage("blurTransform")
            .getPixel(0,0).getR());
    assertEquals(32, model1.getImage("blurTransform")
            .getPixel(0,0).getG());
    assertEquals(0, model1.getImage("blurTransform")
            .getPixel(0,0).getB());

    // save image to actual file path
    File outputFile = new File("res/blurTest.ppm");
    try (FileWriter fileWriter = new FileWriter(outputFile)) {
      PPMImageSaver ppmImageSaver = new PPMImageSaver("res/blurTest.ppm",
              blurTransformImage, fileWriter);
      ppmImageSaver.run();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test case for blur image transformation.
   */

  @Test
  public void testModelBlurTransformImage() {
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
    // manually create blur filter kernel
    IKernel blurKernel = new KernelImpl(3);
    blurKernel.setValue(0, 0, 1.0 / 16);
    blurKernel.setValue(0, 1, 1.0 / 8);
    blurKernel.setValue(0, 2, 1.0 / 16);

    blurKernel.setValue(1, 0, 1.0 / 8);
    blurKernel.setValue(1, 1, 1.0 / 4);
    blurKernel.setValue(1, 2, 1.0 / 8);

    blurKernel.setValue(2, 0, 1.0 / 16);
    blurKernel.setValue(2, 1, 1.0 / 8);
    blurKernel.setValue(2, 2, 1.0 / 16);
    // blur transform
    ITransformation blurTransform = new FilterTransformation(blurKernel);
    IImageState blurTransformImage = blurTransform.run(image1);

    Appendable appendable = new StringBuilder();
    // save image to appendable
    IImageSaver saveImage = new PPMImageSaver("res/twobytwoBlur.ppm",
            blurTransformImage, appendable);
    saveImage.run();
    // expected string of image PPM AFTER transform
    // used to verify image was loaded and blurred calculated correctly
    String expectedOutput =
            "P3\n"
                    + "2 2\n"
                    + "255\n"
                    + "99 83 73 94 90 83 \n"
                    + "102 80 83 99 83 73 \n";
    assertEquals(expectedOutput, appendable.toString());
  }

  /**
   * Test case for sharpen image transformation and save to file path.
   */

  @Test
  public void testModelSharpenTransformImageAndSave() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    Appendable appendableOriginal = new StringBuilder();
    // save original image
    IImageSaver saveImageOriginal = new PPMImageSaver("res/fourbyfour.ppm",
            image1, appendableOriginal);
    saveImageOriginal.run();
    // expected string of image PPM BEFORE transform
    // used to verify image was loaded correctly
    String expectedOutputOriginal =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 0 0 0 255 0 0 0 255 255 255 255 \n"
                    + "0 0 0 255 0 0 0 255 0 0 0 255 \n"
                    + "255 0 255 0 0 0 255 0 0 0 255 0 \n"
                    + "0 255 255 255 0 255 0 0 0 255 0 0 \n";
    assertEquals(expectedOutputOriginal, appendableOriginal.toString());

    // create sharpen filter
    double[][] sharpenFilter = {
            {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8}
    };

    // manually create sharpen filter kernel using constructor 2
    IKernel sharpenKernel = new KernelImpl(sharpenFilter);

    // sharpen transform
    ITransformation sharpenTransform = new FilterTransformation(sharpenKernel);
    IImageState sharpenTransformImage = sharpenTransform.run(image1);
    model1.addImage("sharpenTransform", sharpenTransformImage);

    // confirm blur image height/width are unchanged
    assertEquals(4, model1.getImage("sharpenTransform").getHeight());
    assertEquals(4, model1.getImage("sharpenTransform").getWidth());
    // confirm calculated channel values for pixel at 0,0 are as expected
    assertEquals(255, model1.getImage("sharpenTransform")
            .getPixel(0,0).getR());
    assertEquals(32, model1.getImage("sharpenTransform")
            .getPixel(0,0).getG());
    assertEquals(0, model1.getImage("sharpenTransform")
            .getPixel(0,0).getB());
    // confirm calculated channel values for pixel at 2,1 are as expected
    assertEquals(159, model1.getImage("sharpenTransform")
            .getPixel(2,1).getR());
    assertEquals(32, model1.getImage("sharpenTransform")
            .getPixel(2,1).getG());
    assertEquals(96, model1.getImage("sharpenTransform")
            .getPixel(2,1).getB());

    // save image to actual file path
    File outputFile = new File("res/sharpenTest.ppm");
    try (FileWriter fileWriter = new FileWriter(outputFile)) {
      PPMImageSaver ppmImageSaver = new PPMImageSaver("res/sharpenTest.ppm",
              sharpenTransformImage, fileWriter);
      ppmImageSaver.run();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test case for sharpen image transformation.
   */

  @Test
  public void testModelSharpenTransformImage() {
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

    // manually create sharpen filter kernel using constructor 1
    IKernel sharpenKernel = new KernelImpl(5);
    sharpenKernel.setValue(0, 0, -1.0 / 8);
    sharpenKernel.setValue(0, 1, -1.0 / 8);
    sharpenKernel.setValue(0, 2, -1.0 / 8);
    sharpenKernel.setValue(0, 3, -1.0 / 8);
    sharpenKernel.setValue(0, 4, -1.0 / 8);

    sharpenKernel.setValue(1, 0, -1.0 / 8);
    sharpenKernel.setValue(1, 1, 1.0 / 4);
    sharpenKernel.setValue(1, 2, 1.0 / 4);
    sharpenKernel.setValue(1, 3, 1.0 / 4);
    sharpenKernel.setValue(1, 4, -1.0 / 8);

    sharpenKernel.setValue(2, 0, -1.0 / 8);
    sharpenKernel.setValue(2, 1, 1.0 / 4);
    sharpenKernel.setValue(2, 2, 1);
    sharpenKernel.setValue(2, 3, 1.0 / 4);
    sharpenKernel.setValue(2, 4, -1.0 / 8);

    sharpenKernel.setValue(3, 0, -1.0 / 8);
    sharpenKernel.setValue(3, 1, 1.0 / 4);
    sharpenKernel.setValue(3, 2, 1.0 / 4);
    sharpenKernel.setValue(3, 3, 1.0 / 4);
    sharpenKernel.setValue(3, 4, -1.0 / 8);

    sharpenKernel.setValue(4, 0, -1.0 / 8);
    sharpenKernel.setValue(4, 1, -1.0 / 8);
    sharpenKernel.setValue(4, 2, -1.0 / 8);
    sharpenKernel.setValue(4, 3, -1.0 / 8);
    sharpenKernel.setValue(4, 4, -1.0 / 8);

    // sharpen transform
    ITransformation sharpenTransform = new FilterTransformation(sharpenKernel);
    IImageState sharpenTransformImage = sharpenTransform.run(image1);

    Appendable appendable = new StringBuilder();
    // save image to appendable
    IImageSaver saveImage = new PPMImageSaver("res/twobytwoSharpen.ppm",
            sharpenTransformImage, appendable);
    saveImage.run();
    // expected string of image PPM AFTER transform
    // used to verify image was loaded and sharpened calculated correctly
    String expectedOutput =
            "P3\n"
                    + "2 2\n"
                    + "255\n"
                    + "255 253 182 255 255 255 \n"
                    + "255 253 255 255 253 182 \n";
    assertEquals(expectedOutput, appendable.toString());
  }


  /**
   * Test case for save image to actual file path.
   */

  @Test
  public void testSaveImageAppendableFile() {
    IModel model1 = new ModelImpl();
    // load image
    IImageLoader imageLoader = new PPMImageLoader("res/fourbyfour.ppm");
    IImageState image1 = imageLoader.run();
    // brighten image
    ITransformation brightenTransform = new BrightenTransformation(10);
    IImageState brightenedImage = brightenTransform.run(image1);
    // save image to actual file path
    File outputFile = new File("res/IModelTestFle.ppm");
    try (FileWriter fileWriter = new FileWriter(outputFile)) {
      PPMImageSaver ppmImageSaver = new PPMImageSaver("res/IModelTestFle.ppm",
              brightenedImage, fileWriter);
      ppmImageSaver.run();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    // load saved image back into model
    IImageLoader imageLoader2 = new PPMImageLoader("res/IModelTestFle.ppm");
    IImageState image2 = imageLoader2.run();
    Appendable appendable = new StringBuilder();
    // save saved image to view ppm string
    IImageSaver saveImage = new PPMImageSaver("res/IModelTestFle.ppm",
            image2, appendable);
    saveImage.run();
    // confirm contents of saved brightened image are as expected
    String expectedOutput =
            "P3\n"
                    + "4 4\n"
                    + "255\n"
                    + "255 10 10 10 255 10 10 10 255 255 255 255 \n"
                    + "10 10 10 255 10 10 10 255 10 10 10 255 \n"
                    + "255 10 255 10 10 10 255 10 10 10 255 10 \n"
                    + "10 255 255 255 10 255 10 10 10 255 10 10 \n";
    assertEquals(expectedOutput, appendable.toString());
  }


}
