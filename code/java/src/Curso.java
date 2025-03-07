import java.io.*;
import java.util.Scanner;

public class Curso {
    private static final String ARQUIVOCURSO = "code/java/csv/cursos.txt";

    private int idCurso;
    private String nome;
    private int creditos;

    public Curso(String nome, int creditos) {
        this.idCurso = getProximoId();
        this.nome = nome;
        this.creditos = creditos;
    }

    public void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOCURSO, true))) {
            writer.write(idCurso + ";" + nome + ";" + creditos);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listar() {
        System.out.println("\nCursos disponíveis:");
        try (Scanner scanner = new Scanner(new File(ARQUIVOCURSO))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                System.out.println("ID: " + dados[0] + " | Nome: " + dados[1] + " | Créditos: " + dados[2]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nenhum curso cadastrado.");
        }
    }

    public static boolean cursoExiste(int idCurso) {
        try (Scanner scanner = new Scanner(new File(ARQUIVOCURSO))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                if (Integer.parseInt(dados[0]) == idCurso) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    public static int getProximoId() {
        int ultimoId = 0;
        try (Scanner scanner = new Scanner(new File(ARQUIVOCURSO))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                ultimoId = Integer.parseInt(dados[0]);
            }
        } catch (FileNotFoundException e) {
            return 1;
        }
        return ultimoId + 1;
    }
}
