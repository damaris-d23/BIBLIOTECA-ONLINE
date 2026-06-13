package ro.damaris.biblioteca.model;

/**
 *clasa Carte reprezinta o carte din biblioteca
 *contine informatii de baza despre o carte si
 *este folosita in aplicatie pentru afisare si gestionare
 *
 */
public class Carte {

    /**id-ul unic al cartii */
    private int idCarte;

    /**titlul cartii */
    private String titlu;

    /**autorul cartii */
    private String autor;

    /**anul aparitiei */
    private int anAparitie;

    /**arata daca o carte este disponibila sau nu */
    private boolean disponibila;

    /**
     *constructor pentru carte
     *la crearea unei carti, aceasta este setata implicit ca disponibila
     *
     *@param idCarte id-ul cartii
     *@param titlu titlul cartii
     *@param autor autorul cartii
     *@param anAparitie anul aparitiei
     */
    public Carte(int idCarte, String titlu, String autor, int anAparitie) {
        this.idCarte = idCarte;
        this.titlu = titlu;
        this.autor = autor;
        this.anAparitie = anAparitie;
        this.disponibila = true; //setam la inceput cartea disponibila
    }

    //getteri

    /**
     *@return id-ul cartii
     */
    public int getIdCarte() {
        return idCarte;
    }

    /**
     *@return titlul cartii
     */
    public String getTitlu() {
        return titlu;
    }

    /**
     *@return autorul cartii
     */
    public String getAutor() {
        return autor;
    }

    /**
     *@return anul aparitiei
     */
    public int getAnAparitie() {
        return anAparitie;
    }

    /**
     *@return true daca cartea este disponibila, false altfel
     */
    public boolean isDisponibila() {
        return disponibila;
    }

    //setteri

    /**
     *seteaza titlul cartii
     *
     *@param titlu noul titlu
     */
    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    /**
     *seteaza autorul cartii
     *
     *@param autor noul autor
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     *seteaza anul aparitiei
     *
     *@param anAparitie noul an al aparitiei
     */
    public void setAnAparitie(int anAparitie) {
        this.anAparitie = anAparitie;
    }

    /**
     *modifica statusul cartii (disponibila/indisponibila)
     *
     *@param disponibila noul status al cartii
     */
    public void setDisponibila(boolean disponibila) {
        this.disponibila = disponibila;
    }

    /**
     *returneaza o reprezentare text a cartii
     *
     *@return string cu informatiile cartii
     */
    @Override
    public String toString() {
        return "Carte{" + "id=" + idCarte + ", titlu='" + titlu + '\'' + ", autor='" + autor + '\'' + ", anAparitie=" + anAparitie + ", disponibila=" + disponibila + '}';
    }
}
