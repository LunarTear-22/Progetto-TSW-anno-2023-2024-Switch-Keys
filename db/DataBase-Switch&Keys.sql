DROP DATABASE IF EXISTS SwitchAndKeys;
CREATE schema SwitchAndKeys;
USE SwitchAndKeys;

DROP TABLE IF EXISTS Cliente;
CREATE TABLE Cliente(
username				VARCHAR(15) NOT NULL PRIMARY KEY,
password				VARCHAR(16) NOT NULL
						CHECK (LENGTH(password) BETWEEN 8 AND 16),
email					VARCHAR(319) UNIQUE NOT NULL,
amministratore			BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS Dati_Anagrafici;
CREATE TABLE Dati_Anagrafici(
cf						CHAR(16) NOT NULL,
username_cliente		VARCHAR(15) NOT NULL,
nome					VARCHAR(25) NOT NULL,
cognome					VARCHAR(25) NOT NULL,
telefono				CHAR(10) NOT NULL,

FOREIGN KEY (username_cliente) 			REFERENCES Cliente(username)
										ON UPDATE CASCADE
										ON DELETE CASCADE,
PRIMARY KEY(cf, username_cliente)
);

DROP TABLE IF EXISTS Metodo_di_Pagamento;
CREATE TABLE Metodo_di_Pagamento(
numero_carta			CHAR(16) NOT NULL,
username_cliente		VARCHAR(15) NOT NULL,
nome_carta				VARCHAR(25) NOT NULL,
cognome_carta			VARCHAR(25) NOT NULL,
scadenza				DATE NOT NULL,
cvv						CHAR(3),

FOREIGN KEY (username_cliente) 			REFERENCES Cliente(username)
										ON UPDATE CASCADE
										ON DELETE CASCADE,
PRIMARY KEY(numero_carta, username_cliente)
);

DROP TABLE IF EXISTS Indirizzo_Spedizione;
CREATE TABLE Indirizzo_Spedizione(
id_indirizzo			INT AUTO_INCREMENT NOT NULL,
username_cliente		VARCHAR(15) NOT NULL,
città					VARCHAR(60) NOT NULL,
via						VARCHAR(30) NOT NULL,
cap						CHAR(5) NOT NULL,

FOREIGN KEY (username_cliente) 			REFERENCES Cliente(username)
										ON UPDATE CASCADE
										ON DELETE CASCADE,
PRIMARY KEY(id_indirizzo, username_cliente)
);

DROP TABLE IF EXISTS Catalogo;
CREATE TABLE Catalogo(
id_catalogo				INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
quantità_prodotti		INT NOT NULL,
informazioni			VARCHAR(200)
);

DROP TABLE IF EXISTS Prodotto;
CREATE TABLE Prodotto(
id_prodotto				INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
rif_id_catalogo			INT NOT NULL,
nome					VARCHAR(50) NOT NULL,
prezzo					DECIMAL(6,2) NOT NULL,
iva						DECIMAL(5, 2) CHECK (iva >= 0 AND iva <= 100),
quantità				INT,
descrizione				VARCHAR(400) NOT NULL,
disponibile				BOOLEAN NOT NULL,

FOREIGN KEY (rif_id_catalogo) 			REFERENCES Catalogo(id_catalogo)
										ON UPDATE CASCADE
										ON DELETE CASCADE
);

DROP TABLE IF EXISTS Immagine;
CREATE TABLE Immagine(
id_immagine				INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
nome 					VARCHAR(100),
dati_immagine			LONGBLOB,
rif_id_prodotto			INT NOT NULL,

FOREIGN KEY (rif_id_prodotto) 			REFERENCES Prodotto(id_prodotto)
										ON UPDATE CASCADE
										ON DELETE CASCADE
);

DROP TABLE IF EXISTS Pagamento;
CREATE TABLE Pagamento(
id_pagamento			INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
metodo_di_pagamento		CHAR(16) NOT NULL,
iva_prodotto			DECIMAL(5, 2) CHECK (iva_prodotto >= 0 AND iva_prodotto <= 100),
data					DATE NOT NULL,
importo					DECIMAL(6,2) NOT NULL
);

DROP TABLE IF EXISTS Spedizione;
CREATE TABLE Spedizione(
id_spedizione			INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
indirizzo_spedizione	VARCHAR(200) NOT NULL,
metodo_di_spedizione	VARCHAR(25) NOT NULL,
costi					DECIMAL(6,2) NOT NULL,
data					DATE NOT NULL
);

DROP TABLE IF EXISTS Ordine;
CREATE TABLE Ordine(
id_ordine				INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
username_cliente		VARCHAR(15) NOT NULL,
rif_id_pagamento		INT NOT NULL,
rif_id_spedizione		INT NOT NULL,
data					DATE NOT NULL,
importo					DECIMAL(6,2) NOT NULL,
stato_ordine			VARCHAR(20) NOT NULL,

FOREIGN KEY (username_cliente) 			REFERENCES Cliente(username)
										ON UPDATE CASCADE
										ON DELETE CASCADE,
                                    
FOREIGN KEY (rif_id_pagamento) 			REFERENCES Pagamento(id_pagamento)
										ON UPDATE CASCADE
										ON DELETE CASCADE,
										
FOREIGN KEY (rif_id_spedizione)			REFERENCES Spedizione(id_spedizione)
										ON UPDATE CASCADE
										ON DELETE CASCADE
);

DROP TABLE IF EXISTS Incluso;
CREATE TABLE Incluso(
rif_id_ordine			INT NOT NULL,
rif_id_prodotto			INT NOT NULL,

FOREIGN KEY (rif_id_ordine) 			REFERENCES Ordine(id_ordine)
										ON UPDATE CASCADE
										ON DELETE CASCADE,
                                        
FOREIGN KEY (rif_id_prodotto) 			REFERENCES Prodotto(id_prodotto)
										ON UPDATE CASCADE
										ON DELETE CASCADE
);

DROP TABLE IF EXISTS Acquistato;
CREATE TABLE Acquistato(
username_cliente		VARCHAR(15) NOT NULL,
rif_id_prodotto			INT NOT NULL,

FOREIGN KEY (username_cliente) 			REFERENCES Cliente(username)
										ON UPDATE CASCADE
										ON DELETE CASCADE,

FOREIGN KEY (rif_id_prodotto) 			REFERENCES Prodotto(id_prodotto)
										ON UPDATE CASCADE
										ON DELETE CASCADE
);