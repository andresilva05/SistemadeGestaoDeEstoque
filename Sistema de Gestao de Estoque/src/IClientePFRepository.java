import java.util.List;

public interface IClientePFRepository {

    void adicionar(ClientePF clientePF);

    ClientePF buscarPorId(int id);

    ClientePF buscarPorCpf(String cpf);

    List<ClientePF> getAll();

    boolean excluir(int id);

}
