package ro.damaris.biblioteca;

import ro.damaris.biblioteca.model.Carte;
import ro.damaris.biblioteca.model.Cititor;
import ro.damaris.biblioteca.service.Biblioteca;

/**
 *clasa de test pentru functionalitatile bibliotecii
 *folosita pentru rulare in consola
 *
 */
public class Main {

    /**
     *metoda main de test
     */
    public static void main(String[] args) {

        Biblioteca b = new Biblioteca("Biblioteca Online");

        // am adaugat cateva carti
        b.adaugaCarte(new Carte(1, "1984", "George Orwell", 1949));
        b.adaugaCarte(new Carte(2, "Good Omens", "Neil Gaiman", 1990));
        b.adaugaCarte(new Carte(3, "The Picture of Dorian Gray", "Oscar Wilde", 1890));

        // am creat un cititor
        Cititor ana = new Cititor(1, "Ana", "ana@gmail.com", "1234", 1001);

        System.out.println("\n-- CARTI DISPONIBILE:");
        System.out.println(b.cartiDisponibile());

        System.out.println("\n-- Cautare dupa autor 'orwell':");
        System.out.println(b.cautaDupaAutor("orwell"));

        System.out.println("\n-- Ana imprumuta cartea id=1:");
        var imprumut = b.imprumutaCarte(1, ana, "2026-01-13");
        System.out.println(imprumut);

        System.out.println("\n-- Carti disponibile dupa imprumut:");
        System.out.println(b.cartiDisponibile());

        System.out.println("\n-- Returnare carte (imprumut id=" + imprumut.getIdImprumut() + "):");
        b.returneazaCarte(imprumut.getIdImprumut(), "2026-01-20");

        System.out.println("\n-- Carti disponibile dupa returnare:");
        System.out.println(b.cartiDisponibile());

        System.out.println("\n-- Lista imprumuturi (istoric):");
        System.out.println(b.getListaImprumuturi());
    }
}
