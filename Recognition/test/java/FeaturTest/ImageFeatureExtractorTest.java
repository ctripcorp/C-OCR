package FeaturTest;

import Feature.FeatureVector;
import Feature.ImageFeatureExtractor;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ImageFeatureExtractorTest {

  private static final Logger logger = LoggerFactory.getLogger(ImageFeatureExtractorTest.class);
  public void validateFeature(Map<String, List<Double>> denseFeatures,
                              String name,
                              int expectedCount) {
    assertTrue(denseFeatures.containsKey(name));
    assertTrue(denseFeatures.get(name).size() == expectedCount);
    System.out.println("feature " + name + "[0] = " + denseFeatures.get(name).get(0));
  }

  public void validateFeatureVector(FeatureVector featureVector) {
    Map<String, List<Double>> denseFeatures = featureVector.getDenseFeatures();
    assertTrue(denseFeatures != null);
    assertTrue(denseFeatures.containsKey("rgb"));
    final int kNumGrids = 1 + 4 + 16;
    validateFeature(denseFeatures, "rgb", 512 * kNumGrids);
    validateFeature(denseFeatures, "hog", 9 * kNumGrids);
    validateFeature(denseFeatures, "lbp", 256 * kNumGrids);
    validateFeature(denseFeatures, "hsv", 65 * kNumGrids);
  }

  @Test
  public void FeatureTest() {
    String path1 = this.getClass().getClassLoader().getResource("0/38-26-cut-codeRow2-26.jpg").getPath();
    int length = path1.length();
    String path =  path1.substring(1,length);

    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(path));

//    BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);
    ImageFeatureExtractor featureExtractor = ImageFeatureExtractor.getInstance();
    FeatureVector featureVector = featureExtractor.getFeatureVector(image);
    validateFeatureVector(featureVector);
    }
    catch (IOException e) {
      logger.error(e.getMessage());
    }
  }


}
