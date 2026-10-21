
package br.com.fraudekafka.consumer;

import br.com.fraudekafka.model.Transacao;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DetectorFraudeTest {

    @Test
    void deveIdentificarTransacaoDeAltoValor() {
        DetectorFraude detector = new DetectorFraude();

        Transacao transacao = criarTransacao(
                "1", "CLIENTE-1", 10000,
                "Juazeiro do Norte", LocalDateTime.now()
        );

        List<String> fraudes = detector.analisar(transacao);

        assertTrue(fraudes.contains("ALTO_VALOR"));
    }

    @Test
    void deveIdentificarQuatroTransacoesEmMenosDe60Segundos() {
        DetectorFraude detector = new DetectorFraude();
        LocalDateTime horario = LocalDateTime.of(
                2026, 10, 21, 10, 0
        );

        detector.analisar(criarTransacao(
                "1", "CLIENTE-1", 100,
                "Juazeiro do Norte", horario
        ));

        detector.analisar(criarTransacao(
                "2", "CLIENTE-1", 100,
                "Juazeiro do Norte", horario.plusSeconds(10)
        ));

        detector.analisar(criarTransacao(
                "3", "CLIENTE-1", 100,
                "Juazeiro do Norte", horario.plusSeconds(20)
        ));

        List<String> fraudes = detector.analisar(criarTransacao(
                "4", "CLIENTE-1", 100,
                "Juazeiro do Norte", horario.plusSeconds(30)
        ));

        assertTrue(fraudes.contains("TEMPO_60s"));
    }

    @Test
    void deveIdentificarTransacoesEmCidadesDiferentes() {
        DetectorFraude detector = new DetectorFraude();
        LocalDateTime horario = LocalDateTime.of(
                2026, 10, 21, 10, 0
        );

        detector.analisar(criarTransacao(
                "1", "CLIENTE-1", 100,
                "Juazeiro do Norte", horario
        ));

        List<String> fraudes = detector.analisar(criarTransacao(
                "2", "CLIENTE-1", 100,
                "Crato", horario.plusMinutes(5)
        ));

        assertTrue(fraudes.contains("GEO_10m"));
    }

    @Test
    void naoDeveIdentificarFraudeEmTransacaoComum() {
        DetectorFraude detector = new DetectorFraude();

        Transacao transacao = criarTransacao(
                "1", "CLIENTE-1", 500,
                "Juazeiro do Norte", LocalDateTime.now()
        );

        List<String> fraudes = detector.analisar(transacao);

        assertFalse(fraudes.contains("ALTO_VALOR"));
        assertFalse(fraudes.contains("TEMPO_60s"));
        assertFalse(fraudes.contains("GEO_10m"));
    }

    private Transacao criarTransacao(
            String id,
            String clienteId,
            double valor,
            String cidade,
            LocalDateTime dataHora
    ) {
        return new Transacao(
                id, clienteId, valor, cidade, dataHora
        );
    }
}
