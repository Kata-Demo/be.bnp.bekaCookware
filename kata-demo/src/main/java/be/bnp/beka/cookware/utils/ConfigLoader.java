package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigLoader {

    private static final String CONFIG_DIR = "config/";
    private static final String BASE_CONFIG = CONFIG_DIR + "default.properties";
    private static final String ENV_DIR = CONFIG_DIR + "environments/";
    private static final String ENV_KEY = "environment";

    private final Properties merged = new Properties();

    private static final ConfigLoader INSTANCE = new ConfigLoader();

    private ConfigLoader() {
        reload();
    }

    public static ConfigLoader get() {
        return INSTANCE;
    }

    /**
     * Reloads configs: base -> environment overlay.
     */
    public synchronized void reload() {
        merged.clear();
        // 1) load base config.properties from classpath
        try (InputStream is = getResourceStream(BASE_CONFIG)) {
            if (is != null) merged.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + BASE_CONFIG, e);
        }

        // 2) determine environment (System property -> ENV var -> base config value -> default "test")
        String env = firstNonEmpty(
                System.getProperty(ENV_KEY),
                System.getenv(envToEnvKey(ENV_KEY)),
                merged.getProperty(ENV_KEY),
                "test"
        ).trim();

        // 3) load environment-specific properties and overlay (if exists)
        String envFile = ENV_DIR + env + ".properties";
        try (InputStream eis = getResourceStream(envFile)) {
            if (eis != null) {
                Properties envProps = new Properties();
                envProps.load(eis);
                // overlay: envProps take precedence over base
                for (String name : toIterable(envProps.propertyNames())) {
                    merged.setProperty(name, envProps.getProperty(name));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + envFile, e);
        }

        // Note: don't read secrets from file; use CI secrets / env vars / system properties for sensitive values
    }

    // Primary resolver: JVM system property -> env var -> merged properties
    public String get(String key) {
        String sys = System.getProperty(key);
        if (isNotEmpty(sys)) return sys;

        String envKey = envToEnvKey(key);
        String envVal = System.getenv(envKey);
        if (isNotEmpty(envVal)) return envVal;

        String prop = merged.getProperty(key);
        return prop != null ? prop : null;
    }

    // Typed getters convenience
    public String getBaseUrl() { return get("base.url"); }
    public String getRemoteUrl() { return get("remote.url"); }
    public String getDefaultBrowser() { return get("default.browser"); }
    public String getDefaultDevice() { return get("default.device"); }
    public String getDefaultLang() { return get("default.lang"); }
    public String getEnvironment() {
        String env = get(ENV_KEY);
        return isNotEmpty(env) ? env : "test";
    }

    // Return immutable map of merged properties (after resolving overlay)
    public Map<String, String> asMap() {
        Map<String, String> m = new LinkedHashMap<>();
        for (String name : toIterable(merged.propertyNames())) {
            m.put(name, merged.getProperty(name));
        }
        return Collections.unmodifiableMap(m);
    }

    // Helper utilities
    private static InputStream getResourceStream(String path) {
        return ConfigLoader.class.getClassLoader().getResourceAsStream(path);
    }

    private static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String firstNonEmpty(String... vals) {
        for (String v : vals) if (isNotEmpty(v)) return v;
        return "";
    }

    private static String envToEnvKey(String key) {
        // turn dot.case -> UPPER_SNAKE
        return key.toUpperCase().replace('.', '_');
    }

    // helper to iterate over PropertyNames enumeration
    private static Iterable<String> toIterable(Enumeration<?> e) {
        return () -> new java.util.Iterator<>() {
            public boolean hasNext() { return e.hasMoreElements(); }
            public String next() { return (String) e.nextElement(); }
        };
    }
}
