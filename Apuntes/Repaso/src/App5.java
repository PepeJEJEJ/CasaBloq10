import java.sql.*;

public class App5 {
    public static void main(String[] args) {
        // Ejemplo 1: lectura con executeQuery() y resultados con ResultSet
        String url1 = "jdbc:mysql://localhost:3306/instituto";
        String user1 = "root";
        String password1 = "admin123";
        String sqlSelect = "SELECT * FROM instituto";

        try {
            Connection conn = DriverManager.getConnection(url1, user1, password1);
            PreparedStatement stmt = conn.prepareStatement(sqlSelect);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                System.out.println(id + ": " + nombre);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erorr de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido" + e.getMessage());
        }

        // Ejemplo 2: inserción con executeUpdate()
        String url2 = "jdbc:mysql://localhost:3306/instituto";
        String user2 = "root";
        String password2 = "admin123";
        String sqlInsert = "INSERT INTO alumnos (nombre, nota_media) VALUES (?, ?)";

        try {
            Connection conn = DriverManager.getConnection(url2, user2, password2);
            PreparedStatement pstmt = conn.prepareStatement(sqlInsert);

            pstmt.setString(1, "Ana Garcia");
            pstmt.setDouble(2, 8.5);

            int filasInsertadas = pstmt.executeUpdate();

            System.out.println("Insertados: " + filasInsertadas + " registros.");

            pstmt.close();
            conn.close();

        } catch (SQLException sqle) {
            System.out.println("Error de SQL al insertar: " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }

        // Ejemplo 3: actualización con executeUpdate()
        String url3 = "jdbc:mysql://localhost:3306/instituto";
        String user3 = "root";
        String password3 = "admin123";
        String sqlUpdate = "UPDATE alumnos SET curso = ? WHERE nombre = ?";

        try {
            Connection conn = DriverManager.getConnection(url3, user3, password3);
            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);

            pstmt.setString(1, "2DAM");
            pstmt.setString(2, "Ana Garcia");

            int filasActualizadas = pstmt.executeUpdate();

            System.out.println("Actualizados: " + filasActualizadas + " registros");

            pstmt.close();
            conn.close();

        } catch (SQLException sqle) {
            System.out.println("Error de SQL al actualizar: " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }

        // Ejemplo 4: borrado con executeUpdate()
        String url4 = "jdbc:mysql://localhost:3306/instituto";
        String user4 = "root";
        String password4 = "admin123";
        String sqlDelete = "DELETE FROM alumnos WHERE nombre = ?";

        try {
            Connection conn = DriverManager.getConnection(url4, user4, password4);
            PreparedStatement pstmt = conn.prepareStatement(sqlDelete);

            pstmt.setString(1, "Ana Garcia");
            int filasBorradas = pstmt.executeUpdate();

            System.out.println("Borrados: " + filasBorradas + " registros");

            pstmt.close();
            conn.close();

        } catch (SQLException sqle) {
            System.out.println("Error de SQL al borrar: " + sqle.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }
}
