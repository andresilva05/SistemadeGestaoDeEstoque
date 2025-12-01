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
}