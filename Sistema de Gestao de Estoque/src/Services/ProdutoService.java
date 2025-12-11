package Services;

import Models.Produto;
import Repositories.IProdutoRepository;
import Repositories.IVendasRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Classe de serviço para gerenciar operações relacionadas a produtos
public class ProdutoService {
    private IProdutoRepository repository;        // Repositório para operações de CRUD de produtos
    private IVendasRepository vendasRepository;   // Repositório para verificar vendas associadas

    // Construtor que recebe as dependências necessárias
    public ProdutoService(IProdutoRepository repository, IVendasRepository vendasRepository) {
        this.repository = repository;
        this.vendasRepository = vendasRepository;
    }

    // ================================
    // INCLUIR PRODUTO
    // ================================
    public boolean incluirProduto(String nome, BigDecimal preco, int estoqueInicial) {
        // Validação do nome: não pode ser nulo ou vazio
        if (nome == null || nome.isBlank()) {
            return false;  // Falha: nome inválido
        }

        // Validação do preço: não pode ser nulo, zero ou negativo
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            return false;  // Falha: preço inválido
        }

        // Validação do estoque: não pode ser negativo
        if (estoqueInicial < 0) {
            return false;  // Falha: estoque inválido
        }

        // VERIFICAÇÃO DE NOME DUPLICADO
        // Agora buscarPorNome() retorna List, não Produto
        List<Produto> produtosComMesmoNome = repository.buscarPorNome(nome);
        if (!produtosComMesmoNome.isEmpty()) {
            // Já existe produto com este nome
            return false;  // Falha: nome duplicado
        }

        // Criar produto (ID 0 será substituído pelo repositório)
        Produto novo = new Produto(0, nome, preco, estoqueInicial);
        return repository.adicionar(novo);  // Delega para o repositório
    }

    // ================================
    // BUSCAR PRODUTOS POR NOME (PARCIAL)
    // ================================
    public List<Produto> buscarProdutosPorNome(String parteNome) {
        // Delega a busca para o repositório
        return repository.buscarPorNome(parteNome);
    }

    // ================================
    // EXCLUIR PRODUTO
    // ================================
    public boolean excluirProduto(int id) {
        // Primeiro: verificar se produto existe
        Produto produto = repository.buscarPorId(id);

        if (produto == null) {
            return false; // Main vai tratar mensagem
        }

        // Segundo: verificar se produto tem vendas registradas
        if (vendasRepository.produtoTemVendas(id)) {
            return false; // Main vai tratar mensagem
        }

        // Terceiro: se passou nas validações, excluir
        return repository.excluir(id);
    }

    // ================================
    // BUSCAR PRODUTO POR ID
    // ================================
    public Produto buscarProdutoPorId(int id) {
        // Delega a busca para o repositório
        return repository.buscarPorId(id);
    }

    // ================================
    // LISTAR TODOS OS PRODUTOS
    // ================================
    public List<Produto> listarProdutos() {
        // Delega para o repositório
        return repository.getAll();
    }

    // ================================
    // LISTAR PRODUTOS COM ESTOQUE BAIXO
    // ================================
    public List<Produto> estoqueBaixo(int valorLimite) {
        // Delega para o repositório com o valor limite
        return repository.estoqueBaixo(valorLimite);
    }

    // ================================
    // ATUALIZAR ESTOQUE
    // ================================
    public boolean atualizarEstoque(int id, int novaQuantidade) {
        // Validação no service também (segurança dupla)
        if (novaQuantidade < 0) {
            return false;  // Falha: quantidade negativa
        }

        // Verificar se produto existe
        Produto produto = repository.buscarPorId(id);
        if (produto == null) {
            return false;  // Falha: produto não encontrado
        }

        // Verificar se produto está ativo
        if (!produto.isAtivo()) {
            return false;  // Falha: produto inativo
        }

        // Se todas as validações passaram, atualizar estoque
        return repository.atualizarEstoque(id, novaQuantidade);
    }

    // ================================
    // CALCULAR VALOR TOTAL DO ESTOQUE
    // ================================
    public BigDecimal calcularValorTotalEstoque() {
        // Pegar todos produtos ativos
        List<Produto> produtosAtivos = repository.getAll();

        // Iniciar total com zero
        BigDecimal total = BigDecimal.ZERO;

        // Somar valor de cada produto: preço × quantidade
        for (Produto produto : produtosAtivos) {
            BigDecimal valorProduto = produto.getPreco()
                    .multiply(BigDecimal.valueOf(produto.getQtdEstoque()));
            total = total.add(valorProduto);
        }

        // Retornar total calculado
        return total;
    }

    // ================================
    // OBTER ÚLTIMA MENSAGEM DO REPOSITÓRIO
    // ================================
    public String getUltimaMensagem() {
        return repository.getUltimaMensagem();
    }
}