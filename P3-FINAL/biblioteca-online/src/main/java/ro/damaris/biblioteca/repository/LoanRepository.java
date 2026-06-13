package ro.damaris.biblioteca.repository;

import ro.damaris.biblioteca.db.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *repository pentru gestionarea imprumuturilor (tabela loans)
 *contine operatii pentru creare, verificare si inchidere imprumuturi
 *
 */
public class LoanRepository {

    /**
     *creeaza un imprumut nou pentru o carte si un cititor
     *data returnarii este setata implicit NUL
     *
     *@param bookId id-ul cartii imprumutate
     *@param readerUserId id-ul utilizatorului cititor
     *@param dataImprumut data la care se face imprumutul
     *@return id-ul imprumutului creat sau null daca nu se poate obtine
     *@throws RuntimeException daca apare o eroare la insert
     */
    public Integer createLoan(int bookId, int readerUserId, String dataImprumut) {
        String sql = "INSERT INTO loans(book_id, reader_user_id, data_imprumut, data_returnare) VALUES(?,?,?,NULL)";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ps.setInt(2, readerUserId);
            ps.setString(3, dataImprumut);
            ps.executeUpdate();

            try (PreparedStatement ps2 = conn.prepareStatement("SELECT last_insert_rowid()");
                 ResultSet rs = ps2.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("eroare createLoan: " + e.getMessage(), e);
        }
    }

    /**
     *verifica daca exista un imprumut activ pentru o carte
     *un imprumut este activ daca data_returnare este NULL.
     *
     *@param bookId id-ul cartii
     *@return true daca exista un imprumut activ, false altfel
     *@throws RuntimeException daca apare o eroare la interogare
     */
    public boolean isLoanActiveForBook(int bookId) {
        String sql = "SELECT 1 FROM loans WHERE book_id=? AND data_returnare IS NULL LIMIT 1";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException("eroare isLoanActiveForBook: " + e.getMessage(), e);
        }
    }

    /**
     *inchide imprumutul activ pentru o carte
     *seteaza data returnarii pentru imprumutul care nu este inca inchis
     *
     *@param bookId id-ul cartii
     *@param dataReturnare data la care cartea este returnata
     *@throws IllegalStateException daca nu exista un imprumut activ
     *@throws RuntimeException daca apare o eroare la update
     */
    public void closeLoanByBook(int bookId, String dataReturnare) {
        String sql = "UPDATE loans SET data_returnare=? WHERE book_id=? AND data_returnare IS NULL";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dataReturnare);
            ps.setInt(2, bookId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new IllegalStateException("nu exista imprumut activ pentru carte");
            }
        } catch (Exception e) {
            throw new RuntimeException("eroare closeLoan: " + e.getMessage(), e);
        }
    }
}
