package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v142.emulation.Emulation; // adjust version to match your Selenium
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import utils.typedEnum.Device;

/**
 * Utility to apply CDP Device Metrics emulation via Selenium DevTools.
 * Supports deviceEnum: PHONE, TABLET, DESKTOP.
 */
public final class DevToolsEmulation {

    public static final class DeviceMetrics {
        public final int width;
        public final int height;
        public final double deviceScaleFactor;
        public final boolean mobile;
        public final Optional<String> userAgent;

        public DeviceMetrics(int width, int height, double deviceScaleFactor, boolean mobile, String userAgent) {
            this.width = width;
            this.height = height;
            this.deviceScaleFactor = deviceScaleFactor;
            this.mobile = mobile;
            this.userAgent = Optional.ofNullable(userAgent);
        }
    }

    // Common presets (you can adjust or extend)
    private static final Map<Device, DeviceMetrics> PRESETS = new HashMap<>();
    static {
        PRESETS.put(Device.PHONE,  new DeviceMetrics(390, 844, 3.0, true,
                "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Mobile Safari/537.36"));
        PRESETS.put(Device.TABLET, new DeviceMetrics(820, 1180, 2.0, true,
                "Mozilla/5.0 (Linux; Android 11; Pixel C) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"));
        PRESETS.put(Device.DESKTOP,new DeviceMetrics(1920, 1080, 1.0, false,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"));
    }

    private DevToolsEmulation() { /* utils only */ }

    /**
     * Apply emulation preset to a Chromium WebDriver (ChromeDriver/RemoteWebDriver backed by Chromium).
     * This will create a DevTools session (if not present) and call Emulation.setDeviceMetricsOverride,
     * and set user agent override if available in the preset.
     *
     * @param driver a WebDriver instance implementing HasDevTools (Chromium-based)
     * @param device one of deviceEnum
     */
    public static void applyEmulation(WebDriver driver, Device device) {
        applyEmulation(driver, PRESETS.get(device));
    }

    /**
     * Apply custom metrics.
     *
     * @param driver  a WebDriver instance implementing HasDevTools (Chromium-based)
     * @param metrics custom DeviceMetrics
     */
    public static void applyEmulation(WebDriver driver, DeviceMetrics metrics) {
        if (!(driver instanceof HasDevTools)) {
            throw new IllegalArgumentException("Driver does not support DevTools (must be Chromium-based and implement HasDevTools).");
        }

        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        // set device metrics
        devTools.send(Emulation.setDeviceMetricsOverride(
                metrics.width,
                metrics.height,
                metrics.deviceScaleFactor,
                metrics.mobile,
                Optional.empty(),       // scale (deprecated) - keep empty
                Optional.empty(),       // screenWidth
                Optional.empty(),       // screenHeight
                Optional.empty(),       // positionX
                Optional.empty(),       // positionY
                Optional.empty(),       // dontSetVisibleSize
                Optional.empty(),       // screenOrientation
                Optional.empty(),       // viewport
                Optional.empty(),       // displayFeature
                Optional.empty()        // devicePosture
        ));

        // set user agent if provided
        metrics.userAgent.ifPresent(ua -> devTools.send(Emulation.setUserAgentOverride(ua, Optional.empty(), Optional.empty(), Optional.empty())));
    }

    /**
     * Convenience builder to create a metrics instance for custom DPI or orientation.
     */
    public static DeviceMetrics customMetrics(int width, int height, double deviceScaleFactor, boolean mobile, String userAgent) {
        return new DeviceMetrics(width, height, deviceScaleFactor, mobile, userAgent);
    }
}
