import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    List<Produto> produtos = new ArrayList<>();

    public void adicionar(Produto produto) {
        produtos.add(produto);
    }


    public Produto buscarPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }


    public void listar() {
        for (Produto p : produtos) {
            System.out.println(p);
        }
    }

    public boolean excluir(int id) {
        Produto produto = buscarPorId(id);
        if (produto != null) {
            produto.setAtivo(false); // soft delete
            return true;
        }
        return false;
    }


}
