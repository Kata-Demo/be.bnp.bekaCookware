## Best practices

- Keep YAML keys stable and small; use dotted access for nested structure.
- Provide fallback/default YAML (en.yml) for missing keys.
- Do not store secrets in i18n files.
- For dynamic values (placeholders), you can extend I18nLoader to support simple templating (String.format or replace tokens).

## Example of use

<p>
I18nLoader  i18n = I18nLoader.get();            // uses System property/env/default

String submitLabel = i18n.getOrDefault("buttons.submit", "Submit");

driver.findElement(By.cssSelector("button[type='submit']")).getText().equals(submitLabel);
</p>