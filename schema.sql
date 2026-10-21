CREATE TABLE IF NOT EXISTS transacoes (
                                          id VARCHAR(36) PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL,
    valor NUMERIC(12, 2) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    fraude BOOLEAN NOT NULL DEFAULT FALSE,
    tipo_fraude VARCHAR(30)
    );
