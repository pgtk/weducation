---------------------------------------------------------
--  Добавление поля для формы экзамена в модуле
--------------------------------------------------------

ALTER TABLE modules ADD COLUMN mod_exfcode int;
ALTER TABLE modules ADD CONSTRAINT modules_examforms_fk FOREIGN KEY(mod_exfcode) REFERENCES examforms(exf_pcode) ON UPDATE CASCADE ON DELETE NO ACTION;