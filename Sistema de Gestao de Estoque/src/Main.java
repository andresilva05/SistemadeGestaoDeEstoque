import Models.ClientePF;
import Models.ClientePJ;
import Models.Produto;
import Models.Venda;

import Repositories.ClienteRepository;
import Repositories.IClienteRepository;
import Repositories.IProdutoRepository;
import Repositories.ProdutoRepository;
import Repositories.IVendasRepository;
import Repositories.VendasRepository;

import Services.ClienteService;
import Services.ProdutoService;
import Services.VendaService;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.util.List;

public class Main {

    private static ClienteService<ClientePF> clientePFService;
    private static ClienteService<ClientePJ> clientePJService;
    private static ProdutoService produtoService;
    private static VendaService vendasService;

    private static final String TITLE = "Sistema de Gestão de Estoque";

    public static void main(String[] args) {

        // Repositórios CORRIGIDOS
        IClienteRepository<ClientePF> repoClientePF = new ClienteRepository<>();
        IClienteRepository<ClientePJ> repoClientePJ = new ClienteRepository<>();
        IProdutoRepository repoProduto = new ProdutoRepository();
        IVendasRepository repoVenda = new VendasRepository();

        // Services
        clientePFService = new ClienteService<>(repoClientePF);
        clientePJService = new ClienteService<>(repoClientePJ);
        produtoService = new ProdutoService(repoProduto);
        vendasService = new VendaService(repoVenda, repoClientePF, repoClientePJ, repoProduto);

        int opcao;

        do {
            opcao = mostrarMenuPrincipal();

            switch (opcao) {
                case 1: menuClientes(); break;
                case 2: menuProdutos(); break;
                case 3: menuVendas(); break;
                case 0:
                    mostrarMensagem("Obrigado por utilizar o sistema!");
                    break;
            }

        } while (opcao != 0);
    }

    // ============================================================
    // MENU PRINCIPAL
    // ============================================================
    private static int mostrarMenuPrincipal() {

        Object[] opcoes = {
                "Gestão de Clientes",
                "Gestão de Produtos",
                "Gestão de Vendas",
                "Sair"
        };

        String r = (String) JOptionPane.showInputDialog(
                null,
                "MENU PRINCIPAL",
                TITLE,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (r == null) return 0;

        if (r.equals(opcoes[0])) return 1;
        if (r.equals(opcoes[1])) return 2;
        if (r.equals(opcoes[2])) return 3;

        return 0;
    }

    // ============================================================
    // MENU CLIENTES
    // ============================================================
    private static void menuClientes() {

        Object[] opcoes = {
                "Cadastrar Cliente PF",
                "Cadastrar Cliente PJ",
                "Listar Clientes PF",
                "Listar Clientes PJ",
                "Buscar Cliente PF por ID",
                "Buscar Cliente PJ por ID",
                "Excluir Cliente PF",
                "Excluir Cliente PJ",
                "Voltar"
        };

        String r;

        do {
            r = (String) JOptionPane.showInputDialog(
                    null,
                    "MENU CLIENTES",
                    TITLE,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (r == null || r.equals("Voltar")) return;

            switch (r) {
                case "Cadastrar Cliente PF": cadastrarClientePF(); break;
                case "Cadastrar Cliente PJ": cadastrarClientePJ(); break;
                case "Listar Clientes PF": listarClientesPF(); break;
                case "Listar Clientes PJ": listarClientesPJ(); break;
                case "Buscar Cliente PF por ID": buscarClientePFPorId(); break;
                case "Buscar Cliente PJ por ID": buscarClientePJPorId(); break;
                case "Excluir Cliente PF": excluirClientePF(); break;
                case "Excluir Cliente PJ": excluirClientePJ(); break;
            }

        } while (true);
    }

    // ============================================================
    // CLIENTE PF
    // ============================================================
    private static void cadastrarClientePF() {

        String nome = JOptionPane.showInputDialog(null, "Nome:");
        if (nome == null || nome.trim().isEmpty()) return;

        String cpf = JOptionPane.showInputDialog(null, "CPF:");
        if (cpf == null || cpf.trim().isEmpty()) return;

        ClientePF c = new ClientePF(0, nome, cpf);

        clientePFService.cadastrarCliente(c);
        mostrarMensagem(clientePFService.getUltimaMensagem());
    }

    private static void listarClientesPF() {

        List<ClientePF> lista = clientePFService.listarTodos();
        if (lista.isEmpty()) {
            mostrarMensagem("Nenhum cliente PF cadastrado.");
            return;
        }

        StringBuilder sb = new StringBuilder("Clientes PF:\n\n");

        for (ClientePF c : lista) sb.append(c).append("\n");

        mostrarMensagem(sb.toString());
    }

    private static void buscarClientePFPorId() {

        String idTxt = JOptionPane.showInputDialog("ID:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        ClientePF c = clientePFService.buscarPorId(Integer.parseInt(idTxt));

        if (c == null) mostrarErro("Cliente não encontrado.");
        else mostrarMensagem(c.toString());
    }

    private static void excluirClientePF() {

        String idTxt = JOptionPane.showInputDialog("ID:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        clientePFService.excluirCliente(Integer.parseInt(idTxt));
        mostrarMensagem(clientePFService.getUltimaMensagem());
    }

    // ============================================================
    // CLIENTE PJ
    // ============================================================
    private static void cadastrarClientePJ() {

        String nome = JOptionPane.showInputDialog("Razão Social:");
        if (nome == null || nome.trim().isEmpty()) return;

        String cnpj = JOptionPane.showInputDialog("CNPJ:");
        if (cnpj == null || cnpj.trim().isEmpty()) return;

        ClientePJ c = new ClientePJ(0, nome, cnpj);

        clientePJService.cadastrarCliente(c);
        mostrarMensagem(clientePJService.getUltimaMensagem());
    }

    private static void listarClientesPJ() {

        List<ClientePJ> lista = clientePJService.listarTodos();
        if (lista.isEmpty()) {
            mostrarMensagem("Nenhum cliente PJ cadastrado.");
            return;
        }

        StringBuilder sb = new StringBuilder("Clientes PJ:\n\n");
        for (ClientePJ c : lista) sb.append(c).append("\n");

        mostrarMensagem(sb.toString());
    }

    private static void buscarClientePJPorId() {

        String idTxt = JOptionPane.showInputDialog("ID:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        ClientePJ c = clientePJService.buscarPorId(Integer.parseInt(idTxt));

        if (c == null) mostrarErro("Cliente não encontrado.");
        else mostrarMensagem(c.toString());
    }

    private static void excluirClientePJ() {

        String idTxt = JOptionPane.showInputDialog("ID:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        clientePJService.excluirCliente(Integer.parseInt(idTxt));
        mostrarMensagem(clientePJService.getUltimaMensagem());
    }

    // ============================================================
    // MENU PRODUTOS
    // ============================================================
    private static void menuProdutos() {

        Object[] opcoes = {
                "Cadastrar Produto",
                "Listar Produtos",
                "Buscar Produto por ID",
                "Buscar Produto por Nome",
                "Atualizar Estoque",
                "Excluir Produto",
                "Voltar"
        };

        String r;

        do {
            r = (String) JOptionPane.showInputDialog(
                    null,
                    "MENU PRODUTOS",
                    TITLE,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (r == null || r.equals("Voltar")) return;

            switch (r) {
                case "Cadastrar Produto": cadastrarProduto(); break;
                case "Listar Produtos": listarProdutos(); break;
                case "Buscar Produto por ID": buscarProdutoPorId(); break;
                case "Buscar Produto por Nome": buscarProdutoPorNome(); break;
                case "Atualizar Estoque": atualizarEstoque(); break;
                case "Excluir Produto": excluirProduto(); break;
            }

        } while (true);
    }

    // ============================================================
    // FUNÇÕES PRODUTOS
    // ============================================================
    private static void cadastrarProduto() {

        String nome = JOptionPane.showInputDialog("Nome do Produto:");
        if (nome == null || nome.trim().isEmpty()) return;

        String precoTxt = JOptionPane.showInputDialog("Preço:");
        if (precoTxt == null) return;

        BigDecimal preco;
        try {
            preco = new BigDecimal(precoTxt.replace(",", "."));
        } catch (Exception e) {
            mostrarErro("Preço inválido!");
            return;
        }

        String qtdTxt = JOptionPane.showInputDialog("Quantidade:");
        if (qtdTxt == null || !qtdTxt.matches("\\d+")) return;

        produtoService.incluirProduto(nome, preco, Integer.parseInt(qtdTxt));
        mostrarMensagem(produtoService.getUltimaMensagem());
    }

    private static void listarProdutos() {

        List<Produto> produtos = produtoService.listarProdutos();

        if (produtos.isEmpty()) {
            mostrarMensagem("Nenhum produto cadastrado.");
            return;
        }

        StringBuilder sb = new StringBuilder("Produtos:\n\n");

        for (Produto p : produtos) sb.append(p).append("\n");

        mostrarMensagem(sb.toString());
    }

    private static void buscarProdutoPorId() {

        String idTxt = JOptionPane.showInputDialog("ID:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        Produto p = produtoService.buscarProdutoPorId(Integer.parseInt(idTxt));

        if (p == null) mostrarErro("Produto não encontrado.");
        else mostrarMensagem(p.toString());
    }

    private static void buscarProdutoPorNome() {

        String nome = JOptionPane.showInputDialog("Nome:");
        if (nome == null) return;

        List<Produto> lista = produtoService.buscarProdutosPorNome(nome);

        if (lista.isEmpty()) mostrarMensagem("Nenhum produto encontrado.");
        else {
            StringBuilder sb = new StringBuilder("Resultados:\n\n");
            for (Produto p : lista) sb.append(p).append("\n");
            mostrarMensagem(sb.toString());
        }
    }

    private static void atualizarEstoque() {

        String idTxt = JOptionPane.showInputDialog("ID:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        String qtdTxt = JOptionPane.showInputDialog("Nova quantidade:");
        if (qtdTxt == null || !qtdTxt.matches("\\d+")) return;

        produtoService.atualizarEstoque(Integer.parseInt(idTxt), Integer.parseInt(qtdTxt));
        mostrarMensagem(produtoService.getUltimaMensagem());
    }

    private static void excluirProduto() {

        String idTxt = JOptionPane.showInputDialog("ID:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        produtoService.excluirProduto(Integer.parseInt(idTxt));
        mostrarMensagem(produtoService.getUltimaMensagem());
    }

    // ============================================================
    // MENU VENDAS
    // ============================================================
    private static void menuVendas() {

        Object[] opcoes = {
                "Registrar Venda",
                "Listar Vendas",
                "Buscar Venda por Cliente",
                "Buscar Venda por Produto",
                "Cancelar Venda",
                "Voltar"
        };

        String r;

        do {
            r = (String) JOptionPane.showInputDialog(
                    null,
                    "MENU VENDAS",
                    TITLE,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (r == null || r.equals("Voltar")) return;

            switch (r) {
                case "Registrar Venda": registrarVenda(); break;
                case "Listar Vendas": listarVendas(); break;
                case "Buscar Venda por Cliente": buscarVendaPorCliente(); break;
                case "Buscar Venda por Produto": buscarVendaPorProduto(); break;
                case "Cancelar Venda": cancelarVenda(); break;
            }

        } while (true);
    }

    // ============================================================
    // FUNÇÕES VENDAS
    // ============================================================
    private static void registrarVenda() {

        String idClienteTxt = JOptionPane.showInputDialog("ID do Cliente:");
        if (idClienteTxt == null || !idClienteTxt.matches("\\d+")) return;

        String idProdutoTxt = JOptionPane.showInputDialog("ID do Produto:");
        if (idProdutoTxt == null || !idProdutoTxt.matches("\\d+")) return;

        String qtdTxt = JOptionPane.showInputDialog("Quantidade:");
        if (qtdTxt == null || !qtdTxt.matches("\\d+")) return;

        vendasService.registrarVenda(
                Integer.parseInt(idClienteTxt),
                Integer.parseInt(idProdutoTxt),
                Integer.parseInt(qtdTxt)
        );

        mostrarMensagem(vendasService.getUltimaMensagem());
    }

    private static void listarVendas() {

        List<Venda> vendas = vendasService.listarVendas();

        if (vendas.isEmpty()) {
            mostrarMensagem("Nenhuma venda registrada.");
            return;
        }

        StringBuilder sb = new StringBuilder("Vendas:\n\n");
        for (Venda v : vendas) sb.append(v).append("\n");

        mostrarMensagem(sb.toString());
    }

    private static void buscarVendaPorCliente() {

        String idTxt = JOptionPane.showInputDialog("ID Cliente:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        List<Venda> vendas = vendasService.buscarVendasPorCliente(Integer.parseInt(idTxt));

        if (vendas.isEmpty()) mostrarMensagem("Nenhuma venda encontrada.");
        else {
            StringBuilder sb = new StringBuilder();
            for (Venda v : vendas) sb.append(v).append("\n");
            mostrarMensagem(sb.toString());
        }
    }

    private static void buscarVendaPorProduto() {

        String idTxt = JOptionPane.showInputDialog("ID Produto:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        List<Venda> vendas = vendasService.buscarVendasPorProduto(Integer.parseInt(idTxt));

        if (vendas.isEmpty()) mostrarMensagem("Nenhuma venda desse produto.");
        else {
            StringBuilder sb = new StringBuilder();
            for (Venda v : vendas) sb.append(v).append("\n");
            mostrarMensagem(sb.toString());
        }
    }

    private static void cancelarVenda() {

        String idTxt = JOptionPane.showInputDialog("ID da Venda:");
        if (idTxt == null || !idTxt.matches("\\d+")) return;

        vendasService.cancelarVenda(Integer.parseInt(idTxt));
        mostrarMensagem(vendasService.getUltimaMensagem());
    }

    // ============================================================
    // AUXILIARES
    // ============================================================
    private static void mostrarMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg, TITLE, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(null, msg, TITLE, JOptionPane.ERROR_MESSAGE);
    }
}