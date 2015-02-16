-- Создадим таблицу для ГОС экзаменов по отдельным дисциплинам

CREATE TABLE gosexams(
gex_pcode serial,
gex_subcode int NOT NULL,
gex_plncode int NOT NULL,
CONSTRAINT gosexams_pk PRIMARY KEY(gex_pcode),
CONSTRAINT gosexams_plans_fk FOREIGN KEY(gex_plncode) REFERENCES plans(pln_pcode) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT gosexams_subjects_fk FOREIGN KEY(gex_subcode) REFERENCES subjects(sub_pcode) ON UPDATE CASCADE ON DELETE CASCADE);
