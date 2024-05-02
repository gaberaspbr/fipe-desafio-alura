package br.com.gabera.fipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosFinais {
    @JsonAlias("Modelo")
    private String modelo;
    @JsonAlias("Marca")
    private String marca;
    @JsonAlias("Valor")
    private String valor;
    @JsonAlias("Combustivel")
    private String combustivel;
    @JsonAlias("AnoModelo")
    private String ano;
    @JsonAlias("MesReferencia")
    private String mes;

    @Override
    public String toString() {
        return "Modelo: " + modelo +
                ", Marca: " + marca +
                ", Valor: " + valor +
                ", Combustivel: " + combustivel +
                ", Ano Modelo: " + ano +
                ", Mês de referência: " + mes;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public String getValor() {
        return valor;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public String getAno() {
        return ano;
    }

    public String getMes() {
        return mes;
    }
}
