package Estudo.model;

import java.sql.Date;

public class Pessoa {
    private Integer id;
    private String cpf;
    private Date data_nascimento;
    private String nome;
    private String sexo;

    public Pessoa() {}
    
    public Pessoa(Integer id, String cpf, Date data_nascimento, String nome, String sexo) {
        this.id = id;
        this.cpf = cpf;
        this.data_nascimento = data_nascimento;
        this.nome = nome;
        this.sexo = sexo;
    }

    // sem id
    public Pessoa(String cpf, Date data_nascimento, String nome, String sexo) {
        this.cpf = cpf;
        this.data_nascimento = data_nascimento;
        this.nome = nome;
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setData_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "Pessoa{ [ID - " + id + "] [CPF - " + cpf + "] [DATA_NASCIMENTO - " + data_nascimento + "] [NOME - " + nome + "] [SEXO - " + sexo + "]";
    }

}