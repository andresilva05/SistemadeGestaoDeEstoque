package Models;

import util.CPFValidator; // Importa a classe utilitária para validação de CPF

// Classe que representa um Cliente Pessoa Física (herda de Cliente)
public class ClientePF extends Cliente {

    // Construtor da classe ClientePF
    public ClientePF(int id, String nome, String cpf) {
        // Chama o construtor da classe pai (Cliente) passando os parâmetros
        super(id, nome, cpf);
    }

    // Método específico para obter o CPF formatado como String
    public String getCpf() {
        // Retorna o documento (que no caso de PF é o CPF)
        return getDocumento(); // CPF é o documento para PF
    }

    // Implementação do método abstrato da classe pai
    @Override
    public boolean validarDocumento() {
        // Usa o CPFValidator para verificar se o CPF é válido
        return CPFValidator.isValid(getCpf());
    }
}