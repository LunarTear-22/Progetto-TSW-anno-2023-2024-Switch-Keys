DROP DATABASE IF EXISTS SwitchAndKeys;
CREATE schema SwitchAndKeys;
USE SwitchAndKeys;

DROP TABLE IF EXISTS Cliente;
CREATE TABLE Cliente(
    email                  VARCHAR(254) NOT NULL PRIMARY KEY,
    password               VARCHAR(1000) NOT NULL,                        
    nome                   VARCHAR(25) NOT NULL,
    cognome                VARCHAR(25) NOT NULL,
    amministratore         BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS Metodo_di_Pagamento;
CREATE TABLE Metodo_di_Pagamento(
    id_metodo              INT AUTO_INCREMENT NOT NULL,
    username_cliente       VARCHAR(254) NOT NULL,
    tipo                   VARCHAR(16) NOT NULL,
    numero_carta           VARCHAR(16),
    nome_carta             VARCHAR(25),
    cognome_carta          VARCHAR(25),
    scadenza               DATE,
    cvv                    CHAR(3),
    mail_paypal            VARCHAR(254),
    
    FOREIGN KEY (username_cliente) REFERENCES Cliente(email)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
                                    
	PRIMARY KEY(id_metodo, username_cliente)
);

DROP TABLE IF EXISTS Indirizzo_Spedizione;
CREATE TABLE Indirizzo_Spedizione(
    id_indirizzo           INT AUTO_INCREMENT NOT NULL,
    username_cliente       VARCHAR(254) NOT NULL,
    nome_destinatario      VARCHAR(25) NOT NULL,
    cognome_destinatario   VARCHAR(25) NOT NULL,
    telefono_destinatario  CHAR(10) NOT NULL,
    provincia              CHAR(2) NOT NULL,
    citta                  VARCHAR(60) NOT NULL,
    via                    VARCHAR(30) NOT NULL,
    n_civico               INT NOT NULL,
    cap                    CHAR(5) NOT NULL,
    
    FOREIGN KEY (username_cliente) REFERENCES Cliente(email)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
                                    
	PRIMARY KEY(id_indirizzo, username_cliente)
);

DROP TABLE IF EXISTS Catalogo;
CREATE TABLE Catalogo(
    id_catalogo            INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    informazioni           VARCHAR(200)
);

DROP TABLE IF EXISTS Prodotto;
CREATE TABLE Prodotto(
    id_prodotto            INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    rif_id_catalogo        INT NOT NULL,
    nome                   VARCHAR(50) NOT NULL,
    prezzo                 DECIMAL(6,2) NOT NULL DEFAULT 0,
    iva                    DECIMAL(5, 2) CHECK (iva >= 0 AND iva <= 100) NOT NULL DEFAULT 0,
    quantita               INT NOT NULL DEFAULT 0,
    categoria              VARCHAR(20) NOT NULL,
    tipo                   VARCHAR(20) NOT NULL,
    materiale              VARCHAR(20),
    marca                  VARCHAR(20) NOT NULL,
    cablata                BOOLEAN,
    wifi                   BOOLEAN,
    bluetooth              BOOLEAN,
    descrizione            VARCHAR(400) NOT NULL,
    disponibile            BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (rif_id_catalogo) REFERENCES Catalogo(id_catalogo)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS featured_products;
CREATE TABLE featured_products (
    prodotto_id INT PRIMARY KEY,
    
    FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id_prodotto)
								 ON UPDATE CASCADE
								 ON DELETE CASCADE
);

DROP TABLE IF EXISTS Carrello;
CREATE TABLE Carrello (
    id_carrello            INT AUTO_INCREMENT NOT NULL,
    rif_username_cliente   VARCHAR(254) NOT NULL,
    
    PRIMARY KEY(id_carrello, rif_username_cliente),
    FOREIGN KEY (rif_username_cliente) REFERENCES Cliente(email)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
);

DROP TABLE IF EXISTS ProdottiCarrello;
CREATE TABLE ProdottiCarrello (
    id_prodotto_carrello   INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    rif_id_carrello        INT NOT NULL,
    rif_username_cliente   VARCHAR(254) NOT NULL,
    rif_id_prodotto        INT NOT NULL,
    quantita               INT NOT NULL,
    iva                    DECIMAL(5, 2) CHECK (iva >= 0 AND iva <= 100) NOT NULL DEFAULT 0,
    
    FOREIGN KEY (rif_id_carrello) REFERENCES Carrello(id_carrello)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
    FOREIGN KEY (rif_username_cliente) REFERENCES Cliente(email)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
    FOREIGN KEY (rif_id_prodotto) REFERENCES Prodotto(id_prodotto)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Immagine;
CREATE TABLE Immagine(
    id_immagine            INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    dati_immagine          LONGBLOB DEFAULT NULL,
    rif_id_prodotto        INT NOT NULL,
    
    FOREIGN KEY (rif_id_prodotto) REFERENCES Prodotto(id_prodotto)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS Pagamento;
CREATE TABLE Pagamento (
    ID_Pagamento 			INT AUTO_INCREMENT PRIMARY KEY,
    Importo 				DECIMAL(10, 2) NOT NULL,
    Data 					DATETIME NOT NULL,
    rif_id_metodo 			INT NOT NULL,
    rif_username_cliente    VARCHAR(254) NOT NULL,
    
    FOREIGN KEY (rif_id_metodo) REFERENCES Metodo_di_Pagamento(id_metodo)
									ON UPDATE CASCADE
									ON DELETE CASCADE,
    FOREIGN KEY (rif_username_cliente) REFERENCES Cliente(email)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
);

DROP TABLE IF EXISTS Spedizione;
CREATE TABLE Spedizione (
    id_spedizione          INT AUTO_INCREMENT PRIMARY KEY,
    id_indirizzo           INT NOT NULL, 
    costi                  DECIMAL(10, 2) NOT NULL,
    data                   DATETIME NOT NULL,
    rif_username_cliente    VARCHAR(254) NOT NULL,
    
    FOREIGN KEY (id_indirizzo) REFERENCES Indirizzo_Spedizione(id_indirizzo)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
	FOREIGN KEY (rif_username_cliente) REFERENCES Cliente(email)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
);

DROP TABLE IF EXISTS Ordine;
CREATE TABLE Ordine (
    id_ordine              		INT AUTO_INCREMENT PRIMARY KEY,
    importo                		DECIMAL(10, 2) NOT NULL,
    data                   		DATETIME NOT NULL,
    rif_id_spedizione			INT NOT NULL,
    rif_id_pagamento 			INT NOT NULL,
    rif_username_cliente   		VARCHAR(254) NOT NULL,
    
	FOREIGN KEY (rif_id_spedizione) REFERENCES Spedizione(id_spedizione)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
    FOREIGN KEY (rif_username_cliente) REFERENCES Cliente(email)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
	FOREIGN KEY (rif_id_pagamento) REFERENCES Pagamento(ID_Pagamento)
									ON UPDATE CASCADE
                                    ON DELETE CASCADE
);

DROP TABLE IF EXISTS Dettaglio_Ordine;
CREATE TABLE Dettaglio_Ordine (
    ID_Dettaglio 			INT AUTO_INCREMENT PRIMARY KEY,
    Quantita_Totale			INT NOT NULL,
    Importo_Totale 			DECIMAL(10, 2) NOT NULL,
    costi_spedizione		DECIMAL(10, 2) NOT NULL,
    indirizzo_spedizione	VARCHAR(300) NOT NULL,
    metodo_pagamento		CHAR(6) NOT NULL,
    rif_id_ordine 			INT NOT NULL,
        
    FOREIGN KEY (rif_id_ordine) REFERENCES Ordine(id_ordine)
							ON UPDATE CASCADE
							ON DELETE CASCADE
    
);

DROP TABLE IF EXISTS Specifica;
CREATE TABLE Specifica (
    rif_id_prodotto 		INT,
    nome_prodotto			VARCHAR(50),
    IVA_Applicata 			DECIMAL(4, 2) NOT NULL,
    Prezzo_Unitario 		DECIMAL(10, 2) NOT NULL,
    Quantita_Ordine 		INT NOT NULL,
    rif_id_dettaglio 		INT,
    
    PRIMARY KEY (rif_id_prodotto, rif_id_dettaglio),
    

    FOREIGN KEY (rif_id_dettaglio) REFERENCES Dettaglio_Ordine(ID_Dettaglio)
										ON UPDATE CASCADE
										ON DELETE CASCADE		
);

DROP TABLE IF EXISTS Acquistato;
CREATE TABLE Acquistato(
    username_cliente       VARCHAR(254) NOT NULL,
    rif_id_prodotto        INT NOT NULL,
    
    FOREIGN KEY (username_cliente) REFERENCES Cliente(email)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
    FOREIGN KEY (rif_id_prodotto) REFERENCES Prodotto(id_prodotto)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE
);

