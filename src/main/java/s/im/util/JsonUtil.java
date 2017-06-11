package s.im.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {
    private static ObjectMapper mapper = null;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(String input, TypeReference<T> tTypeReference) {
        try {
            if (input == null) {
                return null;
            }
            return mapper.readValue(input.getBytes("UTF8"), tTypeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int getMessageType(String payload) {
        try {
            return mapper.readTree(payload).findValue("message_type").asInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUserName(String payload) {
        try {
            return mapper.readTree(payload).findValue("username").asText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
