ALTER TABLE plans ADD COLUMN pln_years int NOT NULL DEFAULT 0;
ALTER TABLE plans ADD COLUMN pln_months int NOT NULL DEFAULT 0;
ALTER TABLE plans ADD COLUMN pln_date date;
ALTER TABLE specialities DROP COLUMN spc_length;