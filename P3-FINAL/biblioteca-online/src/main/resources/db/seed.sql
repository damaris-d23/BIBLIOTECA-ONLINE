PRAGMA foreign_keys = ON;

INSERT OR IGNORE INTO users(id, nume, email, parola, tip)
VALUES (1, 'Admin', 'admin@lib.ro', 'admin', 'BIBLIOTECAR');

INSERT OR IGNORE INTO users(id, nume, email, parola, tip)
VALUES (2, 'Ana', 'ana@gmail.com', '1234', 'CITITOR');

INSERT OR IGNORE INTO readers(user_id, nr_legitimatie)
VALUES (2, 1001);

INSERT INTO books(titlu, autor, an_aparitie, disponibila, active) VALUES
('1984','George Orwell',1949,1,1),
('Animal Farm','George Orwell',1945,1,1);
('Portretul lui Dorian Gray','Oscar Wilde',1890,1);
