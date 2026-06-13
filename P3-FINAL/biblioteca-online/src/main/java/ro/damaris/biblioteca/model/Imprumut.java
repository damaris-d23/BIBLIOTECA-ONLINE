package ro.damaris.biblioteca.model;

/**
 *clasa Imprumut reprezinta legatura dintre o carte si un cititor
 *retine informatii despre data imprumutului si data returnarii
 *
 *un imprumut este considerat activ daca data de returnare este null
 *
 */
public class Imprumut {

    /**id-ul unic al imprumutului */
    private int idImprumut;

    /**data la care a fost facut imprumutul */
    private String dataImprumut;

    /**data returnarii cartii (null pana la returnare) */
    private String dataReturnare; // este null pana se returneaza

    /**cartea imprumutata */
    private Carte carte;

    /**cititorul care a imprumutat cartea */
    private Cititor cititor;

    /**
     *constructor pentru imprumut
     *la crearea imprumutului, data returnarii este setata null
     *
     *@param idImprumut id-ul imprumutului
     *@param dataImprumut data imprumutului
     *@param carte cartea imprumutata
     *@param cititor cititorul care face imprumutul
     */
    public Imprumut(int idImprumut, String dataImprumut, Carte carte, Cititor cititor) {
        this.idImprumut = idImprumut;
        this.dataImprumut = dataImprumut;
        this.carte = carte;
        this.cititor = cititor;
        this.dataReturnare = null;
    }

    /**
     *@return id-ul imprumutului
     */
    public int getIdImprumut() {
        return idImprumut;
    }

    /**
     *@return data imprumutului
     */
    public String getDataImprumut() {
        return dataImprumut;
    }

    /**
     *@return data returnarii sau null daca imprumutul este activ
     */
    public String getDataReturnare() {
        return dataReturnare;
    }

    /**
     *@return cartea asociata imprumutului
     */
    public Carte getCarte() {
        return carte;
    }

    /**
     *@return cititorul asociat imprumutului
     */
    public Cititor getCititor() {
        return cititor;
    }

    /**
     *verifica daca imprumutul este acti
     *
     *@return true daca dataReturnare este null, false altfel
     */
    public boolean esteActiv() {
        return dataReturnare == null;
    }

    /**
     *marcheaza imprumutul ca fiind returnat
     *
     *@param dataReturnare data la care cartea a fost returnata
     */
    public void returneaza(String dataReturnare) {
        this.dataReturnare = dataReturnare;
    }

    /**
     *returneaza o reprezentare text a imprumutului folosita pentru afisare
     *
     *@return string cu detalii despre imprumut
     */
    @Override
    public String toString() {
        return "Imprumut{" + "id=" + idImprumut + ", carte=" + carte.getTitlu() + ", cititor=" + cititor.getNume() + ", imprumut='" + dataImprumut + '\'' + ", returnare='" + dataReturnare + '\'' + '}';
    }
}
