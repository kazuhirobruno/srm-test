CREATE TABLE recebiveis (
    id BIGSERIAL PRIMARY KEY,
    valor_face NUMERIC(18, 4) NOT NULL,
    prazo INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    moeda_original VARCHAR(10) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP
    WITH
        TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE taxas_cambio (
    id BIGSERIAL PRIMARY KEY,
    moeda_origem VARCHAR(10) NOT NULL,
    moeda_destino VARCHAR(10) NOT NULL,
    fator_conversao NUMERIC(18, 6) NOT NULL,
    created_at TIMESTAMP
    WITH
        TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT uq_par_moedas UNIQUE (moeda_origem, moeda_destino)
);