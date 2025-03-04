ALTER TABLE place MODIFY COLUMN coordinates POINT not null SRID 4326;
CREATE SPATIAL INDEX idx_coordinates ON place (coordinates);