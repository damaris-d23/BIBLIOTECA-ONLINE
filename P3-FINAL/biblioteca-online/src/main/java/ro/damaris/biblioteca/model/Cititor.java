package ro.damaris.biblioteca.model;

/**
 *clasa Cititor reprezinta un utilizator normal al bibliotecii
 *aceasta extinde clasa Utilizator si adauga un campul:
 *numar de legitimatie
 *
 */
public class Cititor extends Utilizator {

    /**numarul de legitimatie al cititorului */
    private int nrLegitimatie;

    /**
     *constructor pentru cititor
     *apeleaza constructorul din clasa Utilizator si seteaza rolul ca "CITITOR"
     *
     *@param id id-ul utilizatorului
     *@param nume numele cititorului
     *@param email adresa de email
     *@param parola parola contului
     @param nrLegitimatie numarul de legitimatie al cititorului
     */
    public Cititor(int id, String nume, String email, String parola, int nrLegitimatie) {
        super(id, nume, email, parola, "CITITOR"); // apeleaza constructorul din Utilizator
        this.nrLegitimatie = nrLegitimatie;
    }

    /**
     *@return numarul de legitimatie al cititorului
     */
    public int getNrLegitimatie() {
        return nrLegitimatie;
    }

    /**
     *seteaza numarul de legitimatie al cititorului
     *
     *@param nrLegitimatie noul numar de legitimatie
     */
    public void setNrLegitimatie(int nrLegitimatie) {
        this.nrLegitimatie = nrLegitimatie;
    }

    /**
     *returneaza o reprezentare text a cititorului folosita pentru afisare
     *
     *@return string cu datele cititorului
     */
    @Override
    public String toString() {
        return "Cititor{" + "nrLegitimatie=" + nrLegitimatie + ", nume='" + getNume() + '\'' + ", email='" + getEmail() + '\'' + '}';
    }
}
