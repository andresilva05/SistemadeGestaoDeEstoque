import java.util.ArrayList;
import java.util.List;

public class ClientePFRepository implements IClientePFRepository {
    List<ClientePF> clientes = new ArrayList<>();

    ClientePFRepository() {
    }

    @Override
    public void adicionar(ClientePF clientePF) {

        clientes.add(clientePF);
    }

    @Override
    public ClientePF buscarPorId(int id) {

        for (ClientePF cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;

    }

    @Override
    public ClientePF buscarPorCpf(String cpf) {

        for (ClientePF cliente : clientes) {
            if (cliente.getCpf().equalsIgnoreCase(cpf)) {
                return cliente;
            }
        }
        return null;
    }

    @Override
    public List<ClientePF> getAll() {
        return clientes;
    }

    @Override
    public boolean excluir(int id) {
        ClientePF clientePF = buscarPorId(id);
        if (clientePF == null) {
            return false;
        }

        clientePF.setAtivo(false);
        return true;
    }
}
