-- 1) Geben Sie alle Mannschaften aus, die bei der Bodenseeregatta im Jahr 2014 teilgenommen haben. Wenn eine Mannschaft mit einem Boot mit der Segelfläche kleiner als 20 m² teilgenommen hat, soll auch die ID des Bootes ausgegeben werden.
SELECT mname FROM nimmt_teil WHERE rname = 'Bodenseeregatta' AND rjahr='2014';
SELECT mname, id FROM nimmt_teil INNER JOIN sportboot ON nimmt_teil.sportboot = sportboot.id WHERE rname = 'Bodenseeregatta' AND rjahr='2014' AND segelflaeche < 20;


-- 2) Geben Sie den Namen und das Geburtsdatum der jüngsten Trainer aus (können auch mehrere sein).
SELECT name, geburtsdatum FROM Person NATURAL JOIN Trainer WHERE geburtsdatum = (SELECT MAX(geburtsdatum) FROM person);


-- 3) Geben Sie alle Personen geordnet nach Geburtsdatum aus, die sowohl Segler als auch Trainer sind, allerdings in keiner Mannschaft dabei sind.
-- SELECT DISTINCT person.key, person.name, geburtsdatum FROM person NATURAL JOIN trainer, segler, mannschaft WHERE person.key != ALL (SELECT DISTINCT mannschaft.key FROM mannschaft) ORDER BY geburtsdatum;
SELECT trainer.name FROM (SELECT * FROM person NATURAL JOIN trainer AS t) AS trainer NATURAL JOIN (SELECT * FROM person NATURAL JOIN segler AS s) AS segler FULL JOIN mannschaft ON trainer.key = mannschaft.key WHERE mannschaft.name IS NULL ORDER BY geburtsdatum;


-- 4) Geben Sie alle Personen geordnet nach Geburtsdatum aus, die entweder Segler oder Trainer sind, jedoch nicht beides und vermerken Sie in einer Spalte, ob es sich um einen Trainer oder einen Segler handelt.


/*
SELECT * FROM trainer UNION SELECT * FROM segler;
-> Alle Personen bzw. keys, die entweder Trainer oder Segler sind.

SELECT * FROM (SELECT * FROM person NATURAL JOIN trainer AS t) AS trainer NATURAL JOIN (SELECT * FROM person NATURAL JOIN segler AS s) AS st;
-> Alle Personen bzw. keys, die sowohl Trainer, als auch Segler sind.


SELECT * FROM (SELECT * FROM trainer UNION SELECT * FROM segler) AS alle FULL JOIN (SELECT * FROM (SELECT * FROM person NATURAL JOIN trainer AS t) AS trainer NATURAL JOIN (SELECT * FROM person NATURAL JOIN segler AS s) AS st) AS abc ON alle.key = abc.key WHERE abc.key IS NULL;
-> Alle Personen bzw. keys, die Trainer oder Segler, aber nicht beides sind.
*/

SELECT person.* FROM (SELECT * FROM trainer UNION SELECT * FROM segler) AS alle LEFT JOIN (SELECT * FROM (SELECT * FROM person NATURAL JOIN trainer AS t) AS trainer NATURAL JOIN (SELECT * FROM person NATURAL JOIN segler AS s) AS st) AS abc ON alle.key = abc.key INNER JOIN person ON alle.key= person.key WHERE abc.key IS NULL ORDER BY geburtsdatum;


-- 5) Geben Sie die Regatten (Name und Jahr) mit den wenigsten Wettfahrten an und geben Sie auch die Anzahl aus.
SELECT name, jahr, COUNT(name) FROM wettfahrt GROUP BY name, jahr HAVING COUNT(name) = (SELECT MIN(anzahl) FROM (SELECT COUNT(name) AS anzahl FROM wettfahrt GROUP BY name, jahr) AS max_anz);


-- 6) Geben Sie die Namen jener Trainer aus, die zwei oder mehr Mannschaften betreuen.
-- SELECT person.name, COUNT(mannschaft.name) FROM mannschaft INNER JOIN person ON person.key = mannschaft.key GROUP BY person.key HAVING COUNT(mannschaft.name) >= 2;
SELECT person.name FROM mannschaft INNER JOIN person ON person.key = mannschaft.key GROUP BY person.key HAVING COUNT(mannschaft.name) >= 2;


-- 7) Welche Altersklasse ist am aktivsten (hat an den meisten Wettfahrten Punkte erzielt)?
-- SELECT mannschaft.aklasse, COUNT(erzielt.punkte) FROM mannschaft INNER JOIN erzielt ON mannschaft.name=erzielt.mname GROUP BY mannschaft.aklasse HAVING COUNT(erzielt.punkte) = (SELECT MAX(max_punkte) FROM (SELECT mannschaft.aklasse, COUNT(erzielt.punkte) AS max_punkte FROM mannschaft INNER JOIN erzielt ON mannschaft.name=erzielt.mname GROUP BY mannschaft.aklasse) AS aktiv);
SELECT mannschaft.aklasse FROM mannschaft INNER JOIN erzielt ON mannschaft.name=erzielt.mname GROUP BY mannschaft.aklasse HAVING COUNT(erzielt.punkte) = (SELECT MAX(max_punkte) FROM (SELECT mannschaft.aklasse, COUNT(erzielt.punkte) AS max_punkte FROM mannschaft INNER JOIN erzielt ON mannschaft.name=erzielt.mname GROUP BY mannschaft.aklasse) AS aktiv);


-- 8) Um wieviel gehen Tourenboote durchschnittlich tiefer als Sportboote?
SELECT (SELECT AVG(tiefgang) FROM tourenboot NATURAL JOIN boot) - (SELECT AVG(tiefgang) FROM sportboot NATURAL JOIN boot) AS tiefgang;


-- 9) Geben Sie für alle Mannschaften aus, an wievielen Regatten sie bereits teilgenommen haben und wieviele Punkte sie dort erzielt haben.
SELECT mname, COUNT(DISTINCT wname), SUM(punkte) FROM erzielt GROUP BY mname;


-- 10) Welches Land bietet die längste Wettfahrtsstrecke und hat zusätzlich nicht die kürzeste?
SELECT def.land FROM (SELECT land FROM wettfahrt NATURAL JOIN regatta WHERE laenge = (SELECT MAX(laenge) FROM wettfahrt AS abc) GROUP BY land) AS def LEFT JOIN (SELECT land FROM wettfahrt NATURAL JOIN regatta WHERE laenge = (SELECT MIN(laenge) FROM wettfahrt AS ghi) GROUP BY land) AS mno ON def.land=mno.land WHERE mno.land IS NULL;


-- 11) Wie heißt der Trainer, der die Mannschaft mit den meisten Punkten trainiert hat?
--SELECT person.name, mname, SUM(punkte) FROM erzielt INNER JOIN mannschaft ON mannschaft.name = erzielt.mname INNER JOIN person ON mannschaft.key = person.key GROUP BY mname, person.name HAVING SUM(punkte) = (SELECT MAX(summe) FROM (SELECT SUM(punkte) AS summe FROM erzielt GROUP BY mname) AS max_summe);
SELECT person.name FROM erzielt INNER JOIN mannschaft ON mannschaft.name = erzielt.mname INNER JOIN person ON mannschaft.key = person.key GROUP BY mname, person.name HAVING SUM(punkte) = (SELECT MAX(summe) FROM (SELECT SUM(punkte) AS summe FROM erzielt GROUP BY mname) AS max_summe);


-- 12) Geben Sie für JEDE Mannschaft aus, wieviele Punkte Sie bei der 'Bodenseeregatta' in 'Oesterreich' erzielt haben.
SELECT mname, SUM(punkte) FROM erzielt NATURAL JOIN regatta WHERE wname = 'Bodenseeregatta' AND land = 'Oesterreich' GROUP BY mname;


-- 13) Geben Sie die ID und den Namen jener Sportboote aus, die mindestens an zwei Regatten Teil genommen haben, aber keiner Mannschaft zugewiesen sind.
-- SELECT boot.id, boot.name, COUNT(boot.id) FROM nimmt_teil INNER JOIN boot ON sportboot = boot.id LEFT JOIN zugewiesen ON boot.id = zugewiesen.id WHERE zugewiesen.id IS NULL GROUP BY boot.id, boot.name HAVING COUNT(boot.id) >= 2;
SELECT boot.id, boot.name FROM nimmt_teil INNER JOIN boot ON sportboot = boot.id LEFT JOIN zugewiesen ON boot.id = zugewiesen.id WHERE zugewiesen.id IS NULL GROUP BY boot.id, boot.name HAVING COUNT(boot.id) >= 2;


-- 14) Geben Sie die Regatten (Name, Jahr und Land) aus, die über die kürzeste Distanz gehen.
-- SELECT name, jahr, land, MIN(laenge) FROM wettfahrt NATURAL JOIN regatta GROUP BY name, jahr, land HAVING MIN(laenge) = (SELECT MIN(laenge) FROM wettfahrt);
SELECT name, jahr, land FROM wettfahrt NATURAL JOIN regatta GROUP BY name, jahr, land HAVING MIN(laenge) = (SELECT MIN(laenge) FROM wettfahrt);