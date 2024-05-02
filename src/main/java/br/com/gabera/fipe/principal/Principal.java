package br.com.gabera.fipe.principal;

import br.com.gabera.fipe.model.Dados;
import br.com.gabera.fipe.model.DadosAnos;
import br.com.gabera.fipe.model.DadosFinais;
import br.com.gabera.fipe.model.DadosVeiculos;
import br.com.gabera.fipe.service.ConsumoApi;
import br.com.gabera.fipe.service.ConverteDados;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private Boolean validador = false;
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private String endereco = "https://parallelum.com.br/fipe/api/v1/";
    private final List<String> tiposVeiculos = Arrays.asList(new String[]{"carro", "moto", "caminhao",
            "carros", "motos", "caminhoes"});

    public void exibeMenu() {
        String json = consumo.obterDados(montaMenu());
        var marcas = conversor.obterDados(json, new TypeReference<List<Dados>>() {});

        montaDados(marcas);

        List<DadosVeiculos> veiculos = montaMenuDois(marcas);

        montaMenuTres(veiculos);

    }

    private String montaMenu() {
        do {
            System.out.println("Digite o tipo de veiculo(carro, caminhao ou moto): ");
            var digitacao = leitura.nextLine();
            if(tiposVeiculos.contains(digitacao.toLowerCase())){
                endereco += retornaLink(digitacao) + "/marcas";
                validador = true;
            } else {
                System.out.println("Opção incorreta.");
            }
        }while(!validador);

        validador = false;
        return endereco;
    }

    private List<DadosVeiculos> montaMenuDois(List<Dados> dados){
        List<DadosVeiculos> veiculos = new ArrayList<>();

        do {
            System.out.println("\nDigite o código da marca ou um trecho do nome da marca: ");
            var digitacao = leitura.nextLine();

            if(validaNumero(digitacao)) {
               if(dados.stream().anyMatch(e -> e.codigo() == Integer.parseInt(digitacao))){
                   dados.stream()
                           .filter(e -> e.codigo() == Integer.parseInt(digitacao))
                           .forEach(e -> {
                               endereco += "/" + e.codigo() + "/modelos";
                               String jsonTemp = consumo.obterDados(endereco);
                               veiculos.add(conversor.obterDados(jsonTemp, DadosVeiculos.class));
                           });
                   validador = true;
               } else {
                   System.out.println("Código ou marca invalida");
               }

            } else if(dados.stream().anyMatch(e -> e.nome().toLowerCase().contains(digitacao.toLowerCase()))){
                var count = dados
                        .stream()
                        .filter(v -> v.nome().toLowerCase().contains(digitacao.toLowerCase()))
                        .count();
                if(count == 1) {
                    dados.stream()
                            .filter(e -> e.nome().toLowerCase().contains(digitacao.toLowerCase()))
                            .forEach(e -> {
                                endereco += "/" + e.codigo() + "/modelos";
                                String jsonTemp = consumo.obterDados(endereco);
                                veiculos.add(conversor.obterDados(jsonTemp, DadosVeiculos.class));
                            });
                    validador = true;
                } else {
                    System.out.println("Mais de uma marcar contém esse nome aumente a quantidade de caractéres digitados");
                }
            } else {
                System.out.println("Código ou marca invalida");
            }
        }while(!validador);

        validador = false;

        List<Dados> veiculosLIst = veiculos.stream()
                .flatMap(v -> v.modelos().stream().map(nv -> new Dados(nv.codigo(), nv.nome())))
                .collect(Collectors.toList());
        veiculosLIst.stream()
                .forEach(v -> System.out.println(v.nome()));

        return veiculos;
    }

    private void montaMenuTres(List<DadosVeiculos> veiculos){
        long count;
        int valorUnidco = 0;
        String gravaDigitacao;
        List<Dados> listaVeiculos = veiculos.stream()
                .flatMap(v -> v.modelos().stream().map(vn -> new Dados(vn.codigo(), vn.nome())))
                .collect(Collectors.toList());

        do{
            System.out.println("\nDigite o modelo(ou um trecho) do veículo que está buscando:");
            var digitacao = leitura.nextLine();
            count = listaVeiculos
                    .stream()
                    .filter(v -> v.nome().toLowerCase().contains(digitacao.toLowerCase()))
                    .count();

            if(listaVeiculos.stream().anyMatch(v -> v.nome().toLowerCase().contains(digitacao.toLowerCase()))){
                listaVeiculos.stream()
                        .filter(v -> v.nome().toLowerCase().contains(digitacao.toLowerCase()))
                        .forEach(v -> System.out.println(v.codigo() + " - " + v.nome()));
                if(count == 1) {
                    valorUnidco = listaVeiculos.stream()
                            .filter(v -> v.nome().toLowerCase().contains(digitacao.toLowerCase()))
                            .map(v -> v.codigo())
                            .collect(Collectors.toList())
                            .getFirst();
                }

                validador = true;
            } else {
                System.out.println("Modelo não encontrado, digite novamente");
            }
        }while(!validador);
        validador = false;

        if(count == 1){
            endereco += "/" + valorUnidco
                    + "/anos";
            System.out.println();
            finalizaMenu();
        } else {
            do {
                System.out.println("\nDigite o código do veículo: ");
                var digitacao = leitura.nextLine();

                if (validaNumero(digitacao)) {
                    if (listaVeiculos.stream().anyMatch(e -> e.codigo() == Integer.parseInt(digitacao))) {
                        endereco += "/" + digitacao + "/anos";
                        validador = true;
                    } else {
                        System.out.println("Código do veículo inválido");
                    }
                } else {
                    System.out.println("Código invalido");
                }
            } while (!validador);
            validador = false;
        }

        finalizaMenu();

    }

    private void finalizaMenu() {
        List<DadosFinais> finais = new ArrayList<>();
        String json = consumo.obterDados(endereco);
        var anos = conversor.obterDados(json, new TypeReference<List<DadosAnos>>() {});
        anos.stream()
                .sorted(Comparator.comparing(DadosAnos::codigo))
                .forEach(e -> {
                    String jsonTemp = consumo.obterDados(endereco + "/" + e.codigo());
                    finais.add(conversor.obterDados(jsonTemp, DadosFinais.class));
                });
        finais.stream()
                .forEach(e -> System.out.println(e.toString()));
    }

    private void montaDados(List<Dados> dados){
        System.out.println();
        dados.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(e -> System.out.println(
                        e.codigo() +
                                " - " + e.nome()));
    }

    private String retornaLink(String tipoVeiculo){
        if(tipoVeiculo.equalsIgnoreCase("carro") ||
        tipoVeiculo.equalsIgnoreCase("carros")){
            return "carros";
        } else if(tipoVeiculo.equalsIgnoreCase("moto") ||
        tipoVeiculo.equalsIgnoreCase("motos"))
        {
            return "motos";
        } else if (tipoVeiculo.equalsIgnoreCase("caminhao") ||
        tipoVeiculo.equalsIgnoreCase("caminhoes")) {
            return "caminhoes";
        } else {
            return null;
        }
    }

    private boolean validaNumero(String texto){
        if(texto == null){
            return false;
        }
        try {
            int i = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
