import java.io.*;
import java.util.*;

public class Disciplina {
    static final String ARQUIVODISCIPLINA = "code/java/csv/disciplinas.txt";
    private static final int MINIMOALUNOS = 3;
    private static final int LIMITEALUNOS = 60;

    private int idDisciplina;
    private String nome;
    private float custo;
    private boolean ehObrigatoria;
    private int idCurso;
    private String status;
    private int numeroMatriculados;
    private List<Aluno> alunosMatriculados;

    public Disciplina(String nome, float custo, boolean ehObrigatoria, int idCurso) {
        this.idDisciplina = getProximoId();
        this.nome = nome;
        this.custo = custo;
        this.ehObrigatoria = ehObrigatoria;
        this.idCurso = idCurso;
        this.status = "Aberta";
        this.numeroMatriculados = 0;
        this.alunosMatriculados = new ArrayList<>();
    }

    public void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVODISCIPLINA, true))) {
            writer.write(idDisciplina + ";" + nome + ";" + custo + ";" + ehObrigatoria + ";" + status + ";" + idCurso
                    + ";" + numeroMatriculados);
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

    public static void listar() {
        System.out.println("\n Disciplinas disponíveis:");
        try (Scanner scanner = new Scanner(new File(ARQUIVODISCIPLINA))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                System.out.println("ID: " + dados[0] + " | Nome: " + dados[1] + " | Custo: " + dados[2] + " | Obrigatória: " + dados[3] + " | Status: " + dados[4] + " | ID do Curso: " + dados[5]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nenhuma disciplina cadastrada.");
        }
    }

    public boolean matricularAluno(Aluno aluno) {
        if (numeroMatriculados < LIMITEALUNOS) {
            alunosMatriculados.add(aluno);
            numeroMatriculados++;
            return true;
        } else {
            System.out.println("Matrículas encerradas para esta disciplina.");
            return false;
        }
    }

    public void cancelarMatricula(Aluno aluno) {
        if (alunosMatriculados.remove(aluno)) {
            numeroMatriculados--;
            System.out.println("Matrícula cancelada para o aluno: " + aluno.getNome());
        }
    }

    public void verificarStatusDisciplina() {
        if (numeroMatriculados < MINIMOALUNOS) {
            status = "Cancelada";
        }
    }

    public List<Aluno> getAlunosMatriculados() {
        return alunosMatriculados;
    }

    public String getNome() {
        return nome;
    }

    // Teste
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDigite o nome da disciplina:");
        String nome = scanner.nextLine();

        System.out.println("Digite o custo da disciplina:");
        float custo = scanner.nextInt();

        System.out.println("A disciplina é obrigatória? (true/false):");
        boolean obrigatoria = scanner.nextBoolean();

        Curso.listar();
        int idCurso;
        do {
            System.out.println("Digite o ID do curso ao qual a disciplina pertence:");
            idCurso = scanner.nextInt();
            if (!Curso.cursoExiste(idCurso)) {
                System.out.println("Curso não encontrado! Escolha um ID válido.");
            }
        } while (!Curso.cursoExiste(idCurso));

        Disciplina novaDisciplina = new Disciplina(nome, custo, obrigatoria, idCurso);
        novaDisciplina.salvar();

        System.out.println("Disciplina cadastrada com sucesso!");

        scanner.close();
    }
}
