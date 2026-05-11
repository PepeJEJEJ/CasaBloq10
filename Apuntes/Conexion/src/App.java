import java.sql.*;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";
        String sqlSelect = "SELECT * FROM alumnos";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
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
    }
}
/*ANEXOS NO ENTRAN*/
/*12345 Casa*/
/*Med@c Clase*/