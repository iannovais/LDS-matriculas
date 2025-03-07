import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Usuario {
    private static final String ARQUIVOUSUARIO = "code/java/csv/usuarios.txt";

    private int id;
    private String nome;
    private String login;
    private String senha;
    private boolean logado;

    private Usuario(String nome, String login, String senha) {
        this.id = getProximoId();
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.logado = false;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public static Usuario cadastrar(String nome, String login, String senha) {
        if (loginExiste(login)) 
            throw new IllegalArgumentException("Este login já está em uso. Por favor, escolha outro login.");
        
        Usuario novoUsuario = new Usuario(nome, login, senha);
        novoUsuario.salvar();

        return novoUsuario;
    }

    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOUSUARIO, true))) {
            writer.write(id + ";" + nome + ";" + login + ";" + senha);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getProximoId() {
        int ultimoId = 0;

        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(";");
                ultimoId = Integer.parseInt(dados[0]);
            }
        } catch (FileNotFoundException e) {
            return 1;
        }

        return ultimoId++;
    }

    public static Usuario carregar(String login) {
        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(";");

                if (dados[2].equals(login)) {
                    return new Usuario(dados[1], dados[2], dados[3]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean loginExiste(String login) {
        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(";");

                if (dados[2].equals(login)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }

        return false;
    }

    public void entrar(String login, String senha) {
        if (this.login.equals(login) && this.senha.equals(senha)) {
            this.logado = true;
        } else {
            System.out.println("Login ou senha incorretos.");
        }
    }

    public void sair() {
        if (this.logado) {
            this.logado = false;
        } else 
            System.out.println("Você precisa estar logado para sair.");
    }

    // TIRAR ESSE MAIN, APENAS PARA TESTES
    public static void main(String[] args) {
        int opcao;
        Scanner scanner = new Scanner(System.in);

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

                    try {
                        Usuario.cadastrar(nome, login, senha);
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
                        if (usuarioCarregado.logado) {
                            System.out.println("Bem-vindo, " + usuarioCarregado.getNome() + "!");
                            usuarioCarregado.sair();
                        }
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

        } while (opcao == 1);

        scanner.close();
    }
}