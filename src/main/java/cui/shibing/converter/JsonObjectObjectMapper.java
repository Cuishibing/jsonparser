package cui.shibing.converter;

import cui.shibing.config.JsonConfig;
import cui.shibing.converter.reflection.ReflectionUtils;
import cui.shibing.json.JsonObject;

import java.lang.reflect.Field;
import java.util.Map;

public class JsonObjectObjectMapper extends AbstractObjectMapper implements ObjectMapper {

    public JsonObjectObjectMapper(JsonConfig config) {
        super(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T map(Object source, Class<?> clazz) throws IllegalAccessException, InstantiationException {
        JsonObject jsonObject = (JsonObject) source;
        Object instance = clazz.newInstance();
        for (String k : jsonObject.keySet()) {
            ReflectionUtils.ClassInfo classInfo = ReflectionUtils.getClassInfo(clazz);
            Object field = classInfo.getField(k);
            if (field == ReflectionUtils.NON_FIELD) {
                // ignore un known property
                continue;
            }
            Field f = (Field) field;
            classInfo.setValue(f, instance, mapValue(jsonObject.get(k), f.getType()));
        }
        return (T)instance;
    }

}
