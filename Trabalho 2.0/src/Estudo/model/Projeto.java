package Estudo.model;

import java.sql.Date;

public class Projeto {

    private Integer id;
    private Date data_inicial;
    private Date data_final;
    private String nome;

    public Projeto() {}

    public Projeto(Integer id, Date data_inicial, Date data_final, String nome) {
        this.id = id;
        this.data_inicial = data_inicial;
        this.data_inicial = data_final;
        this.nome = nome;
    }

    // sem id
    public Projeto(Date data_inicial, Date data_final, String nome) {
        this.data_inicial = data_inicial;
        this.data_inicial = data_final;
        this.nome = nome;
    }    

    public Date getData_final() {
        return data_final;
    }

    public Date getData_inicial() {
        return data_inicial;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setData_final(Date data_final) {
        this.data_final = data_final;
    }
    
    public void setData_inicial(Date data_inicial) {
        this.data_inicial = data_inicial;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Projeto{ [ID - " + id + "] [DATA_INICIAL - " + data_inicial + "] [DATA_FINAL - " + data_final + "] [NOME - " + nome + "]";
    }
}