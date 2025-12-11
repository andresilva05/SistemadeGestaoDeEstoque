package Models;

import util.CNPJValidator; // Importa a classe utilitária para validação de CNPJ

// Classe que representa um Cliente Pessoa Jurídica (herda de Cliente)
public class ClientePJ extends Cliente {

    // Construtor da classe ClientePJ
    public ClientePJ(int id, String nome, String cnpj) {
        // Chama o construtor da classe pai (Cliente) passando os parâmetros
        super(id, nome, cnpj);
    }

    // Método específico para obter o CNPJ formatado como String
    public String getCnpj() {
        // Retorna o documento (que no caso de PJ é o CNPJ)
        return getDocumento(); // CNPJ é o documento para PJ
    }

    // Implementação do método abstrato da classe pai
    @Override
    public boolean validarDocumento() {
        // Usa o CNPJValidator para verificar se o CNPJ é válido
        return CNPJValidator.isValid(getCnpj());
    }
}