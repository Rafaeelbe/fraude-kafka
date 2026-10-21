
package br.com.fraudekafka.consumer;

import br.com.fraudekafka.database.RepositorioTransacao;
import br.com.fraudekafka.model.Transacao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConsumidorTransacoes {

    public static void main(String[] args) {

        Properties propriedades = new Properties();
        propriedades.put("bootstrap.servers", "localhost:9092");
        propriedades.put("group.id", "detector-fraudes");
        propriedades.put("auto.offset.reset", "earliest");
        propriedades.put("enable.auto.commit", "true");
        propriedades.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        propriedades.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        DetectorFraude detector = new DetectorFraude();
        RepositorioTransacao repositorio = new RepositorioTransacao();

        try (KafkaConsumer<String, String> consumidor =
                     new KafkaConsumer<>(propriedades)) {

            consumidor.subscribe(List.of("transacoes"));

            System.out.println("Consumidor iniciado. Aguardando transações...");

            while (true) {
                ConsumerRecords<String, String> registros =
                        consumidor.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> registro : registros) {
                    try {
                        String[] dados = registro.value().split(";", -1);

                        Transacao transacao = new Transacao(
                                dados[0],
                                dados[1],
                                Double.parseDouble(dados[2]),
                                dados[3],
                                LocalDateTime.parse(dados[4])
                        );

                        List<String> fraudes = detector.analisar(transacao);

                        boolean fraude = !fraudes.isEmpty();
                        String tipoFraude = fraude
                                ? String.join(",", fraudes)
                                : null;

                        repositorio.salvar(transacao, fraude, tipoFraude);

                        System.out.println("Transação processada: " + transacao);

                        if (fraude) {
                            System.out.println("⚠️ ALERTA DE FRAUDE: "
                                    + tipoFraude + " | Cliente: "
                                    + transacao.getClienteId()
                                    + " | Valor: R$ " + transacao.getValor());
                        }

                    } catch (Exception e) {
                        System.err.println("Erro ao processar transação: "
                                + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Erro no consumidor Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
