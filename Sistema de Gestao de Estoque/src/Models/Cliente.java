package Models;

public abstract class Cliente {
    private int id;
    private String nome;
    private String documento;
    private boolean ativo;

    public Cliente(int id, String nome, String documento) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.ativo = true;
    }

    // Getters e Setters
    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDocumento() { return documento; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    // Método abstrato para validação
    public abstract boolean validarDocumento();

    @Override
    public String toString() {
        String tipo = this instanceof ClientePF ? "PF" : "PJ";
        String documentoFormatado = this instanceof ClientePF
                ? formatarCPF(getDocumento())
                : formatarCNPJ(getDocumento());

        return String.format(
                "ID: %d | Tipo: %s | Nome: %s | Documento: %s | Status: %s",
                id, tipo, nome, documentoFormatado, ativo ? "ATIVO" : "INATIVO"
        );
    }

    // Métodos auxiliares para formatação
    private String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) return cnpj;
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}