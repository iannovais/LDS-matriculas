import java.io.*;
import java.util.*;

public class Aluno extends Usuario {
    private static final int MAXOBRIGATORIAS = 4;
    private static final int MAXOPTATIVAS = 2;

    int idCurso;

    public Aluno(String nome, String login, String senha, int idCurso) {
        super(nome, login, senha, TipoUsuario.ALUNO);
        this.idCurso = idCurso;
    }

    public void matricularEmDisciplina(Disciplina disciplina) {
        if (disciplina.getStatus() != StatusDisciplina.ATIVA) {
            System.out.print("\033[H\033[2J"); // Limpar tela
            System.out.println("\u001B[31mOPS! Esta disciplina não está disponível para matrícula.\u001B[0m\n");
            return;
        }
    
        if (Matricula.alunoJaMatriculado(this.getId(), disciplina.getIdDisciplina())) {
            System.out.print("\033[H\033[2J"); // Limpar tela
            System.out.println("\n\u001B[31mOPS! Você já está matriculado nesta disciplina.\u001B[0m\n");
            return;
        }
    
        List<Integer> disciplinasIds = Matricula.carregarDisciplinasDoAluno(this.getId());
        int countObrigatorias = 0;
        int countOptativas = 0;
    
        for (int idDisciplina : disciplinasIds) {
            Disciplina disc = Disciplina.carregarPorId(idDisciplina);
            if (disc != null) {
                if (disc.isEhObrigatoria()) {
                    countObrigatorias++;
                } else {
                    countOptativas++;
                }
            }
        }
    
        System.out.print("\033[H\033[2J"); // Limpar tela
    
        if (disciplina.isEhObrigatoria()) {
            if (countObrigatorias >= MAXOBRIGATORIAS) {
                System.out.println("\n\u001B[31mOPS! Limite de matrículas em disciplinas obrigatórias atingido.\u001B[0m\n");
                return;
            }
        } else {
            if (countOptativas >= MAXOPTATIVAS) {
                System.out.println("\u001B[31mOPS! Limite de matrículas em disciplinas optativas atingido.\u001B[0m");
                return;
            }
        }
    
        if (disciplina.matricularAluno(this)) {
            Matricula matricula = new Matricula(this.getId(), disciplina.getIdDisciplina(), true);
            matricula.salvar();
            System.out.print("\033[H\033[2J"); // Limpar tela
            System.out.println("\u001B[32mMatrícula realizada com sucesso!\u001B[0m\n");
        }
    }

    public void cancelarMatricula(Disciplina disciplina) {
        List<Matricula> matriculas = Matricula.carregarTodasMatriculas();
        for (Matricula matricula : matriculas) {
            if (matricula.getIdAluno() == this.getId() && matricula.getIdDisciplina() == disciplina.getIdDisciplina()
                    && matricula.isAtiva()) {
                matricula.setAtiva(false);
                Matricula.atualizarArquivo(matriculas);
                disciplina.cancelarMatriculaAluno(this);
                System.out.print("\033[H\033[2J"); //limpar tela
                System.out.println("\u001B[32mMatrícula cancelada com sucesso!\u001B[0m\n");
                return;
            }
        }
        System.out.println("Aluno não está matriculado nesta disciplina.");
    }

    public void visualizarMatriculas() {
        List<Integer> disciplinasIds = Matricula.carregarDisciplinasDoAluno(this.getId());
        System.out.println("Você está matrículado nas seguintes matérias: ");
        for (int idDisciplina : disciplinasIds) {
            Disciplina disciplina = Disciplina.carregarPorId(idDisciplina);
            if (disciplina != null) {
                System.out.println("- " + disciplina.getNome());
            }
        }
        System.out.println();
    }

    public static Aluno carregarPorId(int idAluno) {
        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                int id = Integer.parseInt(dados[0]);
                if (id == idAluno && dados[4].equals("ALUNO")) {
                    String nome = dados[1];
                    String login = dados[2];
                    String senha = dados[3];
                    int idCurso = Integer.parseInt(dados[5]);
                    return new Aluno(nome, login, senha, idCurso);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de usuários não encontrado.");
        }
        return null;
    }

    public static void listarAlunos() {
        System.out.println("Alunos disponíveis:");
        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                if (dados[4].equals("ALUNO")) {
                    System.out.println("ID: " + dados[0] + " | Nome: " + dados[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nenhum professor cadastrado.");
        }
    }

    public void consultarCobranca() {
        Cobranca cobranca = new Cobranca(this);
        if (cobranca.gerarCobranca()) {
            cobranca.consultarCobranca();
        } else {
            System.out.println("Erro ao gerar cobrança.");
        }
    }
}