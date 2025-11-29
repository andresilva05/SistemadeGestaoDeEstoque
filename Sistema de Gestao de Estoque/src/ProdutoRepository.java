import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository implements IProdutoRepository {

    List<Produto> produtos = new ArrayList<>();


    @Override
    public void adicionar(Produto produto) {
        produtos.add(produto);
    }

    @Override
    public Produto buscarPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Produto buscarPorNome(String nome) {
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome) && p.isAtivo()) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void listar() {
        for (Produto p : produtos) {
            System.out.println(p);
        }
    }

    @Override
    public boolean excluir(int id) {
        Produto produto = buscarPorId(id);
        if (produto == null) {
            return false;
        }

        produto.setAtivo(false); // soft delete
        return true;
    }

    @Override
    public List<Produto> getAll() {
        return produtos;
    }

}
