import org.junit.Test;

import model.kernel.IKernel;
import model.kernel.IKernelState;
import model.kernel.KernelImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test class for the IKernelState and IKernel interfaces and
 *     the KernelImpl class.
 */

public class KernelImplTest {

  /**
   * This is a test case for the kernel constructors. Getters
   *     are also tested.
   */

  @Test
  public void testKernelConstructorsAndMethods() {
    // construct a kernel using constructor 1
    IKernel kernel1 = new KernelImpl(3);
    // confirm values are as expected
    assertEquals(3, kernel1.getSize());
    // confirm empty 2D array
    assertEquals(0.0, kernel1.getValue(0,0), 0.001);
    assertEquals(0.0, kernel1.getValue(0,1), 0.001);
    assertEquals(0.0, kernel1.getValue(1,1), 0.001);

    kernel1.setValue(0,0, 10.5);
    kernel1.setValue(0,1, 0.5);
    kernel1.setValue(1,1, 1 / 8);
    // confirm values were set correctly
    assertEquals(10.5, kernel1.getValue(0,0), 0.001);
    assertEquals(0.5, kernel1.getValue(0,1), 0.001);
    assertEquals(1 / 8, kernel1.getValue(1,1), 0.001);


    // create Gaussian blur filter
    double[][] blurFilter = {
            {1.0 / 16, 1.0 / 8, 1.0 / 16},
            {1.0 / 8, 1.0 / 4, 1.0 / 8},
            {1.0 / 16, 1.0 / 8, 1.0 / 16}
    };

    // construct a kernel using constructor 2 using Gaussian blur
    IKernelState kernel2 = new KernelImpl(blurFilter);
    // confirm values are as expected
    assertEquals(3, kernel2.getSize());
    // confirm kernel array values
    assertEquals(1.0 / 16, kernel2.getValue(0,0), 0.01);
    assertEquals(1.0 / 8, kernel2.getValue(0,1), 0.01);
    assertEquals(1.0 / 4, kernel2.getValue(1,1), 0.01);

    // test IllegalArgumentExceptions
    // check to see error message appears as expected - invalid kernel
    // constructor 1
    try {
      new KernelImpl(4);
    } catch (IllegalArgumentException e) {
      assertEquals("Size must be odd or greater than 3", e.getMessage());
    }
    // even kernel size
    assertThrows(IllegalArgumentException.class, () -> {
      new KernelImpl(4);
    });
    // set kernel size - invalid row
    assertThrows(IllegalArgumentException.class, () -> {
      kernel1.setValue(-1,1, 10.5);
    });
    // set kernel size - invalid col
    assertThrows(IllegalArgumentException.class, () -> {
      kernel1.setValue(1,3, 10.5);
    });

    // create invalid filter
    double[][] invalidFilter = {
            {1.0 / 16, 1.0 / 8, 1.0 / 16, 1.0 / 16},
            {1.0 / 8, 1.0 / 4, 1.0 / 8, 1.0 / 16},
            {1.0 / 16, 1.0 / 8, 1.0 / 16, 1.0 / 16},
            {1.0 / 16, 1.0 / 8, 1.0 / 16, 1.0 / 16}
    };
    // constructor 2 - invalid filter
    try {
      new KernelImpl(invalidFilter);
    } catch (IllegalArgumentException e) {
      assertEquals("Kernel size must be odd or greater than 3", e.getMessage());
    }
    // even kernel size
    assertThrows(IllegalArgumentException.class, () -> {
      new KernelImpl(invalidFilter);
    });
    // get kernel at invalid col
    assertThrows(IllegalArgumentException.class, () -> {
      kernel2.getValue(0,3);

    });
    // get kernel at invalid row
    assertThrows(IllegalArgumentException.class, () -> {
      kernel2.getValue(-1,2);
    });
    // create invalid filter
    double[][] invalidFilter2 = {
            {1.0 / 16, 1.0 / 8},
            {1.0 / 8, 1.0 / 4},
            {1.0 / 16, 1.0 / 8},
    };
    // constructor 2 - invalid filter
    // kernel not a square
    assertThrows(IllegalArgumentException.class, () -> {
      new KernelImpl(invalidFilter2);
    });
  }

}
