import java.util.ArrayList;
import java.util.List;

public class Professor extends Usuario {
    private List<Disciplina> disciplinas;

    public Professor(String nome, String login, String senha, List<Disciplina> disciplinas) {
        super(nome, login, senha);
        this.disciplinas = disciplinas;
    }

    public List<Aluno> alunosMatriculados(Disciplina disciplina) {
        if (!disciplinas.contains(disciplina)) {
            System.out.println("O professor não ministra essa disciplina.");
            return new ArrayList<>();
        }

        return disciplina.getAlunosMatriculados();
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
}
