CREATE FUNCTION clearOldGroupMarks(integer) RETURNS void AS $$
DELETE FROM fmarks WHERE fmk_subcode NOT IN (SELECT sub_pcode FROM subjects, groups 
  WHERE (sub_plncode = grp_plncode) AND (grp_pcode = $1)) 
AND (fmk_crdcode IN (SELECT crd_pcode FROM cards WHERE (crd_grpcode = $1)));
DELETE FROM fmarks WHERE fmk_modcode NOT IN (SELECT mod_pcode FROM modules, groups 
  WHERE (mod_plncode = grp_plncode) AND (grp_pcode = $1)) 
AND (fmk_crdcode IN (SELECT crd_pcode FROM cards WHERE (crd_grpcode = $1)));
DELETE FROM cmarks WHERE cmk_subcode NOT IN (SELECT sub_pcode FROM subjects, groups 
  WHERE (sub_plncode = grp_plncode) AND (grp_pcode = $1)) 
AND (cmk_crdcode IN (SELECT crd_pcode FROM cards WHERE (crd_grpcode = $1)));
DELETE FROM fpmarks WHERE fpm_fpccode NOT IN (SELECT fpc_pcode FROM finalpractics, groups 
  WHERE (fpc_plncode = grp_plncode) AND (grp_pcode = $1)) 
AND (fpm_crdcode IN (SELECT crd_pcode FROM cards WHERE (crd_grpcode = $1)));
DELETE FROM gmarks WHERE gmk_subcode NOT IN (SELECT sub_pcode FROM subjects, groups 
  WHERE (sub_plncode = grp_plncode) AND (grp_pcode = $1)) 
AND (gmk_crdcode IN (SELECT crd_pcode FROM cards WHERE (crd_grpcode = $1)));
DELETE FROM pmarks WHERE pmk_prccode NOT IN (SELECT prc_pcode FROM practics, groups 
  WHERE (prc_plncode = grp_plncode) AND (grp_pcode = $1)) 
AND (pmk_crdcode IN (SELECT crd_pcode FROM cards WHERE (crd_grpcode = $1)));
DELETE FROM monthmarks WHERE mmk_subcode NOT IN (SELECT sub_pcode FROM subjects, groups 
  WHERE (sub_plncode = grp_plncode) AND (grp_pcode = $1)) 
AND (mmk_crdcode IN (SELECT crd_pcode FROM cards WHERE (crd_grpcode = $1)));
$$ LANGUAGE SQL;


CREATE VIEW fmarkview AS 
SELECT 1 AS fmv_id, fmk_crdcode AS fmv_crdcode, SUM(fmk_mark) AS fmv_total, COUNT(fmk_mark) AS fmv_count 
  FROM fmarks WHERE (fmk_mark IS NOT NULL) AND (fmk_mark >= 0) AND (fmk_mark < 6) GROUP BY fmk_crdcode 
UNION SELECT 2, cmk_crdcode, SUM(cmk_mark), COUNT(cmk_mark) 
  FROM cmarks WHERE (cmk_mark IS NOT NULL) AND (cmk_mark >= 0) AND (cmk_mark < 6) GROUP BY cmk_crdcode 
UNION SELECT 3, fpm_crdcode, SUM(fpm_mark), COUNT(fpm_mark) 
  FROM fpmarks WHERE (fpm_mark IS NOT NULL) AND (fpm_mark >= 0) AND (fpm_mark < 6) GROUP BY fpm_crdcode 
UNION SELECT 4, gmk_crdcode, SUM(gmk_mark), COUNT(gmk_mark) 
  FROM gmarks WHERE (gmk_mark IS NOT NULL) AND (gmk_mark >= 0) AND (gmk_mark < 6) GROUP BY gmk_crdcode;


CREATE FUNCTION getAverageMark(integer) RETURNS decimal(3,2) AS $$ 
  SELECT CAST(CAST(SUM(fmv_total) AS decimal(5,2)) / SUM(fmv_count) AS decimal(3,2)) FROM fmarkview 
  WHERE (fmv_crdcode = $1); 
$$ LANGUAGE sql;


CREATE FUNCTION countMarks(int) RETURNS bigint AS $$ 
SELECT 
  (SELECT COUNT(*) FROM monthmarks WHERE mmk_psncode = $1) + 
  (SELECT COUNT(*) FROM fmarks, persons, cards WHERE (crd_pcode = fmk_crdcode) AND (psn_pcode = crd_psncode) AND (psn_pcode = $1));
$$ LANGUAGE SQL;

CREATE VIEW dublicates AS
SELECT psn_firstname AS dbl_fname, psn_middlename AS dbl_mname, psn_lastname dbl_lname, psn_birthdate AS dbl_birthdate, 
COUNT(psn_pcode) AS dbl_count
FROM persons WHERE (substring(psn_firstname, 1, 3) IN (SELECT DISTINCT substring(psn_firstname, 1, 3) FROM persons)) 
GROUP BY psn_firstname, psn_middlename, psn_lastname, psn_birthdate 
HAVING (COUNT(psn_pcode) > 1)
ORDER BY dbl_count DESC, psn_firstname;