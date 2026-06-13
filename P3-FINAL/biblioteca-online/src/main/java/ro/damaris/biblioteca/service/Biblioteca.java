package ro.damaris.biblioteca.service;

import ro.damaris.biblioteca.model.Carte;
import ro.damaris.biblioteca.model.Cititor;
import ro.damaris.biblioteca.model.Imprumut;

import java.util.ArrayList;
import java.util.List;

/**
 *clasa Biblioteca gestioneaza functionalitatile de baza ale bibliotecii folosind liste tinute in memorie
 *
 *aici sunt implementate:
 *adaugare /stergere /actualizare carti
 *cautare carti
 *imprumut si returnare carti
 *
 */
public class Biblioteca {

    /**numele bibliotecii */
    private String nume;

    /**lista cartilor existente in biblioteca */
    private List<Carte> listaCarti;

    /**lista imprumuturilor realizate */
    private List<Imprumut> listaImprumuturi;

    /**id incremental pentru imprumuturi */
    private int nextImprumutId;

    /**
     *constructor pentru biblioteca
     *
     *@param nume numele bibliotecii
     */
    public Biblioteca(String nume) {
        this.nume = nume;
        this.listaCarti = new ArrayList<>();
        this.listaImprumuturi = new ArrayList<>();
        this.nextImprumutId = 1;
    }

    /**
     *@return numele bibliotecii
     */
    public String getNume() {
        return nume;
    }

    /**
     *adauga o carte in biblioteca.
     *
     *@param carte cartea care va fi adaugata
     */
    public void adaugaCarte(Carte carte) {
        listaCarti.add(carte);
    }

    /**
     *returneaza lista cartilor disponibile.
     *
     *@return lista de carti care pot fi imprumutate
     */
    public List<Carte> cartiDisponibile() {
        List<Carte> rez = new ArrayList<>();
        for (Carte c : listaCarti) {
            if (c.isDisponibila()) {
                rez.add(c);
            }
        }
        return rez;
    }

    /**
     *returneaza lista cartilor imprumutate (indisponibile)
     *
     *@return lista cartilor imprumutate
     */
    public List<Carte> cartiImprumutate() {
        List<Carte> rez = new ArrayList<>();
        for (Carte c : listaCarti) {
            if (!c.isDisponibila()) {
                rez.add(c);
            }
        }
        return rez;
    }

    /**
     *sterge o carte din biblioteca daca exista si nu este imprumutata
     *
     *@param idCarte id-ul cari
     *@throws IllegalArgumentException daca cartea nu exista
     *@throws IllegalStateException daca cartea este imprumutata
     */
    public void stergeCarte(int idCarte) {
        Carte carte = gasesteCarteDupaId(idCarte);

        if (carte == null) {
            throw new IllegalArgumentException("cartea nu exista");
        }
        if (!carte.isDisponibila()) {
            throw new IllegalStateException("nu poti sterge o carte imprumutata");
        }

        listaCarti.remove(carte);
    }

    /**
     *actualizeaza datele unei carti
     *daca un parametru este null, acel camp nu este modificat
     *
     *@param idCarte id-ul cartii
     *@param titlu noul titlu
     *@param autor noul autor
     *@param anAparitie noul an al aparitiei
     *@throws IllegalArgumentException daca cartea nu exista
     */
    public void actualizeazaCarte(int idCarte, String titlu, String autor, Integer anAparitie) {
        Carte carte = gasesteCarteDupaId(idCarte);

        if (carte == null) {
            throw new IllegalArgumentException("cartea nu exista");
        }

        if (titlu != null) {
            carte.setTitlu(titlu);
        }
        if (autor != null) {
            carte.setAutor(autor);
        }
        if (anAparitie != null) {
            carte.setAnAparitie(anAparitie);
        }
    }

    /**
     *returneaza lista imprumuturilor active
     *
     *@return lista de imprumuturi active
     */
    public List<Imprumut> imprumuturiActive() {
        List<Imprumut> rez = new ArrayList<>();
        for (Imprumut i : listaImprumuturi) {
            if (i.esteActiv()) {
                rez.add(i);
            }
        }
        return rez;
    }

    /**
     *getter pentru lista cartilor
     *
     *@return lista cartilor
     */
    public List<Carte> getListaCarti() {
        return listaCarti;
    }

    /**
     *cauta carti dupa titlu
     *
     *@param titlu textul cautat
     *@return lista cartilor care contin textul in titlu
     */
    public List<Carte> cautaDupaTitlu(String titlu) {
        List<Carte> rez = new ArrayList<>();
        String cautare = titlu.toLowerCase();

        for (Carte c : listaCarti) {
            if (c.getTitlu().toLowerCase().contains(cautare)) {
                rez.add(c);
            }
        }
        return rez;
    }

    /**
     *cauta carti dupa autor
     *
     *@param autor textul cautat
     *@return lista cartilor care contin textul in autor
     */
    public List<Carte> cautaDupaAutor(String autor) {
        List<Carte> rez = new ArrayList<>();
        String cautare = autor.toLowerCase();

        for (Carte c : listaCarti) {
            if (c.getAutor().toLowerCase().contains(cautare)) {
                rez.add(c);
            }
        }
        return rez;
    }

    /**
     *imprumuta o carte daca aceasta este disponibila
     *
     *@param idCarte id-ul cartii
     *@param cititor cititorul care imprumuta cartea
     *@param dataImprumut data imprumutului
     *@return imprumutul creat
     *@throws IllegalArgumentException daca cartea nu exista
     *@throws IllegalStateException daca cartea este deja imprumutata
     */
    public Imprumut imprumutaCarte(int idCarte, Cititor cititor, String dataImprumut) {
        Carte carte = gasesteCarteDupaId(idCarte);

        if (carte == null) {
            throw new IllegalArgumentException("cartea nu exista");
        }
        if (!carte.isDisponibila()) {
            throw new IllegalStateException("cartea este deja imprumutata");
        }

        carte.setDisponibila(false);

        Imprumut imprumut = new Imprumut(nextImprumutId, dataImprumut, carte, cititor);
        nextImprumutId++;

        listaImprumuturi.add(imprumut);
        return imprumut;
    }

    /**
     *returneaza o carte imprumutata si inchide imprumutul
     *
     *@param idImprumut id-ul imprumutului
     *@param dataReturnare data returnarii
     *@throws IllegalArgumentException daca imprumutul nu exista
     *@throws IllegalStateException daca imprumutul este deja inchis
     */
    public void returneazaCarte(int idImprumut, String dataReturnare) {
        Imprumut imprumut = gasesteImprumutDupaId(idImprumut);

        if (imprumut == null) {
            throw new IllegalArgumentException("imprumut inexistent");
        }
        if (!imprumut.esteActiv()) {
            throw new IllegalStateException("imprumutul este deja inchis");
        }

        imprumut.returneaza(dataReturnare);
        imprumut.getCarte().setDisponibila(true);
    }

    /**
     *cauta o carte dupa id
     */
    private Carte gasesteCarteDupaId(int idCarte) {
        for (Carte c : listaCarti) {
            if (c.getIdCarte() == idCarte) {
                return c;
            }
        }
        return null;
    }

    /**
     *cauta un imprumut dupa id
     */
    private Imprumut gasesteImprumutDupaId(int idImprumut) {
        for (Imprumut i : listaImprumuturi) {
            if (i.getIdImprumut() == idImprumut) {
                return i;
            }
        }
        return null;
    }

    /**
     *@return lista tuturor imprumuturilor
     */
    public List<Imprumut> getListaImprumuturi() {
        return listaImprumuturi;
    }
}
