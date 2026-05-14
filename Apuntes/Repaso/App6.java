import java.sql.*;

public class App6 {
    public static void main(String[] args) {
                Scanner sc = new Scanner(System.in);
        int opcion = 0;
        // EL MENU
        do {
            System.out.println("1. Listado basico");
            System.out.println("2. Buscar por curso");
            System.out.println("3. Media por curso");
            System.out.println("4. Aprobados/Suspensos");
            System.out.println("5. Mejor estudiante");
            System.out.println("0. Chau");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    mostrarListadoBasico();
                    break;
                case 2:
                    BuscarCurso();
                    break;
                case 3:
                    MediaCursos();
                    break;
                case 4:
                    AprobSus();
                    break;
                case 5:
                    SpesialOne();
                    break;
                case 6:
                    Matricular();
                    break;
                case 7:
                    actualizarNota();
                    break;
                case 8:
                    DarBaja();
                    break;
            }

        } while (opcion != 0);
    }
        public static void mostrarListadoBasico() {
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
                Double nota_media = rs.getString(nota_media);
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
