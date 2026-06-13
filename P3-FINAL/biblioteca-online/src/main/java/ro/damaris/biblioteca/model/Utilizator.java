package ro.damaris.biblioteca.model;

/**
 *clasa Utilizator reprezinta baza pentru toti utilizatorii aplicatiei
 *un utilizator poate fi cititor sau bibliotcar
 *
 *aceasta clasa contine datele utilizatorilor:
 *nume, email, parola si tipul
 *
 */
public class Utilizator {

    /**id-ul unic al utilizatorului */
    private int id;

    /**numele utilizatorului */
    private String nume;

    /**adresa de email a utilizatorului */
    private String email;

    /**parola contului */
    private String parola;

    /**tipul utilizatorului(cititor sau bibliotecar) */
    private String tipUtilizator; // cititor sau bibliotecar

    /**
     *constructor pentru utilizator
     *
     *@param id id-ul utilizatorului
     *@param nume numele utilizatorului
     *@param email email-ul utilizatorului
     *@param parola parola contului
     *@param tipUtilizator tipul utilizatorului
     */
    public Utilizator(int id, String nume, String email, String parola, String tipUtilizator) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.parola = parola;
        this.tipUtilizator = tipUtilizator;
    }

    /**
     *verifica daca parola introdusa este corecta
     *
     *@param parolaIntrodusa parola introdusa de utilizator
     *@return true daca parola este corecta, false altfel
     */
    public boolean autentificare(String parolaIntrodusa) {
        return parolaIntrodusa != null && parolaIntrodusa.equals(parola);
    }

    /**
     *@return id-ul utilizatorului
     */
    public int getId() {
        return id;
    }

    /**
     *@return numele utilizatorului
     */
    public String getNume() {
        return nume;
    }

    /**
     *@return email-ul utilizatorului
     */
    public String getEmail() {
        return email;
    }

    /**
     *@return tipul utilizatorului
     */
    public String getTipUtilizator() {
        return tipUtilizator;
    }

    /**
     *seteaza numele utilizatorului
     *
     *@param nume noul nume
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /**
     *seteaza email-ul utilizatorului
     *
     *@param email noul email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *seteaza parola utilizatorului
     *
     *@param parola noua parola
     */
    public void setParola(String parola) {
        this.parola = parola;
    }

    /**
     *seteaza tipul utilizatorului
     *
     *@param tipUtilizator noul tip(cititor sau bibliotecar)
     */
    public void setTipUtilizator(String tipUtilizator) {
        this.tipUtilizator = tipUtilizator;
    }

    /**
     *returneaza o reprezentare text a utilizatorului folosita pentru afisare
     *
     *@return string cu datele utilizatorului
     */
    @Override
    public String toString() {
        return "Utilizator{" + "id=" + id + ", nume='" + nume + '\'' + ", email='" + email + '\'' + ", tip='" + tipUtilizator + '\'' + '}';
    }
}
