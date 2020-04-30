INSERT INTO patterns (ID,type, name, pattern, sample)
	values
	(1,'MovieFileName', 'TamilRockers', 'www.TamilRockers.[a-z]{2,4} - (?<title>[a-zA-Z0-9. ()-]*) \((?<year>[0-9]{4})\) *[a-zA-Z .\[\]0-9\-]*.[a-z0-9]{2,4}',''),
	(2,'MovieFileName', 'TamilRockerrs', 'www.TamilRockerrs.[a-z]{2,4} - (?<title>[a-zA-Z0-9. ()-]*) \((?<year>[0-9]{4})\) *[a-zA-Z .\[\]0-9\-]*.[a-z0-9]{2,4}','www.TamilRockerrs.co - Lonappante Mamodisa (2019) [Malayalam Original - DVDRip - x264 - 850MB].mkv'),
	(3,'MovieFileName', 'TamilRockerss', 'www.TamilRockerrss.[a-z]{2,4} - (?<title>[a-zA-Z0-9. ()-]*) \((?<year>[0-9]{4})\) *[a-zA-Z .\[\]0-9\-]*.[a-z0-9]{2,4}','www.TamilRockerrs.co - Lonappante Mamodisa (2019) [Malayalam Original - DVDRip - x264 - 850MB].mkv'),
	(4,'MovieFileName','Normal','(?<title>[a-zA-Z0-9. \)\(]+) *\[(?<year>[0-9]{4})\].[a-z0-9]{2,4}',''),
  (5,'MovieFileName','Normal_2','(?<title>[a-zA-Z0-9. \)\(]+) *\((?<year>[0-9]{4})\).[a-z0-9]{2,4}','');