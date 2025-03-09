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
                    System.out.println(ANSI_RED + "OPS! Opção inválida." + ANSI_RESET);
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
                System.out.println(ANSI_RED + "OPS! Este login já está em uso. Por favor, escolha outro login." + ANSI_RESET);
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
                    System.out.println(ANSI_RED + "OPS! Tipo de usuário inválido." + ANSI_RESET);
            }
        } while (tipo < 1 || tipo > 3);

        try {
            Usuario.cadastrar(nome, login, senha, tipoUsuario);
            limparTela();
            System.out.println(ANSI_GREEN + "EBA! Usuário cadastrado com sucesso!\n" + ANSI_RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "OPS! " + e.getMessage() + ANSI_RESET);
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
                    System.out.println(ANSI_GREEN + "EBA! Login bem-sucedido!" + ANSI_RESET);
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
                System.out.println(ANSI_RED + "OPS! Login ou senha incorretos. Tente novamente." + ANSI_RESET);
                System.out.println();
            }
        } else {
            limparTela();
            System.out.println(ANSI_RED + "OPS! Login ou senha incorretos. Tente novamente." + ANSI_RESET);
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
                    System.out.println(ANSI_RED + "OPS! Opção inválida." + ANSI_RESET);
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
            System.out.println(ANSI_RED + "OPS! Disciplina não encontrada.\n" + ANSI_RESET);
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
                System.out.println(ANSI_RED + "OPS! Disciplina não encontrada.\n" + ANSI_RESET);
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
                    limparTela();
                    exibirAlunosMatriculados(scanner, professor);
                    break;

                case 2:
                    limparTela();
                    break;

                default:
                    System.out.println(ANSI_RED + "OPS! Opção inválida." + ANSI_RESET);
            }
        } while (opcaoProfessor != 2);
    }

    private static void exibirAlunosMatriculados(Scanner scanner, Professor professor) {
        Disciplina.listarDisciplinasDoProfessor(professor.getId());

        System.out.println("\nDigite o ID da disciplina para ver os alunos matriculados:");
        System.out.print("> ");
        int idDisciplina = scanner.nextInt();
        scanner.nextLine();

        Disciplina disciplina = Disciplina.carregarPorId(idDisciplina);
        if (disciplina != null) {
            List<Aluno> alunosMatriculados = professor.getAlunosMatriculadosNaDisciplina(idDisciplina);
            if (alunosMatriculados.isEmpty()) {
                limparTela();
                System.out.println("Nenhum aluno matriculado nesta disciplina.\n");
            } else {
                limparTela();
                System.out.println("Alunos matriculados na disciplina:");
                for (Aluno aluno : alunosMatriculados) {
                    System.out.println("- " + aluno.getNome());
                }
                System.out.println();
            }
        } else {
            limparTela();
            System.out.println(ANSI_RED + "OPS! Disciplina não encontrada.\n" + ANSI_RESET);
        }
    }


    public static void menuSecretaria(Scanner scanner, Usuario usuarioCarregado) {
        int opcaoSecretaria;
        do {
            System.out.println("Bem-vindo, Secretaria!");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Cadastrar curso");
            System.out.println("2 - Cadastrar disciplina");
            System.out.println("3 - Abrir período de matrículas");
            System.out.println("4 - Fechar período de matrículas");
            System.out.println("5 - Atualizar informações de disciplina");
            System.out.println("6 - Atualizar informações de professor");
            System.out.println("7 - Atualizar informações de aluno");
            System.out.println("8 - Visualizar disciplinas");
            System.out.println("9 - Visualizar professores");
            System.out.println("10 - Visualizar alunos");
            System.out.println("11 - Sair");
            System.out.print("> ");
            opcaoSecretaria = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoSecretaria) {
                case 1:
                    cadastrarCurso(scanner);
                    break;

                case 2:
                    cadastrarDisciplina(scanner);
                    break;

                case 3:
                    if (usuarioCarregado.getTipoUsuario() == TipoUsuario.SECRETARIA) {
                        Secretaria secretaria = (Secretaria) usuarioCarregado;
                        secretaria.abrirPeriodoMatriculas(); 
                    } else {
                        System.out.println(ANSI_RED + "OPS! Apenas a secretaria pode abrir o período de matrículas." + ANSI_RESET);
                    }
                    break;

                case 4:
                    if (usuarioCarregado.getTipoUsuario() == TipoUsuario.SECRETARIA) {
                        Secretaria secretaria = (Secretaria) usuarioCarregado;
                        secretaria.fecharPeriodoMatriculas();
                    } else {
                        System.out.println(ANSI_RED + "OPS! Apenas a secretaria pode fechar o período de matrículas." + ANSI_RESET);
                    }
                    break;

                case 5:
                    atualizarInformacoesDisciplina(scanner, usuarioCarregado);
                    break;

                case 6:
                    atualizarInformacoesProfessor(scanner, usuarioCarregado);
                    break;

                case 7:
                    atualizarInformacoesAluno(scanner, usuarioCarregado);
                    break;

                case 8:
                    limparTela();
                    Disciplina.listar();
                    System.out.println();
                    break;

                case 9:
                    limparTela();
                    Professor.listar();
                    System.out.println();
                    break;
                
                case 10:
                    limparTela();
                    Aluno.listar();
                    System.out.println();
                    break;

                case 11:
                    limparTela();
                    break;

                default:
                    System.out.println(ANSI_RED + "OPS! Opção inválida." + ANSI_RESET);
            }
        } while (opcaoSecretaria != 11); 
    }
    
    public static void cadastrarCurso(Scanner scanner) {
        System.out.println("\nDigite o nome do curso:");
        System.out.print("> ");
        String nomeCurso = scanner.nextLine();
    
        System.out.println("\nDigite a quantidade de créditos do curso:");
        System.out.print("> ");
        int creditosCurso = scanner.nextInt();
        scanner.nextLine();
    
        limparTela();
    
        Curso curso = new Curso(nomeCurso, creditosCurso);
        curso.salvar();
    
        System.out.println(ANSI_GREEN + "EBA! Curso cadastrado com sucesso!\n" + ANSI_RESET);
    }
    
    public static void cadastrarDisciplina(Scanner scanner) {
        System.out.println("\nDigite o nome da disciplina:");
        System.out.print("> ");
        String nomeDisciplina = scanner.nextLine();

        System.out.println("\nDigite o custo da disciplina:");
        System.out.print("> ");
        float custoDisciplina = scanner.nextFloat();
        scanner.nextLine();

        System.out.println("\nA disciplina é obrigatória? (sim/não):");
        System.out.print("> ");
        String respostaObrigatoria = scanner.nextLine().toLowerCase();
        boolean obrigatoriaDisciplina = respostaObrigatoria.equals("sim");

        Curso.listar();
        int idCursoDisciplina;
        do {
            System.out.println("\nDigite o ID do curso ao qual a disciplina pertence:");
            System.out.print("> ");
            idCursoDisciplina = scanner.nextInt();
            if (!Curso.existe(idCursoDisciplina)) {
                System.out.println(ANSI_RED + "OPS! Curso não encontrado! Escolha um ID válido." + ANSI_RESET);
            }
        } while (!Curso.existe(idCursoDisciplina));
        scanner.nextLine();

        Professor.listar();
        int idProfessorDisciplina;
        do {
            System.out.println("\nDigite o ID do professor que vai lecionar a disciplina:");
            System.out.print("> ");
            idProfessorDisciplina = scanner.nextInt();
            if (!Professor.existe(idProfessorDisciplina)) {
                System.out.println(ANSI_RED + "\nOPS! Professor não encontrado! Escolha um ID válido.\n" + ANSI_RESET);
            }
        } while (!Professor.existe(idProfessorDisciplina));
        scanner.nextLine();

        limparTela();

        Disciplina novaDisciplina = new Disciplina(nomeDisciplina, custoDisciplina, obrigatoriaDisciplina,
                idCursoDisciplina, idProfessorDisciplina);
        novaDisciplina.salvar();

        System.out.println(ANSI_GREEN + "EBA! Disciplina cadastrada com sucesso!\n" + ANSI_RESET);
    }
    
    public static void gerarCurriculo() {

    }
    
    public static void atualizarInformacoesDisciplina(Scanner scanner, Usuario usuarioCarregado) {
        limparTela();
        Disciplina.listar();
    
        System.out.println("\nDigite o ID da disciplina que deseja atualizar:");
        System.out.print("> ");
        int idDisciplina = scanner.nextInt();
        scanner.nextLine();
    
        Disciplina disciplina = Disciplina.carregarPorId(idDisciplina);
        if (disciplina != null) {
            System.out.println("\nDigite o novo nome da disciplina:");
            System.out.print("> ");
            String novoNome = scanner.nextLine();
    
            System.out.println("\nDigite o novo custo da disciplina:");
            System.out.print("> ");
            float novoCusto = scanner.nextInt();
            scanner.nextLine();
    
            System.out.println("\nA disciplina é obrigatória? (sim/não):");
            System.out.print("> ");
            String resposta = scanner.nextLine().toLowerCase();
            boolean novaObrigatoriedade = resposta.equals("sim");
    
            limparTela();
    
            if (usuarioCarregado.getTipoUsuario() == TipoUsuario.SECRETARIA) {
                Secretaria secretaria = (Secretaria) usuarioCarregado;
                secretaria.atualizarInformacoesDisciplina(idDisciplina, novoNome, novoCusto, novaObrigatoriedade);
            } else {
                System.out.println(ANSI_RED + "OPS! Usuário não autorizado para atualizar a disciplina.\n" + ANSI_RESET);
            }
        } else {
            limparTela();
            System.out.println(ANSI_RED + "OPS! Disciplina não encontrada.\n" + ANSI_RESET);
        }
    }
    
    public static void atualizarInformacoesProfessor(Scanner scanner, Usuario usuarioCarregado) {
        limparTela();
        if (usuarioCarregado.getTipoUsuario() == TipoUsuario.SECRETARIA) {
            Professor.listar();
    
            System.out.println("\nDigite o ID do professor que deseja atualizar:");
            System.out.print("> ");
            int idProfessor = scanner.nextInt();
            scanner.nextLine();
    
            Professor professor = Professor.carregarPorId(idProfessor);
            if (professor != null) {
                System.out.println("\nDigite o novo nome do professor:");
                System.out.print("> ");
                String novoNomeProfessor = scanner.nextLine();
    
                limparTela();
    
                Secretaria secretaria = (Secretaria) usuarioCarregado;
                secretaria.atualizarInformacoesProfessor(idProfessor, novoNomeProfessor);
            } else {
                limparTela();
                System.out.println(ANSI_RED + "OPS! Professor não encontrado.\n" + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "OPS! Usuário não autorizado para atualizar informações de professor." + ANSI_RESET);
        }
    }

    public static void atualizarInformacoesAluno(Scanner scanner, Usuario usuarioCarregado) {
        limparTela();
        if (usuarioCarregado.getTipoUsuario() == TipoUsuario.SECRETARIA) {
            Aluno.listar();
    
            System.out.println("\nDigite o ID do aluno que deseja atualizar:");
            System.out.print("> ");
            int idAluno = scanner.nextInt();
            scanner.nextLine();
    
            Aluno aluno = Aluno.carregarPorId(idAluno);
            if (aluno != null) {
                System.out.println("\nDigite o novo nome do aluno:");
                System.out.print("> ");
                String novoNomeAluno = scanner.nextLine();
    
                limparTela();
    
                Secretaria secretaria = (Secretaria) usuarioCarregado;
                secretaria.atualizarInformacoesAluno(idAluno, novoNomeAluno);
            } else {
                limparTela();
                System.out.println(ANSI_RED + "OPS! Aluno não encontrado.\n" + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "OPS! Usuário não autorizado para atualizar informações de aluno." + ANSI_RESET);
        }
    }

    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
