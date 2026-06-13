package ro.damaris.biblioteca.repository;

import ro.damaris.biblioteca.db.DbManager;
import ro.damaris.biblioteca.model.Carte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *repository pentru tabela "books"
 *metodele care comunica direct cu baza de date:
 *citire carti
 *cautare
 *insert /update
 *"stergere" logica (active = 0)
 *schimbare disponibilitate
 *
 */
public class BookRepository {

    /**
     *returneaza toate cartile active din baza de date
     *sunt filtrate doar cartile cu active = 1.
     *
     *@return lista de carti
     *@throws RuntimeException daca apare o eroare la accesarea bazei de date
     */
    public List<Carte> findAll() {
        String sql = "SELECT id, titlu, autor, an_aparitie, disponibila FROM books WHERE active = 1 ORDER BY id";
        List<Carte> rez = new ArrayList<>();

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Carte c = new Carte(
                        rs.getInt("id"),
                        rs.getString("titlu"),
                        rs.getString("autor"),
                        rs.getInt("an_aparitie")
                );
                c.setDisponibila(rs.getInt("disponibila") == 1);
                rez.add(c);
            }
            return rez;
        } catch (Exception e) {
            throw new RuntimeException("eroare findAll carti: " + e.getMessage(), e);
        }
    }

    /**
     *cauta carti dupa text in titlu sau autor (case-insensitive)
     *
     *@param text textul introdus de utilizator
     *@return lista de carti gasite
     *@throws RuntimeException daca apare o eroare la accesarea bazei de date
     */
    public List<Carte> search(String text) {
        String sql = """
                SELECT id, titlu, autor, an_aparitie, disponibila
                FROM books
                WHERE lower(titlu) LIKE ? OR lower(autor) LIKE ?
                ORDER BY id
                """;
        List<Carte> rez = new ArrayList<>();
        String q = "%" + (text == null ? "" : text.toLowerCase()) + "%";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, q);
            ps.setString(2, q);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Carte c = new Carte(
                            rs.getInt("id"),
                            rs.getString("titlu"),
                            rs.getString("autor"),
                            rs.getInt("an_aparitie")
                    );
                    c.setDisponibila(rs.getInt("disponibila") == 1);
                    rez.add(c);
                }
            }
            return rez;
        } catch (Exception e) {
            throw new RuntimeException("eroare search carti: " + e.getMessage(), e);
        }
    }

    /**
     *insereaza o carte noua in tabela books
     *disponibilitatea este setata implicit la 1
     *
     *@param titlu titlul cartii
     *@param autor autorul cartii
     *@param an anul aparitiei
     *@return id-ul generat pentru cart
     *@throws RuntimeException daca apare o eroare la insert
     */
    public int insert(String titlu, String autor, int an) {
        String sql = "INSERT INTO books(titlu, autor, an_aparitie, disponibila) VALUES(?,?,?,1)";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, titlu);
            ps.setString(2, autor);
            ps.setInt(3, an);
            ps.executeUpdate();

            try (PreparedStatement ps2 = conn.prepareStatement("SELECT last_insert_rowid()");
                 ResultSet rs = ps2.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (Exception e) {
            throw new RuntimeException("eroare insert carte: " + e.getMessage(), e);
        }
    }

    /**
     *actualizeaza datele unei carti existente
     *
     *@param id id-ul cartii
     *@param titlu titlul nou
     *@param autor autorul nou
     *@param an anul aparitiei nou
     *@throws IllegalArgumentException daca nu exista cartea
     *@throws RuntimeException daca apare o eroare la update
     */
    public void update(int id, String titlu, String autor, int an) {
        String sql = "UPDATE books SET titlu=?, autor=?, an_aparitie=? WHERE id=?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, titlu);
            ps.setString(2, autor);
            ps.setInt(3, an);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("cartea nu exista");
            }
        } catch (Exception e) {
            throw new RuntimeException("eroare update carte: " + e.getMessage(), e);
        }
    }

    /**
     *sterge logic o carte (nu o scoate fizic din db).
     *seteaza active = 0 doar daca cartea este disponibila (disponibila =1).
     *
     *@param id id-ul cartii
     *@throws IllegalArgumentException daca nu exista cartea sau nu poate fi stearsa
     *@throws RuntimeException daca apare o eroare la update
     */
    public void delete(int id) {
        String sql = "UPDATE books SET active = 0 WHERE id = ? AND disponibila = 1";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("cartea nu exista");
            }
        } catch (Exception e) {
            throw new RuntimeException("eroare delete carte: " + e.getMessage(), e);
        }
    }

    /**
     *modifica disponibilitatea unei carti
     metoda este folosita cand se imprumuta/returneaza o carte
     *
     *@param idCarte id-ul cartii
     *@param disponibila noul status (true/false)
     *@throws RuntimeException daca apare o eroare la update
     */
    public void setDisponibila(int idCarte, boolean disponibila) {
        String sql = "UPDATE books SET disponibila=? WHERE id=?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disponibila ? 1 : 0);
            ps.setInt(2, idCarte);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("eroare update disponibila: " + e.getMessage(), e);
        }
    }
}
