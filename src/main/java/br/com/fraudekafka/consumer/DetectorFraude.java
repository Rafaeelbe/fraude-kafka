
package br.com.fraudekafka.consumer;

import br.com.fraudekafka.model.Transacao;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetectorFraude {

    private final Map<String, Deque<Transacao>> historicoClientes =
            new HashMap<>();

    public List<String> analisar(Transacao transacao) {
        List<String> fraudes = new ArrayList<>();

        // Regra 1: transação de valor alto
        if (transacao.getValor() >= 10000) {
            fraudes.add("ALTO_VALOR");
        }

        Deque<Transacao> historico = historicoClientes.computeIfAbsent(
                transacao.getClienteId(),
                chave -> new ArrayDeque<>()
        );

        // Mantém apenas transações dos últimos 10 minutos
        while (!historico.isEmpty()) {
            Transacao antiga = historico.peekFirst();

            if (Duration.between(
                    antiga.getDataHora(),
                    transacao.getDataHora()
            ).getSeconds() >= 600) {
                historico.removeFirst();
            } else {
                break;
            }
        }

        // Regra 2: quatro transações em menos de 60 segundos
        long transacoesUltimoMinuto = historico.stream()
                .filter(t -> Duration.between(
                        t.getDataHora(),
                        transacao.getDataHora()
                ).getSeconds() < 60)
                .count();

        if (transacoesUltimoMinuto >= 3) {
            fraudes.add("TEMPO_60s");
        }

        // Regra 3: transações em cidades diferentes em até 10 minutos
        boolean cidadeDiferente = historico.stream()
                .anyMatch(t ->
                        !t.getCidade().equalsIgnoreCase(transacao.getCidade())
                );

        if (cidadeDiferente) {
            fraudes.add("GEO_10m");
        }

        historico.addLast(transacao);

        return fraudes;
    }
}
