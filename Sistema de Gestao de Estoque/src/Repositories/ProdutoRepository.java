package Repositories;

import Models.Produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Implementação do repositório de produtos
public class ProdutoRepository implements IProdutoRepository {

    // Lista para armazenar os produtos em memória
    private List<Produto> produtos = new ArrayList<>();

    // Armazena a última mensagem gerada pelas operações
    private String ultimaMensagem;

    // Contador para gerar IDs automáticos e sequenciais
    private int proximoId = 1;

    // ================================
    // ADICIONAR PRODUTO
    // ================================
    @Override
    public boolean adicionar(Produto produto) {
        // Atribui um ID automático ao produto
        produto.setId(proximoId);
        proximoId++; // Incrementa para o próximo produto

        // ✅ CORRIGIDO: Verifica se já existe produto com o mesmo nome
        // Busca produtos com o mesmo nome (case-insensitive)
        List<Produto> produtosComMesmoNome = buscarPorNome(produto.getNome());
        if (!produtosComMesmoNome.isEmpty()) {
            ultimaMensagem = "Já existe produto com nome '" + produto.getNome() + "'";
            return false; // Falha na operação - nome duplicado
        }

        // Adiciona o produto à lista
        produtos.add(produto);
        ultimaMensagem = "Produto adicionado com sucesso!";
        return true; // Sucesso na operação
    }

    // ================================
    // BUSCAR POR ID
    // ================================
    @Override
    public Produto buscarPorId(int id) {
        // Usa Stream API para buscar um produto pelo ID e que esteja ativo
        return produtos.stream()
                .filter(p -> p.getId() == id && p.isAtivo()) // Filtra por ID e status ativo
                .findFirst() // Retorna o primeiro que encontrar
                .orElse(null); // Retorna null se não encontrar
    }

    // ================================
    // BUSCAR POR NOME (PARCIAL)
    // ================================
    @Override
    public List<Produto> buscarPorNome(String parteNome) {
        // Converte a busca para minúsculas para busca case-insensitive
        String parteLower = parteNome.toLowerCase();

        // Busca produtos ativos cujo nome contenha a string fornecida
        return produtos.stream()
                .filter(p -> p.isAtivo() &&
                        p.getNome().toLowerCase().contains(parteLower))
                .collect(Collectors.toList()); // Converte para List
    }

    // ================================
    // LISTAR TODOS OS PRODUTOS ATIVOS
    // ================================
    @Override
    public List<Produto> getAll() {
        // Retorna uma nova lista contendo apenas os produtos ativos
        return produtos.stream()
                .filter(Produto::isAtivo) // Referência de método para filtrar ativos
                .collect(Collectors.toList()); // Converte o Stream para List
    }

    // ================================
    // EXCLUIR PRODUTO (EXCLUSÃO LÓGICA)
    // ================================
    @Override
    public boolean excluir(int id) {
        // Debug: mostra qual ID está sendo excluído (útil para desenvolvimento)
        System.out.println("Repository.excluir() chamado para ID: " + id);

        // Busca o produto pelo ID
        Produto produto = buscarPorId(id);

        if (produto != null) {
            // Faz exclusão lógica - marca como inativo ao invés de remover da lista
            produto.setAtivo(false);
            ultimaMensagem = "Produto excluído com sucesso!";
            return true; // Sucesso na operação
        }

        // Produto não encontrado
        ultimaMensagem = "Produto não encontrado com ID: " + id;
        return false; // Falha na operação
    }

    // ================================
    // ATUALIZAR ESTOQUE
    // ================================
    @Override
    public boolean atualizarEstoque(int id, int novaQuantidade) {
        // Busca o produto pelo ID
        Produto produto = buscarPorId(id);

        if (produto != null) {
            // Atualiza a quantidade em estoque
            produto.setQtdEstoque(novaQuantidade);
            ultimaMensagem = "Estoque atualizado para " + novaQuantidade;
            return true; // Sucesso na operação
        }

        // Produto não encontrado
        ultimaMensagem = "Produto não encontrado com ID: " + id;
        return false; // Falha na operação
    }

    // ================================
    // ATUALIZAR PREÇO
    // ================================
    @Override
    public boolean atualizarPreco(int id, BigDecimal novoPreco) {
        // Busca o produto pelo ID
        Produto produto = buscarPorId(id);

        // Verifica se produto existe, preço não é nulo e é maior que zero
        if (produto != null && novoPreco != null && novoPreco.compareTo(BigDecimal.ZERO) > 0) {
            // Atualiza o preço do produto
            produto.setPreco(novoPreco);
            ultimaMensagem = "Preço atualizado para R$ " + novoPreco;
            return true; // Sucesso na operação
        }

        // Falha na atualização - produto não encontrado ou preço inválido
        ultimaMensagem = "Falha ao atualizar preço";
        return false; // Falha na operação
    }

    // ================================
    // LISTAR PRODUTOS (MÉTODO OPCIONAL)
    // ================================
    // Método para listar produtos no console (mantém compatibilidade com código antigo)
    public void listar() {
        // Obtém todos os produtos ativos
        List<Produto> ativos = getAll();

        if (ativos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            // Usa forEach com referência de método para imprimir cada produto
            ativos.forEach(System.out::println);
        }
    }

    // ================================
    // ESTOQUE BAIXO
    // ================================
    @Override
    public List<Produto> estoqueBaixo(int valorLimite) {
        // Obtém todos os produtos ativos
        List<Produto> produtos = getAll();
        List<Produto> estoqueBaixo = new ArrayList<>();

        // Percorre todos os produtos procurando aqueles com estoque abaixo do limite
        for (Produto p : produtos) {
            if (p.getQtdEstoque() <= valorLimite) {
                estoqueBaixo.add(p);
            }
        }

        // Define mensagem apropriada baseada no resultado
        if (estoqueBaixo.isEmpty()) {
            ultimaMensagem = "Nenhum produto com estoque ≤ " + valorLimite;
        } else {
            ultimaMensagem = estoqueBaixo.size() + " produto(s) encontrado(s)";
        }

        return estoqueBaixo;
    }

    // ================================
    // OBTER ÚLTIMA MENSAGEM
    // ================================
    @Override
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
}