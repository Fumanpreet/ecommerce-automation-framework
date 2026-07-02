package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> readJsonList(String path, Class<T> clazz) throws Exception {

        InputStream input = Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(path);

        return mapper.readValue(input, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}

