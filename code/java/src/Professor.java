import java.util.List;

public class Professor extends Usuario {
    private List<Disciplina> disciplinas;

    public Professor(String nome, String login, String senha) {
        super(nome, login, senha, TipoUsuario.PROFESSOR);
    }

    public List<Aluno> alunosMatriculados(Disciplina disciplina) {
        return null;
    }
}
