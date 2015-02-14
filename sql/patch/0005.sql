-- Добавим таблицу итоговых практик

CREATE TABLE finalpractics(
fpc_pcode serial,
fpc_name varchar(255),
fpc_number int NOT NULL DEFAULT 0,
fpc_length float NOT NULL,
fpc_plncode int NOT NULL,
CONSTRAINT finalpractics_pk PRIMARY KEY(fpc_pcode),
CONSTRAINT finalpractics_plans_fk FOREIGN KEY(fpc_plncode) REFERENCES plans(pln_pcode) ON UPDATE CASCADE ON DELETE CASCADE);
