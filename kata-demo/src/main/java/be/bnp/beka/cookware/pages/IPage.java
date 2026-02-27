package pages;

import utils.ConfigLoader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.time.Duration;

/**
 * Basic contract for all Page Objects.
 * - getUrlPath(): page-relative path (e.g. "/login")
 * - goToUrl(driver): navigates to baseUrl + getUrlPath() and waits until isLoaded(driver) is true
 * - isLoaded(driver): page-specific accessibility check (e.g. root element visible or URL match)
 */
public interface IPage {

    /**
     * Return the page-relative path (must start with "/" or be empty for base).
     * Example: "/login" or "".
     */
    String getUrlPath();

    /**
     * Implement a verification that the page is loaded and accessible.
     * Examples: check presence/visibility of a root element, or assert URL contains expected path.
     *
     * @param driver active WebDriver
     * @return true if page is considered loaded
     */
    boolean isLoaded(WebDriver driver);

    /**
     * Build full URL = baseUrl (from config) + getUrlPath()
     */
    default String getFullUrl() {
        String base = ConfigLoader.get().get("base.url");
        if (base == null) base = "";
        String path = getUrlPath() == null ? "" : getUrlPath();
        // normalize slashes
        if (base.endsWith("/") && path.startsWith("/")) {
            return base.substring(0, base.length() - 1) + path;
        } else if (!base.endsWith("/") && !path.startsWith("/")) {
            return base + "/" + path;
        } else {
            return base + path;
        }
    }

    /**
     * Navigate to page full URL and wait until isLoaded returns true.
     * Uses a default timeout of 8 seconds; caller can implement their own wait if needed.
     *
     * @param driver active WebDriver
     */
    default void goToUrl(WebDriver driver) {
        driver.get(getFullUrl());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        wait.until((ExpectedCondition<Boolean>) drv -> {
            try {
                return isLoaded(drv);
            } catch (Exception e) {
                return false;
            }
        });
    }
}
