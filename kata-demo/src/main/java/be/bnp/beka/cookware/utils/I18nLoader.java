package be.bnp.beka.cookware.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Loads i18n YAML files from classpath:/i18n/{lang}.yml and exposes flattened keys like "buttons.submit".
 * Precedence for language selection: explicit param -> System property "lang" -> ENV LANG or DEFAULT ("en").
 */
public final class I18nLoader {

    private static final String I18N_PATH = "i18n/";
    private static final String DEFAULT_LANG = "en";
    private static volatile I18nLoader INSTANCE;

    private final String lang;
    private final Map<String, String> messages;

    private I18nLoader(String lang) {
        this.lang = normalizeLang(lang);
        this.messages = loadMessagesFor(this.lang);
    }

    public static I18nLoader get() {
        if (INSTANCE == null) {
            synchronized (I18nLoader.class) {
                if (INSTANCE == null) {
                    String requested = System.getProperty("lang");
                    if (requested == null || requested.isBlank()) requested = System.getenv("LANG");
                    if (requested == null || requested.isBlank()) requested = DEFAULT_LANG;
                    INSTANCE = new I18nLoader(requested);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Create/replace singleton for specific lang (useful in tests to force language).
     */
    public static I18nLoader forLang(String lang) {
        synchronized (I18nLoader.class) {
            INSTANCE = new I18nLoader(lang);
            return INSTANCE;
        }
    }

    public String getLang() {
        return lang;
    }

    /**
     * Get message by dotted key, e.g. "buttons.submit". Returns null if not found.
     */
    public String get(String key) {
        return messages.get(key);
    }

    /**
     * Get message or fallback
     */
    public String getOrDefault(String key, String defaultValue) {
        return messages.getOrDefault(key, defaultValue);
    }

    /**
     * Load YAML and flatten nested maps into dotted keys.
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> loadMessagesFor(String lang) {
        String resource = I18N_PATH + lang + ".yml";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (is == null) {
                // fallback to default lang if file missing
                if (!Objects.equals(lang, DEFAULT_LANG)) {
                    return loadMessagesFor(DEFAULT_LANG);
                }
                return Collections.emptyMap();
            }
            Yaml yaml = new Yaml();
            Object loaded = yaml.load(new java.io.InputStreamReader(is, StandardCharsets.UTF_8));
            if (!(loaded instanceof Map)) return Collections.emptyMap();
            Map<String, Object> root = (Map<String, Object>) loaded;
            Map<String, String> flat = new LinkedHashMap<>();
            flatten("", root, flat);
            return Collections.unmodifiableMap(flat);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load i18n resource: " + resource, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void flatten(String prefix, Map<String, Object> map, Map<String, String> out) {
        for (Map.Entry<String, Object> e : map.entrySet()) {
            String k = prefix.isEmpty() ? e.getKey() : prefix + "." + e.getKey();
            Object v = e.getValue();
            if (v instanceof Map) {
                flatten(k, (Map<String, Object>) v, out);
            } else if (v != null) {
                out.put(k, String.valueOf(v));
            }
        }
    }

    private String normalizeLang(String l) {
        if (l == null || l.isBlank()) return DEFAULT_LANG;
        return l.trim().toLowerCase().split("[-_]")[0]; // "en-US" -> "en"
    }
}
