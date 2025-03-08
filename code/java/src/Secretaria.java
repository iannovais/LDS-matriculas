import java.io.*;
import java.util.*;

public class Secretaria extends Usuario {
    public Secretaria(String nome, String login, String senha) {
        super(nome, login, senha, TipoUsuario.SECRETARIA);
    }

    public void gerarCurriculo() {
    }

    public void atualizarInformacoesDisciplina(int idDisciplina, String novoNome, float novoCusto,
            boolean novaObrigatoriedade, String novoStatus) {
        List<String> linhas = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(Disciplina.ARQUIVODISCIPLINA))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (Integer.parseInt(dados[0]) == idDisciplina) {
                    encontrado = true;
                    dados[1] = novoNome;
                    dados[2] = String.valueOf(novoCusto);
                    dados[3] = String.valueOf(novaObrigatoriedade);
                    dados[4] = novoStatus;
                    linha = String.join(";", dados);
                }
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (encontrado) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Disciplina.ARQUIVODISCIPLINA))) {
                for (String linha : linhas) {
                    writer.write(linha);
                    writer.newLine();
                }
                System.out.println("\n\u001B[32mDisciplina atualizada com sucesso!\u001B[0m\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("\n\u001B[32mDisciplina não encontrada!\u001B[0m\n");
        }
    }

    public void atualizarInformacoesProfessor() {
    }

    public void atualizarInformacoesAluno() {
    }
}
