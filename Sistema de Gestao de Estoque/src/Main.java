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

    private static final String TITULO = "Sistema de Gestão de Estoque";

    public static void main(String[] args) {
        inicializarSistema();
        executarMenuPrincipal();
    }

    private static void inicializarSistema() {
        IClienteRepository<ClientePF> repoClientePF = new ClienteRepository<>();
        IClienteRepository<ClientePJ> repoClientePJ = new ClienteRepository<>();
        IProdutoRepository repoProduto = new ProdutoRepository();
        IVendasRepository repoVenda = new VendasRepository();

        clientePFService = new ClienteService<>(repoClientePF, repoVenda);

        clientePJService = new ClienteService<>(repoClientePJ, repoVenda);
        produtoService = new ProdutoService(repoProduto, repoVenda);
        vendasService = new VendaService(repoVenda, repoClientePF, repoClientePJ, repoProduto);
    }

    // ============== MENU PRINCIPAL ==============
    private static void executarMenuPrincipal() {
        while (true) {
            String[] opcoes = {"Gestão de Clientes", "Gestão de Produtos", "Gestão de Vendas", "Sair"};
            String escolha = exibirMenu("MENU PRINCIPAL", opcoes);

            if (escolha == null || escolha.equals("Sair")) break;
            if (escolha.equals("Gestão de Clientes")) menuClientes();
            if (escolha.equals("Gestão de Produtos")) menuProdutos();
            if (escolha.equals("Gestão de Vendas")) menuVendas();
        }
        mostrarMensagem("Obrigado por usar o sistema!");
    }

    // ============== MENU CLIENTES ==============
    private static void menuClientes() {
        while (true) {
            String[] opcoes = {
                    "Cadastrar Cliente PF",
                    "Cadastrar Cliente PJ",
                    "Listar Clientes PF",
                    "Listar Clientes PJ",
                    "Buscar Cliente PF",
                    "Buscar Cliente PJ",
                    "Excluir Cliente PF",
                    "Excluir Cliente PJ",
                    "Voltar"
            };

            String escolha = exibirMenu("MENU CLIENTES", opcoes);

            if (escolha == null || escolha.equals("Voltar")) break;

            switch (escolha) {
                case "Cadastrar Cliente PF" -> cadastrarClientePF();
                case "Cadastrar Cliente PJ" -> cadastrarClientePJ();
                case "Listar Clientes PF" -> listarClientesPF();
                case "Listar Clientes PJ" -> listarClientesPJ();
                case "Buscar Cliente PF" -> buscarClientePF();
                case "Buscar Cliente PJ" -> buscarClientePJ();
                case "Excluir Cliente PF" -> excluirClientePF();
                case "Excluir Cliente PJ" -> excluirClientePJ();
            }
        }
    }

    private static void cadastrarClientePF() {
        String nome;
        do {
            nome = JOptionPane.showInputDialog(null, "Nome do Cliente:", "Cadastro PF", JOptionPane.QUESTION_MESSAGE);
            if (nome == null) return;
            if (nome.trim().isEmpty()) {
                mostrarErro("Nome obrigatório!");
            }
        } while (nome.trim().isEmpty());

        String cpf;
        do {
            cpf = JOptionPane.showInputDialog(null, "CPF do Cliente:", "Cadastro PF", JOptionPane.QUESTION_MESSAGE);
            if (cpf == null) return;
            if (cpf.trim().isEmpty()) {
                mostrarErro("CPF obrigatório!");
            }
        } while (cpf.trim().isEmpty());

        ClientePF cliente = new ClientePF(0, nome.trim(), cpf.trim());
        clientePFService.cadastrarCliente(cliente);
        exibirMensagemService(clientePFService.getUltimaMensagem());
    }

    private static void cadastrarClientePJ() {
        String razaoSocial;
        do {
            razaoSocial = JOptionPane.showInputDialog(null, "Razão Social:", "Cadastro PJ", JOptionPane.QUESTION_MESSAGE);
            if (razaoSocial == null) return;
            if (razaoSocial.trim().isEmpty()) {
                mostrarErro("Razão Social obrigatória!");
            }
        } while (razaoSocial.trim().isEmpty());

        String cnpj;
        do {
            cnpj = JOptionPane.showInputDialog(null, "CNPJ:", "Cadastro PJ", JOptionPane.QUESTION_MESSAGE);
            if (cnpj == null) return;
            if (cnpj.trim().isEmpty()) {
                mostrarErro("CNPJ obrigatório!");
            }
        } while (cnpj.trim().isEmpty());

        ClientePJ cliente = new ClientePJ(0, razaoSocial.trim(), cnpj.trim());
        clientePJService.cadastrarCliente(cliente);
        exibirMensagemService(clientePJService.getUltimaMensagem());
    }

    private static void listarClientesPF() {
        List<ClientePF> clientes = clientePFService.listarTodos();
        if (clientes.isEmpty()) {
            mostrarMensagem("Nenhum cliente PF cadastrado.");
            return;
        }

        StringBuilder lista = new StringBuilder("Clientes PF:\n\n");
        clientes.forEach(c -> lista.append(c).append("\n"));
        mostrarMensagem(lista.toString());
    }

    private static void listarClientesPJ() {
        List<ClientePJ> clientes = clientePJService.listarTodos();
        if (clientes.isEmpty()) {
            mostrarMensagem("Nenhum cliente PJ cadastrado.");
            return;
        }

        StringBuilder lista = new StringBuilder("Clientes PJ:\n\n");
        clientes.forEach(c -> lista.append(c).append("\n"));
        mostrarMensagem(lista.toString());
    }

    private static void buscarClientePF() {
        Integer id = solicitarNumero("ID do Cliente PF:", "Buscar");
        if (id == null) return;

        ClientePF cliente = clientePFService.buscarPorId(id);
        if (cliente == null) {
            mostrarErro("Cliente PF não encontrado!");
            return;
        }

        mostrarMensagem(cliente.toString());
    }

    private static void buscarClientePJ() {
        Integer id = solicitarNumero("ID do Cliente PJ:", "Buscar");
        if (id == null) return;

        ClientePJ cliente = clientePJService.buscarPorId(id);
        if (cliente == null) {
            mostrarErro("Cliente PJ não encontrado!");
            return;
        }

        mostrarMensagem(cliente.toString());
    }

    private static void excluirClientePF() {
        Integer id = solicitarNumero("ID do Cliente PF a excluir:", "Excluir");
        if (id == null) return;

        clientePFService.excluirCliente(id);
        exibirMensagemService(clientePFService.getUltimaMensagem());
    }

    private static void excluirClientePJ() {
        Integer id = solicitarNumero("ID do Cliente PJ a excluir:", "Excluir");
        if (id == null) return;

        clientePJService.excluirCliente(id);
        exibirMensagemService(clientePJService.getUltimaMensagem());
    }

    // ============== MENU PRODUTOS ==============
    private static void menuProdutos() {
        while (true) {
            String[] opcoes = {
                    "Cadastrar Produto",
                    "Listar Produtos",
                    "Buscar por ID",
                    "Buscar por Nome",
                    "Atualizar Estoque",
                    "Estoque Baixo",
                    "Excluir Produto",
                    "Voltar"
            };

            String escolha = exibirMenu("MENU PRODUTOS", opcoes);

            if (escolha == null || escolha.equals("Voltar")) break;

            switch (escolha) {
                case "Cadastrar Produto" -> cadastrarProduto();
                case "Listar Produtos" -> listarProdutos();
                case "Buscar por ID" -> buscarProdutoId();
                case "Buscar por Nome" -> buscarProdutoNome();
                case "Atualizar Estoque" -> atualizarEstoque();
                case "Estoque Baixo" -> relatorioEstoqueBaixo();
                case "Excluir Produto" -> excluirProduto();
            }
        }
    }

    private static void cadastrarProduto() {
        String nome;
        do {
            nome = JOptionPane.showInputDialog(null, "Nome do Produto:", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (nome == null) return;
            if (nome.trim().isEmpty()) {
                mostrarErro("Nome obrigatório!");
            }
        } while (nome.trim().isEmpty());

        BigDecimal preco;
        do {
            String texto = JOptionPane.showInputDialog(null, "Preço (ex: 10.50):", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (texto == null) return;

            try {
                texto = texto.replace(",", ".").trim();
                preco = new BigDecimal(texto);

                if (preco.compareTo(BigDecimal.ZERO) <= 0) {
                    mostrarErro("Preço deve ser maior que zero!");
                    continue;
                }
                break;
            } catch (Exception e) {
                mostrarErro("Preço inválido! Use formato: 10.50");
            }
        } while (true);

        Integer quantidade;
        do {
            quantidade = solicitarNumero("Quantidade em estoque:", "Cadastro");
            if (quantidade == null) return;
            if (quantidade < 0) {
                mostrarErro("Quantidade não pode ser negativa!");
            }
        } while (quantidade < 0);

        produtoService.incluirProduto(nome.trim(), preco, quantidade);
        exibirMensagemService(produtoService.getUltimaMensagem());
    }

    private static void listarProdutos() {
        List<Produto> produtos = produtoService.listarProdutos();
        if (produtos.isEmpty()) {
            mostrarMensagem("Nenhum produto cadastrado.");
            return;
        }

        StringBuilder lista = new StringBuilder("Produtos:\n\n");
        produtos.forEach(p -> lista.append(p).append("\n"));
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

        mostrarMensagem(produto.toString());
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
        resultados.forEach(p -> lista.append(p).append("\n"));
        mostrarMensagem(lista.toString());
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

        produtoService.atualizarEstoque(id, novaQtd);
        exibirMensagemService(produtoService.getUltimaMensagem());
    }

    private static void relatorioEstoqueBaixo() {
        // 1. Solicita e valida limite
        Integer limite = solicitarNumero("Digite o limite máximo de estoque:", "Estoque Baixo");
        if (limite == null) return;

        if (limite < 0) {
            mostrarErro("O limite não pode ser negativo!");
            return;
        }

        // 2. Busca produtos
        List<Produto> produtos = produtoService.estoqueBaixo(limite);

        if (produtos == null) {
            mostrarErro("Erro ao buscar produtos!");
            return;
        }

        // 3. Exibe resultados
        if (produtos.isEmpty()) {
            mostrarMensagem(" Nenhum produto com estoque ≤ " + limite +
                    "\n\nEstoque está em níveis adequados.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("PRODUTOS COM ESTOQUE BAIXO (").append(produtos.size()).append(")\n");
            sb.append("Limite: ").append(limite).append(" unidades\n\n");

            for (Produto p : produtos) {
                sb.append("• ").append(p.getNome())
                        .append(" | Estoque: ").append(p.getQtdEstoque())
                        .append(" | ID: ").append(p.getId()).append("\n");
            }

            sb.append("\nAtenção: Estes produtos necessitam de reposição!");
            mostrarMensagem(sb.toString());
        }
    }

    private static void excluirProduto() {
        Integer id = solicitarNumero("ID do Produto a excluir:", "Excluir");
        if (id == null) return;

        boolean sucesso = produtoService.excluirProduto(id);

        if (sucesso) {
            exibirMensagemService(produtoService.getUltimaMensagem());
        } else {
            Produto produto = produtoService.buscarProdutoPorId(id);
            if (produto == null) {
                mostrarErro("Produto não encontrado!");
            } else {
                mostrarErro("Produto possui vendas - não pode ser excluído!");
            }
        }
    }

    // ============== MENU VENDAS ==============
    private static void menuVendas() {
        while (true) {
            String[] opcoes = {
                    "Registrar Venda",
                    "Listar Vendas",
                    "Buscar por Cliente",
                    "Buscar por Produto",
                    "Cancelar Venda",
                    "Voltar"
            };

            String escolha = exibirMenu("MENU VENDAS", opcoes);

            if (escolha == null || escolha.equals("Voltar")) break;

            switch (escolha) {
                case "Registrar Venda" -> registrarVenda();
                case "Listar Vendas" -> listarVendas();
                case "Buscar por Cliente" -> buscarVendaCliente();
                case "Buscar por Produto" -> buscarVendaProduto();
                case "Cancelar Venda" -> cancelarVenda();
            }
        }
    }

    private static void registrarVenda() {
        Integer idCliente = solicitarNumero("ID do Cliente:", "Venda");
        if (idCliente == null) return;

        Integer idProduto = solicitarNumero("ID do Produto:", "Venda");
        if (idProduto == null) return;

        Integer quantidade;
        do {
            quantidade = solicitarNumero("Quantidade:", "Venda");
            if (quantidade == null) return;
            if (quantidade <= 0) mostrarErro("Quantidade deve ser maior que zero!");
        } while (quantidade <= 0);

        vendasService.registrarVenda(idCliente, idProduto, quantidade);
        exibirMensagemService(vendasService.getUltimaMensagem());
    }

    private static void listarVendas() {
        List<Venda> vendas = vendasService.listarVendas();
        if (vendas.isEmpty()) {
            mostrarMensagem("Nenhuma venda registrada.");
            return;
        }

        StringBuilder lista = new StringBuilder("Vendas:\n\n");
        vendas.forEach(v -> lista.append(v).append("\n"));
        mostrarMensagem(lista.toString());
    }

    private static void buscarVendaCliente() {
        Integer idCliente = solicitarNumero("ID do Cliente:", "Buscar Vendas");
        if (idCliente == null) return;

        List<Venda> vendas = vendasService.buscarVendasPorCliente(idCliente);
        if (vendas.isEmpty()) {
            mostrarMensagem("Nenhuma venda encontrada para este cliente.");
            return;
        }

        StringBuilder lista = new StringBuilder("Vendas do Cliente:\n\n");
        vendas.forEach(v -> lista.append(v).append("\n"));
        mostrarMensagem(lista.toString());
    }

    private static void buscarVendaProduto() {
        Integer idProduto = solicitarNumero("ID do Produto:", "Buscar Vendas");
        if (idProduto == null) return;

        List<Venda> vendas = vendasService.buscarVendasPorProduto(idProduto);
        if (vendas.isEmpty()) {
            mostrarMensagem("Nenhuma venda encontrada para este produto.");
            return;
        }

        StringBuilder lista = new StringBuilder("Vendas do Produto:\n\n");
        vendas.forEach(v -> lista.append(v).append("\n"));
        mostrarMensagem(lista.toString());
    }

    private static void cancelarVenda() {
        Integer idVenda = solicitarNumero("ID da Venda a cancelar:", "Cancelar");
        if (idVenda == null) return;

        vendasService.cancelarVenda(idVenda);
        exibirMensagemService(vendasService.getUltimaMensagem());
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

    private static void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(null, "❌ " + mensagem, TITULO, JOptionPane.ERROR_MESSAGE);
    }
}