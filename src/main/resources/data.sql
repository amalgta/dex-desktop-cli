DROP TABLE IF EXISTS patterns;
 
CREATE TABLE patterns (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  type VARCHAR(250) NOT NULL,
  name VARCHAR(250) NOT NULL,
  pattern VARCHAR(250) DEFAULT NULL
);
 
INSERT INTO patterns (type, name, pattern)
	VALUES
	('MovieFileName', 'TamilRockers', 'www.TamilRockers.[a-z]{2,4} - (?<title>[a-zA-Z ()-]*) \((?<year>[0-9]{4})\).[a-z]{2,4}');
INSERT INTO patterns (type, name, pattern)
	VALUES
	('MovieFileName','Normal','(?<title>[a-zA-Z ]+) *\[(?<year>[0-9]{4})\].[a-z]{2,4}');
INSERT INTO patterns (type, name, pattern)
  	VALUES('MovieFileName','Normal_2','(?<title>[a-zA-Z ]+) *\((?<year>[0-9]{4})\).[a-z]{2,4}');