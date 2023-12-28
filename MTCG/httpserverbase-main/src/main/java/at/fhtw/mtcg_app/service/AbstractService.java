package at.fhtw.mtcg_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public abstract class AbstractService {
    private final ObjectMapper objectMapper;

    public AbstractService() {
        this.objectMapper = new ObjectMapper();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected <T> String convertToJson(T t) {
        try {
            return getObjectMapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T convertFromJson(String json, TypeReference<List<String>> valueType) throws JsonProcessingException {
        return (T) getObjectMapper().readValue(json, valueType);
    }
}
