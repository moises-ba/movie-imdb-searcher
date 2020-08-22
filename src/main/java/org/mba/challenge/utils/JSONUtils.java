package org.mba.challenge.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class JSONUtils {

    private ObjectMapper mapper = new ObjectMapper();
    private final static JSONUtils INSTANCE = new JSONUtils();
    public final static JSONUtils getInstance() {
        return INSTANCE;
    }

    private JSONUtils() {
        this.mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
           .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String asJSON(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> T toObject(byte[] json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
