import java.util.List;

public class Aluno extends Usuario {
    private String nome;
    private Curso curso;
    private List<Disciplina> disciplinasObrigatorias;
    private List<Disciplina> disciplinasOptativas;
    private int maxObrigatorias = 4;
    private int maxOptativas = 2;

    public void matricularEmDisciplina(Disciplina disciplina) {
    }

    public void cancelarMatricula(Disciplina disciplina) {
    }

    public void confirmarMatricula() {
    }
}
