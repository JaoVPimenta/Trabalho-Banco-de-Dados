package Estudo.model;

public class Integrante {
    // não possui PK (id)
    private String cargo;
    private Integer pessoa_id;
    private Integer projeto_id;

    public Integrante() {}

    public Integrante(String cargo, Integer pessoa_id, Integer projeto_id) {
        this.cargo = cargo;
        this.pessoa_id = pessoa_id;
        this.projeto_id = projeto_id;
    }

    public String getCargo() {
        return cargo;
    }

    public Integer getPessoa_id() {
        return pessoa_id;
    }

    public Integer getProjeto_id() {
        return projeto_id;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setPessoa_id(Integer pessoa_id) {
        this.pessoa_id = pessoa_id;
    }

    public void setProjeto_id(Integer projeto_id) {
        this.projeto_id = projeto_id;
    }

    @Override
    public String toString() {
        return "Integrante{ [CARGO - " + cargo + "] [PESSOA_ID - " + pessoa_id + "] [PROJETO_ID - " + projeto_id + "]";
    }
}