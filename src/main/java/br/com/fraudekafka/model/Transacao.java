
package br.com.fraudekafka.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transacao {

    private String id;
    private String clienteId;
    private double valor;
    private String cidade;
    private LocalDateTime dataHora;

    public Transacao(String clienteId, double valor, String cidade) {
        this.id = UUID.randomUUID().toString();
        this.clienteId = clienteId;
        this.valor = valor;
        this.cidade = cidade;
        this.dataHora = LocalDateTime.now();
    }


    public Transacao(
            String id,
            String clienteId,
            double valor,
            String cidade,
            LocalDateTime dataHora
    ) {
        this.id = id;
        this.clienteId = clienteId;
        this.valor = valor;
        this.cidade = cidade;
        this.dataHora = dataHora;
    }


    public String getId() {
        return id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public double getValor() {
        return valor;
    }

    public String getCidade() {
        return cidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id='" + id + '\'' +
                ", clienteId='" + clienteId + '\'' +
                ", valor=" + valor +
                ", cidade='" + cidade + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}
