ALTER TABLE recebiveis ADD COLUMN cedente VARCHAR(255);

UPDATE recebiveis
SET
    cedente = 'Cedente Não Informado'
WHERE
    cedente IS NULL;

ALTER TABLE recebiveis ALTER COLUMN cedente SET NOT NULL;