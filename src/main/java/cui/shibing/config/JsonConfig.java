package cui.shibing.config;


import lombok.Getter;
import lombok.Setter;

public final class JsonConfig {

    private static final JsonConfig DEFAULT_CONFIG = new JsonConfig();

    @Getter
    @Setter
    private boolean preferBigDecimal = false;

    public JsonConfig() {

    }

    public static JsonConfig getDefaultConfig() {
        return DEFAULT_CONFIG;
    }


}
