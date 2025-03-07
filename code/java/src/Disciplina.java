import java.io.*;
import java.util.*;

public class Disciplina {
    private static final String ARQUIVODISCIPLINA = "code/java/csv/disciplinas.txt";
    private static final int MINIMOALUNOS = 3;
    private static final int LIMITEALUNOS = 60;
    
    private int idDisciplina;
    private String nome;
    private int creditos;
    private boolean ehObrigatoria;
    private int idCurso;
    private String status;
    private int numeroMatriculados;

    public Disciplina(String nome, int creditos, boolean ehObrigatoria, int idCurso) {
        this.idDisciplina = getProximoId();
        this.nome = nome;
        this.creditos = creditos;
        this.ehObrigatoria = ehObrigatoria;
        this.idCurso = idCurso;
        this.status = "Aberta";
        this.numeroMatriculados = 0;
    }

    public void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVODISCIPLINA, true))) {
            writer.write(idDisciplina + ";" + nome + ";" + creditos + ";" + ehObrigatoria + ";" + status + ";" + idCurso + ";" + numeroMatriculados);
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

    public boolean matricularAluno() {
        if (numeroMatriculados < LIMITEALUNOS) {
            numeroMatriculados++;
            return true;
        } else {
            System.out.println("Matrículas encerradas para esta disciplina.");
            return false;
        }
    }

    public void verificarStatusDisciplina() {
        if (numeroMatriculados < MINIMOALUNOS) {
            status = "Cancelada";
        }
    }

    // Teste
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDigite o nome da disciplina:");
        String nome = scanner.nextLine();

        System.out.println("Digite a quantidade de créditos:");
        int creditos = scanner.nextInt();

        System.out.println("A disciplina é obrigatória? (true/false):");
        boolean obrigatoria = scanner.nextBoolean();

        Curso.listarCursos();
        int idCurso;
        do {
            System.out.println("Digite o ID do curso ao qual a disciplina pertence:");
            idCurso = scanner.nextInt();
            if (!Curso.cursoExiste(idCurso)) {
                System.out.println("Curso não encontrado! Escolha um ID válido.");
            }
        } while (!Curso.cursoExiste(idCurso));

        Disciplina novaDisciplina = new Disciplina(nome, creditos, obrigatoria, idCurso);
        novaDisciplina.salvar();

        System.out.println("Disciplina cadastrada com sucesso!");

        scanner.close();
    }

    public String getNome() {
        return nome;
    }
}
    
