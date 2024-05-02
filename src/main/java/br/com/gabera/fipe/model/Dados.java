package br.com.gabera.fipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Dados(@JsonAlias("codigo") Integer codigo,
                    @JsonAlias("nome") String nome) {
}
