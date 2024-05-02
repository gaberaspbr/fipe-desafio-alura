package br.com.gabera.fipe.service;

import com.fasterxml.jackson.core.type.TypeReference;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
    <T> T obterDados(String json, TypeReference<T> type);
}
