# Assignment 10: GRIME - Graphical Image Manipulation and Enhancement (Part 3) - USEME
Nicole Quinn\
quinn.nic@northeastern.edu

### Supported Script Commands
* `load image-path image-name`
  * load an image from the given image path and provide it with an image name that will be its ID moving forward.
* `save image-path image-name`
  * save the image with the given image name to the provided file path.
* `brighten increment image-name dest-image-name`
  * brighten the image by the given increment to create a new image, referred to henceforth by the given destination name. The increment may be positive (brightening) or negative (darkening).
* `red-component image-name dest-image-name`
  * Create a greyscale image with the red-component of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `green-component image-name dest-image-name`
  * Create a greyscale image with the green-component of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `blue-component image-name dest-image-name`
    * Create a greyscale image with the blue-component of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `value image-name dest-image-name`
    * Create a value greyscale image of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `luma image-name dest-image-name`
    * Create a luma greyscale image of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `intensity image-name dest-image-name`
    * Create a luma greyscale image of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `blur image-name dest-image-name`
  * Create a blurred image of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `sharpen image-name dest-image-name`
    * Create a sharpened image of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `sepia image-name dest-image-name`
    * Create a sepia image of the image with the given name, and refer to it henceforth in the program by the given destination name.
* `greyscale image-name dest-image-name`
    * Create a luma greyscale image using the matrix method of the image with the given name, and refer to it henceforth in the program by the given destination name.

### Command Examples: Used in Program with Keyboard
* Brighten
    * `load res/strawberry.ppm strawberry`
    * `brighten 30 strawberry strawberryBrighten`
    * `save res/strawberryBrighten.ppm strawberryBrighten`
* Intensity
    * `load res/strawberry.ppm strawberry`
    * `intensity strawberry strawberryIntensity`
    * `save res/strawberryIntensity.ppm strawberryIntensity`
* Blue Component
    * `load res/strawberry.ppm strawberry `
    * `blue-component strawberry strawberryBlue-Component`
    * `save res/strawberryBlue-Component.ppm strawberryBlue-Component`

**Command Examples: Command Sequence**\
Please note: these commands are used in program with the keyboard
* `load res/strawberry.ppm strawberry`
* `brighten 30 strawberry strawberryBrighten`
* `save res/strawberryBrighten.ppm strawberryBrighten`
* `intensity strawberry strawberryIntensity`
* `save res/strawberryIntensity.ppm strawberryIntensity`
* `blue-component strawberry strawberryBlue-Component`
* `save res/strawberryBlue-Component.ppm strawberryBlue-Component`
* `blur strawberry starwberryBlurred`
* `save res/starwberryBlurred.ppm starwberryBlurred`

### Script Command Conditions
* You must load an image or multiple before performing any of the transformation operations on them.

### Supported File Types 
* PPM
* JPEG
* PNG

### Instructions for running and using the GUI
* To run the program via the GUI, navigate to `IMEMain` in the `src/` folder. Once here, run the main by selecting the run button in the top right corner. Make sure the current file is selected.
* Once the GUI window opens, you are free to interact with it by loading an image, performing edit operations on it by selecting the corresponding buttons, and saving the image. If you do not want to 
  save your image, you can simply load a new one. The GUI will work on the current image in the view and each edit operation will build upon the last. 
* To run the GUI, please feel free to leverage the sample image logan.jpeg in the res/ folder or choose your own photo from your own file dialog.
* To begin the program, start by loading in an image. 
  * To load an image, select the "Load Image" button
    * A file dialog will open and allow you to select and image of a valid file type (png, ppm, or jpeg)
    * If the photo is larger than the GUI, you will be able to scroll the entire image. 
  * To save an image, select the "Save Image" button
    * A file dialog will open and allow you to select your desired save location
    * You can then enter in your desired file name and valid extension (.png, .ppm, or .jpeg)
    * If an incorrect extension is provided, you will be notified via the message box in the bottom right corner of the view.
  * To edit an image select the button associated to your desired edit operation
    * Brighten/Darken
      * Use the slider to select the desired brighten/darken amount
      * You can slide with your mouse or leverage the keyboard arrows to move it to desired value
      * Once you have landed on the desired value, select apply
    * Greyscale 
      * Select the desired greyscale operation button
      * Please note there will be very little to no variation if you select multiple of these operations and apply to the same image. This is because many of these operations update the pixel channel values to similar or the exact same value.
    * Filter
      * Select the desired filter operation button - Blur or Sharpen
      * These can be selected repeatedly to additionally blur or sharpen the current image
    * Color Transformation
      * Select the desired color transformation operation button - Sepia or Greyscale
  * Any program feedback related to the actions executed by clicking buttons or moving the slider will be visible in a message box in the bottom right corner of the view.
  * Below is a screenshot of the GUI for reference to the aforementioned operation:
    ![Failed to Load Image - program_screenshot.png](program_screenshot.png)