import java.sql.*;

public class App6 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";
        String sql = "Select * from Alumnos";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                Double nota_media = rs.getDouble(nota_media);
                System.out.println(nombre + " - " + curso + " - " + nota_media);
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
