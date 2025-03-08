import java.util.*;

public class Sistema {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Cadastrar novo usuário");
            System.out.println("2 - Fazer login");
            System.out.println("3 - Sair do sistema");
            System.out.print("> ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarUsuario(scanner);
                    break;

                case 2:
                    loginUsuario(scanner);
                    break;

                case 3:
                    limparTela();
                    break;

                default:
                    System.out.println(ANSI_RED + "Opção inválida." + ANSI_RESET);
                    break;
            }
        } while (opcao != 3);

        scanner.close();
    }

    private static void cadastrarUsuario(Scanner scanner) {
        System.out.println("\nDigite o nome:");
        System.out.print("> ");
        String nome = scanner.nextLine();

        String login;
        do {
            System.out.println("\nDigite o login:");
            System.out.print("> ");
            login = scanner.nextLine();
            if (Usuario.loginExiste(login)) {
                System.out.println(
                        ANSI_RED + "\nEste login já está em uso. Por favor, escolha outro login." + ANSI_RESET);
            }
        } while (Usuario.loginExiste(login));

        System.out.println("\nDigite a senha:");
        System.out.print("> ");
        String senha = scanner.nextLine();

        int tipo;
        TipoUsuario tipoUsuario = null;

        do {
            System.out.println("\nSelecione o tipo de usuário:");
            System.out.println("1 - Aluno");
            System.out.println("2 - Professor");
            System.out.println("3 - Secretaria");
            System.out.print("> ");
            tipo = scanner.nextInt();
            scanner.nextLine();

            switch (tipo) {
                case 1:
                    tipoUsuario = TipoUsuario.ALUNO;
                    break;

                case 2:
                    tipoUsuario = TipoUsuario.PROFESSOR;
                    break;

                case 3:
                    tipoUsuario = TipoUsuario.SECRETARIA;
                    break;

                default:
                    System.out.println(ANSI_RED + "Tipo de usuário inválido." + ANSI_RESET);
            }
        } while (tipo < 1 || tipo > 3);

        try {
            Usuario.cadastrar(nome, login, senha, tipoUsuario);
            limparTela();
            System.out.println(ANSI_GREEN + "Usuário cadastrado com sucesso!\n" + ANSI_RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
        }
    }

    private static void loginUsuario(Scanner scanner) {
        System.out.println("\nDigite o login:");
        System.out.print("> ");
        String loginLogin = scanner.nextLine();

        System.out.println("\nDigite a senha:");
        System.out.print("> ");
        String senhaLogin = scanner.nextLine();

        Usuario usuarioCarregado = Usuario.carregar(loginLogin);

        if (usuarioCarregado != null) {
            try {
                usuarioCarregado.entrar(loginLogin, senhaLogin);
                limparTela();

                if (usuarioCarregado.getTipoUsuario() != null && usuarioCarregado.getLogin().equals(loginLogin)) {
                    System.out.println(ANSI_GREEN + "\nLogin bem-sucedido!" + ANSI_RESET);
                    System.out.println();

                    switch (usuarioCarregado.getTipoUsuario()) {
                        case ALUNO:
                            menuAluno(scanner, (Aluno) usuarioCarregado);
                            break;

                        case PROFESSOR:
                            menuProfessor(scanner, (Professor) usuarioCarregado);
                            break;

                        case SECRETARIA:
                            menuSecretaria(scanner, usuarioCarregado);
                            break;
                    }
                }
            } catch (IllegalArgumentException e) {
                limparTela();
                System.out.println(ANSI_RED + "\nOPS! Login ou senha incorretos. Tente novamente." + ANSI_RESET);
                System.out.println();
            }
        } else {
            limparTela();
            System.out.println(ANSI_RED + "\nOPS! Usuário não encontrado.\n" + ANSI_RESET);
        }
    }

    public static void menuAluno(Scanner scanner, Aluno aluno) {
        int opcaoAluno;

        do {
            System.out.println("Bem-vindo, Aluno!");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Visualizar matrículas");
            System.out.println("2 - Matricular em disciplina");
            System.out.println("3 - Cancelar matrícula");
            System.out.println("4 - Sair");
            System.out.print("> ");
            opcaoAluno = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoAluno) {
                case 1:
                    visualizarMatriculas(aluno);
                    break;

                case 2:
                    matricularEmDisciplina(scanner, aluno);
                    break;

                case 3:
                    cancelarMatricula(scanner, aluno);
                    break;

                case 4:
                    limparTela();
                    break;

                default:
                    System.out.println(ANSI_RED + "Opção inválida." + ANSI_RESET);
                    break;
            }
        } while (opcaoAluno != 4);
    }

    private static void visualizarMatriculas(Aluno aluno) {
        aluno.visualizarMatriculas();
    }

    private static void matricularEmDisciplina(Scanner scanner, Aluno aluno) {
        Disciplina.listar();
        System.out.println("\nDigite o ID da disciplina para se matricular:");
        System.out.print("> ");
        int idDisciplina = scanner.nextInt();
        scanner.nextLine();

        Disciplina disciplina = Disciplina.carregarPorId(idDisciplina);
        if (disciplina != null) {
            aluno.matricularEmDisciplina(disciplina);
        } else {
            limparTela();
            System.out.println(ANSI_RED + "\nDisciplina não encontrada.\n" + ANSI_RESET);
        }
    }

    private static void cancelarMatricula(Scanner scanner, Aluno aluno) {
        List<Integer> disciplinasMatriculadas = Matricula.carregarDisciplinasDoAluno(aluno.getId());
        if (disciplinasMatriculadas.isEmpty()) {
            System.out.println("\nVocê não está matriculado em nenhuma disciplina.");
        } else {
            System.out.println("\nDisciplinas matriculadas:");
            for (int id : disciplinasMatriculadas) {
                Disciplina disc = Disciplina.carregarPorId(id);
                if (disc != null) {
                    System.out.println("ID: " + disc.getIdDisciplina() + " | Nome: " + disc.getNome());
                }
            }

            System.out.println("\nDigite o ID da disciplina para cancelar a matrícula:");
            System.out.print("> ");
            int idDisciplinaCancelar = scanner.nextInt();
            scanner.nextLine();

            Disciplina disciplinaCancelar = Disciplina.carregarPorId(idDisciplinaCancelar);
            if (disciplinaCancelar != null) {
                aluno.cancelarMatricula(disciplinaCancelar);
            } else {
                limparTela();
                System.out.println(ANSI_RED + "\nDisciplina não encontrada.\n" + ANSI_RESET);
            }
        }
    }

    public static void menuProfessor(Scanner scanner, Professor professor) {
        int opcaoProfessor;
        do {
            System.out.println("Bem-vindo, Professor!");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Alunos matriculados");
            System.out.println("2 - Sair");
            opcaoProfessor = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoProfessor) {
                case 1:
                    exibirAlunosMatriculados(scanner, professor);
                    break;

                case 2:
                    limparTela();
                    break;

                default:
                    System.out.println(ANSI_RED + "Opção inválida." + ANSI_RESET);
            }
        } while (opcaoProfessor != 2);
    }

    private static void exibirAlunosMatriculados(Scanner scanner, Professor professor) {
        Disciplina.listarDisciplinasDoProfessor(professor.getId());

        System.out.println("Digite o ID da disciplina para ver os alunos matriculados:");
        int idDisciplina = scanner.nextInt();
        scanner.nextLine();

        List<Aluno> alunosMatriculados = professor.getAlunosMatriculadosNaDisciplina(idDisciplina);
        if (alunosMatriculados.isEmpty()) {
            System.out.println("Nenhum aluno matriculado nesta disciplina.");
        } else {
            System.out.println("Alunos matriculados na disciplina:");
            for (Aluno aluno : alunosMatriculados) {
                System.out.println("- " + aluno.getNome());
            }
        }
    }

    public static void menuSecretaria(Scanner scanner, Usuario usuarioCarregado) {
        int opcaoSecretaria;
        do {
            System.out.println("Bem-vindo, Secretaria!");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Cadastrar Curso");
            System.out.println("2 - Cadastrar Disciplina");
            System.out.println("3 - Gerar currículo"); // ????
            System.out.println("4 - Atualizar informacoes de disciplina");
            System.out.println("5 - Atualizar informacoes de professor");
            System.out.println("6 - Atualizar informacoes de aluno");
            System.out.println("7 - Sair");
            opcaoSecretaria = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoSecretaria) {
                case 1:
                    System.out.println("Digite o nome do curso:");
                    String nomeCurso = scanner.nextLine();

                    System.out.println("Digite a quantidade de créditos do curso:");
                    int creditosCurso = scanner.nextInt();
                    scanner.nextLine();

                    Curso curso = new Curso(nomeCurso, creditosCurso);
                    curso.salvar();

                    System.out.println("Curso cadastrado com sucesso!");

                    break;

                case 2:
                    System.out.println("Digite o nome da disciplina:");
                    String nomeDisciplina = scanner.nextLine();

                    System.out.println("Digite o custo da disciplina:");
                    float custoDisciplina = scanner.nextFloat();
                    scanner.nextLine();

                    System.out.println("A disciplina é obrigatória? (sim/não):");
                    String respostaObrigatoria = scanner.nextLine().toLowerCase();
                    boolean obrigatoriaDisciplina = respostaObrigatoria.equals("sim");

                    Curso.listar();
                    int idCursoDisciplina;
                    do {
                        System.out.println("Digite o ID do curso ao qual a disciplina pertence:");
                        idCursoDisciplina = scanner.nextInt();
                        if (!Curso.existe(idCursoDisciplina)) {
                            System.out.println("Curso não encontrado! Escolha um ID válido.");
                        }
                    } while (!Curso.existe(idCursoDisciplina));
                    scanner.nextLine();

                    Professor.listar();
                    int idProfessorDisciplina;
                    do {
                        System.out.println("Digite o ID do professor que vai lecionar a disciplina:");
                        idProfessorDisciplina = scanner.nextInt();
                        if (!Professor.existe(idProfessorDisciplina)) {
                            System.out.println("Professor não encontrado! Escolha um ID válido.");
                        }
                    } while (!Professor.existe(idProfessorDisciplina));
                    scanner.nextLine();

                    Disciplina novaDisciplina = new Disciplina(nomeDisciplina, custoDisciplina, obrigatoriaDisciplina,
                            idCursoDisciplina, idProfessorDisciplina);
                    novaDisciplina.salvar();

                    System.out.println("Disciplina cadastrada com sucesso!");

                    break;

                case 3:

                    break;

                case 4:
                    Disciplina.listar();

                    System.out.println("Digite o ID da disciplina que deseja atualizar:");
                    int idDisciplina = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Digite o novo nome da disciplina:");
                    String novoNome = scanner.nextLine();

                    System.out.println("Digite o novo custo da disciplina:");
                    float novoCusto = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("A disciplina é obrigatória? (sim/não):");
                    String resposta = scanner.nextLine().toLowerCase();
                    boolean novaObrigatoriedade = resposta.equals("sim");

                    System.out.println("Digite o novo status da disciplina (Aberta/Fechada):");
                    String novoStatus = scanner.nextLine();

                    if (usuarioCarregado.getTipoUsuario() == TipoUsuario.SECRETARIA) {
                        Secretaria secretaria = (Secretaria) usuarioCarregado;
                        secretaria.atualizarInformacoesDisciplina(idDisciplina, novoNome, novoCusto,
                                novaObrigatoriedade, novoStatus);
                    } else {
                        System.out.println("Usuário não autorizado para atualizar a disciplina.");
                    }

                    break;

                case 5:

                    break;

                case 6:

                    break;

                case 7:
                    limparTela();
                    break;

                default:
                    System.out.println(ANSI_RED + "Opção inválida." + ANSI_RESET);
            }
        } while (opcaoSecretaria != 7);
    }

    public static void limparTela() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.flush();
    }
}
