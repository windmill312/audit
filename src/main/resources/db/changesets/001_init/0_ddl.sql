CREATE TABLE site_visit
(
    id               SERIAL PRIMARY KEY,
    iso_country_code VARCHAR(2)         NOT NULL UNIQUE,
    visit_number     BIGINT NOT NULL
);

COMMENT ON COLUMN site_visit.id IS 'Идентификатор записи';
COMMENT ON COLUMN site_visit.iso_country_code IS 'Код страны в формате ISO 3166-1 alpha-2';
COMMENT ON COLUMN site_visit.visit_number IS 'Счетчик посещений';