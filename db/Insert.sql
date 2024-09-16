USE SwitchAndKeys;

INSERT INTO Catalogo VALUES("1", "Primo catalogo e-commerce Switch&Keys");

INSERT INTO Prodotto VALUES("1","1","MOD 007 V3 HE Year of Dragon - Akko","192.80","22.00","6","Tastiera","75%",NULL,"Akko","1","0","0","Tastiera meccanica 75% (Akko) con profilo keycaps Cherry e switch Akko Cream Yellow Magnetic","1");
INSERT INTO Prodotto VALUES("2","1","MonsGeek M1 - Akko","154.70","22.00","3","Tastiera","75%",NULL,"Akko","1","0","0","Tastiera meccanica 75% (Akko) con profilo keycaps OEM e switch Akko V3 Cream Blue Pro","1");
INSERT INTO Prodotto VALUES("3","1","Blue on White 3068B Plus ISO - Akko","75.00","22.00","8","Tastiera","65%",NULL,"Akko","1","1","1","Tastiera meccanica 65% (Akko) con profilo keycaps Cherry e switch Akko CS Jelly Pinkl","1");
INSERT INTO Prodotto VALUES("4","1","Racoon MAO Keycap Set (142-Key)","54.70","22.00","0","Keycaps","MAO","PBT","Akko",NULL,NULL,NULL,"Set di 142 keycaps in PBT Double Shot, Profilo MAO (Akko)","0");
INSERT INTO Prodotto VALUES("5","1","DROP + GMK RED SAMURAI KEYCAP SET (Kit 65%)","75","22.00","10","Keycaps","Cherry","PBT Double Shot","DROP",NULL,NULL,NULL,"Set di keycaps per tastiere in formato 65%, Materiale: PBT Double Shot, Profilo Cherry (DROP x GMK Keycaps)","1");
INSERT INTO Prodotto VALUES("6","1","EPOMAKER Space Travel Keycaps","36","22.00","2","Keycaps","MDA","PBT","EPOMAKER",NULL,NULL,NULL,"Set di 147 keycaps in PBT, Profilo: MDA (EPOMAKER)","1");
INSERT INTO Prodotto VALUES("7","1","Holy Panda Switch (36 pz.) - Lubed","41.90","22.00","20","Switch","Tattili",NULL,"DROP",NULL,NULL,NULL,"Set di 36 Switch Tattili pre-lubrificati","1");
INSERT INTO Prodotto VALUES("8","1","Wuque WS Jade Linear Switches","0.40","22.00","70","Switch","Lineare",NULL,"Wuque Studio",NULL,NULL,NULL,"Switch(singolo) lineare","1");
INSERT INTO Prodotto VALUES("9","1","Epomaker Budgerigar Switch Set (35 pz.)","12.99","22.00","25","Switch","Tattili",NULL,"EPOMAKER",NULL,NULL,NULL,"Set di 36 Switch Tattili","1");
INSERT INTO Prodotto VALUES("10","1","Epomaker MIX Cable V2","19.99","22.00","15","Accessori","Cavo",NULL,"EPOMAKER",NULL,NULL,NULL,"Cavo a spirale da USB C a USB A a doppia manica per tastiere meccaniche","1");
INSERT INTO Prodotto VALUES("11","1","MXRSKEY DM Stabilizer","9.99","22.00","0","Accessori","Stabilizzatori",NULL,"EPOMAKER",NULL,NULL,NULL,"Set di stabilizzatori montati su piastra POM","0");
INSERT INTO Prodotto VALUES("12","1","Zoom75 - Addons Extra Batch 1","69","22.00","9","Accessori","PCB",NULL,"Meletrix","1","1","1","Bluetooth/VIA Wired e Wireless Hotswap PCB - Flex cut o non flex CUT","1");
INSERT INTO Prodotto VALUES("13","1","Epomaker TH80 pro","64.99","22.0","11","Tastiera","75%",NULL,"EPOMAKER","1","1","1","Tastiera meccanica 75% (EPOMAKER) con profilo keycaps MDA e switch Gateron Pro Yellow, Layout ISO","1");
INSERT INTO Prodotto VALUES("14","1","Ajazz AK820 pro","59.99","22.0","20","Tastiera","75%",NULL,"Ajazz","1","0","1","Tastiera meccanica 75% (Ajazz) con profilo keycaps OEM e switch Flying Fish","1");
INSERT INTO Prodotto VALUES("15","1","Keychron K2 v2","84.99","22.0","5","Tastiera","75%",NULL,"Keychron","1","0","1","Tastiera meccanica 75% (Keychron) con profilo keycaps Cherry e switch Gateron Pro Red","1");
INSERT INTO Prodotto VALUES("16","1","Akko x TTC Demon Switch","41.98","22.0","15","Switch","Lineare",NULL,"Akko",NULL,NULL,NULL,"Set di 90 Switch Lineari","1");
INSERT INTO Prodotto VALUES("17","1","Keycaps Industries Cherry Blossom","34.99","22.0","8","Keycaps","Cherry","PBT","Keycaps Industries",NULL,NULL,NULL,"Set di 135 keycaps in PBT, Profilo: Cherry (Keycaps Industries)","1");
INSERT INTO Prodotto VALUES("18","1","Epomaker EK21 VIA","45.99","22.00","3","Accessori","Tastierino Numerico",NULL,"EPOMAKER","1","1","1","Tastierino Numerico (EPOMAKER) con profilo keycaps Cherry e switch EPOMAKER Flamingo Switch","1");
INSERT INTO Prodotto VALUES("19","1","ROG Strix Scope RX EVA-02 Edition","199.90","22.0","1","Tastiera","100%",NULL,"Asus ROG","1","0","0","Tastiera meccanica 100% (Asus ROG) EVA 02 Edition, con profilo keycaps OEM e switch ROG RX Optical","1");
INSERT INTO Prodotto VALUES("20","1","Keychron C1 Pro","55.0","22.0","9","Tastiera","80%",NULL,"Keychron","1","0","0","Tastiera meccanica 80% (Keychron) con profilo keycaps OEM e switch KPRO Red","1");
INSERT INTO Prodotto VALUES("21","1","Meletrix ZOOM65 V3","189.00","22.0","5","Tastiera","65%",NULL,"Meletrix","1","1","1","Tastiera meccanica 65% (Meletrix) con profilo keycaps Cherry e switch Gateron Pro Red","1");
INSERT INTO Prodotto VALUES("22","1","Gamakay TK75 HE","89.00","22.0","6","Tastiera","75%",NULL,"Gamakay","1","1","1","Tastiera meccanica 75% (Gamakay) con profilo keycaps Cherry e switch Magnetic Mercury","1");
INSERT INTO Prodotto VALUES("23","1","PolyCaps Octopus Double-shot PBT Keycaps","64.99","22.0","4","Keycaps","Cherry","PBT Double Shot","Kinetic Labs",NUll,NULL,NULL,"Set di 155 keycaps in PBT, Profilo: Cherry (Kinetic Labs)","1");

INSERT INTO Immagine(dati_immagine, rif_id_prodotto) 
VALUES
	(load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/MOD 007 V3 HE Year of Dragon - Akko.png"), 1),
	(load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/MonsGeek M1 - Akko.png"), 2),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Blue on White 3068B Plus ISO - Akko.jpg"), 3),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Racoon MAO Keycap Set (142-Key).png"), 4),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Drop_Set_Samurai.png"), 5),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/EPOMAKER Space Travel Keycaps.png"), 6),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Holy Panda.jpg"), 7),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Wuque WS Jade Linear Switches.png"), 8),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Epomaker Budgerigar.png"), 9),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Epomaker MIX Cable V2.png"), 10),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/MXRSKEY DM Stabilizer.png"), 11),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Zoom75 - Addons Extra Batch 1.png"), 12),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/th80pro.png"), 13),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/ak820pro.png"), 14),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/keychronk2v2.png"), 15),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/AkkoDemon.jpg"), 16),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cherrykeycaps2.png"), 17),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/tasierinoepomaker.png"), 18),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/design_list_03.jpg"), 19),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/keychronc1.jpg"), 20),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Meletrix ZOOM65 V3.png"), 21),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Gamakay TK75 HE.png"), 22),
    (load_file("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/octopuskeycaps2.png"), 23);



INSERT INTO cliente VALUES("mary@gmail.com","d82494f05d6917ba02f7aaa29689ccb444bb73f20380876cb05d1f37537b7892","Maria Antonietta", "Cervo", 1);
INSERT INTO cliente VALUES("maryantony.913@gmail.com","220be2608460b9ac29e2c7109d0233c1b9b7302b2e627fe64c08f3378f8dce3b","Maria Antonietta", "Cervo", 0);

INSERT INTO Carrello VALUES(1,"mary@gmail.com");
INSERT INTO Carrello VALUES(1,"maryantony.913@gmail.com");

INSERT INTO featured_products VALUES(13);
INSERT INTO featured_products VALUES(14);
INSERT INTO featured_products VALUES(15);
INSERT INTO featured_products VALUES(16);
INSERT INTO featured_products VALUES(17);
INSERT INTO featured_products VALUES(18);


INSERT INTO Indirizzo_Spedizione VALUES(1,"maryantony.913@gmail.com","Maria Antonietta","Cervo","3396534007","SA","Pagani","Via dello Stadio",18,"84016");
INSERT INTO Indirizzo_Spedizione VALUES(2,"maryantony.913@gmail.com","Maria Antonietta","Cervo","3396534007","SA","Soverato","Via dello Stadio",18,"84016");
INSERT INTO Indirizzo_Spedizione VALUES(3,"maryantony.913@gmail.com","Maria Antonietta","Cervo","3396534007","SA","Nocera Inferiore","Via dello Stadio",18,"84016");


INSERT INTO Metodo_di_Pagamento VALUES (1,"maryantony.913@gmail.com","PayPal",NULL,NULL,NULL,NULL,NULL,"maryantony.913@gmail.com");
INSERT INTO Metodo_di_Pagamento VALUES (2,"maryantony.913@gmail.com","Carta di Credito","1111111111111111","Maria Antonietta","Cervo","2025-11-11","123",NULL);

INSERT INTO Pagamento (ID_Pagamento, Importo, Data, rif_id_metodo, rif_username_cliente)
VALUES 
    (1, 475.44, '2020-03-15 10:30:00', 1, 'maryantony.913@gmail.com'),
    (2, 193.73, '2021-05-20 14:45:00', 2, 'maryantony.913@gmail.com'),
    (3, 279.50, '2022-07-12 16:00:00', 2, 'maryantony.913@gmail.com'),
    (4, 338.65, '2023-09-25 18:15:00', 1, 'maryantony.913@gmail.com'),
    (5, 96.50, '2024-08-31 23:59:00', 1, 'maryantony.913@gmail.com');

INSERT INTO Spedizione (id_spedizione, id_indirizzo, costi, data, rif_username_cliente)
VALUES
	(1, 1, 5.00, '2020-03-15 10:30:00', 'maryantony.913@gmail.com'),
	(2, 3, 5.00, '2021-05-20 14:45:00', 'maryantony.913@gmail.com'),
	(3, 1, 5.00, '2022-07-12 16:00:00', 'maryantony.913@gmail.com'),
	(4, 1, 5.00, '2023-09-25 18:15:00', 'maryantony.913@gmail.com'),
	(5, 1, 5.00, '2024-08-31 23:59:00', 'maryantony.913@gmail.com');

INSERT INTO Ordine (id_ordine, importo, data, rif_id_spedizione, rif_id_pagamento, rif_username_cliente)
VALUES 
    (1, 475.44, '2020-03-15 10:30:00', 1, 1, 'maryantony.913@gmail.com'),
    (2, 193.73, '2021-05-20 14:45:00', 2, 2, 'maryantony.913@gmail.com'),
    (3, 279.50, '2022-07-12 16:00:00', 3, 3, 'maryantony.913@gmail.com'),
    (4, 338.65, '2023-09-25 18:15:00', 4, 4, 'maryantony.913@gmail.com'),
    (5, 96.50, '2024-08-31 23:59:00', 5, 5, 'maryantony.913@gmail.com');

INSERT INTO Dettaglio_Ordine (ID_Dettaglio, Quantita_Totale, Importo_Totale, costi_spedizione, indirizzo_spedizione, metodo_pagamento, rif_id_ordine) 
VALUES 
	(1, 3, 475.44, 5.00, 'Maria Antonietta Cervo, Via dello Stadio 18, Pagani, SA, 84016', 'PayPal', 1),
	(2, 5, 193.73, 5.00, 'Maria Antonietta Cervo, Via dello Stadio 18, Nocera Inferiore, SA, 84016', '1111', 2),
	(3, 2, 279.50, 5.00, 'Maria Antonietta Cervo, Via dello Stadio 18, Pagani, SA, 84016', '1111', 3),
	(4, 10, 338.65, 5.00, 'Maria Antonietta Cervo, Via dello Stadio 18, Pagani, SA, 84016', 'PayPal', 4),
	(5, 1, 96.50, 5.00, 'Maria Antonietta Cervo, Via dello Stadio 18, Pagani, SA, 84016', 'PayPal', 5);

    
INSERT INTO Specifica (rif_id_prodotto, nome_prodotto, IVA_Applicata, Prezzo_Unitario, Quantita_Ordine, rif_id_dettaglio) 
VALUES
	(1, "MOD 007 V3 HE Year of Dragon - Akko" , 22.00, 192.80, 2, 1),
	(2, "MonsGeek M1 - Akko",22.00, 154.70, 1, 2),
	(3, "Blue on White 3068B Plus ISO - Akko", 22.00, 75.00, 3, 3),
	(4, "Racoon MAO Keycap Set (142-Key)", 22.00, 54.70, 5, 4),
	(5, "DROP + GMK RED SAMURAI KEYCAP SET (Kit 65%)",22.00, 75.00, 1, 5);
