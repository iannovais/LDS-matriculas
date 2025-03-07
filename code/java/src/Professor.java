import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Professor extends Usuario {
    private List<Disciplina> disciplinas;

    public Professor(String nome, String login, String senha) {
        super(nome, login, senha, TipoUsuario.PROFESSOR);
    }

    public List<Aluno> alunosMatriculados(Disciplina disciplina) {
        if (disciplinas.contains(disciplina)) {
            return disciplina.getAlunosMatriculados();
        } else {
            System.out.println("O professor não leciona esta disciplina.");
            return null;
        }
    }

    public static void listar() {
        System.out.println("\nProfessores disponíveis:");
        try (Scanner scanner = new Scanner(new File("code/java/csv/usuarios.txt"))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                if (dados[4].equals("PROFESSOR")) { 
                    System.out.println("ID: " + dados[0] + " | Nome: " + dados[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nenhum professor cadastrado.");
        }
    }
    
    public static boolean existe(int idProfessor) {
        try (Scanner scanner = new Scanner(new File("code/java/csv/usuarios.txt"))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                if (Integer.parseInt(dados[0]) == idProfessor && dados[4].equals("PROFESSOR")) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }
    

    public void adicionarDisciplina(Disciplina disciplina) {
        disciplinas.add(disciplina);
    }
}
