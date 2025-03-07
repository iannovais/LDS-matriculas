import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Cadastrar novo usuário");
            System.out.println("2 - Fazer login");
            System.out.println("3 - Sair do sistema");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Digite o nome:");
                    String nome = scanner.nextLine();

                    String login;
                    do {
                        System.out.println("Digite o login:");
                        login = scanner.nextLine();
                        if (Usuario.loginExiste(login)) {
                            System.out.println("Este login já está em uso. Por favor, escolha outro login.");
                        }
                    } while (Usuario.loginExiste(login));

                    System.out.println("Digite a senha:");
                    String senha = scanner.nextLine();

                    int tipo;
                    TipoUsuario tipoUsuario = null;

                    do {
                        System.out.println("Selecione o tipo de usuário:");
                        System.out.println("1 - Aluno");
                        System.out.println("2 - Professor");
                        System.out.println("3 - Secretaria");
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
                                System.out.println("Tipo de usuário inválido.");
                        }
                    } while (tipo < 1 || tipo > 3);

                    try {
                        Usuario.cadastrar(nome, login, senha, tipoUsuario);
                        System.out.println("Usuário cadastrado com sucesso!");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("Digite o login:");
                    String loginLogin = scanner.nextLine();

                    System.out.println("Digite a senha:");
                    String senhaLogin = scanner.nextLine();

                    Usuario usuarioCarregado = Usuario.carregar(loginLogin);

                    try {
                        usuarioCarregado.entrar(loginLogin, senhaLogin);
                        if (usuarioCarregado.getTipoUsuario() != null
                                && usuarioCarregado.getLogin().equals(loginLogin)) {
                            System.out.println("Login bem-sucedido!");

                            if (usuarioCarregado.getTipoUsuario() == TipoUsuario.ALUNO) {
                                menuAluno(scanner);
                            } else if (usuarioCarregado.getTipoUsuario() == TipoUsuario.PROFESSOR) {
                                menuProfessor(scanner);
                            } else if (usuarioCarregado.getTipoUsuario() == TipoUsuario.SECRETARIA) {
                                menuSecretaria(scanner, usuarioCarregado);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Login ou senha incorretos. Tente novamente.");
                    }

                    break;

                case 3:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida.");
                    break;
            }

        } while (opcao != 3);

        scanner.close();
    }

    public static void menuAluno(Scanner scanner) {
        int opcaoAluno;
        do {
            System.out.println("Bem-vindo, Aluno!");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Matricular em disciplina");
            System.out.println("2 - Cancelar matricula");
            System.out.println("3 - Confirmar matricula");
            System.out.println("4 - Sair");
            opcaoAluno = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoAluno) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcaoAluno != 4);
    }

    public static void menuProfessor(Scanner scanner) {
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

                    break;
                case 2:

                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcaoProfessor != 2);
    }

    public static void menuSecretaria(Scanner scanner, Usuario usuarioCarregado) {
        int opcaoSecretaria;
        do {
            System.out.println("Bem-vindo, Secretaria!");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Gerar currículo");
            System.out.println("2 - Atualizar informacoes de disciplina");
            System.out.println("3 - Atualizar informacoes de professor");
            System.out.println("4 - Atualizar informacoes de aluno");
            System.out.println("5 - Sair");
            opcaoSecretaria = scanner.nextInt();
            scanner.nextLine();

            switch (opcaoSecretaria) {
                case 1:

                    break;
                case 2:
                Disciplina.listar();
                
                System.out.println("Digite o ID da disciplina que deseja atualizar:");
                int idDisciplina = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Digite o novo nome da disciplina:");
                String novoNome = scanner.nextLine();

                System.out.println("Digite o novo custo da disciplina:");
                float novoCusto = scanner.nextInt();
                scanner.nextLine();

                System.out.println("A disciplina é obrigatória? (true/false):");
                boolean novaObrigatoriedade = scanner.nextBoolean();
                scanner.nextLine();

                System.out.println("Digite o novo status da disciplina (Aberta/Fechada):");
                String novoStatus = scanner.nextLine();

                if (usuarioCarregado.getTipoUsuario() == TipoUsuario.SECRETARIA) {
                    Secretaria secretaria = (Secretaria) usuarioCarregado; 
                    secretaria.atualizarInformacoesDisciplina(idDisciplina, novoNome, novoCusto, novaObrigatoriedade, novoStatus);
                } else {
                    System.out.println("Usuário não autorizado para atualizar a disciplina.");
                }
                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcaoSecretaria != 5);
    }
}
