// Importações necessárias para o funcionamento do sistema
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

import javax.swing.JOptionPane;  // Biblioteca para interface gráfica (caixas de diálogo)
import java.math.BigDecimal;      // Para precisão decimal em valores monetários
import java.util.List;           // Para trabalhar com listas de objetos

// Classe principal que controla todo o sistema
public class Main {
    // Serviços estáticos que serão compartilhados por todo o sistema
    private static ClienteService<ClientePF> clientePFService;    // Serviço para clientes Pessoa Física
    private static ClienteService<ClientePJ> clientePJService;    // Serviço para clientes Pessoa Jurídica
    private static ProdutoService produtoService;                 // Serviço para produtos
    private static VendaService vendasService;                    // Serviço para vendas

    // Título constante usado em todas as caixas de diálogo
    private static final String TITULO = "Sistema de Gestão de Estoque";

    // Método principal - ponto de entrada da aplicação
    public static void main(String[] args) {
        inicializarSistema();      // Configura todas as dependências do sistema
        executarMenuPrincipal();   // Inicia o loop do menu principal
    }

    // ================================
    // INICIALIZAÇÃO DO SISTEMA
    // ================================
    // Configura todas as dependências (repositórios e serviços)
    private static void inicializarSistema() {
        // Criação dos repositórios (camada de acesso a dados)
        IClienteRepository<ClientePF> repoClientePF = new ClienteRepository<>();
        IClienteRepository<ClientePJ> repoClientePJ = new ClienteRepository<>();
        IProdutoRepository repoProduto = new ProdutoRepository();
        IVendasRepository repoVenda = new VendasRepository();

        // Criação dos serviços (camada de lógica de negócio)
        // Cada serviço recebe os repositórios necessários como dependências
        clientePFService = new ClienteService<>(repoClientePF, repoVenda);
        clientePJService = new ClienteService<>(repoClientePJ, repoVenda);
        produtoService = new ProdutoService(repoProduto, repoVenda);
        vendasService = new VendaService(repoVenda, repoClientePF, repoClientePJ, repoProduto);
    }

    // ================================
    // MENU PRINCIPAL
    // ================================
    // Controla o menu principal do sistema com loop infinito
    private static void executarMenuPrincipal() {
        while (true) {
            // Define as opções do menu principal
            String[] opcoes = {"Gestão de Clientes", "Gestão de Produtos", "Gestão de Vendas", "Sair"};

            // Exibe o menu e captura a escolha do usuário
            String escolha = exibirMenu("MENU PRINCIPAL", opcoes);

            // Verifica se o usuário quer sair ou fechou a janela
            if (escolha == null || escolha.equals("Sair")) break;

            // Encaminha para o menu correspondente baseado na escolha
            if (escolha.equals("Gestão de Clientes")) menuClientes();
            if (escolha.equals("Gestão de Produtos")) menuProdutos();
            if (escolha.equals("Gestão de Vendas")) menuVendas();
        }

        // Mensagem de despedida ao sair do sistema
        mostrarMensagem("Obrigado por usar o sistema!");
    }

    // ================================
    // MENU DE CLIENTES
    // ================================
    private static void menuClientes() {
        while (true) {
            // Define as opções do menu de clientes
            String[] opcoes = {
                    "Cadastrar Cliente PF",     // Cadastro Pessoa Física
                    "Cadastrar Cliente PJ",     // Cadastro Pessoa Jurídica
                    "Listar Clientes PF",       // Listar PF
                    "Listar Clientes PJ",       // Listar PJ
                    "Buscar Cliente PF",        // Buscar PF por ID
                    "Buscar Cliente PJ",        // Buscar PJ por ID
                    "Excluir Cliente PF",       // Excluir PF
                    "Excluir Cliente PJ",       // Excluir PJ
                    "Voltar"                    // Retornar ao menu principal
            };

            // Exibe o menu e captura a escolha
            String escolha = exibirMenu("MENU CLIENTES", opcoes);

            // Verifica se o usuário quer voltar ou fechou a janela
            if (escolha == null || escolha.equals("Voltar")) break;

            // Switch para tratar cada opção do menu
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

    // ================================
    // CADASTRO DE CLIENTE PESSOA FÍSICA
    // ================================
    private static void cadastrarClientePF() {
        String nome;
        // Loop para garantir que o nome não seja vazio
        do {
            nome = JOptionPane.showInputDialog(null, "Nome do Cliente:", "Cadastro PF", JOptionPane.QUESTION_MESSAGE);
            if (nome == null) return; // Usuário cancelou a operação
            if (nome.trim().isEmpty()) {
                mostrarErro("Nome obrigatório!");
            }
        } while (nome.trim().isEmpty());

        String cpf;
        // Loop para garantir que o CPF não seja vazio
        do {
            cpf = JOptionPane.showInputDialog(null, "CPF do Cliente:", "Cadastro PF", JOptionPane.QUESTION_MESSAGE);
            if (cpf == null) return; // Usuário cancelou a operação
            if (cpf.trim().isEmpty()) {
                mostrarErro("CPF obrigatório!");
            }
        } while (cpf.trim().isEmpty());

        // Cria o objeto ClientePF (ID 0 será substituído pelo serviço)
        ClientePF cliente = new ClientePF(0, nome.trim(), cpf.trim());

        // Chama o serviço para cadastrar o cliente
        clientePFService.cadastrarCliente(cliente);

        // Exibe a mensagem retornada pelo serviço
        exibirMensagemService(clientePFService.getUltimaMensagem());
    }

    // ================================
    // CADASTRO DE CLIENTE PESSOA JURÍDICA
    // ================================
    private static void cadastrarClientePJ() {
        String razaoSocial;
        // Loop para garantir que a razão social não seja vazia
        do {
            razaoSocial = JOptionPane.showInputDialog(null, "Razão Social:", "Cadastro PJ", JOptionPane.QUESTION_MESSAGE);
            if (razaoSocial == null) return; // Usuário cancelou a operação
            if (razaoSocial.trim().isEmpty()) {
                mostrarErro("Razão Social obrigatória!");
            }
        } while (razaoSocial.trim().isEmpty());

        String cnpj;
        // Loop para garantir que o CNPJ não seja vazio
        do {
            cnpj = JOptionPane.showInputDialog(null, "CNPJ:", "Cadastro PJ", JOptionPane.QUESTION_MESSAGE);
            if (cnpj == null) return; // Usuário cancelou a operação
            if (cnpj.trim().isEmpty()) {
                mostrarErro("CNPJ obrigatório!");
            }
        } while (cnpj.trim().isEmpty());

        // Cria o objeto ClientePJ (ID 0 será substituído pelo serviço)
        ClientePJ cliente = new ClientePJ(0, razaoSocial.trim(), cnpj.trim());

        // Chama o serviço para cadastrar o cliente
        clientePJService.cadastrarCliente(cliente);

        // Exibe a mensagem retornada pelo serviço
        exibirMensagemService(clientePJService.getUltimaMensagem());
    }

    // ================================
    // LISTAR CLIENTES PESSOA FÍSICA
    // ================================
    private static void listarClientesPF() {
        // Obtém a lista de todos os clientes PF ativos
        List<ClientePF> clientes = clientePFService.listarTodos();

        // Verifica se há clientes para mostrar
        if (clientes.isEmpty()) {
            mostrarMensagem("Nenhum cliente PF cadastrado.");
            return;
        }

        // Constrói uma string formatada com todos os clientes
        StringBuilder lista = new StringBuilder("Clientes PF:\n\n");
        clientes.forEach(c -> lista.append(c).append("\n"));

        // Exibe a lista em uma caixa de diálogo
        mostrarMensagem(lista.toString());
    }

    // ================================
    // LISTAR CLIENTES PESSOA JURÍDICA
    // ================================
    private static void listarClientesPJ() {
        // Obtém a lista de todos os clientes PJ ativos
        List<ClientePJ> clientes = clientePJService.listarTodos();

        // Verifica se há clientes para mostrar
        if (clientes.isEmpty()) {
            mostrarMensagem("Nenhum cliente PJ cadastrado.");
            return;
        }

        // Constrói uma string formatada com todos os clientes
        StringBuilder lista = new StringBuilder("Clientes PJ:\n\n");
        clientes.forEach(c -> lista.append(c).append("\n"));

        // Exibe a lista em uma caixa de diálogo
        mostrarMensagem(lista.toString());
    }

    // ================================
    // BUSCAR CLIENTE PF POR ID
    // ================================
    private static void buscarClientePF() {
        // Solicita o ID do cliente
        Integer id = solicitarNumero("ID do Cliente PF:", "Buscar");
        if (id == null) return; // Usuário cancelou a operação

        // Busca o cliente pelo ID usando o serviço
        ClientePF cliente = clientePFService.buscarPorId(id);

        // Verifica se o cliente foi encontrado
        if (cliente == null) {
            mostrarErro("Cliente PF não encontrado!");
            return;
        }

        // Exibe os dados do cliente
        mostrarMensagem(cliente.toString());
    }

    // ================================
    // BUSCAR CLIENTE PJ POR ID
    // ================================
    private static void buscarClientePJ() {
        // Solicita o ID do cliente
        Integer id = solicitarNumero("ID do Cliente PJ:", "Buscar");
        if (id == null) return; // Usuário cancelou a operação

        // Busca o cliente pelo ID usando o serviço
        ClientePJ cliente = clientePJService.buscarPorId(id);

        // Verifica se o cliente foi encontrado
        if (cliente == null) {
            mostrarErro("Cliente PJ não encontrado!");
            return;
        }

        // Exibe os dados do cliente
        mostrarMensagem(cliente.toString());
    }

    // ================================
    // EXCLUIR CLIENTE PF
    // ================================
    private static void excluirClientePF() {
        // Solicita o ID do cliente a excluir
        Integer id = solicitarNumero("ID do Cliente PF a excluir:", "Excluir");
        if (id == null) return; // Usuário cancelou a operação

        // Chama o serviço para excluir o cliente
        clientePFService.excluirCliente(id);

        // Exibe a mensagem retornada pelo serviço
        exibirMensagemService(clientePFService.getUltimaMensagem());
    }

    // ================================
    // EXCLUIR CLIENTE PJ
    // ================================
    private static void excluirClientePJ() {
        // Solicita o ID do cliente a excluir
        Integer id = solicitarNumero("ID do Cliente PJ a excluir:", "Excluir");
        if (id == null) return; // Usuário cancelou a operação

        // Chama o serviço para excluir o cliente
        clientePJService.excluirCliente(id);

        // Exibe a mensagem retornada pelo serviço
        exibirMensagemService(clientePJService.getUltimaMensagem());
    }

    // ================================
    // MENU DE PRODUTOS
    // ================================
    private static void menuProdutos() {
        while (true) {
            // Define as opções do menu de produtos
            String[] opcoes = {
                    "Cadastrar Produto",        // Adicionar novo produto
                    "Listar Produtos",          // Listar todos produtos
                    "Buscar por ID",            // Buscar produto por ID
                    "Buscar por Nome",          // Buscar produto por nome (parcial)
                    "Atualizar Estoque",        // Alterar quantidade em estoque
                    "Estoque Baixo",            // Relatório de produtos com estoque baixo
                    "Excluir Produto",          // Excluir produto
                    "Voltar"                    // Retornar ao menu principal
            };

            // Exibe o menu e captura a escolha
            String escolha = exibirMenu("MENU PRODUTOS", opcoes);

            // Verifica se o usuário quer voltar ou fechou a janela
            if (escolha == null || escolha.equals("Voltar")) break;

            // Switch para tratar cada opção do menu
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

    // ================================
    // CADASTRAR PRODUTO
    // ================================
    private static void cadastrarProduto() {
        String nome;
        // Loop para garantir que o nome não seja vazio
        do {
            nome = JOptionPane.showInputDialog(null, "Nome do Produto:", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (nome == null) return; // Usuário cancelou a operação
            if (nome.trim().isEmpty()) {
                mostrarErro("Nome obrigatório!");
            }
        } while (nome.trim().isEmpty());

        BigDecimal preco;
        // Loop para garantir que o preço seja válido
        do {
            String texto = JOptionPane.showInputDialog(null, "Preço (ex: 10.50):", "Cadastro", JOptionPane.QUESTION_MESSAGE);
            if (texto == null) return; // Usuário cancelou a operação

            try {
                // Substitui vírgula por ponto para aceitar formato brasileiro
                texto = texto.replace(",", ".").trim();
                preco = new BigDecimal(texto);

                // Valida se o preço é maior que zero
                if (preco.compareTo(BigDecimal.ZERO) <= 0) {
                    mostrarErro("Preço deve ser maior que zero!");
                    continue; // Continua no loop
                }
                break; // Preço válido, sai do loop
            } catch (Exception e) {
                mostrarErro("Preço inválido! Use formato: 10.50");
            }
        } while (true);

        Integer quantidade;
        // Loop para garantir que a quantidade seja válida
        do {
            quantidade = solicitarNumero("Quantidade em estoque:", "Cadastro");
            if (quantidade == null) return; // Usuário cancelou a operação
            if (quantidade < 0) {
                mostrarErro("Quantidade não pode ser negativa!");
            }
        } while (quantidade < 0);

        // Chama o serviço para incluir o produto
        produtoService.incluirProduto(nome.trim(), preco, quantidade);

        // Exibe a mensagem retornada pelo serviço
        exibirMensagemService(produtoService.getUltimaMensagem());
    }

    // ================================
    // LISTAR TODOS OS PRODUTOS
    // ================================
    private static void listarProdutos() {
        // Obtém a lista de todos os produtos ativos
        List<Produto> produtos = produtoService.listarProdutos();

        // Verifica se há produtos para mostrar
        if (produtos.isEmpty()) {
            mostrarMensagem("Nenhum produto cadastrado.");
            return;
        }

        // Constrói uma string formatada com todos os produtos
        StringBuilder lista = new StringBuilder("Produtos:\n\n");
        produtos.forEach(p -> lista.append(p).append("\n"));

        // Exibe a lista em uma caixa de diálogo
        mostrarMensagem(lista.toString());
    }

    // ================================
    // BUSCAR PRODUTO POR ID
    // ================================
    private static void buscarProdutoId() {
        // Solicita o ID do produto
        Integer id = solicitarNumero("ID do Produto:", "Buscar");
        if (id == null) return; // Usuário cancelou a operação

        // Busca o produto pelo ID usando o serviço
        Produto produto = produtoService.buscarProdutoPorId(id);

        // Verifica se o produto foi encontrado
        if (produto == null) {
            mostrarErro("Produto não encontrado!");
            return;
        }

        // Exibe os dados do produto
        mostrarMensagem(produto.toString());
    }

    // ================================
    // BUSCAR PRODUTO POR NOME (PARCIAL)
    // ================================
    private static void buscarProdutoNome() {
        // Solicita parte do nome do produto
        String texto = JOptionPane.showInputDialog(null, "Buscar por nome:", "Buscar", JOptionPane.QUESTION_MESSAGE);
        if (texto == null || texto.trim().isEmpty()) return; // Usuário cancelou ou deixou vazio

        // Busca produtos cujo nome contém o texto informado
        List<Produto> resultados = produtoService.buscarProdutosPorNome(texto.trim());

        // Verifica se encontrou resultados
        if (resultados.isEmpty()) {
            mostrarMensagem("Nenhum produto encontrado.");
            return;
        }

        // Constrói uma string formatada com os resultados
        StringBuilder lista = new StringBuilder("Resultados:\n\n");
        resultados.forEach(p -> lista.append(p).append("\n"));

        // Exibe os resultados em uma caixa de diálogo
        mostrarMensagem(lista.toString());
    }

    // ================================
    // ATUALIZAR ESTOQUE
    // ================================
    private static void atualizarEstoque() {
        // Solicita o ID do produto
        Integer id = solicitarNumero("ID do Produto:", "Atualizar");
        if (id == null) return; // Usuário cancelou a operação

        // Busca o produto para mostrar informações atuais
        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto == null) {
            mostrarErro("Produto não encontrado!");
            return;
        }

        // Mostra as informações atuais do produto
        mostrarMensagem("Produto: " + produto.getNome() + "\nEstoque atual: " + produto.getQtdEstoque());

        Integer novaQtd;
        // Loop para garantir que a nova quantidade seja válida
        do {
            novaQtd = solicitarNumero("Nova quantidade:", "Atualizar");
            if (novaQtd == null) return; // Usuário cancelou a operação
            if (novaQtd < 0) mostrarErro("Não pode ser negativo!");
        } while (novaQtd < 0);

        // Chama o serviço para atualizar o estoque
        produtoService.atualizarEstoque(id, novaQtd);

        // Exibe a mensagem retornada pelo serviço
        exibirMensagemService(produtoService.getUltimaMensagem());
    }

    // ================================
    // RELATÓRIO DE ESTOQUE BAIXO
    // ================================
    private static void relatorioEstoqueBaixo() {
        // 1. Solicita e valida limite
        Integer limite = solicitarNumero("Digite o limite máximo de estoque:", "Estoque Baixo");
        if (limite == null) return; // Usuário cancelou a operação

        // Valida se o limite não é negativo
        if (limite < 0) {
            mostrarErro("O limite não pode ser negativo!");
            return;
        }

        // 2. Busca produtos com estoque abaixo do limite
        List<Produto> produtos = produtoService.estoqueBaixo(limite);

        // Verifica se houve erro na busca
        if (produtos == null) {
            mostrarErro("Erro ao buscar produtos!");
            return;
        }

        // 3. Exibe resultados
        if (produtos.isEmpty()) {
            // Se não há produtos com estoque baixo
            mostrarMensagem(" Nenhum produto com estoque ≤ " + limite +
                    "\n\nEstoque está em níveis adequados.");
        } else {
            // Constrói relatório detalhado
            StringBuilder sb = new StringBuilder();
            sb.append("PRODUTOS COM ESTOQUE BAIXO (").append(produtos.size()).append(")\n");
            sb.append("Limite: ").append(limite).append(" unidades\n\n");

            // Adiciona cada produto ao relatório
            for (Produto p : produtos) {
                sb.append("• ").append(p.getNome())
                        .append(" | Estoque: ").append(p.getQtdEstoque())
                        .append(" | ID: ").append(p.getId()).append("\n");
            }

            // Adiciona mensagem de alerta
            sb.append("\nAtenção: Estes produtos necessitam de reposição!");

            // Exibe o relatório
            mostrarMensagem(sb.toString());
        }
    }

    // ================================
    // EXCLUIR PRODUTO
    // ================================
    private static void excluirProduto() {
        // Solicita o ID do produto a excluir
        Integer id = solicitarNumero("ID do Produto a excluir:", "Excluir");
        if (id == null) return; // Usuário cancelou a operação

        // Tenta excluir o produto
        boolean sucesso = produtoService.excluirProduto(id);

        if (sucesso) {
            // Se excluiu com sucesso, mostra mensagem do serviço
            exibirMensagemService(produtoService.getUltimaMensagem());
        } else {
            // Se falhou, verifica o motivo
            Produto produto = produtoService.buscarProdutoPorId(id);
            if (produto == null) {
                mostrarErro("Produto não encontrado!");
            } else {
                mostrarErro("Produto possui vendas - não pode ser excluído!");
            }
        }
    }

    // ================================
    // MENU DE VENDAS
    // ================================
    private static void menuVendas() {
        while (true) {
            // Define as opções do menu de vendas
            String[] opcoes = {
                    "Registrar Venda",          // Cadastrar nova venda
                    "Listar Vendas",            // Listar todas as vendas
                    "Buscar por Cliente",       // Buscar vendas de um cliente
                    "Buscar por Produto",       // Buscar vendas de um produto
                    "Cancelar Venda",           // Cancelar uma venda
                    "Voltar"                    // Retornar ao menu principal
            };

            // Exibe o menu e captura a escolha
            String escolha = exibirMenu("MENU VENDAS", opcoes);

            // Verifica se o usuário quer voltar ou fechou a janela
            if (escolha == null || escolha.equals("Voltar")) break;

            // Switch para tratar cada opção do menu
            switch (escolha) {
                case "Registrar Venda" -> registrarVenda();
                case "Listar Vendas" -> listarVendas();
                case "Buscar por Cliente" -> buscarVendaCliente();
                case "Buscar por Produto" -> buscarVendaProduto();
                case "Cancelar Venda" -> cancelarVenda();
            }
        }
    }

    // ================================
    // REGISTRAR VENDA
    // ================================
    private static void registrarVenda() {
        // Solicita ID do cliente
        Integer idCliente = solicitarNumero("ID do Cliente:", "Venda");
        if (idCliente == null) return; // Usuário cancelou a operação

        // Solicita ID do produto
        Integer idProduto = solicitarNumero("ID do Produto:", "Venda");
        if (idProduto == null) return; // Usuário cancelou a operação

        Integer quantidade;
        // Loop para garantir que a quantidade seja válida
        do {
            quantidade = solicitarNumero("Quantidade:", "Venda");
            if (quantidade == null) return; // Usuário cancelou a operação
            if (quantidade <= 0) mostrarErro("Quantidade deve ser maior que zero!");
        } while (quantidade <= 0);

        // Chama o serviço para registrar a venda
        vendasService.registrarVenda(idCliente, idProduto, quantidade);

        // Exibe a mensagem retornada pelo serviço
        exibirMensagemService(vendasService.getUltimaMensagem());
    }

    // ================================
    // LISTAR VENDAS
    // ================================
    private static void listarVendas() {
        // Obtém a lista de todas as vendas ativas
        List<Venda> vendas = vendasService.listarVendas();

        // Verifica se há vendas para mostrar
        if (vendas.isEmpty()) {
            mostrarMensagem("Nenhuma venda registrada.");
            return;
        }

        // Constrói uma string formatada com todas as vendas
        StringBuilder lista = new StringBuilder("Vendas:\n\n");
        vendas.forEach(v -> lista.append(v).append("\n"));

        // Exibe a lista em uma caixa de diálogo
        mostrarMensagem(lista.toString());
    }

    // ================================
    // BUSCAR VENDAS POR CLIENTE
    // ================================
    private static void buscarVendaCliente() {
        // Solicita ID do cliente
        Integer idCliente = solicitarNumero("ID do Cliente:", "Buscar Vendas");
        if (idCliente == null) return; // Usuário cancelou a operação

        // Busca vendas do cliente usando o serviço
        List<Venda> vendas = vendasService.buscarVendasPorCliente(idCliente);

        // Verifica se encontrou vendas
        if (vendas.isEmpty()) {
            mostrarMensagem("Nenhuma venda encontrada para este cliente.");
            return;
        }

        // Constrói uma string formatada com as vendas encontradas
        StringBuilder lista = new StringBuilder("Vendas do Cliente:\n\n");
        vendas.forEach(v -> lista.append(v).append("\n"));

        // Exibe as vendas em uma caixa de diálogo
        mostrarMensagem(lista.toString());
    }

    // ================================
    // BUSCAR VENDAS POR PRODUTO
    // ================================
    private static void buscarVendaProduto() {
        // Solicita ID do produto
        Integer idProduto = solicitarNumero("ID do Produto:", "Buscar Vendas");
        if (idProduto == null) return; // Usuário cancelou a operação

        // Busca vendas do produto usando o serviço
        List<Venda> vendas = vendasService.buscarVendasPorProduto(idProduto);

        // Verifica se encontrou vendas
        if (vendas.isEmpty()) {
            mostrarMensagem("Nenhuma venda encontrada para este produto.");
            return;
        }

        // Constrói uma string formatada com as vendas encontradas
        StringBuilder lista = new StringBuilder("Vendas do Produto:\n\n");
        vendas.forEach(v -> lista.append(v).append("\n"));

        // Exibe as vendas em uma caixa de diálogo
        mostrarMensagem(lista.toString());
    }

    // ================================
    // CANCELAR VENDA
    // ================================
    private static void cancelarVenda() {
        // Solicita ID da venda a cancelar
        Integer idVenda = solicitarNumero("ID da Venda a cancelar:", "Cancelar");
        if (idVenda == null) return; // Usuário cancelou a operação

        // Chama o serviço para cancelar a venda
        vendasService.cancelarVenda(idVenda);

        // Exibe a mensagem retornada pelo serviço
        exibirMensagemService(vendasService.getUltimaMensagem());
    }

    // ================================
    // MÉTODOS AUXILIARES (UTILITÁRIOS)
    // ================================

    // Exibe um menu suspenso (combo box) com opções
    private static String exibirMenu(String titulo, String[] opcoes) {
        return (String) JOptionPane.showInputDialog(null, titulo, TITULO,
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
    }

    // Solicita um número inteiro do usuário com tratamento de erro
    private static Integer solicitarNumero(String mensagem, String titulo) {
        String texto = JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
        if (texto == null) return null; // Usuário cancelou a operação

        try {
            // Tenta converter o texto para inteiro
            return Integer.parseInt(texto.trim());
        } catch (Exception e) {
            // Se falhar, mostra erro e tenta novamente (recursão)
            mostrarErro("Número inválido!");
            return solicitarNumero(mensagem, titulo);
        }
    }

    // Exibe mensagem retornada por um serviço (se não for nula)
    private static void exibirMensagemService(String mensagem) {
        if (mensagem != null) mostrarMensagem(mensagem);
    }

    // Exibe uma mensagem informativa (caixa de diálogo com ícone de informação)
    private static void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    // Exibe uma mensagem de erro (caixa de diálogo com ícone de erro)
    private static void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(null, "❌ " + mensagem, TITULO, JOptionPane.ERROR_MESSAGE);
    }
}