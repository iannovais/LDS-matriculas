public class Aluno extends Usuario {
    private static final int MAXOBRIGATORIAS = 4;
    private static final int MAXOPTATIVAS = 2;

    public Aluno(String nome, String login, String senha) {
        super(nome, login, senha, TipoUsuario.ALUNO);
    }

    public void matricularEmDisciplina(Disciplina disciplina) {
    }

    public void cancelarMatricula(Disciplina disciplina) {
    }

    public void confirmarMatricula() {
    }
}
