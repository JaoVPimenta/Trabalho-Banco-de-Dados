package Projeto.model;

import java.sql.Date;

public class Projeto {
    private Integer id;
    private Date data_inicial;
    private Date data_final;
    private String nome;

    public Projeto(Integer id, Date data_inicial, Date data_final, String nome) {
        this.id = id;
        this.data_inicial = data_inicial;
        this.data_final = data_final;
        this.nome = nome;
    }

    public Projeto(Date data_inicial, Date data_final, String nome) {
        this.data_inicial = data_inicial;
        this.data_final = data_final;
        this.nome = nome;
    }

    public Projeto() {}

    public Integer getId() {
        return id;
    }

    public Date getData_inicial() {
        return data_inicial;
    }

    public Date getData_final() {
        return data_final;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setData_inicial(Date data_inicial) {
        this.data_inicial = data_inicial;
    }

    public void setData_final(Date data_final) {
        this.data_final = data_final;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Projeto{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", data_inicial=" + data_inicial +
               ", data_final=" + data_final +
               '}';
    }
}