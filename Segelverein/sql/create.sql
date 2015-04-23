-- df personennamen: word=datafiles/personennamen.txt
-- df bootsnamen: word=datafiles/bootsnamen.txt
-- df altersklassen: word=datafiles/altersklassen.txt
-- df bootsklassen: word=datafiles/bootsklassen.txt
-- df laender: word=datafiles/laender.txt
-- df jahre: word=datafiles/jahre.txt
-- df regattennamen: word=datafiles/regattennamen.txt
-- df mannschaftsnamen: word=datafiles/mannschaftsnamen.txt

CREATE TABLE Person (			-- df: mult=2.0
	key SERIAL,
	name TEXT,			-- df: use=personennamen
	geburtsdatum DATE,
	PRIMARY KEY (key),
	CHECK (geburtsdatum >= '1900-01-01')
);


CREATE TABLE Segler	(
	key INTEGER,
	PRIMARY KEY (key),
	FOREIGN KEY (key) REFERENCES Person,
	CHECK (key > 0)
);


CREATE TABLE Trainer	(
	key INTEGER,
	PRIMARY KEY (key),
	FOREIGN KEY (key) REFERENCES Person,
	CHECK (key > 0)
);


CREATE TABLE Boot	(
	id SERIAL,
	name TEXT,			-- df: use=bootsnamen
	personen INTEGER,
	tiefgang DECIMAL,
	PRIMARY KEY (id),
	CHECK (personen > 0 AND personen < 20),
	CHECK (tiefgang > 0)
);


CREATE TABLE Tourenboot	(
	id INTEGER,
	bootsklasse TEXT,		-- df: use=mannschaftsnamen
	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES Boot,
	CHECK (id > 0)
);


CREATE TABLE Sportboot	(
	id INTEGER,
	segelflaeche DECIMAL,
	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES Boot,
	CHECK (id > 0)
);


CREATE TABLE Mannschaft	(
	name TEXT,			-- df: use=mannschaftsnamen
	aklasse TEXT,			-- df: use=altersklassen
	key INTEGER,
	PRIMARY KEY (name),
	UNIQUE (name, key),
	/* Jeder Mannschaft darf nur genau ein Trainer zugeordnet werden! */
	FOREIGN KEY (key) REFERENCES Trainer,
	CHECK (key > 0)
);


CREATE TABLE Regatta	(			-- df: mult=1.0
	name TEXT NOT NULL,			-- df: use=regattennamen
	jahr INTEGER NOT NULL,			-- df: use=jahre
	land TEXT NOT NULL,			-- df: use=laender
	PRIMARY KEY (name, jahr),
	CHECK (jahr >= 1900)
);


CREATE TABLE Wettfahrt	(
	name TEXT,
	jahr INTEGER,
	datum DATE,
	laenge DECIMAL,
	PRIMARY KEY (name, jahr, datum),
	UNIQUE (name, jahr, datum),
	/* Wettfahrt muss einer Regatta eindeutig zugeordnet werden koennen! */
	FOREIGN KEY (name, jahr) REFERENCES Regatta,
	CHECK (jahr >= 1900),
	CHECK (datum >= '1900-01-01'),
	CHECK (laenge > 0)
);


CREATE TABLE bildet	(
	key SERIAL,
	name TEXT,
	PRIMARY KEY (key, name),
	FOREIGN KEY (key) REFERENCES Segler,
	FOREIGN KEY (name) REFERENCES Mannschaft
);


CREATE TABLE zugewiesen	(
	id SERIAL,
	name TEXT,
	PRIMARY KEY (id, name),
	FOREIGN KEY (id) REFERENCES Boot,
	FOREIGN KEY (name) REFERENCES Mannschaft
);


CREATE TABLE nimmt_teil	(
	mname TEXT,
	rname TEXT,
	rjahr INTEGER,
	sportboot INTEGER,
	startnr INTEGER, 
	PRIMARY KEY (mname, rname, rjahr, sportboot),
	FOREIGN KEY (mname) REFERENCES Mannschaft (name),
	
	/*
	FOREIGN KEY (rname) REFERENCES Regatta (name),
	FOREIGN KEY (rjahr) REFERENCES Regatta (jahr),
	-> kein Unique Constraint!
	*/
	
	FOREIGN KEY (rname, rjahr) REFERENCES Regatta (name, jahr),
	FOREIGN KEY (sportboot) REFERENCES Sportboot (id),
	CHECK (rjahr >= 1900),
	CHECK (sportboot > 0),
	CHECK (startnr > 0)
);


CREATE TABLE erzielt	(
	mname TEXT,
	wname TEXT,
	wjahr INTEGER,
	wdatum DATE,
	punkte INTEGER,
	PRIMARY KEY (mname, wname, wjahr, wdatum),
	FOREIGN KEY (mname) REFERENCES Mannschaft (name),
	
	/*
	FOREIGN KEY (wname) REFERENCES Wettfahrt (name),
	FOREIGN KEY (wjahr) REFERENCES Wettfahrt (jahr),
	FOREIGN KEY (wdatum) REFERENCES Wettfahrt (datum)
	-> kein Unique Constraint!
	*/
	
	FOREIGN KEY (wname, wjahr, wdatum) REFERENCES Wettfahrt (name, jahr, datum),
	CHECK (wjahr >= 1900),
	CHECK (wdatum >= '1900-01-01'),
	CHECK (punkte > 0)
);