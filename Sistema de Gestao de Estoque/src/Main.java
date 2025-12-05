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
import java.util.List;

public class Main {
    private static ClientePFService<ClientePF> clientePFService;
    private static ProdutoService produtoService;
    private static final String TITULO = "Sistema de Gestão de Estoque";

    public static void main(String[] args) {
        inicializarSistema();
        executarMenuPrincipal();
    }

    private static void inicializarSistema() {
        clientePFService = new ClientePFService<>(new ClientePFRepository<>());
        produtoService = new ProdutoService(new ProdutoRepository());
    }

    // ============== MENU PRINCIPAL ==============
    private static void executarMenuPrincipal() {
        while (true) {
            String[] opcoes = {"Gestão de Clientes", "Gestão de Produtos", "Sair"};
            String escolha = exibirMenu("MENU PRINCIPAL", opcoes);

            if (escolha == null || escolha.equals("Sair")) break;
            if (escolha.equals("Gestão de Clientes")) menuClientes();
            if (escolha.equals("Gestão de Produtos")) menuProdutos();
        }
        mostrarMensagem("Obrigado por usar o sistema!");
    }

    // ============== MENU CLIENTES ==============
    private static void menuClientes() {
        while (true) {
            String[] opcoes = {"Cadastrar", "Listar", "Buscar", "Excluir", "Voltar"};
            String escolha = exibirMenu("MENU CLIENTES", opcoes);

            if (escolha == null || escolha.equals("Voltar")) break;

            switch (escolha) {
                case "Cadastrar" -> cadastrarCliente();
                case "Listar" -> listarClientes();
                case "Buscar" -> buscarCliente();
                case "Excluir" -> excluirCliente();
            }
        }
    }

    private static void cadastrarCliente() {
        String nome;
        do {
            nome = JOptionPane.showInputDialog(null, "Nome do Cliente:", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (nome == null) return;
            if (nome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nome obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (nome.trim().isEmpty());

        String cpf;
        do {
            cpf = JOptionPane.showInputDialog(null, "CPF do Cliente:", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (cpf == null) return;
            if (cpf.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "CPF obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (cpf.trim().isEmpty());

        // Criar e cadastrar cliente (se passar todas as validações)
        ClientePF cliente = new ClientePF(0, nome.trim(), cpf.trim());
        clientePFService.cadastrarCliente(cliente);

        // Mostrar mensagem do service
        String mensagem = clientePFService.getUltimaMensagem();
        if (mensagem != null) {
            mostrarMensagem(mensagem);
        }
    }

    private static void listarClientes() {
        List<ClientePF> clientes = clientePFService.listarTodos();
        if (clientes.isEmpty()) {
            mostrarMensagem("Nenhum cliente cadastrado.");
            return;
        }

        StringBuilder lista = new StringBuilder("Clientes:\n\n");
        clientes.forEach(c -> lista.append(String.format("ID: %d | Nome: %s | CPF: %s\n",
                c.getId(), c.getNome(), c.getCpf())));
        mostrarMensagem(lista.toString());
    }

    private static void buscarCliente() {
        Integer id = solicitarNumero("ID do Cliente:", "Buscar");
        if (id == null) return;

        ClientePF cliente = clientePFService.buscarPorId(id);
        if (cliente == null) {
            mostrarErro("Cliente não encontrado!");
            return;
        }

        mostrarMensagem(String.format("Cliente encontrado:\nID: %d\nNome: %s\nCPF: %s",
                cliente.getId(), cliente.getNome(), cliente.getCpf()));
    }

    private static void excluirCliente() {
        Integer id = solicitarNumero("Informe o id do cliente a ser excluido:", "Excluir");
        if (id == null) return;

        clientePFService.excluirCliente(id);
        exibirMensagemService(clientePFService.getUltimaMensagem());
    }

    // ============== MENU PRODUTOS ==============
    private static void menuProdutos() {
        while (true) {
            String[] opcoes = {"Cadastrar", "Listar", "Buscar ID", "Buscar Nome", "Valor Total do Estoque", "Atualizar", "Excluir", "Voltar"};
            String escolha = exibirMenu("MENU PRODUTOS", opcoes);

            if (escolha == null || escolha.equals("Voltar")) break;

            switch (escolha) {
                case "Cadastrar" -> cadastrarProduto();
                case "Listar" -> listarProdutos();
                case "Buscar ID" -> buscarProdutoId();
                case "Buscar Nome" -> buscarProdutoNome();
                case "Valor Total" -> valorTotal();
                case "Atualizar" -> atualizarEstoque();
                case "Excluir" -> excluirProduto();
            }
        }
    }

    private static void cadastrarProduto() {
        // NOME - Pedir até ser válido
        String nome;
        do {
            nome = JOptionPane.showInputDialog(null, "Nome do Produto:", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (nome == null) return; // Usuário cancelou
            if (nome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nome obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (nome.trim().isEmpty());

        // PREÇO - Pedir até ser válido
        BigDecimal preco;
        do {
            String texto = JOptionPane.showInputDialog(null, "Preço (ex: 10.50):", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (texto == null) return; // Usuário cancelou

            try {
                texto = texto.replace(",", ".").trim();
                preco = new BigDecimal(texto);

                if (preco.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(null, "Preço deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    continue; // Volta para pedir novamente
                }
                break; // Preço válido, sai do loop
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Preço inválido! Use formato: 10.50", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (true);

        // QUANTIDADE - Pedir até ser válido
        int quantidade;
        do {
            String texto = JOptionPane.showInputDialog(null, "Quantidade em estoque:", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (texto == null) return; // Usuário cancelou

            try {
                quantidade = Integer.parseInt(texto.trim());

                if (quantidade < 0) {
                    JOptionPane.showMessageDialog(null, "Quantidade não pode ser negativa!", "Erro", JOptionPane.ERROR_MESSAGE);
                    continue; // Volta para pedir novamente
                }
                break; // Quantidade válida, sai do loop
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Quantidade inválida! Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (true);

        // Cadastrar produto
        boolean sucesso = produtoService.incluirProduto(nome.trim(), preco, quantidade);

        if (sucesso) {
            JOptionPane.showMessageDialog(null, "✅ Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String mensagem = produtoService.getUltimaMensagem();
            JOptionPane.showMessageDialog(null, mensagem != null ? mensagem : "❌ Erro ao cadastrar!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void listarProdutos() {
        List<Produto> produtos = produtoService.listarProdutos();
        if (produtos.isEmpty()) {
            mostrarMensagem("Nenhum produto.");
            return;
        }

        StringBuilder lista = new StringBuilder("Produtos:\n\n");
        produtos.forEach(p -> lista.append(String.format("ID: %d | Nome: %s | Preço: R$ %s | Estoque: %d\n",
                p.getId(), p.getNome(), p.getPreco(), p.getQtdEstoque())));
        mostrarMensagem(lista.toString());
    }

    private static void buscarProdutoId() {
        Integer id = solicitarNumero("ID do Produto:", "Buscar");
        if (id == null) return;

        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto == null) {
            mostrarErro("Produto não encontrado!");
            return;
        }

        mostrarMensagem(String.format("Produto:\nID: %d\nNome: %s\nPreço: R$ %s\nEstoque: %d",
                produto.getId(), produto.getNome(), produto.getPreco(), produto.getQtdEstoque()));
    }

    private static void buscarProdutoNome() {
        String texto = JOptionPane.showInputDialog(null, "Buscar por nome:", "Buscar", JOptionPane.QUESTION_MESSAGE);
        if (texto == null || texto.trim().isEmpty()) return;

        List<Produto> resultados = produtoService.buscarProdutosPorNome(texto.trim());
        if (resultados.isEmpty()) {
            mostrarMensagem("Nenhum produto encontrado.");
            return;
        }

        StringBuilder lista = new StringBuilder("Resultados:\n\n");
        resultados.forEach(p -> lista.append(String.format("ID: %d | Nome: %s | Estoque: %d\n",
                p.getId(), p.getNome(), p.getQtdEstoque())));
        mostrarMensagem(lista.toString());
    }

    private static void valorTotal() {
        BigDecimal total = produtoService.calcularValorTotalEstoque();
        mostrarMensagem("Valor Total: " + String.format("R$ %,.2f", total));
    }

    private static void atualizarEstoque() {
        Integer id = solicitarNumero("ID do Produto:", "Atualizar");
        if (id == null) return;

        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto == null) {
            mostrarErro("Produto não encontrado!");
            return;
        }

        mostrarMensagem("Produto: " + produto.getNome() + "\nEstoque atual: " + produto.getQtdEstoque());

        Integer novaQtd;
        do {
            novaQtd = solicitarNumero("Nova quantidade:", "Atualizar");
            if (novaQtd == null) return;
            if (novaQtd < 0) mostrarErro("Não pode ser negativo!");
        } while (novaQtd < 0);

        boolean sucesso = produtoService.atualizarEstoque(id, novaQtd);
        exibirResultado(sucesso, "Estoque atualizado!", produtoService.getUltimaMensagem());
    }

    private static void excluirProduto() {
        Integer id = solicitarNumero("Informe o ID do produto a ser excluido:", "Excluir");
        if (id == null) return;

        boolean sucesso = produtoService.excluirProduto(id);
        exibirResultado(sucesso, "Produto excluído!", produtoService.getUltimaMensagem());
    }

    // ============== MÉTODOS AUXILIARES ==============
    private static String exibirMenu(String titulo, String[] opcoes) {
        return (String) JOptionPane.showInputDialog(null, titulo, TITULO,
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    private static Integer solicitarNumero(String mensagem, String titulo) {
        String texto = JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
        if (texto == null) return null;

        try {
            return Integer.parseInt(texto.trim());
        } catch (Exception e) {
            mostrarErro("Número inválido!");
            return solicitarNumero(mensagem, titulo);
        }
    }

    private static void exibirMensagemService(String mensagem) {
        if (mensagem != null) mostrarMensagem(mensagem);
    }

    private static void exibirResultado(boolean sucesso, String mensagemSucesso, String mensagemErro) {
        mostrarMensagem(sucesso ? "✅ " + mensagemSucesso : "❌ " + (mensagemErro != null ? mensagemErro : "Operação falhou"));
    }

    private static void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(null, "❌ " + mensagem, TITULO, JOptionPane.ERROR_MESSAGE);
    }
}