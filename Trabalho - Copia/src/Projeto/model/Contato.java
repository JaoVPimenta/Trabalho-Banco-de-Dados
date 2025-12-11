package Projeto.model;

public class Contato {
    private Integer id; // recebe também null
    private String contato;
    private String tipo;
    private Integer pessoa_id; // fk

    public Contato() {}

    public Contato(Integer id, String contato, String tipo, Integer pessoa_id) {
        this.id = id;
        this.contato = contato;
        this.tipo = tipo;
        this.pessoa_id = pessoa_id;
    }

    // sem id
    public Contato(String contato, String tipo, Integer pessoa_id) {
        this.contato = contato;
        this.tipo = tipo;
        this.pessoa_id = pessoa_id;
    }

    public String getContato() {
        return contato;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPessoa_id() {
        return pessoa_id;
    }

    public String getTipo() {
        return tipo;
    }


    public void setContato(String contato) {
        this.contato = contato;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPessoa_id(Integer pessoa_id) {
        this.pessoa_id = pessoa_id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Contato{ [ID - " + id + "] [CONTATO - " + contato + "] [TIPO - " + tipo + "] [PESSOA_ID - " + pessoa_id + "]";
    }

}