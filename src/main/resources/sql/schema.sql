CREATE TABLE IF NOT EXISTS coordinates (
    id  SERIAL PRIMARY KEY CHECK (id > 0),
    x   DOUBLE PRECISION NOT NULL,
    y   DOUBLE PRECISION NOT NULL
);;

CREATE TABLE if not exists location (
    id   SERIAL PRIMARY KEY CHECK (id > 0),
    x    DOUBLE PRECISION NOT NULL,
    y    DOUBLE PRECISION NOT NULL,
    z    BIGINT NOT NULL,
    name VARCHAR(255) CHECK (name IS NULL OR length(trim(name)) > 0)
);;

CREATE TABLE if not exists person (
    id             SERIAL PRIMARY KEY CHECK (id > 0),
    name           VARCHAR(255) NOT NULL CHECK (length(trim(name)) > 0),
    coordinates_id INT  NOT NULL REFERENCES coordinates (id) ON DELETE RESTRICT,
    creation_date  DATE NOT NULL DEFAULT CURRENT_DATE,
    eye_color      VARCHAR(10) CHECK (eye_color IN ('GREEN', 'BLUE', 'YELLOW', 'ORANGE', 'BROWN')),
    hair_color     VARCHAR(10) NOT NULL CHECK (hair_color IN ('GREEN', 'BLUE', 'YELLOW', 'ORANGE', 'BROWN')),
    location_id    INT  NOT NULL REFERENCES location (id) ON DELETE RESTRICT,
    height         REAL NOT NULL CHECK (height > 0),
    nationality    VARCHAR(10) CHECK (nationality IN ('RUSSIA', 'CHINA', 'INDIA'))
);;

CREATE OR REPLACE FUNCTION delete_one_person_by_height(h REAL) RETURNS INT AS $$
DECLARE
    deleted_id INT;
BEGIN
    DELETE FROM person
    WHERE id = (SELECT id FROM person WHERE height = h ORDER BY id LIMIT 1)
    RETURNING id INTO deleted_id;
    RETURN COALESCE(deleted_id, 0);
END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION sum_person_height() RETURNS DOUBLE PRECISION AS $$
    SELECT COALESCE(SUM(height), 0)::DOUBLE PRECISION FROM person;
$$ LANGUAGE sql;;

CREATE OR REPLACE FUNCTION person_lower_than(h REAL) RETURNS SETOF person AS $$
    SELECT * FROM person WHERE height < h ORDER BY id;
$$ LANGUAGE sql;;

CREATE OR REPLACE FUNCTION hair_color_percent(c VARCHAR) RETURNS DOUBLE PRECISION AS $$
    SELECT (CASE WHEN COUNT(*) = 0 THEN 0
                 ELSE 100.0 * COUNT(*) FILTER (WHERE hair_color = c) / COUNT(*)
            END)::DOUBLE PRECISION
    FROM person;
$$ LANGUAGE sql;;

CREATE OR REPLACE FUNCTION eye_color_count(c VARCHAR) RETURNS BIGINT AS $$
    SELECT COUNT(*) FROM person WHERE eye_color = c;
$$ LANGUAGE sql;;
