package com.manoelcampos.retornoboleto;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Processa arquivos de retorno de boletos bancários utilizando a implementação de
 * alguma estratégia ({@link LeituraRetorno}).
 * Esta é uma classe que chamamos de Estrategista,
 * por poder utilizar diferentes estratégias de acordo com as necessidades,
 * podendo mudar a estratégia a ser utilizada até em tempo de execução.
 *
 * @author Manoel Campos da Silva Filho
 */
public class ProcessarBoletos {
    private LeituraRetorno leituraRetorno;

    /**
     * Instancia a classe estrategista, já indicando
     * @param leituraRetorno
     */
    public ProcessarBoletos(final LeituraRetorno leituraRetorno){
        this.leituraRetorno = leituraRetorno;
    }

    /**
     * Realiza de fato o processamento de um dado arquivo de retorno de boleto bancário,
     * utilizando uma estratégia definida em {@link #leituraRetorno}.
     * Este método pode realizar diversas operações após a leitura do arquivo,
     * como gravar dados em um banco, enviar emails de notificação, etc.
     * Neste caso, por simplificação, estamos apenas imprimindo os dados no terminal.
     *
     * @param caminhoArquivo Caminho (URI) do arquivo a ser lido
     */
    public final void processar(URI caminhoArquivo){
        System.out.println("Boletos");
        System.out.println("----------------------------------------------------------------------------------");
        final List<Boleto> boletos = processarB(caminhoArquivo);
        for (Boleto boleto : boletos) {
            System.out.println(boleto);
        }
    }

    public final List<Boleto> processarB(URI caminhoArquivo){
        try (var reader = Files.newBufferedReader(Paths.get(caminhoArquivo))) {
            String line;
            final var listaBoletos = new ArrayList<Boleto>();
            while ((line = reader.readLine()) != null) {
                final String[] vetor = line.split(";");
                var boleto = leituraRetorno.lerArquivo(vetor);
                listaBoletos.add(boleto);
            }
            return listaBoletos;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Altera a estratégia a ser utilizada para leitura de arquivos de retorno de boletos bancários.
     * @param leituraRetorno nova estratégia a ser utilizada
     */
    public void setLeituraRetorno(final LeituraRetorno leituraRetorno) {
        this.leituraRetorno = leituraRetorno;
    }
}
