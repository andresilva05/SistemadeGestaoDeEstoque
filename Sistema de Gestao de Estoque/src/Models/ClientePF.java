package Models;

import util.CPFValidator;
public class ClientePF extends Cliente {
    public ClientePF(int id, String nome, String cpf) {
        super(id, nome, cpf);
    }

    public String getCpf() {
        return getDocumento(); // CPF é o documento para PF
    }

    @Override
    public boolean validarDocumento() {
        return CPFValidator.isValid(getCpf());
    }
}
