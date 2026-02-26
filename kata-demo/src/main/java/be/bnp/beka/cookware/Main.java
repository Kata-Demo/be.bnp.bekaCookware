
import utils.ConfigLoader;

public class Main {
    public static void main(String[] args) {

        ConfigLoader cfg = ConfigLoader.get();

        String env = cfg.getEnvironment();                          // e.g., "test"
        String baseUrl = cfg.get("base.url");                  // resolved with precedence

        System.out.println("Hello Tester!");
        System.out.println("You are configured to use: " + env );
        System.out.println("Your base url is : " + baseUrl );
    }
}