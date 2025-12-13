package Projeto;

import Projeto.model.*;
import Projeto.repository.*;

import java.sql.Date;
import java.util.InputMismatchException;
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

        int opcao = 0;
        do {
            exibirMenu();      
            try {
                
                System.out.print("Escolha uma opção abaixo: ");
                opcao = leia.nextInt();
                leia.nextLine();
                executarOpcao(opcao);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida." + e.getMessage());
                leia.nextLine(); // limpa o buffer
                opcao = 0;
            }
        } while (opcao != 15);
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
        System.out.println("10. Listar Contatos");
        System.out.println("11. Associar Pessoa a um Projeto (Integrante)");
        System.out.println("12. Listar Integrantes");
        System.out.println("13. Editar Associação");
        System.out.println("14. Excluir associação");
        System.out.println("---------------------------------");
        System.out.println("SISTEMA");
        System.out.println("15. Sair");
        System.out.println("=================================");
    }

    private static void executarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1:
                    cadastrarPessoa();
                    break;
                case 2:
                    listarPessoas();
                    break;
                case 3:
                    editarPessoa();
                    break;
                case 4:
                    excluirPessoa();
                    break;
                case 5:
                    cadastrarProjeto();
                    break;
                case 6:
                    listarProjetos();
                    break;
                case 7:
                    editarProjeto();
                    break;
                case 8:
                    excluirProjeto();
                    break;
                case 9:
                    adicionarContatoAPessoa();
                    break;
                case 10:
                    listarContatos();
                    break;
                case 11:
                    associarPessoaAProjeto();
                    break;
                case 12:
                    listarIntegrantes();
                    break;
                case 13:
                    editarAssociacao();
                    break;
                case 14:
                    excluirAssociacao();
                    break;
                case 15:
                    break; // sair

                default:
                    System.out.println("Opção inválida. Tente novamente");
                    break;
            }

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }

    }

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

            System.out.printf("Pessoa com ID %d excluída com sucesso!", id);
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

    private static void listarProjetos() {

        System.out.println(" [Listagem de Projetos Cadastrados] ");

        List<Projeto> projetos = projetoRep.findAll();

        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
        } else {
            for (Projeto p : projetos) {
                System.out.println(p);
            }
        }

    }

    private static void editarProjeto() {

        System.out.println(" [Editar Projeto] ");

        System.out.print("Digite o ID do projeto para editar: ");
        int id = leia.nextInt();
        leia.nextLine();

        Optional<Projeto> opProjeto = projetoRep.findById(id);
        if (opProjeto.isEmpty()) {

            System.out.printf("Projeto com ID %d não encontrado.", id);
            return;
        }

        Projeto projeto = opProjeto.get();
        System.out.println("Projeto atual: " + projeto);

        System.out.print("Novo nome: ");
        String nome = leia.nextLine();
        projeto.setNome(nome.isEmpty() ? projeto.getNome() : nome);

        System.out.println("Nova Data Inicial (AAAA-MM-DD)");
        String dataInicialStr = leia.nextLine();
        if (!dataInicialStr.isEmpty()) {
            projeto.setData_inicial(Date.valueOf(dataInicialStr));
        }

        System.out.println("Nova Data Final (AAAA-MM-DD)");
        String dataFinalStr = leia.nextLine();
        if (!dataFinalStr.isEmpty()) {
            projeto.setData_final(Date.valueOf(dataFinalStr));
        }

        if (projetoRep.update(projeto)) {

            System.out.println("Projeto atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualiza Projeto.");
        }
    }

    private static void excluirProjeto() {

        System.out.println(" [Excluir Projeto] ");

        System.out.print("Digite o ID do projeto para excluir: ");
        int id = leia.nextInt();
        leia.nextLine();

        if (projetoRep.delete(id)) {

            System.out.println("Projeto excluído com sucesso!");
        } else {
            System.out.println("Erro ao excluir projeto.");
        }
    }

    // MÉTODOS ACOSSIAÇÕES

    private static void adicionarContatoAPessoa() {

        System.out.println(" [Adicionar Contato] ");

        System.out.print("Digite o ID da pessoa para adicionar o contato: ");
        int pessoaId = leia.nextInt();
        leia.nextLine();

        if (pessoaRep.findById(pessoaId).isEmpty()) {
            System.out.printf("Pessoa com ID %d não encontrada.", pessoaId);
            return;
        }
        System.out.println(pessoaRep.findById(pessoaId));

        String tipoTeste = "";
        int cont = 0;
        while (cont == 0) {

            System.out.println("Tipo de Contato (Email, Telefone): ");
            tipoTeste = leia.nextLine();
            tipoTeste = tipoTeste.toUpperCase();

            if (tipoTeste.equals("TELEFONE") || tipoTeste.equals("EMAIL")) {
                cont++;
            }
        }
        String tipo = tipoTeste;

        System.out.println("Digite o contato: ");
        String contatoV = leia.nextLine();

        Contato novoContato = new Contato(contatoV, tipo, pessoaId);
        Contato contatoCriado = contatoRep.create(novoContato);

        if (contatoCriado != null) {

            System.out.println("Contato adicionado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar contato");
        }
    }

    private static void listarContatos() {

        System.out.println(" [Listagem de Contatos] ");

        List<Contato> contatos = contatoRep.findAll();

        if (contatos.isEmpty()) {
            System.out.println("Nenhum contato adicionado.");
        }
    }

    private static void associarPessoaAProjeto() {

        System.out.println(" [Associar Pessoa a um Projeto] ");

        System.out.print("Digite o ID da Pessoa: ");
        int pessoaId = leia.nextInt();

        System.out.print("Digite o ID do projeto: ");
        int projetoId = leia.nextInt();

        leia.nextLine();

        if (pessoaRep.findById(pessoaId).isEmpty()) {
            System.out.printf("Pessoa com ID %d não encontrada.", pessoaId);
            return;
        }
        if (projetoRep.findById(projetoId).isEmpty()) {
            System.out.printf("Projeto com ID %d não encontrado.", projetoId);
            return;
        }

        System.out.println("Digite o cargo em que você se encontra: ");
        String cargo = leia.nextLine();

        Integrante novoIntegrante = new Integrante(cargo, pessoaId, projetoId);
        Integrante integranteCriado = integranteRep.create(novoIntegrante);

        if (integranteCriado != null) {
            System.out.println("Pessoa associada ao Projeto com sucesso!");
        } else {
            System.out.println("Erro ao associar Pessoa ao Projeto.");
        }
    }

    private static void listarIntegrantes() {

        System.out.println(" [Listagem de Integrantes] ");

        List<Integrante> integrantes = integranteRep.findAll();

        if (integrantes.isEmpty()) {
            System.out.println("Nenhuma pessoa associada a projetos");
        }
    }

    private static void editarAssociacao() {

        System.out.println(" [Editar Associação] ");

        System.out.print("Digite o ID da Pessoa: ");
        int pessoaId = leia.nextInt();

        System.out.println("Digite o ID do Projeto: ");
        int projetoId = leia.nextInt();

        leia.nextLine();

        Optional<Pessoa> opPessoa = pessoaRep.findById(pessoaId);
        if (opPessoa.isEmpty()) {
            System.out.printf("Pessoa com ID %d não encontrada.", pessoaId);
            return;
        }

        Optional<Projeto> opProjeto = projetoRep.findById(projetoId);
        if (opProjeto.isEmpty()) {
            System.out.printf("Projeto com ID %d não encontrado.", projetoId);
            return;
        }

        Optional<Integrante> opIntegrante = integranteRep.findById(pessoaId, projetoId);
        Integrante integrante = opIntegrante.get();
        System.out.println("Associação atual: " + integrante);

        System.out.println("Digite o novo cargo: ");
        String cargo = leia.nextLine();
        integrante.setCargo(cargo.isEmpty() ? integrante.getCargo() : cargo);

        if (integranteRep.update(integrante)) {
            System.out.println("Associaçã atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar associação.");
        }
    }

    private static void excluirAssociacao() {

        System.out.println(" [Excluir Associação] ");

        System.out.print("Digite o ID da pessoa: ");
        int pessoaId = leia.nextInt();

        System.out.println("Digite o ID do projeto: ");
        int projetoId = leia.nextInt();
        leia.nextLine();

        if (integranteRep.delete(pessoaId, projetoId)) {
            System.out.println("Associação excluída com sucesso!");
        } else {
            System.out.println("Erro ao excluir associação.");
        }
    }
}
