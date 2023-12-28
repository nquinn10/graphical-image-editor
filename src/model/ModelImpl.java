package model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents an image database model. It implements the IModel interface
 * and implements all mandated operations.
 */

public class ModelImpl implements IModel {
  private final Map<String,IImageState> loadedImages;

  /**
   * Construct an empty image database model that is represented
   *     by an empty hashmap.
   */

  public ModelImpl() {
    this.loadedImages = new LinkedHashMap<String,IImageState>();
  }

  @Override
  public void addImage(String imageID, IImageState image) throws IllegalArgumentException {
    if (imageID == null || image == null) {
      throw new IllegalArgumentException("ID or Image cannot be null");
    }
    // replacing image with same ID already exists
    loadedImages.put(imageID, image);
  }

  @Override
  public IImageState getImage(String imageID) {
    return loadedImages.get(imageID);
  }

}
