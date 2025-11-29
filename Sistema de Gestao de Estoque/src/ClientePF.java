public class ClientePF {
    private int id;
    private String nome;
    private String cpf;
    boolean ativo ;

    public ClientePF() {

    }

    public ClientePF(int id, String nome, String cpf, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.ativo = ativo;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String toString() {

        return "ID: " + id +
                " | Nome: " + nome +
                " | CPF: " + cpf;
    }
}
