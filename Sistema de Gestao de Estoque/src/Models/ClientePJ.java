package Models;

import util.CNPJValidator;

public class ClientePJ extends Cliente {
    public ClientePJ(int id, String nome, String cnpj) {
        super(id, nome, cnpj);
    }

    public String getCnpj() {
        return getDocumento(); // CNPJ é o documento para PJ
    }

    @Override
    public boolean validarDocumento() {
        return CNPJValidator.isValid(getCnpj());
    }
}
