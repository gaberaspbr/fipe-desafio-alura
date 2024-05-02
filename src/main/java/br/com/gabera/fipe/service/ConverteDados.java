package br.com.gabera.fipe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ConverteDados implements IConverteDados {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException | NoSuchMethodError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T obterDados(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException | RuntimeException | NoSuchMethodError e) {
            throw new RuntimeException(e);
        }
    }
}
