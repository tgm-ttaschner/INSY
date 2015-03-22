CREATE TABLE Person (
	key SERIAL,
	name TEXT,
	geburtsdatum DATE,
	PRIMARY KEY (key)
);


CREATE TABLE Segler	(
	key INTEGER,
	PRIMARY KEY (key),
	FOREIGN KEY (key) REFERENCES Person
);


CREATE TABLE Trainer	(
	key INTEGER,
	PRIMARY KEY (key),
	FOREIGN KEY (key) REFERENCES Person
);


CREATE TABLE Boot	(
	id SERIAL,
	name TEXT,
	personen INTEGER,
	tiefgang DECIMAL,
	PRIMARY KEY (id)
);


CREATE TABLE Tourenboot	(
	id INTEGER,
	bootsklasse TEXT,
	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES Boot
);


CREATE TABLE Sportboot	(
	id INTEGER,
	segelflaeche DECIMAL,
	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES Boot
);


CREATE TABLE Mannschaft	(
	name TEXT,
	aklasse TEXT,
	key INTEGER,
	PRIMARY KEY (name),
	UNIQUE (name, key),
	/* Jeder Mannschaft darf nur genau ein Trainer zugeordnet werden! */
	FOREIGN KEY (key) REFERENCES Trainer 
);


CREATE TABLE Regatta	(
	name TEXT,
	jahr INTEGER,
	land TEXT,
	PRIMARY KEY (name, jahr)
);


CREATE TABLE Wettfahrt	(
	name TEXT,
	jahr INTEGER,
	datum DATE,
	laenge DECIMAL,
	PRIMARY KEY (name, jahr, datum),
	UNIQUE (name, jahr, datum),
	/* Wettfahrt muss einer Regatta eindeutig zugeordnet werden koennen! */
	FOREIGN KEY (name, jahr) REFERENCES Regatta
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
	FOREIGN KEY (sportboot) REFERENCES Sportboot (id)
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
	
	FOREIGN KEY (wname, wjahr, wdatum) REFERENCES Wettfahrt (name, jahr, datum)
);