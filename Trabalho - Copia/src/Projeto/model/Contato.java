package Projeto.model;

public class Contato {
    private Integer id;
    private String contato;
    private String tipo;
    private Integer pessoa_id; // FK

    public Contato(Integer id, String contato, String tipo, Integer pessoa_id) {
        this.id = id;
        this.contato = contato;
        this.tipo = tipo;
        this.pessoa_id = pessoa_id;
    }

    public Contato(String contato, String tipo, Integer pessoa_id) {
        this.contato = contato;
        this.tipo = tipo;
        this.pessoa_id = pessoa_id;
    }

    public Contato() {}

    public Integer getId() {
        return id;
    }

    public String getContato() {
        return contato;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getPessoa_id() {
        return pessoa_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPessoa_id(Integer pessoa_id) {
        this.pessoa_id = pessoa_id;
    }

    @Override
    public String toString() {
        return "Contato{" +
               "id=" + id +
               ", contato='" + contato + '\'' +
               ", tipo='" + tipo + '\'' +
               ", pessoa_id=" + pessoa_id +
               '}';
    }
}