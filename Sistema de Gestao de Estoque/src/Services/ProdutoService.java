package Services;

import Models.Produto;
import Repositories.IProdutoRepository;
import Repositories.IVendasRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService {
    private IProdutoRepository repository;
    private IVendasRepository vendasRepository;

    public ProdutoService(IProdutoRepository repository, IVendasRepository vendasRepository) {
        this.repository = repository;
        this.vendasRepository = vendasRepository;
    }

    public boolean incluirProduto(String nome, BigDecimal preco, int estoqueInicial) {
        // Validar nome
        if (nome == null || nome.isBlank()) {
            return false;
        }

        // Validar preco
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Validar estoque inicial
        if (estoqueInicial < 0) {
            return false;
        }

        //  VERIFICAÇÃO DE NOME DUPLICADO
        // Agora buscarPorNome() retorna List, não Produto
        List<Produto> produtosComMesmoNome = repository.buscarPorNome(nome);
        if (!produtosComMesmoNome.isEmpty()) {
            // Já existe produto com este nome
            return false;
        }

        // Criar produto
        Produto novo = new Produto(0, nome, preco, estoqueInicial);
        return repository.adicionar(novo);
    }

    //  MÉTODO PARA BUSCA POR NOME
    public List<Produto> buscarProdutosPorNome(String parteNome) {
        return repository.buscarPorNome(parteNome);
    }

    public boolean excluirProduto(int id) {
        Produto produto = repository.buscarPorId(id);

        if (produto == null) {
            return false; // Main vai tratar mensagem
        }

        if (vendasRepository.produtoTemVendas(id)) {
            return false; // Main vai tratar mensagem
        }

        return repository.excluir(id);
    }

    public Produto buscarProdutoPorId(int id) {
        return repository.buscarPorId(id);
    }

    public List<Produto> listarProdutos() {
        return repository.getAll();
    }

    public List<Produto> estoqueBaixo(int valorLimite) {

        return repository.estoqueBaixo(valorLimite);
    }

    public boolean atualizarEstoque(int id, int novaQuantidade) {
        // Validação no service também (segurança dupla)
        if (novaQuantidade < 0) {
            return false;
        }

        Produto produto = repository.buscarPorId(id);
        if (produto == null) {
            return false;
        }

        if (!produto.isAtivo()) {
            return false;
        }

        return repository.atualizarEstoque(id, novaQuantidade);
    }

    public BigDecimal calcularValorTotalEstoque() {
        // Pegar todos produtos ativos
        List<Produto> produtosAtivos = repository.getAll();

        //  Iniciar total com zero
        BigDecimal total = BigDecimal.ZERO;

        //  Somar valor de cada produto: preço × quantidade
        for (Produto produto : produtosAtivos) {
            BigDecimal valorProduto = produto.getPreco()
                    .multiply(BigDecimal.valueOf(produto.getQtdEstoque()));
            total = total.add(valorProduto);
        }

        // Retornar total
        return total;
    }

    // Método para obter mensagem do repositório (se tiver)
    public String getUltimaMensagem() {
        return repository.getUltimaMensagem();
    }
}