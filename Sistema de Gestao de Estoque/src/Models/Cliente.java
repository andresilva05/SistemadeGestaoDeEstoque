package Models;

public abstract class Cliente {
    // Atributos básicos de um cliente
    private int id;                // Identificador único do cliente
    private String nome;           // Nome do cliente
    private String documento;      // CPF ou CNPJ (sem formatação)
    private boolean ativo;         // Status (ativo/inativo) do cliente

    // Construtor da classe Cliente
    public Cliente(int id, String nome, String documento) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.ativo = true; // Por padrão, cliente é criado como ativo
    }

    // ======= GETTERS E SETTERS =======

    // Retorna o ID do cliente
    public int getId() {
        return id;
    }

    // Define o ID do cliente
    public void setId(int id) {
        this.id = id;
    }

    // Retorna o nome do cliente
    public String getNome() {
        return nome;
    }

    // Define o nome do cliente
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Retorna o documento (CPF/CNPJ) sem formatação
    public String getDocumento() {
        return documento;
    }

    // Retorna se o cliente está ativo
    public boolean isAtivo() {
        return ativo;
    }

    // Define o status do cliente (ativo/inativo)
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // ======= MÉTODO ABSTRATO =======

    // Método abstrato que deve ser implementado pelas subclasses
    // para validar o documento específico (CPF ou CNPJ)
    public abstract boolean validarDocumento();

    // ======= MÉTODO DE FORMATAÇÃO PARA EXIBIÇÃO =======

    @Override
    public String toString() {
        // Determina o tipo do cliente (PF ou PJ) usando instanceof
        String tipo = this instanceof ClientePF ? "PF" : "PJ";

        // Formata o documento de acordo com o tipo do cliente
        String documentoFormatado = this instanceof ClientePF
                ? formatarCPF(getDocumento())  // Formata como CPF
                : formatarCNPJ(getDocumento()); // Formata como CNPJ

        // Retorna uma string formatada com todas as informações do cliente
        return String.format(
                "ID: %d | Tipo: %s | Nome: %s | Documento: %s | Status: %s",
                id, tipo, nome, documentoFormatado, ativo ? "ATIVO" : "INATIVO"
        );
    }

    // ======= MÉTODOS AUXILIARES PRIVADOS PARA FORMATAÇÃO =======

    // Formata um CPF (11 dígitos) no padrão 000.000.000-00
    private String formatarCPF(String cpf) {
        // Verifica se o CPF é válido para formatação
        if (cpf == null || cpf.length() != 11) return cpf;
        // Aplica a máscara de formatação
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    // Formata um CNPJ (14 dígitos) no padrão 00.000.000/0000-00
    private String formatarCNPJ(String cnpj) {
        // Verifica se o CNPJ é válido para formatação
        if (cnpj == null || cnpj.length() != 14) return cnpj;
        // Aplica a máscara de formatação
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}