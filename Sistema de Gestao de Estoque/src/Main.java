import Models.ClientePF;
import Models.Produto;
import Repositories.ClientePFRepository;
import Repositories.IClientePFRepository;
import Repositories.IProdutoRepository;
import Repositories.ProdutoRepository;
import Services.ClientePFService;
import Services.ProdutoService;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static ClientePFService<ClientePF> clientePFService;
    private static ProdutoService produtoService;
    private static final String TITLE = "Sistema de Gestão de Estoque";

    public static void main(String[] args) {
        // Inicialização dos repositórios e services
        IClientePFRepository<ClientePF> repoCliente = new ClientePFRepository<>();
        IProdutoRepository repoProduto = new ProdutoRepository();

        clientePFService = new ClientePFService<>(repoCliente);
        produtoService = new ProdutoService(repoProduto);

        int opcaoMenu;

        // Loop principal do sistema
        do {
            opcaoMenu = mostrarMenuPrincipal();

            if (opcaoMenu == 1) {
                menuClientes();
            } else if (opcaoMenu == 2) {
                menuProdutos();
            } else if (opcaoMenu == 0) {
                mostrarMensagem("Obrigado por utilizar o Sistema de Gestão de Estoque!");
            }
        } while (opcaoMenu != 0);
    }

    // Menu Principal
    private static int mostrarMenuPrincipal() {
        Object[] opcoes = {
                "Gestão de Clientes",
                "Gestão de Produtos",
                "Sair do Sistema"
        };

        String resposta = (String) JOptionPane.showInputDialog(
                null,
                "MENU PRINCIPAL\nSelecione uma opção:",
                TITLE,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (opcoes[0].equals(resposta)) {
            return 1;
        } else if (opcoes[1].equals(resposta)) {
            return 2;
        } else {
            return 0;
        }
    }

    // ==================== MENU CLIENTES ====================
    private static void menuClientes() {
        Object[] opcoes = {
                "Cadastrar Cliente PF",
                "Listar Clientes",
                "Buscar Cliente por ID",
                "Excluir Cliente",
                "Voltar ao Menu Principal"
        };

        String resposta = (String) JOptionPane.showInputDialog(
                null,
                "MENU CLIENTES\nSelecione uma opção:",
                TITLE,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (opcoes[0].equals(resposta)) {
            cadastrarClientePF();
        } else if (opcoes[1].equals(resposta)) {
            listarClientes();
        } else if (opcoes[2].equals(resposta)) {
            buscarClientePorId();
        } else if (opcoes[3].equals(resposta)) {
            excluirCliente();
        }
    }

    // Cadastrar Cliente Pessoa Física
    public static void cadastrarClientePF() {
        // Solicitar Nome
        String nome = JOptionPane.showInputDialog(
                null,
                "Nome do Cliente:",
                "Cadastro de Cliente PF",
                JOptionPane.QUESTION_MESSAGE
        );
        if (nome == null || nome.trim().isEmpty()) {
            mostrarErro("Erro: Nome é obrigatório!");
            return;
        }

        // Solicitar CPF
        String cpf = JOptionPane.showInputDialog(
                null,
                "CPF do Cliente:",
                "Cadastro de Cliente PF",
                JOptionPane.QUESTION_MESSAGE
        );
        if (cpf == null || cpf.trim().isEmpty()) {
            mostrarErro("Erro: CPF é obrigatório!");
            return;
        }

        // Criar e cadastrar cliente
        ClientePF cliente = new ClientePF(0, nome, cpf);
        clientePFService.cadastrarCliente(cliente);
        mostrarMensagem(clientePFService.getUltimaMensagem());
    }

    // Listar todos os clientes
    private static void listarClientes() {
        List<ClientePF> clientes = clientePFService.listarTodos();

        if (clientes.isEmpty()) {
            mostrarMensagem("Nenhum cliente cadastrado no sistema.");
            return;
        }

        String lista = "Lista de Clientes:\n\n";
        for (ClientePF cliente : clientes) {
            lista = lista + "ID: " + cliente.getId() + " | ";
            lista = lista + "Nome: " + cliente.getNome() + " | ";
            lista = lista + "CPF: " + cliente.getCpf() + " | ";
            lista = lista + "Status: " + (cliente.isAtivo() ? "Ativo" : "Inativo");
            lista = lista + "\n";
        }

        mostrarMensagem(lista);
    }

    // Buscar cliente por ID
    private static void buscarClientePorId() {
        String idTexto = JOptionPane.showInputDialog(
                null,
                "Digite o ID do cliente:",
                "Buscar Cliente",
                JOptionPane.QUESTION_MESSAGE
        );
        if (idTexto == null) {
            return;
        }

        // Verificar se ID contém apenas números
        boolean idValido = true;
        for (int i = 0; i < idTexto.length(); i++) {
            if (!Character.isDigit(idTexto.charAt(i))) {
                idValido = false;
                break;
            }
        }

        if (!idValido) {
            mostrarErro("Erro: ID deve ser um número válido!");
            return;
        }

        int id = Integer.parseInt(idTexto);
        ClientePF cliente = clientePFService.buscarPorId(id);

        if (cliente != null) {
            String informacoes = "Cliente encontrado:\n\n";
            informacoes = informacoes + "ID: " + cliente.getId() + "\n";
            informacoes = informacoes + "Nome: " + cliente.getNome() + "\n";
            informacoes = informacoes + "CPF: " + cliente.getCpf() + "\n";
            informacoes = informacoes + "Status: " + (cliente.isAtivo() ? "Ativo" : "Inativo");
            mostrarMensagem(informacoes);
        } else {
            mostrarErro("Cliente não encontrado com ID: " + id);
        }
    }

    // Excluir cliente
    private static void excluirCliente() {
        String idTexto = JOptionPane.showInputDialog(
                null,
                "Digite o ID do cliente para excluir:",
                "Excluir Cliente",
                JOptionPane.QUESTION_MESSAGE
        );
        if (idTexto == null) {
            return;
        }

        // Verificar se ID contém apenas números
        boolean idValido = true;
        for (int i = 0; i < idTexto.length(); i++) {
            if (!Character.isDigit(idTexto.charAt(i))) {
                idValido = false;
                break;
            }
        }

        if (!idValido) {
            mostrarErro("Erro: ID deve ser um número válido!");
            return;
        }

        int id = Integer.parseInt(idTexto);
        clientePFService.excluirCliente(id);
        mostrarMensagem(clientePFService.getUltimaMensagem());
    }

    // ==================== MENU PRODUTOS ====================
    private static void menuProdutos() {
        Object[] opcoes = {
                "Cadastrar Produto",
                "Listar Produtos",
                "Buscar Produto por ID",
                "Buscar Produto por Nome",
                "Atualizar Estoque",
                "Excluir Produto",
                "Voltar ao Menu Principal"
        };

        String resposta = (String) JOptionPane.showInputDialog(
                null,
                "MENU PRODUTOS\nSelecione uma opção:",
                TITLE,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (opcoes[0].equals(resposta)) {
            cadastrarProduto();
        } else if (opcoes[1].equals(resposta)) {
            listarProdutos();
        } else if (opcoes[2].equals(resposta)) {
            buscarProdutoPorId();
        } else if (opcoes[3].equals(resposta)) {
            buscarProdutoPorNome();
        } else if (opcoes[4].equals(resposta)) {
            atualizarEstoque();
        } else if (opcoes[5].equals(resposta)) {
            excluirProduto();
        }
    }

    // Cadastrar Produto
    public static void cadastrarProduto() {

        // Solicitar Nome
        String nome = JOptionPane.showInputDialog(
                null,
                "Nome do Produto:",
                "Cadastro de Produto",
                JOptionPane.QUESTION_MESSAGE
        );
        if (nome == null || nome.trim().isEmpty()) {
            mostrarErro("Erro: Nome é obrigatório!");
            return;
        }

        // Solicitar Preço
        String precoTexto = JOptionPane.showInputDialog(
                null,
                "Preço do Produto (ex: 10.50):",
                "Cadastro de Produto",
                JOptionPane.QUESTION_MESSAGE
        );
        if (precoTexto == null) {
            return;
        }

        // Converter para BigDecimal
        BigDecimal preco;
        try {
            // Trocar vírgula por ponto para padronizar
            precoTexto = precoTexto.replace(",", ".");
            preco = new BigDecimal(precoTexto);
        } catch (Exception e) {
            mostrarErro("Erro: Preço deve ser um número válido!");
            return;
        }

        // Verificar se preço é positivo
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarErro("Erro: Preço deve ser maior que zero!");
            return;
        }

        // Solicitar Quantidade
        String quantidadeTexto = JOptionPane.showInputDialog(
                null,
                "Quantidade em Estoque:",
                "Cadastro de Produto",
                JOptionPane.QUESTION_MESSAGE
        );
        if (quantidadeTexto == null) {
            return;
        }

        // Verificar se quantidade contém apenas números
        boolean quantidadeValida = true;
        for (int i = 0; i < quantidadeTexto.length(); i++) {
            if (!Character.isDigit(quantidadeTexto.charAt(i))) {
                quantidadeValida = false;
                break;
            }
        }

        if (!quantidadeValida) {
            mostrarErro("Erro: Quantidade deve ser um número inteiro!");
            return;
        }

        int quantidade = Integer.parseInt(quantidadeTexto);

        // Verificar se quantidade é positiva
        if (quantidade < 0) {
            mostrarErro("Erro: Quantidade não pode ser negativa!");
            return;
        }

        // Criar e cadastrar produto usando o construtor correto
        Produto produto = new Produto(0, nome, preco, quantidade);
        boolean sucesso = produtoService.incluirProduto(nome, preco, quantidade);

        if (sucesso) {
            mostrarMensagem("Produto cadastrado com sucesso!");
        } else {
            mostrarMensagem(produtoService.getUltimaMensagem());
        }
    }

    // Listar todos os produtos
    private static void listarProdutos() {
        List<Produto> produtos = produtoService.listarProdutos();

        if (produtos.isEmpty()) {
            mostrarMensagem("Nenhum produto cadastrado no sistema.");
            return;
        }

        String lista = "Lista de Produtos:\n\n";
        for (Produto produto : produtos) {
            lista = lista + "ID: " + produto.getId() + " | ";
            lista = lista + "Nome: " + produto.getNome() + " | ";
            lista = lista + "Preço: R$ " + produto.getPreco() + " | ";
            lista = lista + "Estoque: " + produto.getQtdEstoque() + " | ";
            lista = lista + "Status: " + (produto.isAtivo() ? "Ativo" : "Inativo");
            lista = lista + "\n";
        }

        mostrarMensagem(lista);
    }

    private static void buscarProdutoPorNome() {
        // 1. Solicitar parte do nome
        String textoProduto = JOptionPane.showInputDialog(
                null,
                "Digite parte do nome do produto:",
                "Buscar Produto",
                JOptionPane.QUESTION_MESSAGE
        );

        // Verificar se usuário cancelou
        if (textoProduto == null) {
            return;
        }

        // 2. Buscar produtos
        List<Produto> resultados = produtoService.buscarProdutosPorNome(textoProduto);

        // 3. Verificar resultados
        if (resultados.isEmpty()) {
            // Lista VAZIA = nenhum produto encontrado
            mostrarMensagem("Nenhum produto encontrado com: '" + textoProduto + "'");
            return;
        }

        // 4. Mostrar resultados
        StringBuilder lista = new StringBuilder();
        lista.append("Produtos encontrados com '").append(textoProduto).append("':\n\n");

        for (Produto produto : resultados) {
            lista.append("ID: ").append(produto.getId()).append(" | ");
            lista.append("Nome: ").append(produto.getNome()).append(" | ");
            lista.append("Estoque: ").append(produto.getQtdEstoque()).append(" | ");
            lista.append("Preço: R$ ").append(produto.getPreco()).append("\n");
        }

        mostrarMensagem(lista.toString());
    }

    // Buscar produto por ID
    private static void buscarProdutoPorId() {
        String idTexto = JOptionPane.showInputDialog(
                null,
                "Digite o ID do produto:",
                "Buscar Produto",
                JOptionPane.QUESTION_MESSAGE
        );
        if (idTexto == null) {
            return;
        }

        // Verificar se ID contém apenas números
        boolean idValido = true;
        for (int i = 0; i < idTexto.length(); i++) {
            if (!Character.isDigit(idTexto.charAt(i))) {
                idValido = false;
                break;
            }
        }

        if (!idValido) {
            mostrarErro("Erro: ID deve ser um número válido!");
            return;
        }

        int id = Integer.parseInt(idTexto);
        Produto produto = produtoService.buscarProdutoPorId(id);

        if (produto != null) {
            String informacoes = "Produto encontrado:\n\n";
            informacoes = informacoes + "ID: " + produto.getId() + "\n";
            informacoes = informacoes + "Nome: " + produto.getNome() + "\n";
            informacoes = informacoes + "Preço: R$ " + produto.getPreco() + "\n";
            informacoes = informacoes + "Estoque: " + produto.getQtdEstoque() + "\n";
            informacoes = informacoes + "Status: " + (produto.isAtivo() ? "Ativo" : "Inativo");
            mostrarMensagem(informacoes);
        } else {
            mostrarErro("Produto não encontrado com ID: " + id);
        }
    }

    // Atualizar estoque
    private static void atualizarEstoque() {
        // Solicitar ID do produto
        String idTexto = JOptionPane.showInputDialog(
                null,
                "Digite o ID do produto:",
                "Atualizar Estoque",
                JOptionPane.QUESTION_MESSAGE
        );
        if (idTexto == null) {
            return;
        }

        // Verificar se ID contém apenas números
        boolean idValido = true;
        for (int i = 0; i < idTexto.length(); i++) {
            if (!Character.isDigit(idTexto.charAt(i))) {
                idValido = false;
                break;
            }
        }

        if (!idValido) {
            mostrarErro("Erro: ID deve ser um número válido!");
            return;
        }

        int id = Integer.parseInt(idTexto);

        // Verificar se produto existe
        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto == null) {
            mostrarErro("Produto não encontrado com ID: " + id);
            return;
        }

        // Verificar se produto está ativo
        if (!produto.isAtivo()) {
            mostrarErro("Erro: Não é possível atualizar estoque de produto inativo!");
            return;
        }

        // Mostrar dados atuais
        mostrarMensagem("Produto: " + produto.getNome() +
                "\nEstoque atual: " + produto.getQtdEstoque());

        // Solicitar nova quantidade
        String quantidadeTexto = JOptionPane.showInputDialog(
                null,
                "Nova quantidade em estoque:",
                "Atualizar Estoque",
                JOptionPane.QUESTION_MESSAGE
        );
        if (quantidadeTexto == null) {
            return;
        }

        // Verificar se quantidade contém apenas números
        boolean quantidadeValida = true;
        for (int i = 0; i < quantidadeTexto.length(); i++) {
            char caracter = quantidadeTexto.charAt(i);
            if (!Character.isDigit(caracter)) {
                // Permitir sinal de negativo no início
                if (i == 0 && caracter == '-') {
                    continue;
                }
                quantidadeValida = false;
                break;
            }
        }

        if (!quantidadeValida) {
            mostrarErro("Erro: Quantidade deve ser um número inteiro!");
            return;
        }

        int novaQuantidade = Integer.parseInt(quantidadeTexto);

        // VALIDAÇÃO IMPORTANTE: não permitir negativo
        if (novaQuantidade < 0) {
            mostrarErro("Erro: Estoque não pode ser negativo!");
            return;
        }

        // Atualizar estoque
        boolean sucesso = produtoService.atualizarEstoque(id, novaQuantidade);

        if (sucesso) {
            mostrarMensagem("Estoque atualizado com sucesso!\nNovo estoque: " + novaQuantidade);
        } else {
            mostrarErro("Erro: Não foi possível atualizar o estoque!");
        }
    }

    // Excluir produto
    private static void excluirProduto() {
        String idTexto = JOptionPane.showInputDialog(
                null,
                "Digite o ID do produto para excluir:",
                "Excluir Produto",
                JOptionPane.QUESTION_MESSAGE
        );
        if (idTexto == null) {
            return;
        }

        // Verificar se ID contém apenas números
        boolean idValido = true;
        for (int i = 0; i < idTexto.length(); i++) {
            if (!Character.isDigit(idTexto.charAt(i))) {
                idValido = false;
                break;
            }
        }

        if (!idValido) {
            mostrarErro("Erro: ID deve ser um número válido!");
            return;
        }

        int id = Integer.parseInt(idTexto);
        boolean sucesso = produtoService.excluirProduto(id);

        if (sucesso) {
            mostrarMensagem("Produto excluído com sucesso!");
        } else {
            mostrarMensagem(produtoService.getUltimaMensagem());
        }
    }

    // Métodos auxiliares
    private static void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(
                null,
                mensagem,
                TITLE,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(
                null,
                mensagem,
                TITLE,
                JOptionPane.ERROR_MESSAGE
        );
    }
}