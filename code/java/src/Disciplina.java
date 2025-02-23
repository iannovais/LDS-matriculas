import java.util.List;

public class Disciplina {
    private String nome;
    private int creditos;
    private boolean ehObrigatoria;
    private List<Aluno> alunosMatriculados;
    private int limiteAlunos = 60;
    private int minimoAlunos = 3;
    private String status;

    public String statusDisciplina() {
        return status;
    }

    public void gerarCurriculo() {
    }

    public void fecharMatriculas() {
    }

    public void adicionarAluno(Aluno aluno) {
    }

    public void removerAluno(Aluno aluno) {
    }

    public List<Aluno> alunosMatriculados() {
        return null;
    }

    public void cancelarDisciplina() {
    }
}