import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Aluno extends Usuario {
    private static final int MAXOBRIGATORIAS = 4;
    private static final int MAXOPTATIVAS = 2;

    public Aluno(String nome, String login, String senha) {
        super(nome, login, senha, TipoUsuario.ALUNO);
    }

    public void visualizarMatriculas() {
        List<Integer> disciplinasIds = Matricula.carregarDisciplinasDoAluno(this.getId());
        System.out.println("Você está matruculado nas seguintes matérias: ");
        for (int idDisciplina : disciplinasIds) {
            Disciplina disciplina = Disciplina.carregarPorId(idDisciplina);
            if (disciplina != null) {
                System.out.println("- " + disciplina.getNome());
            }
        }
    }

    public void matricularEmDisciplina(Disciplina disciplina) {
        if (Matricula.alunoJaMatriculado(this.getId(), disciplina.getIdDisciplina())) {
            System.out.println("\u001B[31mOPS! Você já está matriculado nesta disciplina.\u001B[0m");
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

        if (disciplina.isEhObrigatoria()) {
            if (countObrigatorias >= MAXOBRIGATORIAS) {
                System.out.println("\u001B[31mOPS! Limite de matrículas em disciplinas obrigatórias atingido.\u001B[0m");
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
            System.out.println("Matrícula realizada com sucesso!");
        }
    }

    public void cancelarMatricula(Disciplina disciplina) {
        List<Matricula> matriculas = Matricula.carregar();
        for (Matricula matricula : matriculas) {
            if (matricula.getIdAluno() == this.getId() && matricula.getIdDisciplina() == disciplina.getIdDisciplina() && matricula.isAtiva()) {
                matricula.setAtiva(false);
                Matricula.atualizar(matriculas);
                disciplina.cancelarMatricula(this); 
                System.out.println("Matrícula cancelada com sucesso!");
                return;
            }
        }
        System.out.println("Aluno não está matriculado nesta disciplina.");
    }

    public static Aluno carregarPorId(int idAluno) {
    try (Scanner scanner = new Scanner(new File("code/java/csv/usuarios.txt"))) {
        while (scanner.hasNextLine()) {
            String[] dados = scanner.nextLine().split(";");
            int id = Integer.parseInt(dados[0]);
            if (id == idAluno && dados[4].equals("ALUNO")) {
                String nome = dados[1];
                String login = dados[2];
                String senha = dados[3];
                return new Aluno(nome, login, senha);
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("Arquivo de usuários não encontrado.");
    }
    return null;
}
}