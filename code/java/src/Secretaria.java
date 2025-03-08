import java.io.*;
import java.util.*;

public class Secretaria extends Usuario {

    public Secretaria(String nome, String login, String senha) {
        super(nome, login, senha, TipoUsuario.SECRETARIA);
    }

    public void atualizarInformacoesDisciplina(int idDisciplina, String novoNome, float novoCusto, boolean novaObrigatoriedade) {
        Disciplina disciplina = Disciplina.carregarPorId(idDisciplina);
        if (disciplina != null) {
            disciplina.atualizarInformacoes(novoNome, novoCusto, novaObrigatoriedade);
            System.out.println("\u001B[32mDisciplina atualizada com sucesso!\u001B[0m\n");
        } else {
            System.out.println("\u001B[32mDisciplina não encontrada!\u001B[0m\n");
        }
    }

    public void atualizarInformacoesProfessor(int idProfessor, String novoNome) {
        Usuario.atualizar(idProfessor, novoNome, TipoUsuario.PROFESSOR);
    }

    public void atualizarInformacoesAluno(int idAluno, String novoNome) {
        Usuario.atualizar(idAluno, novoNome, TipoUsuario.ALUNO);
    }

    public void abrirPeriodoMatriculas() {
        List<Disciplina> disciplinas = Disciplina.carregarTodasDisciplinas();
        boolean periodoAberto = false;

        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getStatus() == StatusDisciplina.CRIADA) {
                disciplina.setStatus(StatusDisciplina.ATIVA);
                disciplina.atualizarArquivoDisciplina();
                periodoAberto = true; // Indica que pelo menos uma disciplina foi atualizada
            }
        }

        if (periodoAberto) {
            System.out.println("\u001B[32mPeríodo de matrículas aberto com sucesso!\u001B[0m\n");
        } else {
            System.out.println("\u001B[31mNenhuma disciplina com status 'CRIADA' encontrada.\u001B[0m\n");
        }
    }
}