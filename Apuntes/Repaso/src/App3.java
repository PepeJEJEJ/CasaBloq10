import java.sql.*;
import java.util.Scanner;

public class App3 {

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
            System.out.println("6. Nueva matricula");
            System.out.println("7. Actualizar nota");
            System.out.println("8. Dar de baja");
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
                case 0:
                    System.out.println("Chau");
                    break;
                default:
                    System.out.println("Opción no válida");
            }

        } while (opcion != 0);

        sc.close();
    }

    // 1 Listado básico: SELECT -> executeQuery()
    public static void mostrarListadoBasico() {
        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "admin123";

        String sql = "SELECT nombre, curso FROM alumnos";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(); // SELECT -> executeQuery()

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                System.out.println(nombre + " - " + curso);
            }

        } catch (SQLException e) {
            System.out.println("Erorr de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido" + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception ignored) {
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    // 2 Buscar por curso: SELECT con parámetro -> executeQuery()
    public static void BuscarCurso() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Mete el curso que vamos a buscar: ");
        String cursoBuscado = sc.nextLine();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "admin123";

        String sql = "SELECT nombre, curso FROM alumnos WHERE curso = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cursoBuscado);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                System.out.println(nombre + " - " + curso);
            }

        } catch (SQLException e) {
            System.out.println("Erorr de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido" + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception ignored) {
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    // 3 Media por curso: SELECT AVG -> executeQuery()
    public static void MediaCursos() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Dame el curso: ");
        String cursoBuscado = sc.nextLine();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "admin123";

        String sql = "SELECT AVG(nota) AS media FROM alumnos WHERE curso = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cursoBuscado);
            rs = stmt.executeQuery();

            if (rs.next()) {
                double media = rs.getDouble("media");
                System.out.println("Media del curso " + cursoBuscado + ": " + media);
            }

        } catch (SQLException e) {
            System.out.println("Erorr de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido" + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception ignored) {
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    // 4 Aprobados o suspensos: SELECT -> executeQuery()
    public static void AprobSus() {
        Scanner sc = new Scanner(System.in);
        System.out.print("A aprobados o S suspensos: ");
        String opcion = sc.nextLine().toUpperCase();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "admin123";

        String sql = "";

        if (opcion.equals("A")) {
            sql = "SELECT nombre, curso, nota FROM alumnos WHERE nota >= 5";
        } else if (opcion.equals("S")) {
            sql = "SELECT nombre, curso, nota FROM alumnos WHERE nota < 5";
        } else {
            System.out.println("Opción no válida");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                double nota = rs.getDouble("nota");
                System.out.println(nombre + " - " + curso + " - Nota: " + nota);
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception ignored) {
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    // 5 Mejor estudiante: SELECT -> executeQuery()
    public static void SpesialOne() {
        System.out.print("VER AL TOP DE TOPS: ");

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "admin123";

        // Se usa una consulta que devuelve la fila con la nota más alta
        String sql = "SELECT nombre, curso, nota FROM alumnos ORDER BY nota DESC LIMIT 1";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                double nota = rs.getDouble("nota");
                System.out.println(nombre + " - " + curso + " - Nota: " + nota);
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception ignored) {
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    // 6 Nueva matrícula: INSERT -> executeUpdate() (set parámetros antes de
    // ejecutar)
    public static void Matricular() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nombre del alumno: ");
        String nombre = sc.nextLine();
        System.out.print("Curso del alumno: ");
        String curso = sc.nextLine();
        System.out.print("Nota del alumno: ");
        double nota = sc.nextDouble();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "admin123";

        String sql = "INSERT INTO alumnos (nombre, curso, nota) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql);

            // setear parámetros antes de ejecutar (tal y como indica el PDF)
            stmt.setString(1, nombre);
            stmt.setString(2, curso);
            stmt.setDouble(3, nota);

            int filas = stmt.executeUpdate(); // INSERT -> executeUpdate()
            System.out.println("Alumno matriculado correctamente. Filas insertadas: " + filas);

        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    // 7 Actualizar nota: UPDATE -> executeUpdate()
    public static void actualizarNota() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Dame el ID (alumno): ");
        int id = sc.nextInt();

        System.out.print("Pon su nueva nota (X.Y): ");
        double nuevaNota = sc.nextDouble();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "admin123";

        String sql = "UPDATE alumnos SET nota = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql);

            stmt.setDouble(1, nuevaNota);
            stmt.setInt(2, id);

            int filas = stmt.executeUpdate(); // UPDATE -> executeUpdate()
            System.out.println("Filas actualizadas: " + filas);

        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    // 8 Dar de baja: DELETE -> executeUpdate()
    public static void DarBaja() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Dame el ID del alumno que vamos a Borrar: ");
        int id = sc.nextInt();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "admin123";

        String sql = "DELETE FROM alumnos WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            int filas = stmt.executeUpdate(); // DELETE -> executeUpdate()
            System.out.println("Filas borradas: " + filas);

        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception ignored) {
            }
        }
    }
}
