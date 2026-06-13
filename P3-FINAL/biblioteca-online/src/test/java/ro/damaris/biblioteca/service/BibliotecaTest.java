package ro.damaris.biblioteca.service;

import org.junit.jupiter.api.Test;
import ro.damaris.biblioteca.model.Carte;
import ro.damaris.biblioteca.model.Cititor;

import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaTest {

    @Test
    void cautaDupaAutor_returneazaDoarCartileAutorului() {
        Biblioteca b = new Biblioteca("Test");
        b.adaugaCarte(new Carte(1, "1984", "George Orwell", 1949));
        b.adaugaCarte(new Carte(2, "Animal Farm", "George Orwell", 1945));
        b.adaugaCarte(new Carte(3, "Dorian Gray", "Oscar Wilde", 1890));

        var rez = b.cautaDupaAutor("orwell");

        assertEquals(2, rez.size());
        assertTrue(rez.stream().allMatch(c -> c.getAutor().equals("George Orwell")));
    }

    @Test
    void imprumutaCarte_schimbaStatusulSiCreeazaImprumut() {
        Biblioteca b = new Biblioteca("Test");
        b.adaugaCarte(new Carte(1, "1984", "George Orwell", 1949));
        Cititor ana = new Cititor(1, "Ana", "ana@gmail.com", "1234", 1001);

        var imprumut = b.imprumutaCarte(1, ana, "2026-01-13");

        assertNotNull(imprumut);
        assertFalse(imprumut.getCarte().isDisponibila());
        assertEquals(1, b.getListaImprumuturi().size());
        assertTrue(b.getListaImprumuturi().get(0).esteActiv());
    }

    @Test
    void returneazaCarte_faceCarteaDisponibilaSiInchideImprumutul() {
        Biblioteca b = new Biblioteca("Test");
        b.adaugaCarte(new Carte(1, "1984", "George Orwell", 1949));
        Cititor ana = new Cititor(1, "Ana", "ana@gmail.com", "1234", 1001);

        var imprumut = b.imprumutaCarte(1, ana, "2026-01-13");
        b.returneazaCarte(imprumut.getIdImprumut(), "2026-01-20");

        assertTrue(imprumut.getCarte().isDisponibila());
        assertFalse(imprumut.esteActiv());
        assertEquals("2026-01-20", imprumut.getDataReturnare());
    }

    @Test
    void cautaDupaTitlu_ignoraMajusculeSiGasestePartial() {
        Biblioteca b = new Biblioteca("Test");
        b.adaugaCarte(new Carte(1, "Harry Potter", "J.K. Rowling", 1997));
        b.adaugaCarte(new Carte(2, "Hobbitul", "J.R.R. Tolkien", 1937));

        var rez = b.cautaDupaTitlu("pot");

        assertEquals(1, rez.size());
        assertEquals("Harry Potter", rez.get(0).getTitlu());
    }

    @Test
    void imprumutaCarte_aruncaEroareDacaCarteaNuExista() {
        Biblioteca b = new Biblioteca("Test");
        Cititor ana = new Cititor(1, "Ana", "ana@gmail.com", "1234", 1001);

        assertThrows(IllegalArgumentException.class, () ->
                b.imprumutaCarte(999, ana, "2026-01-13")
        );
    }

    @Test
    void imprumutaCarte_aruncaEroareDacaEsteDejaImprumutata() {
        Biblioteca b = new Biblioteca("Test");
        b.adaugaCarte(new Carte(1, "1984", "George Orwell", 1949));

        Cititor ana = new Cititor(1, "Ana", "ana@gmail.com", "1234", 1001);
        Cititor ion = new Cititor(2, "Ion", "ion@gmail.com", "abcd", 1002);

        b.imprumutaCarte(1, ana, "2026-01-13");

        assertThrows(IllegalStateException.class, () ->
                b.imprumutaCarte(1, ion, "2026-01-14")
        );
    }

    @Test
    void returneazaCarte_aruncaEroareDacaImprumutulNuExista() {
        Biblioteca b = new Biblioteca("Test");

        assertThrows(IllegalArgumentException.class, () ->
                b.returneazaCarte(999, "2026-01-20")
        );
    }

    @Test
    void returneazaCarte_aruncaEroareDacaImprumutulEsteDejaInchis() {
        Biblioteca b = new Biblioteca("Test");
        b.adaugaCarte(new Carte(1, "1984", "George Orwell", 1949));
        Cititor ana = new Cititor(1, "Ana", "ana@gmail.com", "1234", 1001);

        var imprumut = b.imprumutaCarte(1, ana, "2026-01-13");
        b.returneazaCarte(imprumut.getIdImprumut(), "2026-01-20");

        assertThrows(IllegalStateException.class, () ->
                b.returneazaCarte(imprumut.getIdImprumut(), "2026-01-21")
        );
    }

}
