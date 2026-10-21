# Sistema de Detecção de Fraudes com Apache Kafka

## Objetivo

Projeto acadêmico desenvolvido em Java para simular transações financeiras, processá-las de forma assíncrona com Apache Kafka, identificar possíveis fraudes e armazenar os registros em PostgreSQL.

## Tecnologias utilizadas

- Java 25
- Apache Kafka 3.9.1
- PostgreSQL 16
- Maven
- Docker Compose
- JUnit 5

## Regras de detecção

- **ALTO_VALOR:** transação com valor igual ou superior a R$ 10.000.
- **TEMPO_60s:** quatro transações do mesmo cliente em menos de 60 segundos.
- **GEO_10m:** transações do mesmo cliente em cidades diferentes dentro de 10 minutos.

## Estrutura do projeto

- `src/main/java/br/com/fraudekafka/producer/`: gera transações simuladas e publica mensagens no Kafka.
- `src/main/java/br/com/fraudekafka/consumer/`: consome mensagens e identifica possíveis fraudes.
- `src/main/java/br/com/fraudekafka/database/`: gerencia a conexão e a persistência no PostgreSQL.
- `src/main/java/br/com/fraudekafka/model/`: contém a representação da transação.
- `src/test/java/br/com/fraudekafka/consumer/`: contém os testes automatizados do detector de fraudes.
- `docker-compose.yml`: configura os serviços Kafka, ZooKeeper e PostgreSQL.
- `schema.sql`: define a estrutura da tabela de transações.
- `pom.xml`: configura as dependências e a compilação do projeto.

## Como executar

### 1. Iniciar os serviços

Com o Docker Desktop em execução, abra um terminal na raiz do projeto e execute:

```powershell
docker compose up -d
```

### 2. Criar a tabela no PostgreSQL

No PowerShell, execute:

```powershell
Get-Content .\schema.sql | docker compose exec -T postgres psql -U frauduser -d fraudedb
```

### 3. Compilar o projeto

No IntelliJ IDEA, abra a janela Maven e execute as tarefas `clean` e `compile`, nessa ordem.

### 4. Executar os testes automatizados

Na janela Maven do IntelliJ IDEA, abra `Lifecycle` e execute a tarefa `test`.

Os testes verificam as três regras de detecção de fraude e um cenário de transação comum.

### 5. Executar o consumidor

No IntelliJ IDEA, execute a classe:

`br.com.fraudekafka.consumer.ConsumidorTransacoes`

O consumidor ficará aguardando as mensagens publicadas no tópico Kafka `transacoes`.

### 6. Executar o produtor

Em outra execução do IntelliJ IDEA, execute a classe:

`br.com.fraudekafka.producer.ProdutorTransacoes`

O produtor envia transações simuladas aproximadamente a cada três segundos.

O consumidor processa as mensagens, imprime alertas quando identifica possíveis fraudes e salva as transações no PostgreSQL.

## Consultar os registros

Para consultar as dez transações mais recentes, execute no PowerShell:

```powershell
docker compose exec postgres psql -U frauduser -d fraudedb -c "SELECT * FROM transacoes ORDER BY data_hora DESC LIMIT 10;"
```

## Observação

Este projeto utiliza dados simulados e regras simplificadas para fins acadêmicos. Os alertas representam suspeitas identificadas pelas regras implementadas, não uma confirmação de fraude real.
