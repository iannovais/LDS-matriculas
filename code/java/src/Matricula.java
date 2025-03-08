import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Matricula {
    private static final String ARQUIVOMATRICULAS = "code/java/csv/matriculas.txt";

    private int idAluno;
    private int idDisciplina;
    private boolean ativa;

    public Matricula(int idAluno, int idDisciplina, boolean ativa) {
        this.idAluno = idAluno;
        this.idDisciplina = idDisciplina;
        this.ativa = ativa;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOMATRICULAS, true))) {
            writer.write(idAluno + ";" + idDisciplina + ";" + ativa);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Matricula> carregar() {
        List<Matricula> matriculas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVOMATRICULAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                int idAluno = Integer.parseInt(dados[0]);
                int idDisciplina = Integer.parseInt(dados[1]);
                boolean ativa = Boolean.parseBoolean(dados[2]);
                matriculas.add(new Matricula(idAluno, idDisciplina, ativa));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matriculas;
    }

    public static void atualizar(List<Matricula> matriculas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOMATRICULAS))) {
            for (Matricula matricula : matriculas) {
                writer.write(matricula.getIdAluno() + ";" + matricula.getIdDisciplina() + ";" + matricula.isAtiva());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> carregarDisciplinasDoAluno(int idAluno) {
        List<Integer> disciplinasIds = new ArrayList<>();
        for (Matricula matricula : carregar()) {
            if (matricula.getIdAluno() == idAluno && matricula.isAtiva()) {
                disciplinasIds.add(matricula.getIdDisciplina());
            }
        }
        return disciplinasIds;
    }

    public static boolean alunoJaMatriculado(int idAluno, int idDisciplina) {
        for (Matricula matricula : carregar()) {
            if (matricula.getIdAluno() == idAluno && matricula.getIdDisciplina() == idDisciplina && matricula.isAtiva()) {
                return true;
            }
        }
        return false;
    }

    public static List<Aluno> getAlunosMatriculadosNaDisciplina(int idDisciplina) {
        List<Aluno> alunos = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(ARQUIVOMATRICULAS))) {
            while (scanner.hasNextLine()) {
                String[] dados = scanner.nextLine().split(";");
                int idDisciplinaMatricula = Integer.parseInt(dados[1]);
                boolean matriculaAtiva = Boolean.parseBoolean(dados[2]);
    
                if (idDisciplinaMatricula == idDisciplina && matriculaAtiva) {
                    int idAluno = Integer.parseInt(dados[0]);
                    Aluno aluno = Aluno.carregarPorId(idAluno);
                    if (aluno != null) {
                        alunos.add(aluno);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de matrículas não encontrado.");
        }
        return alunos;
    }
}