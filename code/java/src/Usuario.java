import java.io.*;
import java.util.*;

public class Usuario {
    protected static final String ARQUIVOUSUARIO = "code/java/csv/usuarios.txt";

    private int id;
    private String nome;
    private String login;
    private String senha;
    private TipoUsuario tipoUsuario;

    protected Usuario(String nome, String login, String senha, TipoUsuario tipoUsuario) {
        this.id = getProximoId();
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
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

    public void setId(int id) {
        this.id = id;
    }

    public void entrar(String login, String senha) {
        if (!(this.login.equals(login) && this.senha.equals(senha)))
            throw new IllegalArgumentException("Login ou senha incorretos.");
    }

    public static Usuario cadastrar(String nome, String login, String senha, TipoUsuario tipoUsuario, int idCurso) {
        if (loginExiste(login))
            throw new IllegalArgumentException("Este login já está em uso. Por favor, escolha outro login.");

        Usuario novoUsuario;
        switch (tipoUsuario) {
            case ALUNO:
                novoUsuario = new Aluno(nome, login, senha, idCurso);
                novoUsuario.salvarAluno(idCurso);
                break;
            case PROFESSOR:
                novoUsuario = new Professor(nome, login, senha);
                novoUsuario.salvar();
                break;
            case SECRETARIA:
                novoUsuario = new Secretaria(nome, login, senha);
                novoUsuario.salvar();
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido.");
        }

        return novoUsuario;
    }

    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOUSUARIO, true))) {
            writer.write(id + ";" + nome + ";" + login + ";" + senha + ";" + tipoUsuario + ";");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarAluno(int idCurso) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOUSUARIO, true))) {
            writer.write(id + ";" + nome + ";" + login + ";" + senha + ";" + tipoUsuario + ";" + idCurso);
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
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    String senha = dados[3];
                    TipoUsuario tipoUsuario = TipoUsuario.valueOf(dados[4]);
    
                    switch (tipoUsuario) {
                        case ALUNO:
                            if (dados.length > 5) {
                                int idCurso = Integer.parseInt(dados[5]);
                                return new Aluno(nome, login, senha, idCurso) {
                                    {
                                        setId(id);
                                    }
                                };
                            } else {
                                throw new IllegalArgumentException("Dados do aluno incompletos no arquivo.");
                            }
                        case PROFESSOR:
                            return new Professor(nome, login, senha) {
                                {
                                    setId(id);
                                }
                            };
                        case SECRETARIA:
                            return new Secretaria(nome, login, senha) {
                                {
                                    setId(id);
                                }
                            };
                        default:
                            throw new IllegalArgumentException("Tipo de usuário inválido.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de usuários não encontrado.");
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

    public static void atualizar(int idUsuario, String novoNome, TipoUsuario tipoUsuario) {
        List<String> linhas = new ArrayList<>();
        boolean encontrado = false;

        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(";");
                if (Integer.parseInt(dados[0]) == idUsuario && dados[4].equals(tipoUsuario.toString())) {
                    encontrado = true;
                    dados[1] = novoNome;
                    linha = String.join(";", dados);
                }
                linhas.add(linha);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (encontrado) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOUSUARIO))) {
                for (String linha : linhas) {
                    writer.write(linha);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("\u001B[32mInformações atualizadas com sucesso!\u001B[0m\n");
        } else {
            System.out.println("\n\u001B[32m" + tipoUsuario.toString().toLowerCase() + " não encontrado!\u001B[0m\n");
        }
    }
}