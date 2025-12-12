package Projeto;

import Projeto.model.*;
import Projeto.repository.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    
    private static final Scanner leia = new Scanner(System.in);
    private static final ContatoRepository contatoRep = new ContatoRepository();
    private static final PessoaRepository pessoaRep = new PessoaRepository();
    private static final ProjetoRepository projetoRep = new ProjetoRepository();
    private static final IntegranteRepository integranteRep = new IntegranteRepository();

    public static void main(String[] args) {
    
    }

    private static void exibirMenu() {
        System.out.println("\n=================================");
        System.out.println("         --- MENU PRINCIPAL ---  ");
        System.out.println("=================================");
        System.out.println("PESSOAS");
        System.out.println("1. Cadastrar Pessoa");
        System.out.println("2. Listar Pessoas");
        System.out.println("3. Editar Pessoa");
        System.out.println("4. Excluir Pessoa");
        System.out.println("---------------------------------");
        System.out.println("PROJETOS");
        System.out.println("5. Cadastrar Projeto");
        System.out.println("6. Listar Projetos");
        System.out.println("7. Editar Projeto");
        System.out.println("8. Excluir Projeto");
        System.out.println("---------------------------------");
        System.out.println("ASSOCIAÇÕES");
        System.out.println("9. Adicionar Contato a uma Pessoa");
        System.out.println("10. Associar Pessoa a um Projeto (Integrante)");
        System.out.println("11. Listar Integrantes");
        System.out.println("---------------------------------");
        System.out.println("SISTEMA");
        System.out.println("12. Sair");
        System.out.println("=================================");
        System.out.print("Escolha uma opção: ");
    }

    // private static void executarOpcao(int opcao) {
    //     try {
            
    // }

    // MÉTODOS PESSOAS
    
    private static void cadastrarPessoa() {
        
        System.out.println(" [Cadastro de Pessoa] ");

        System.out.println("Nome: ");
        String nome = leia.nextLine();

        System.out.println("CPF: ");
        String cpf = leia.nextLine();

        System.out.println("Data de nascimento [AAAA-MM-DD]");
        Date dataNascimento = Date.valueOf(leia.nextLine());

        System.out.println("Sexo: M/F: ");
        String sexo = leia.nextLine();

        Pessoa novaPessoa = new Pessoa(cpf, dataNascimento, nome, sexo); // instancia um objeto
        Pessoa pessoaCriada = pessoaRep.create(novaPessoa); // cria pessoa no banco de dados

        if (pessoaCriada != null) {
            System.out.println("Pessoa cadastrada com sucesso!");
        } else {
            System.out.println("Falha ao cadastrar pessoa.");
        }
    }

    private static void listarPessoas() {

        System.out.println(" [Listagem de Pessoas Cadastradas] ");
        List<Pessoa> pessoas = pessoaRep.findAll();

        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
        } else {
            for (Pessoa p : pessoas) {
                System.out.println(p);
            }
        }
    }

    private static void editarPessoa() {

        System.out.println(" [Editar Pessoa] ");
        
        System.out.print("Digite o ID da pessoa para editar: ");
        int id = leia.nextInt();
        leia.nextLine(); // limpeza do buffer

        Optional<Pessoa> opPessoa = pessoaRep.findById(id);
        if (opPessoa.isEmpty()) {
            
            System.out.printf("Pessoa com ID %d não encontrada.", id);
            return; // encerra o método
        }
        Pessoa pessoa = opPessoa.get();
        System.out.println("Pessoa atual: " + pessoa);

        System.out.println("Novo nome: ");
        String nome = leia.nextLine();
        pessoa.setNome(nome.isEmpty() ? pessoa.getNome() : nome);

        System.out.println("Novo CPF: ");
        String cpf = leia.nextLine();
        pessoa.setCpf(cpf.isEmpty() ? pessoa.getCpf() : cpf);

        System.out.println("Nova data de nascimento: ");
        String dataNascimento = leia.nextLine();
        if (!dataNascimento.isEmpty()) { // não é possível usar a lógica ternária
           pessoa.setData_nascimento(Date.valueOf(dataNascimento)); 
        }

        System.out.println("Novo sexo: ");
        String sexo = leia.nextLine();
        pessoa.setSexo(sexo.isEmpty() ? pessoa.getSexo() : sexo.toUpperCase());

        if (pessoaRep.update(pessoa)) {
            System.out.println("Pessoa atualizada com sucesso!");

        } else {
            System.out.println("Erro ao atualizar pessoa.");
        }

    }

    private static void excluirPessoa() {

        System.out.println(" [Excluir Pessoa] ");

        System.out.print("Digite o ID da pessoa para excluir: ");
        int id = leia.nextInt();
        leia.nextLine();

        if (pessoaRep.delete(id)) {
            
            System.out.printf("Pessoa com ID %d excluída com sucesso!");
        } else {
            System.out.println("Erro ao excluir pessoa.");
        }
    }


    // MÉTODOS PROJETOS

    private static void cadastrarProjeto() {

        System.out.println(" [Cadastro de Projeto] ");

        System.out.println("Digite o nome do projeto: ");
        String nome = leia.nextLine();

        System.out.println("Informe a data de início do Projeto: [AAAA-MM-DD] ");
        String dataInicialstr = leia.nextLine();
        Date dataInicial = dataInicialstr.isEmpty() ? null : Date.valueOf(dataInicialstr);

        System.out.println("Informe a data de conclusão do Projeto: [AAAA-MM-DD]");
        String dataFinalstr = leia.nextLine();
        Date dataFinal = dataFinalstr.isEmpty() ? null : Date.valueOf(dataFinalstr);

        Projeto novoProjetos = new Projeto(dataInicial, dataFinal, nome);
        Projeto projetoCriado = projetoRep.create(novoProjetos);

        if (projetoCriado != null) {
            System.out.println("Projeto cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar projeto.");
        }
    }

    private static void listarProjeto() {

        System.out.println(" [Listagem de Pessoas Cadastradas] ");
        
        List<Projeto> projetos = projetoRep.findAll();

        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
        } else {
            for(Projeto p : projetos) {
                System.out.println(p);
            }
        }
        
    }

    private static void editarProjeto() {

        System.out.println(" [Editar Projeto] ");

        System.out.println("Digite o ID do projeto que será atualizado: ");
        int id = leia.nextInt();
        leia.nextLine();

        Optional<Projeto> opProjeto = projetoRep.findById(id);
        if (opProjeto.isEmpty()) {
            
            System.out.printf("Projet0 com ID %d não encontrado.", id);
            return;
        }

        Projeto projeto = opProjeto.get();
        System.out.println("Projeto atual: " + projeto);

        System.out.println("Novo nome: ");
        String nome = leia.nextLine();
        projeto.setNome(nome.isEmpty() ? projeto.getNome() : nome);

        System.out.println("Nova data inicial: ");
        String dataInicialstr = leia.nextLine();
        if (dataInicialstr.isEmpty()) {
            
            projeto.getData_inicial();
        } else {
            Date dataInicial = Date.valueOf(dataInicialstr);
            projeto.setData_inicial(dataInicial);
        }
        
    }
}
