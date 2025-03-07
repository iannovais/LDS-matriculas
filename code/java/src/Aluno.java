import java.util.*;

public class Aluno extends Usuario {
    private static final int MAXOBRIGATORIAS = 4;
    private static final int MAXOPTATIVAS = 2;

    private Curso curso;
    private List<Disciplina> disciplinasMatriculadas = new ArrayList<>();

    public Aluno(String nome, String login, String senha, Curso curso) {
        super(nome, login, senha);
        this.curso = curso;
    }

    public void matricularEmDisciplina(Disciplina disciplina) {
        if (disciplinasMatriculadas.size() >= MAXOBRIGATORIAS + MAXOPTATIVAS) {
            System.out.println("Limite de disciplinas atingido.");
            return;
        }
        disciplinasMatriculadas.add(disciplina);
        System.out.println("Matrícula realizada na disciplina: " + disciplina.getNome());
    }

    public void cancelarMatricula(Disciplina disciplina) {
        if (disciplinasMatriculadas.remove(disciplina)) {
            System.out.println("Matrícula cancelada na disciplina: " + disciplina.getNome());
        } else {
            System.out.println("Você não está matriculado nesta disciplina.");
        }
    }

    public void confirmarMatricula() {
        System.out.println("Matrícula confirmada para as seguintes disciplinas:");
        for (Disciplina disciplina : disciplinasMatriculadas) {
            System.out.println("- " + disciplina.getNome());
        }
    }
}
