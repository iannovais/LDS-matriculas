import java.util.*;

public class Cobranca {
    private Aluno aluno;
    private List<Disciplina> disciplinasCobradas;
    private float valorTotal;

    public Cobranca(Aluno aluno) {
        this.aluno = aluno;
        this.disciplinasCobradas = new ArrayList<>();
        this.valorTotal = 0.0f;
    }

    public boolean gerarCobranca() {
        List<Integer> disciplinasIds = Matricula.carregarDisciplinasDoAluno(aluno.getId());
        for (int idDisciplina : disciplinasIds) {
            Disciplina disciplina = Disciplina.carregarPorId(idDisciplina);
            if (disciplina != null) {
                disciplinasCobradas.add(disciplina);
            }
        }
        this.valorTotal = calcularValorTotal();
        return true;
    }

    public float calcularValorTotal() {
        float total = 0.0f;
        for (Disciplina disciplina : disciplinasCobradas) {
            total += disciplina.getCusto();
        }
        return total;
    }

    public void consultarCobranca() {
        System.out.print("\033[H\033[2J");
        System.out.println("\nDisciplinas cobradas:");
        for (Disciplina disciplina : disciplinasCobradas) {
            System.out.println("- " + disciplina.getNome() + " (R$ " + disciplina.getCusto() + ")");
        }
        System.out.println("\nValor total a ser pago: R$ " + valorTotal + "\n");
    }
}