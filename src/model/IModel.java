package model;

/**
 * This interface represents an image database that allows image storage and retrieval.
 */

public interface IModel {

  /**
   * Adds the given image ID and image object to the model.
   *
   * @param imageID imageID of the image object to be added
   * @param image image object to be added to the model
   *
   * @throws IllegalArgumentException if the imageID or image are null
   */

  void addImage(String imageID, IImageState image) throws IllegalArgumentException;

  /**
   * Returns the current image object.
   *
   * @param imageID imageID of the image object to be retrieved
   *
   * @return the current image object
   *
   * @throws IllegalArgumentException if imageID is not found in model
   */

  IImageState getImage(String imageID) throws IllegalArgumentException;

}
