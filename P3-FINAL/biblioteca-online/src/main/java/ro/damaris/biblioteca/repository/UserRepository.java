package ro.damaris.biblioteca.repository;

import ro.damaris.biblioteca.db.DbManager;
import ro.damaris.biblioteca.model.Cititor;
import ro.damaris.biblioteca.model.Utilizator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *repository pentru utilizatori(tabelele users + readers)
 *gestioneaza autentificarea si inregistrarea
 *cand utilizatorul este de tip CITITOR, se ia si nr_legitimatie din tabela readers
 *
 */
public class UserRepository {

    /**
     *autentifica un utilizator pe baza de email si parola
     *
     *@param email email-ul introdus
     *@param parola parola introdusa
     *@return utilizatorul logat (Cititor sau Utilizator) sau null daca login-ul esueaza
     @throws RuntimeException daca apare o eroare la accesarea bazei de date
     */
    public Utilizator login(String email, String parola) {
        String sql = "SELECT id, nume, email, parola, tip FROM users WHERE email=?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String parolaDb = rs.getString("parola");
                if (parolaDb == null || !parolaDb.equals(parola)) return null;

                int id = rs.getInt("id");
                String tip = rs.getString("tip");

                if ("CITITOR".equals(tip)) {
                    Integer nrLeg = getNrLegitimatie(conn, id);
                    if (nrLeg == null) return null;

                    return new Cititor(
                            id,
                            rs.getString("nume"),
                            rs.getString("email"),
                            parolaDb,
                            nrLeg
                    );
                }

                return new Utilizator(
                        id,
                        rs.getString("nume"),
                        rs.getString("email"),
                        parolaDb,
                        tip
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("eroare login: " + e.getMessage(), e);
        }
    }

    /**
     *citeste numarul de legitimatie pentru un cititor din tabela readers
     *
     *@param conn conexiunea deja deschisa
     *@param userId id-ul utilizatorului
     *@return numarul de legitimatie sau null daca nu exista in tabela readers
     *@throws RuntimeException daca apare o eroare la interogare
     */
    private Integer getNrLegitimatie(Connection conn, int userId) {
        String sql = "SELECT nr_legitimatie FROM readers WHERE user_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getInt("nr_legitimatie");
            }
        } catch (Exception e) {
            throw new RuntimeException("eroare readers: " + e.getMessage(), e);
        }
    }

    /**
     *inregistreaza un utilizator nou
     *metoda face validari de baza si foloseste tranzactie (commit/rollback) pentru insert in users si daca tipul este CITITOR, insert si in readers
     *
     *@param nume numele utilizatorului
     *@param email email-ul
     *@param parola parola
     *@param tip tipul utilizatorului ("CITITOR" sau "BIBLIOTECAR")
     *@param nrLegitimatie numarul de legitimatie (obligatoriu daca tipul este CITITOR)
     *@return id-ul utilizatorului creat
     *@throws IllegalArgumentException daca datele sunt invalide sau email-ul este deja folosit
     *@throws RuntimeException daca apare o eroare la baza de date
     */
    public int register(String nume, String email, String parola, String tip, Integer nrLegitimatie) {
        if (nume == null || nume.isBlank()) throw new IllegalArgumentException("nume invalid");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email invalid");
        if (parola == null || parola.isBlank()) throw new IllegalArgumentException("parola invalida");
        if (!"CITITOR".equals(tip) && !"BIBLIOTECAR".equals(tip)) throw new IllegalArgumentException("tip invalid");

        try (Connection conn = DbManager.getConnection()) {
            conn.setAutoCommit(false);

            int userId = insertUser(conn, nume.trim(), email.trim(), parola, tip);

            if ("CITITOR".equals(tip)) {
                if (nrLegitimatie == null) throw new IllegalArgumentException("nr legitimatie lipsa");
                insertReader(conn, userId, nrLegitimatie);
            }

            conn.commit();
            conn.setAutoCommit(true);
            return userId;

        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            String msg = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
            if (msg.contains("unique") && msg.contains("users.email")) {
                throw new IllegalArgumentException("email deja folosit");
            }
            throw new RuntimeException("eroare register: " + e.getMessage(), e);
        }
    }

    /**
     *insereaza utilizatorul in tabela users si returneaza id-ul generat
     *
     *@param conn conexiunea deschisa
     *@param nume numele
     *@param email email-ul
     *@param parola parola
     *@param tip tipul utilizatorului
     *@return id-ul generat in tabela users
     *@throws Exception daca insert-ul esueaza sau nu se poate obtine id-ul
     */
    private int insertUser(Connection conn, String nume, String email, String parola, String tip) throws Exception {
        String sql = "INSERT INTO users(nume, email, parola, tip) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nume);
            ps.setString(2, email);
            ps.setString(3, parola);
            ps.setString(4, tip);
            ps.executeUpdate();
        }

        try (PreparedStatement ps2 = conn.prepareStatement("SELECT last_insert_rowid()");
             ResultSet rs = ps2.executeQuery()) {
            if (!rs.next()) throw new RuntimeException("nu pot lua id user");
            return rs.getInt(1);
        }
    }

    /**
     *insereaza datele specifice cititorului in tabela readers
     *
     *@param conn conexiunea deschisa
     *@param userId id-ul utilizatorului din tabela users
     *@param nrLegitimatie numarul de legitimatie al cititorului
     *@throws Exception daca insert-ul esueaza
     */
    private void insertReader(Connection conn, int userId, int nrLegitimatie) throws Exception {
        String sql = "INSERT INTO readers(user_id, nr_legitimatie) VALUES(?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, nrLegitimatie);
            ps.executeUpdate();
        }
    }
}
