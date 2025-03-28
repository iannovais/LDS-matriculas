import java.io.*;
import java.util.*;

public class Disciplina {
    static final String ARQUIVODISCIPLINA = "code/java/csv/disciplinas.txt";
    protected static final int MINIMOALUNOS = 3;
    protected static final int LIMITEALUNOS = 60;

    private int idDisciplina;
    private String nome;
    private float custo;
    private boolean ehObrigatoria;
    private int idCurso;
    private int idProfessor;
    private StatusDisciplina status;
    private int numeroMatriculados;

    public Disciplina(String nome, float custo, boolean ehObrigatoria, int idCurso, int idProfessor) {
        this.idDisciplina = getProximoId();
        this.nome = nome;
        this.custo = custo;
        this.ehObrigatoria = ehObrigatoria;
        this.idCurso = idCurso;
        this.idProfessor = idProfessor;
        this.status = StatusDisciplina.CRIADA; 
        this.numeroMatriculados = 0;
    }

    public String getNome() {
        return nome;
    }

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public int getNumeroMatriculados() {
        return numeroMatriculados;
    }

    public float getCusto() {
        return custo;
    }

    public boolean isEhObrigatoria() {
        return ehObrigatoria;
    }

    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public StatusDisciplina getStatus() {
        return this.status;
    }

    public void setStatus(StatusDisciplina status) {
        this.status = status;
    }

    public void setNumeroMatriculados(int numeroMatriculados) {
        this.numeroMatriculados = numeroMatriculados;
    }

    public void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVODISCIPLINA, true))) {
            writer.write(idDisciplina + ";" + nome + ";" + custo + ";" + ehObrigatoria + ";" + status + ";" + idCurso
                    + ";" + idProfessor + ";" + numeroMatriculados);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getProximoId() {
        int ultimoId = 0;
        try (Scanner scanner = new Scanner(new File(ARQUIVODISCIPLINA))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                ultimoId = Integer.parseInt(dados[0]);
            }
        } catch (FileNotFoundException e) {
            return 1;
        }
        return ultimoId + 1;
    }

    public static void listarDisciplinas() {
        System.out.println("Disciplinas disponíveis:");
        try (Scanner scanner = new Scanner(new File(ARQUIVODISCIPLINA))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                System.out.println("ID: " + dados[0] + " | Nome: " + dados[1] + " | Custo: " + dados[2]
                        + " | Obrigatória: " + dados[3] + " | Status: " + dados[4] + " | ID do Curso: " + dados[5]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nenhuma disciplina cadastrada.");
        }
    }

    public boolean matricularAluno(Aluno aluno) {
        if (numeroMatriculados < LIMITEALUNOS) {
            numeroMatriculados++;
            verificarQuantidadeAlunosDisciplina();
            atualizarArquivoDisciplina();
            return true;
        } else {
            System.out.println("Matrículas encerradas para esta disciplina.");
            return false;
        }
    }

    public void cancelarMatriculaAluno(Aluno aluno) {
        numeroMatriculados--;
        atualizarArquivoDisciplina();
    }

    public void verificarQuantidadeAlunosDisciplina() {
        if (numeroMatriculados >= LIMITEALUNOS) {
            status = StatusDisciplina.ENCERRADA;
        }
    }

    public static Disciplina carregarPorId(int idDisciplina) {
        try (Scanner scanner = new Scanner(new File(ARQUIVODISCIPLINA))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                int id = Integer.parseInt(dados[0]);
                if (id == idDisciplina) {
                    String nome = dados[1];
                    float custo = Float.parseFloat(dados[2]);
                    boolean ehObrigatoria = Boolean.parseBoolean(dados[3]);
                    StatusDisciplina status = StatusDisciplina.valueOf(dados[4]);
                    int idCurso = Integer.parseInt(dados[5]);
                    int idProfessor = Integer.parseInt(dados[6]);
                    int numeroMatriculados = Integer.parseInt(dados[7]);

                    Disciplina disciplina = new Disciplina(nome, custo, ehObrigatoria, idCurso, idProfessor);
                    disciplina.setIdDisciplina(id);
                    disciplina.setStatus(status);
                    disciplina.setNumeroMatriculados(numeroMatriculados);
                    return disciplina;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de disciplinas não encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("Erro ao ler os dados da disciplina.");
        }
        return null;
    }

    public static List<Disciplina> carregarTodasDisciplinas() {
        List<Disciplina> disciplinas = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(ARQUIVODISCIPLINA))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                float custo = Float.parseFloat(dados[2]);
                boolean ehObrigatoria = Boolean.parseBoolean(dados[3]);
                StatusDisciplina status = StatusDisciplina.valueOf(dados[4]);
                int idCurso = Integer.parseInt(dados[5]);
                int idProfessor = Integer.parseInt(dados[6]);
                int numeroMatriculados = Integer.parseInt(dados[7]);

                Disciplina disciplina = new Disciplina(nome, custo, ehObrigatoria, idCurso, idProfessor);
                disciplina.setIdDisciplina(id);
                disciplina.setStatus(status);
                disciplina.setNumeroMatriculados(numeroMatriculados);
                disciplinas.add(disciplina);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de disciplinas não encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("Erro ao ler os dados da disciplina.");
        }
        return disciplinas;
    }

    public void atualizarInformacoes(String novoNome, float novoCusto, boolean novaObrigatoriedade) {
        this.nome = novoNome;
        this.custo = novoCusto;
        this.ehObrigatoria = novaObrigatoriedade;
        this.atualizarArquivoDisciplina();
    }

    public void atualizarArquivoDisciplina() {
        List<String> linhas = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(ARQUIVODISCIPLINA))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(";");
                if (Integer.parseInt(dados[0]) == this.idDisciplina) {
                    linha = idDisciplina + ";" + nome + ";" + custo + ";" + ehObrigatoria + ";" + status + ";" + idCurso
                            + ";" + idProfessor + ";" + numeroMatriculados;
                }
                linhas.add(linha);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVODISCIPLINA))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listarDisciplinasDoProfessor(int idProfessor) {
        System.out.println("Disciplinas do professor:");
        try (Scanner scanner = new Scanner(new File(ARQUIVODISCIPLINA))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                int idProfessorDisciplina = Integer.parseInt(dados[6]);
                if (idProfessorDisciplina == idProfessor) {
                    System.out.println("ID: " + dados[0] + " | Nome: " + dados[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nenhuma disciplina cadastrada.");
        }
    }
}