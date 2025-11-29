import java.util.List;

public interface IProdutoRepository {
    void adicionar(Produto produto);

    Produto buscarPorId(int id);

    Produto buscarPorNome(String nome);

    void listar();

    boolean excluir(int id);

    public List<Produto> getAll();

}
