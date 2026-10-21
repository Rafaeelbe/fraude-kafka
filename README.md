# Sistema de Detecção de Fraudes com Apache Kafka

## Objetivo

Projeto acadêmico desenvolvido em Java para simular transações financeiras, processá-las de forma assíncrona com Apache Kafka, identificar possíveis fraudes e armazenar os registros em PostgreSQL.

## Tecnologias utilizadas

- Java 25
- Apache Kafka 3.9.1
- PostgreSQL 16
- Maven
- Docker Compose

## Regras de detecção

- **ALTO_VALOR:** transação com valor igual ou superior a R$ 10.000.
- **TEMPO_60s:** quatro transações do mesmo cliente em menos de 60 segundos.
- **GEO_10m:** transações do mesmo cliente em cidades diferentes dentro de 10 minutos.

## Estrutura do projeto

- `producer/`: gera transações simuladas e publica mensagens no Kafka.
- `consumer/`: consome as mensagens e identifica possíveis fraudes.
- `database/`: conexão e persistência no PostgreSQL.
- `model/`: representação da transação.
- `docker-compose.yml`: configuração dos serviços Kafka, ZooKeeper e PostgreSQL.
- `schema.sql`: estrutura da tabela de transações.

## Como executar

### 1. Iniciar os serviços

Com Docker Desktop em execução, abra um terminal na raiz do projeto e execute:

```bash
docker compose up -d
```

### 2. Criar a tabela no PostgreSQL

```bash
docker compose exec -T postgres psql -U frauduser -d fraudedb -f /dev/stdin < schema.sql
```

### 3. Compilar o projeto

No IntelliJ IDEA, abra a janela Maven e execute `clean` e depois `compile`.

### 4. Executar o consumidor

Execute a classe `br.com.fraudekafka.consumer.ConsumidorTransacoes`.

### 5. Executar o produtor

Em outra execução do IntelliJ, execute `br.com.fraudekafka.producer.ProdutorTransacoes`.

O produtor envia uma transação aproximadamente a cada três segundos. O consumidor processa as mensagens, imprime alertas de fraude e salva os registros no PostgreSQL.

## Consultar os registros

```bash
docker compose exec postgres psql -U frauduser -d fraudedb -c "SELECT * FROM transacoes ORDER BY data_hora DESC LIMIT 10;"
```

## Observação

Este projeto utiliza dados simulados e regras simplificadas para fins acadêmicos. Os alertas representam suspeitas geradas pelas regras, não uma confirmação de fraude real.