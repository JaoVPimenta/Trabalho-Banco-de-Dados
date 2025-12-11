package Projeto;

import Projeto.model.Pessoa;
import Projeto.model.Projeto;
import Projeto.model.Contato;
import Projeto.model.Integrante;
import Projeto.repository.PessoaRepository;
import Projeto.repository.ProjetoRepository;
import Projeto.repository.ContatoRepository;
import Projeto.repository.IntegranteRepository;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PessoaRepository pessoaRepo = new PessoaRepository();
    private static final ProjetoRepository projetoRepo = new ProjetoRepository();
    private static final ContatoRepository contatoRepo = new ContatoRepository();
    private static final IntegranteRepository integranteRepo = new IntegranteRepository();

    public static void main(String[] args) {
        int opcao = 0;
        do {
            exibirMenu();
            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
                executarOpcao(opcao);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número da opção.");
                scanner.nextLine();
                opcao = 0;
            }
        } while (opcao != 12);

        System.out.println("Programa encerrado. Obrigado!");
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
                    associarPessoaAProjeto();
                    break;
                case 11:
                    listarIntegrantes();
                    break;
                case 12:
                    break; // sair
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }

    // --- MÉTODOS PESSOAS ---

    private static void cadastrarPessoa() {
        System.out.println("\n--- Cadastrar Pessoa ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Data de Nascimento (AAAA-MM-DD): ");
        Date dataNascimento = Date.valueOf(scanner.nextLine());
        System.out.print("Sexo (M/F/O): ");
        String sexo = scanner.nextLine().toUpperCase();

        Pessoa novaPessoa = new Pessoa(cpf, dataNascimento, nome, sexo);
        Pessoa pessoaCriada = pessoaRepo.create(novaPessoa);

        if (pessoaCriada != null) {
            System.out.println("Pessoa cadastrada com sucesso! ID: " + pessoaCriada.getId());
        } else {
            System.out.println("Falha ao cadastrar pessoa.");
        }
    }

    private static void listarPessoas() {
        System.out.println("\n--- Listar Pessoas ---");
        List<Pessoa> pessoas = pessoaRepo.findAll();
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
            return;
        }
        pessoas.forEach(p -> System.out.println(p));
    }

    private static void editarPessoa() {
        System.out.println("\n--- Editar Pessoa ---");
        System.out.print("Digite o ID da pessoa para editar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Pessoa> optPessoa = pessoaRepo.findById(id);
        if (optPessoa.isEmpty()) {
            System.out.println("Pessoa com ID " + id + " não encontrada.");
            return;
        }

        Pessoa pessoa = optPessoa.get();
        System.out.println("Pessoa atual: " + pessoa);

        System.out.print("Novo Nome (" + pessoa.getNome() + "): ");
        String nome = scanner.nextLine();
        pessoa.setNome(nome.isEmpty() ? pessoa.getNome() : nome);

        System.out.print("Novo CPF (" + pessoa.getCpf() + "): ");
        String cpf = scanner.nextLine();
        pessoa.setCpf(cpf.isEmpty() ? pessoa.getCpf() : cpf);

        System.out.print("Nova Data de Nascimento (AAAA-MM-DD) (" + pessoa.getData_nascimento() + "): ");
        String dataNascimentoStr = scanner.nextLine();
        if (!dataNascimentoStr.isEmpty()) {
            pessoa.setData_nascimento(Date.valueOf(dataNascimentoStr));
        }

        System.out.print("Novo Sexo (" + pessoa.getSexo() + "): ");
        String sexo = scanner.nextLine();
        pessoa.setSexo(sexo.isEmpty() ? pessoa.getSexo() : sexo.toUpperCase());

        if (pessoaRepo.update(pessoa)) {
            System.out.println("Pessoa atualizada com sucesso!");
        } else {
            System.out.println("Falha ao atualizar pessoa.");
        }
    }

    private static void excluirPessoa() {
        System.out.println("\n--- Excluir Pessoa ---");
        System.out.print("Digite o ID da pessoa para excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (pessoaRepo.delete(id)) {
            System.out.println("Pessoa ID " + id + " excluída com sucesso!");
        } else {
            System.out.println("Falha ao excluir pessoa (Verifique se há contatos ou associações).");
        }
    }

    // --- MÉTODOS PROJETOS ---

    private static void cadastrarProjeto() {
        System.out.println("\n--- Cadastrar Projeto ---");
        System.out.print("Nome do Projeto: ");
        String nome = scanner.nextLine();
        System.out.print("Data Inicial (AAAA-MM-DD, pode ser vazio): ");
        String dataInicialStr = scanner.nextLine();
        Date dataInicial = dataInicialStr.isEmpty() ? null : Date.valueOf(dataInicialStr);
        System.out.print("Data Final (AAAA-MM-DD, pode ser vazio): ");
        String dataFinalStr = scanner.nextLine();
        Date dataFinal = dataFinalStr.isEmpty() ? null : Date.valueOf(dataFinalStr);

        Projeto novoProjeto = new Projeto(dataInicial, dataFinal, nome);
        Projeto projetoCriado = projetoRepo.create(novoProjeto);

        if (projetoCriado != null) {
            System.out.println("Projeto cadastrado com sucesso! ID: " + projetoCriado.getId());
        } else {
            System.out.println("Falha ao cadastrar projeto.");
        }
    }

    private static void listarProjetos() {
        System.out.println("\n--- Listar Projetos ---");
        List<Projeto> projetos = projetoRepo.findAll();
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
            return;
        }
        projetos.forEach(p -> System.out.println(p));
    }

    private static void editarProjeto() {
        System.out.println("\n--- Editar Projeto ---");
        System.out.print("Digite o ID do projeto para editar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Projeto> optProjeto = projetoRepo.findById(id);
        if (optProjeto.isEmpty()) {
            System.out.println("Projeto com ID " + id + " não encontrado.");
            return;
        }

        Projeto projeto = optProjeto.get();
        System.out.println("Projeto atual: " + projeto);

        System.out.print("Novo Nome (" + projeto.getNome() + "): ");
        String nome = scanner.nextLine();
        projeto.setNome(nome.isEmpty() ? projeto.getNome() : nome);

        System.out.print("Nova Data Inicial (AAAA-MM-DD) (" + projeto.getData_inicial() + "): ");
        String dataInicialStr = scanner.nextLine();
        if (!dataInicialStr.isEmpty()) {
            projeto.setData_inicial(Date.valueOf(dataInicialStr));
        }

        System.out.print("Nova Data Final (AAAA-MM-DD) (" + projeto.getData_final() + "): ");
        String dataFinalStr = scanner.nextLine();
        if (!dataFinalStr.isEmpty()) {
            projeto.setData_final(Date.valueOf(dataFinalStr));
        }

        if (projetoRepo.update(projeto)) {
            System.out.println("Projeto atualizado com sucesso!");
        } else {
            System.out.println("Falha ao atualizar projeto.");
        }
    }

    private static void excluirProjeto() {
        System.out.println("\n--- Excluir Projeto ---");
        System.out.print("Digite o ID do projeto para excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (projetoRepo.delete(id)) {
            System.out.println("Projeto ID " + id + " excluído com sucesso!");
        } else {
            System.out.println("Falha ao excluir projeto (Verifique se há integrantes associados).");
        }
    }

    // --- MÉTODOS ASSOCIAÇÕES ---

    private static void adicionarContatoAPessoa() {
        System.out.println("\n--- Adicionar Contato a uma Pessoa ---");
        System.out.print("Digite o ID da Pessoa para adicionar o contato: ");
        int pessoaId = scanner.nextInt();
        scanner.nextLine();

        if (pessoaRepo.findById(pessoaId).isEmpty()) {
            System.out.println("Pessoa com ID " + pessoaId + " não encontrada.");
            return;
        }

        System.out.print("Tipo de Contato (e.g., Email, Telefone): ");
        String tipo = scanner.nextLine();
        System.out.print("Valor do Contato: ");
        String contatoValor = scanner.nextLine();

        Contato novoContato = new Contato(contatoValor, tipo, pessoaId);
        Contato contatoCriado = contatoRepo.create(novoContato);

        if (contatoCriado != null) {
            System.out.println("Contato adicionado com sucesso! ID: " + contatoCriado.getId());
            System.out.println("Contatos da Pessoa:");
            contatoRepo.findByPessoa_id(pessoaId).forEach(c -> System.out.println("  > " + c));
        } else {
            System.out.println("Falha ao adicionar contato.");
        }
    }

    private static void associarPessoaAProjeto() {
        System.out.println("\n--- Associar Pessoa a um Projeto ---");
        System.out.print("Digite o ID da Pessoa: ");
        int pessoaId = scanner.nextInt();
        System.out.print("Digite o ID do Projeto: ");
        int projetoId = scanner.nextInt();
        scanner.nextLine();

        if (pessoaRepo.findById(pessoaId).isEmpty()) {
            System.out.println("Pessoa com ID " + pessoaId + " não encontrada.");
            return;
        }
        if (projetoRepo.findById(projetoId).isEmpty()) {
            System.out.println("Projeto com ID " + projetoId + " não encontrado.");
            return;
        }

        System.out.print("Digite o Cargo da Pessoa no Projeto: ");
        String cargo = scanner.nextLine();

        Integrante novoIntegrante = new Integrante(cargo, pessoaId, projetoId);
        Integrante integranteCriado = integranteRepo.create(novoIntegrante);

        if (integranteCriado != null) {
            System.out.println("Pessoa associada ao Projeto com sucesso!");
        } else {
            System.out.println("Falha ao associar. (Pode já estar associada)");
        }
    }

    private static void listarIntegrantes() {
        System.out.println("\n--- Listar Integrantes de Todos os Projetos ---");
        List<Integrante> integrantes = integranteRepo.findAll();
        if (integrantes.isEmpty()) {
            System.out.println("Nenhuma pessoa associada a projetos.");
        }
    }
}