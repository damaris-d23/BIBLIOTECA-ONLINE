package ro.damaris.biblioteca.service;

import ro.damaris.biblioteca.model.Carte;
import ro.damaris.biblioteca.model.Cititor;
import ro.damaris.biblioteca.repository.BookRepository;
import ro.damaris.biblioteca.repository.LoanRepository;

import java.time.LocalDate;
import java.util.List;

/**
 *service pentru logica principala a bibliotecii
 *
 */
public class BibliotecaService {

    private final BookRepository bookRepo = new BookRepository();
    private final LoanRepository loanRepo = new LoanRepository();

    /**
     *@return toate cartile din baza de date
     */
    public List<Carte> toateCartile() {
        return bookRepo.findAll();
    }

    /**
     *cauta carti dupa titlu sau autor
     */
    public List<Carte> cauta(String text) {
        return bookRepo.search(text);
    }

    //admin

    /**
     *adauga o carte noua
     */
    public int adaugaCarte(String titlu, String autor, int an) {
        if (titlu == null || titlu.isBlank()) throw new IllegalArgumentException("titlu invalid");
        if (autor == null || autor.isBlank()) throw new IllegalArgumentException("autor invalid");
        return bookRepo.insert(titlu.trim(), autor.trim(), an);
    }

    /**
     *tualizeaza datele unei carti
     */
    public void actualizeazaCarte(int id, String titlu, String autor, int an) {
        bookRepo.update(id, titlu, autor, an);
    }

    /**
     *sterge o carte daca nu are imprumut activ
     */
    public void stergeCarte(int id) {
        // nu lasam stergerea daca are imprumut activ
        if (loanRepo.isLoanActiveForBook(id)) {
            throw new IllegalStateException("cartea are imprumut activ");
        }
        bookRepo.delete(id);
    }

    // cititor

    /**
     *imprumuta o carte pentru un cititor
     */
    public void imprumutaCarte(int bookId, Cititor cititor) {
        if (cititor == null) throw new IllegalArgumentException("cititor invalid");
        if (loanRepo.isLoanActiveForBook(bookId)) {
            throw new IllegalStateException("cartea este deja imprumutata");
        }
        String azi = LocalDate.now().toString();
        loanRepo.createLoan(bookId, cititor.getId(), azi);
        bookRepo.setDisponibila(bookId, false);
    }

    /**
     *returneaza o carte imprumutata
     */
    public void returneazaCarte(int bookId) {
        String azi = LocalDate.now().toString();
        loanRepo.closeLoanByBook(bookId, azi);
        bookRepo.setDisponibila(bookId, true);
    }
}
