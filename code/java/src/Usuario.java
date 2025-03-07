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
    private TipoUsuario tipoUsuario;

    protected Usuario(String nome, String login, String senha, TipoUsuario tipoUsuario) {
        this.id = getProximoId();
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public static Usuario cadastrar(String nome, String login, String senha, TipoUsuario tipoUsuario) {
        if (loginExiste(login))
            throw new IllegalArgumentException("Este login já está em uso. Por favor, escolha outro login.");

        Usuario novoUsuario;
        switch (tipoUsuario) {
            case ALUNO:
                novoUsuario = new Aluno(nome, login, senha);
                break;
            case PROFESSOR:
                novoUsuario = new Professor(nome, login, senha);
                break;
            case SECRETARIA:
                novoUsuario = new Secretaria(nome, login, senha);
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido.");
        }

        novoUsuario.salvar();
        return novoUsuario;
    }

    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOUSUARIO, true))) {
            writer.write(id + ";" + nome + ";" + login + ";" + senha + ";" + tipoUsuario);
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

        return ultimoId + 1;
    }

    public static Usuario carregar(String login) {
        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(";");

                if (dados[2].equals(login)) {
                    TipoUsuario tipoUsuario = TipoUsuario.valueOf(dados[4]);
                    switch (tipoUsuario) {
                        case ALUNO:
                            return new Aluno(dados[1], dados[2], dados[3]);
                        case PROFESSOR:
                            return new Professor(dados[1], dados[2], dados[3]);
                        case SECRETARIA:
                            return new Secretaria(dados[1], dados[2], dados[3]);
                    }
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
}