package be.bnp.beka.cookware.pages.component;

/**
 * Basic contract for Page Objects of Component representative .
 * - waitForVisible(): Wait until is visible; returns true if visible within timeout
 * - isLoaded(): Check if present é displayed
 */
public interface IComponent {
    
    // Wait until is visible; returns true if visible within timeout
    boolean waitForVisible();
    // Check if present & displayed
    boolean isDisplayed();
}
