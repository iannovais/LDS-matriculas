import java.io.*;
import java.util.*;

public class Secretaria extends Usuario {

    public Secretaria(String nome, String login, String senha) {
        super(nome, login, senha, TipoUsuario.SECRETARIA);
    }

    public void gerarCurriculo() {
        /// ?????????????
    }

    public void atualizarInformacoesDisciplina(int idDisciplina, String novoNome, float novoCusto,
            boolean novaObrigatoriedade, String novoStatus) {
        List<String> linhas = lerArquivo(Disciplina.ARQUIVODISCIPLINA);
        boolean encontrado = false;

        for (int i = 0; i < linhas.size(); i++) {
            String[] dados = linhas.get(i).split(";");
            if (Integer.parseInt(dados[0]) == idDisciplina) {
                encontrado = true;
                dados[1] = novoNome;
                dados[2] = String.valueOf(novoCusto);
                dados[3] = String.valueOf(novaObrigatoriedade);
                dados[4] = novoStatus;
                linhas.set(i, String.join(";", dados));
                break;
            }
        }

        if (encontrado) {
            escreverArquivo(Disciplina.ARQUIVODISCIPLINA, linhas);
            System.out.println("\u001B[32mDisciplina atualizada com sucesso!\u001B[0m\n");
        } else {
            System.out.println("\u001B[32mDisciplina não encontrada!\u001B[0m\n");
        }
    }

    public void atualizarInformacoesProfessor(int idProfessor, String novoNome) {
        atualizarInformacoesUsuario(idProfessor, novoNome, "PROFESSOR", Usuario.ARQUIVOUSUARIO);
    }

    public void atualizarInformacoesAluno(int idAluno, String novoNome) {
        atualizarInformacoesUsuario(idAluno, novoNome, "ALUNO", Usuario.ARQUIVOUSUARIO);
    }

    private void atualizarInformacoesUsuario(int idUsuario, String novoNome, String tipoUsuario, String caminhoArquivo) {
        List<String> linhas = lerArquivo(caminhoArquivo);
        boolean encontrado = false;

        for (int i = 0; i < linhas.size(); i++) {
            String[] dados = linhas.get(i).split(";");
            if (Integer.parseInt(dados[0]) == idUsuario && dados[4].equals(tipoUsuario)) {
                encontrado = true;
                dados[1] = novoNome;
                linhas.set(i, String.join(";", dados));
                break;
            }
        }

        if (encontrado) {
            escreverArquivo(caminhoArquivo, linhas);
            System.out.println("\n\u001B[32mInformações do " + tipoUsuario.toLowerCase() + " atualizadas com sucesso!\u001B[0m\n");
        } else {
            System.out.println("\n\u001B[32mO " + tipoUsuario.toLowerCase() + " não foi encontrado!\u001B[0m\n");
        }
    }

    private List<String> lerArquivo(String caminhoArquivo) {
        List<String> linhas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linhas;
    }

    private void escreverArquivo(String caminhoArquivo, List<String> linhas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}