CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE shops (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    location GEOGRAPHY(Point, 4326) NOT NULL,
    last_updated TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_shops_location ON shops USING GIST (location);
CREATE INDEX idx_shops_last_updated ON shops (last_updated);
