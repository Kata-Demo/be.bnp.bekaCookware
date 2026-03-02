package be.bnp.beka.cookware.utils;

import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Simple image comparison utility:
 * - captures element screenshot via WebElement.getScreenshotAs
 * - compares with expected image loaded from classpath (resources/img)
 * - supports pixel tolerance and produces a diff image file when mismatched (optional)
 */
public final class ImageComparator {

    private ImageComparator() {}

    static final String baseResourcePath = "img/";

    // capture element image as BufferedImage
    public static BufferedImage captureElementImage(WebElement element) {
        // Selenium 4 supports element screenshots
        File tmp = element.getScreenshotAs(OutputType.FILE);
        try {
            BufferedImage img = ImageIO.read(tmp);
            Files.deleteIfExists(tmp.toPath());
            return img;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read element screenshot", e);
        }
    }

    // load expected image from classpath based on resource name (e.g., "logo.png")
    public static BufferedImage loadExpectedFromResources(String resourceName) {
        String resourcePath =  baseResourcePath + resourceName;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
            return ImageIO.read(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load expected image: " + resourcePath, e);
        }
    }

    /**
     * Compare two images with allowed per-channel tolerance.
     * @param expected expected BufferedImage
     * @param actual actual BufferedImage
     * @param maxDiffPercent maximum allowed differing pixels percentage (0.0 - 100.0)
     * @param outDiff optional path to write diff image (can be null)
     * @return true if images are considered equal under tolerance
     */
    public static boolean compare(BufferedImage expected, BufferedImage actual, double maxDiffPercent, Path outDiff) {
        Objects.requireNonNull(expected);
        Objects.requireNonNull(actual);

        // quick size check
        if (expected.getWidth() != actual.getWidth() || expected.getHeight() != actual.getHeight()) {
            // produce scaled comparison? for simplicity we treat different sizes as mismatch and optionally create diff canvas
            if (outDiff != null) {
                try { writeDiffImage(expected, actual, outDiff); } catch (Exception ignored) {}
            }
            return false;
        }

        int w = expected.getWidth();
        int h = expected.getHeight();
        long diffPixels = 0;
        BufferedImage diffImage = outDiff != null ? new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB) : null;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int expRGB = expected.getRGB(x, y);
                int actRGB = actual.getRGB(x, y);
                if (expRGB != actRGB) {
                    diffPixels++;
                    if (diffImage != null) {
                        // mark difference in red, and overlay actual for context
                        int red = new Color(255, 0, 0, 128).getRGB();
                        diffImage.setRGB(x, y, red);
                    }
                } else if (diffImage != null) {
                    // copy matching pixel lightly (actual)
                    diffImage.setRGB(x, y, actual.getRGB(x, y));
                }
            }
        }

        double total = (double) w * h;
        double diffPercent = (diffPixels / total) * 100.0;

        if (outDiff != null) {
            try {
                Files.createDirectories(outDiff.getParent());
                ImageIO.write(diffImage, "png", outDiff.toFile());
            } catch (Exception ignored) {}
        }

        return diffPercent <= maxDiffPercent;
    }

    // helper for size-mismatch diff image (draw expected on left, actual on right)
    private static void writeDiffImage(BufferedImage expected, BufferedImage actual, Path outDiff) throws Exception {
        int w = Math.max(expected.getWidth(), actual.getWidth());
        int h = expected.getHeight() + actual.getHeight();
        BufferedImage canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,w,h);
        g.drawImage(expected, 0, 0, null);
        g.drawImage(actual, 0, expected.getHeight(), null);
        g.dispose();
        Files.createDirectories(outDiff.getParent());
        ImageIO.write(canvas, "png", outDiff.toFile());
    }
}
