package at.fhtw.users.service;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class UserAbstractService {
    private final ObjectMapper objectMapper;

    public UserAbstractService() {
        this.objectMapper = new ObjectMapper();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
