package ro.damaris.biblioteca.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 *gestioneaza conexiunea sqlite + init tabele (schema + seed)
 */
public class DbManager {

    /**url-ul jdbc pentru fisierul sqlite folosit de aplicatie*/
    private static final String URL = "jdbc:sqlite:biblioteca-online.db";

    /**
     *creeaza si returneaza o conexiune la baza de date sqlite
     *
     *@return conexiune valida la sqlite
     *@throws RuntimeException daca nu se poate crea conexiunea
     */
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            try (Statement st = conn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }
            return conn;
        } catch (Exception e) {
            throw new RuntimeException("eroare la conexiune sqlite: " + e.getMessage(), e);
        }
    }

    public static void initDb() {
        runSqlResource("db/schema.sql");
        ensureActiveColumnExists();

        if (isTableEmpty("books")) {
            runSqlResource("db/seed.sql");
        }
    }

    /**
     *verifica daca o tabela este goala
     *
     *@param tableName numele tabelei
     *@return true daca tabela nu contine randuri, false altfel
     *@throws RuntimeException daca nu se poate executa interogarea
     */
    private static boolean isTableEmpty(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             var rs = st.executeQuery(sql)) {

            int cnt = rs.next() ? rs.getInt(1) : 0;
            return cnt == 0;

        } catch (Exception e) {
            throw new RuntimeException("nu pot verifica tabela: " + tableName + " -> " + e.getMessage(), e);
        }
    }

    /**
     *ruleaza un fisier .sql din resurse
     *citeste continutul complet si executa comenzile separate prin ';'
     *
     *@param resourcePath calea catre resursa sql din classpath
     *@throws RuntimeException daca resursa nu exista sau daca rularea sql esueaza
     */
    private static void runSqlResource(String resourcePath) {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            String sql = new BufferedReader(new InputStreamReader(
                    DbManager.class.getClassLoader().getResourceAsStream(resourcePath)
            )).lines().collect(Collectors.joining("\n"));

            for (String part : sql.split(";")) {
                String s = part.trim();
                if (!s.isEmpty()) {
                    st.execute(s);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("nu pot rula sql: " + resourcePath + " -> " + e.getMessage(), e);
        }
    }

    /**
     *verifica daca tabela books are coloana "active"
     *daca nu exista, o adauga cu default 1
     *
     *@throws RuntimeException daca nu se poate verifica tabela sau nu se poate face alter table
     */
    private static void ensureActiveColumnExists() {
        try (var conn = getConnection();
             var st = conn.createStatement()) {

            //verifica coloanele din tabela books
            boolean hasActive = false;
            try (var rs = st.executeQuery("PRAGMA table_info(books)")) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    if ("active".equalsIgnoreCase(name)) {
                        hasActive = true;
                        break;
                    }
                }
            }

            //daca lipseste,o adauga
            if (!hasActive) {
                st.executeUpdate("ALTER TABLE books ADD COLUMN active INTEGER NOT NULL DEFAULT 1");
            }

        } catch (Exception e) {
            throw new RuntimeException("eroare migrare active: " + e.getMessage(), e);
        }
    }
}
