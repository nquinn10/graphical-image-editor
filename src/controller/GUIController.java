package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import controller.io.IImageLoader;
import controller.io.IImageSaver;
import controller.io.OtherImageTypeLoader;
import controller.io.OtherImageTypeSaver;
import controller.io.PPMImageLoader;
import controller.io.PPMImageSaver;
import model.GreyscaleComponent;
import model.IImageState;
import model.IModel;
import model.kernel.IKernelState;
import model.transformations.BrightenTransformation;
import model.transformations.ColorTransformation;
import model.transformations.FilterTransformation;
import model.transformations.ITransformation;
import model.transformations.IntensityTransformation;
import model.transformations.LumaTransformation;
import model.transformations.ValueComponentTransformation;
import model.transformations.ValueTransformation;
import view.IGUIView;
import view.ViewListener;

/**
 * This class implements the IController and ViewListener interfaces. This class represents
 * a controller specific to the GUI.
 */

public class GUIController implements IController, ViewListener {
  private final IModel model;
  private final IGUIView view;

  /**
   * Constructs an Image Manipulation and Editor (IME) program GUI controller given
   *  image database model and GUI specific view.
   *
   * @param model the image database model
   * @param view the GUI specific view
   *
   * @throws IllegalArgumentException if model or view are null.
   */

  public GUIController(IModel model, IGUIView view) throws IllegalArgumentException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Constructor argument cannot be null.");
    }
    this.model = model;
    this.view = view;
    // add controller as listener to view
    this.view.addViewListener(this);
  }

  @Override
  public void run() {
    view.setVisible(true);
  }

  @Override
  public void handleLoadEvent(String filePath) {
    try {
      String fileExtension = ImageUtil.getFileExtension(filePath);
      // if file extension is not a PPM
      if (!fileExtension.equals("ppm")) {
        // call to load strategy
        IImageLoader imageLoader = new OtherImageTypeLoader(filePath);
        IImageState loadImage = imageLoader.run();
        if (loadImage == null) {
          throw new IllegalStateException("Image cannot be null.\n");
        }
        // add to model
        this.model.addImage("currentImage", loadImage);
      } else {
        // otherwise is PPM
        // call to load strategy
        IImageLoader imageLoader = new PPMImageLoader(filePath);
        IImageState loadImage = imageLoader.run();
        if (loadImage == null) {
          throw new IllegalStateException("Image cannot be null.\n");
        }
        // add to model
        this.model.addImage("currentImage", loadImage);
      }
      // convert IImageState object from model to buffered image
      BufferedImage bufferedImage =
              ImageUtil.createBufferedImage(model.getImage("currentImage"));
      // set image to view + display message to user
      view.setViewImage(bufferedImage);
      view.setViewText("Image loaded successfully");
      view.requestFrameFocus();
      // catch any exceptions thrown from executing transformation and set to view
    } catch (IllegalStateException e) {
      view.setViewText("Error: " + e.getMessage());
    }
  }

  @Override
  public void handleSaveEvent(String filePath) {
    // get current image from model
    IImageState currentImage = model.getImage("currentImage");
    try {
      // get and store file extension
      String fileExtension = ImageUtil.getFileExtension(filePath);
      // if file extension is not a PPM
      if (!fileExtension.equals("ppm")) {
        // call to other image save strategy
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IImageSaver otherImageTypeSaver = new OtherImageTypeSaver(filePath, currentImage, output);
        otherImageTypeSaver.run();
      } else {
        // call to ppm save strategy
        // create file at given file path and write to file
        File outputFile = new File(filePath);
        try (FileWriter fileWriter = new FileWriter(outputFile)) {
          PPMImageSaver ppmImageSaver = new PPMImageSaver(filePath, currentImage, fileWriter);
          ppmImageSaver.run();
        } catch (IOException e) {
          throw new IllegalStateException("Failed to write to file.\n");
        }
      }
      // set message to user in view
      view.setViewText("Image successfully saved");
      view.requestFrameFocus();
      // catch any exceptions thrown from executing transformation and set to view
    } catch (IllegalStateException e) {
      view.setViewText("Error: " + e.getMessage());
    }
  }

  @Override
  public void handleGreyscaleEvent(GreyscaleComponent greyscaleComponent) {
    // get current image from model
    IImageState currentImage = model.getImage("currentImage");
    // call to correct transformation based on greyscaleComponent enum
    ITransformation greyscaleComponentTransformation = null;
    if (greyscaleComponent == GreyscaleComponent.RED
            || greyscaleComponent == GreyscaleComponent.GREEN
            || greyscaleComponent == GreyscaleComponent.BLUE) {
      greyscaleComponentTransformation = new ValueComponentTransformation(greyscaleComponent);
    } else if (greyscaleComponent == GreyscaleComponent.VALUE) {
      greyscaleComponentTransformation = new ValueTransformation();
    } else if (greyscaleComponent == GreyscaleComponent.INTENSITY) {
      greyscaleComponentTransformation = new IntensityTransformation();
    } else if (greyscaleComponent == GreyscaleComponent.LUMA) {
      greyscaleComponentTransformation = new LumaTransformation();
    }
    // run the transformation
    IImageState greyscaleComponentImage = greyscaleComponentTransformation.run(currentImage);
    // add to model
    model.addImage("currentImage", greyscaleComponentImage);
    // convert IImageState object from model to buffered image
    BufferedImage bufferedImage
            = ImageUtil.createBufferedImage(model.getImage("currentImage"));
    // set image to view + display message to user
    view.setViewImage(bufferedImage);
    view.setViewText("Greyscale applied successfully");
    view.requestFrameFocus();
  }

  @Override
  public void handleBrightenEvent(int brightenValue) {
    // get current image from model
    IImageState currentImage = model.getImage("currentImage");
    ITransformation brightenTransformation = new BrightenTransformation(brightenValue);
    // call to brighten transformation
    IImageState brightenedImage = brightenTransformation.run(currentImage);
    // add to model
    model.addImage("currentImage", brightenedImage);
    // convert IImageState object from model to buffered image
    BufferedImage bufferedImage =
            ImageUtil.createBufferedImage(model.getImage("currentImage"));
    // set image to view + display message to user
    view.setViewImage(bufferedImage);
    view.setViewText("Brighten applied successfully");
    view.requestFrameFocus();
  }

  @Override
  public void handleBlurEvent(IKernelState kernel) {
    // get current image from model
    IImageState currentImage = model.getImage("currentImage");
    // run the transformation
    ITransformation blurTransformation = new FilterTransformation(kernel);
    IImageState blurImage = blurTransformation.run(currentImage);
    // add to model
    model.addImage("currentImage", blurImage);
    // convert IImageState object from model to buffered image
    BufferedImage bufferedImage
            = ImageUtil.createBufferedImage(model.getImage("currentImage"));
    // set image to view + display message to user
    view.setViewImage(bufferedImage);
    view.setViewText("Blur applied successfully");
    view.requestFrameFocus();
  }

  @Override
  public void handleSharpenEvent(IKernelState kernel) {
    // get current image from model
    IImageState currentImage = model.getImage("currentImage");
    // run the transformation
    ITransformation sharpenTransformation = new FilterTransformation(kernel);
    IImageState sharpenImage = sharpenTransformation.run(currentImage);
    // add to model
    model.addImage("currentImage", sharpenImage);
    // convert IImageState object from model to buffered image
    BufferedImage bufferedImage
            = ImageUtil.createBufferedImage(model.getImage("currentImage"));
    // set image to view + display message to user
    view.setViewImage(bufferedImage);
    view.setViewText("Sharpen applied successfully");
    view.requestFrameFocus();
  }

  @Override
  public void handleGreyscaleMatrixEvent(IKernelState kernel) {
    // get current image from model
    IImageState currentImage = model.getImage("currentImage");
    // run the transformation
    ITransformation greyscaleColorTransformation = new ColorTransformation(kernel);
    IImageState greyscaleImage = greyscaleColorTransformation.run(currentImage);
    // add to model
    model.addImage("currentImage", greyscaleImage);
    // convert IImageState object from model to buffered image
    BufferedImage bufferedImage
            = ImageUtil.createBufferedImage(model.getImage("currentImage"));
    // set image to view + display message to user
    view.setViewImage(bufferedImage);
    view.setViewText("Greyscale applied successfully");
    view.requestFrameFocus();
  }

  @Override
  public void handleSepiaMatrixEvent(IKernelState kernel) {
    // get current image from model
    IImageState currentImage = model.getImage("currentImage");
    // run the transformation
    ITransformation sepiaColorTransformation = new ColorTransformation(kernel);
    IImageState sepiaImage = sepiaColorTransformation.run(currentImage);
    // add to model
    model.addImage("currentImage", sepiaImage);
    // convert IImageState object from model to buffered image
    BufferedImage bufferedImage
            = ImageUtil.createBufferedImage(model.getImage("currentImage"));
    // set image to view + display message to user
    view.setViewImage(bufferedImage);
    view.setViewText("Sepia applied successfully");
    view.requestFrameFocus();
  }
}
