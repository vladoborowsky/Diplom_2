import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SerializationUtils {
    public static JsonObject excludeFieldSerialization(Object object, String excludeField) {
        JsonObject jsonObject;
        if (object instanceof JsonObject) {
            jsonObject = (JsonObject) object;
        } else {
            jsonObject = new Gson()
                    .toJsonTree(object)
                    .getAsJsonObject();
        }

        if (excludeField != null) {
            jsonObject.remove(excludeField);
        }
        return jsonObject;
    }
}
