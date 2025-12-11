package Models;

public abstract class Cliente {
    private int id;
    private String nome;
    private String documento; // CPF ou CNPJ
    private boolean ativo = true;

    public Cliente(int id, String nome, String documento) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
    }

    public abstract boolean validarDocumento();

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public String getDocumento() { return documento; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", nome='" + nome + "', documento='" + documento + "'}";
    }
}