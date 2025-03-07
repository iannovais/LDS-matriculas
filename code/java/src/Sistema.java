import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Cadastrar novo usuário");
            System.out.println("2 - Fazer login");
            System.out.println("3 - Sair");

            opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    System.out.println("Digite o nome:");
                    String nome = scanner.nextLine();

                    System.out.println("Digite o login:");
                    String login = scanner.nextLine();

                    System.out.println("Digite a senha:");
                    String senha = scanner.nextLine();

                    System.out.println("Selecione o tipo de usuário:");
                    System.out.println("1 - Aluno");
                    System.out.println("2 - Professor");
                    System.out.println("3 - Secretaria");
                    int tipo = scanner.nextInt();
                    scanner.nextLine();

                    TipoUsuario tipoUsuario;
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
                            continue;
                    }

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

                    if (usuarioCarregado != null) {
                        usuarioCarregado.entrar(loginLogin, senhaLogin);
                        if (usuarioCarregado.getTipoUsuario() != null) {
                            System.out.println("Bem-vindo, " + usuarioCarregado.getNome() + "!");
                        } else {
                            System.out.println("Erro ao carregar o tipo de usuário.");
                        }
                        usuarioCarregado.sair();
                    } else {
                        System.out.println("Usuário não encontrado.");
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
}