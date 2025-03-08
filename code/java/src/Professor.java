import java.io.*;
import java.util.*;

public class Professor extends Usuario {
    public Professor(String nome, String login, String senha) {
        super(nome, login, senha, TipoUsuario.PROFESSOR);
    }

    public static Professor carregarPorId(int idProfessor) {
        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                int id = Integer.parseInt(dados[0]);
                if (id == idProfessor && dados[4].equals("PROFESSOR")) {
                    String nome = dados[1];
                    String login = dados[2];
                    String senha = dados[3];
                    return new Professor(nome, login, senha);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de usuários não encontrado.");
        }
        return null;
    }

    public List<Aluno> getAlunosMatriculadosNaDisciplina(int idDisciplina) {
        Disciplina disciplina = Disciplina.carregarPorId(idDisciplina);
        if (disciplina != null && disciplina.getIdProfessor() == this.getId()) {
            return Matricula.getAlunosMatriculadosNaDisciplina(idDisciplina);
        }
        return new ArrayList<>();
    }

    public static void listar() {
        System.out.println("Professores disponíveis:");
        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
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
        try (Scanner scanner = new Scanner(new File(ARQUIVOUSUARIO))) {
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
}
