package at.fhtw.mtcg_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstracService {
    private final ObjectMapper objectMapper;

    public AbstracService() {
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
}
