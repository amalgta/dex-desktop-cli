INSERT INTO patterns (type, name, pattern, sample)
	values
	('MovieFileName', 'TamilRockers', 'www.TamilRockers.[a-z]{2,4} - (?<title>[a-zA-Z ()-]*) \((?<year>[0-9]{4})\) *\[[a-zA-Z 0-9\-]*\].[a-z0-9]{2,4}',''),
	('MovieFileName', 'TamilRockerrs', 'www.TamilRockerrs.[a-z]{2,4} - (?<title>[a-zA-Z ()-]*) \((?<year>[0-9]{4})\) *\[[a-zA-Z 0-9\-]*\].[a-z0-9]{2,4}','www.TamilRockerrs.co - Lonappante Mamodisa (2019) [Malayalam Original - DVDRip - x264 - 850MB].mkv'),
	('MovieFileName','Normal','(?<title>[a-zA-Z \)\(]+) *\[(?<year>[0-9]{4})\].[a-z0-9]{2,4}',''),
  ('MovieFileName','Normal_2','(?<title>[a-zA-Z \)\(]+) *\((?<year>[0-9]{4})\).[a-z0-9]{2,4}','');