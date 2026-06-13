PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nume TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    parola TEXT NOT NULL,
    tip TEXT NOT NULL CHECK (tip IN ('BIBLIOTECAR','CITITOR'))
);

CREATE TABLE IF NOT EXISTS readers (
    user_id INTEGER PRIMARY KEY,
    nr_legitimatie INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titlu TEXT NOT NULL,
    autor TEXT NOT NULL,
    an_aparitie INTEGER NOT NULL,
    disponibila INTEGER NOT NULL CHECK (disponibila IN (0,1)),
    active INTEGER NOT NULL DEFAULT 1 CHECK (active IN (0,1))
);


CREATE TABLE IF NOT EXISTS loans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    reader_user_id INTEGER NOT NULL,
    data_imprumut TEXT NOT NULL,
    data_returnare TEXT,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (reader_user_id) REFERENCES readers(user_id)
);
