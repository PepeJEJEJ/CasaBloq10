import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App1 {

    // Configuración de la base de datos (mejor mover a fichero/variables de entorno)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/instituto";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println();
            System.out.println("1. Listado basico");
            System.out.println("2. Buscar por curso");
            System.out.println("3. Media por curso");
            System.out.println("4. Aprobados/Suspensos");
            System.out.println("5. Mejor estudiante");
            System.out.println("6. Nueva matricula");
            System.out.println("7. Actualizar nota");
            System.out.println("8. Dar de baja");
            System.out.println("0. Chau");
            System.out.print("Elige una opción: ");

            try {
                opcion = sc.nextInt();
                sc.nextLine(); // consumir salto de línea
            } catch (InputMismatchException ime) {
                System.out.println("Entrada no válida. Introduce un número.");
                sc.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    mostrarListadoBasico();
                    break;
                case 2:
                    buscarCurso();
                    break;
                case 3:
                    mediaCursos();
                    break;
                case 4:
                    aprobadosSuspensos();
                    break;
                case 5:
                    mejorEstudiante();
                    break;
                case 6:
                    matricular();
                    break;
                case 7:
                    actualizarNota();
                    break;
                case 8:
                    darBaja();
                    break;
                case 0:
                    System.out.println("Adiós.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        sc.close();
    }

    // 1 Listado básico: nombre y curso
    public static void mostrarListadoBasico() {
        String sql = "SELECT nombre, curso FROM alumnos";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            boolean any = false;
            while (rs.next()) {
                any = true;
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                System.out.println(nombre + " - " + curso);
            }
            if (!any) {
                System.out.println("No hay alumnos registrados.");
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }

    // 2 Buscar por curso
    public static void buscarCurso() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Mete el curso que vamos a buscar: ");
        String cursoBuscado = sc.nextLine().trim();

        if (cursoBuscado.isEmpty()) {
            System.out.println("Curso vacío.");
            return;
        }

        String sql = "SELECT nombre, curso FROM alumnos WHERE curso = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cursoBuscado);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    String nombre = rs.getString("nombre");
                    String curso = rs.getString("curso");
                    System.out.println(nombre + " - " + curso);
                }
                if (!any) {
                    System.out.println("No se encontraron alumnos para el curso: " + cursoBuscado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }

    // 3 Media por curso
    public static void mediaCursos() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Dame el curso: ");
        String cursoBuscado = sc.nextLine().trim();

        if (cursoBuscado.isEmpty()) {
            System.out.println("Curso vacío.");
            return;
        }

        String sql = "SELECT AVG(nota) AS media FROM alumnos WHERE curso = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cursoBuscado);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double media = rs.getDouble("media");
                    if (rs.wasNull()) {
                        System.out.println("No hay notas para el curso " + cursoBuscado);
                    } else {
                        System.out.printf("Media del curso %s: %.2f%n", cursoBuscado, media);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }

    // 4 Aprobados o suspensos
    public static void aprobadosSuspensos() {
        Scanner sc = new Scanner(System.in);
        System.out.print("A aprobados o S suspensos: ");
        String opcion = sc.nextLine().trim().toUpperCase();

        String sql;
        if ("A".equals(opcion)) {
            sql = "SELECT nombre, curso, nota FROM alumnos WHERE nota >= 5";
        } else if ("S".equals(opcion)) {
            sql = "SELECT nombre, curso, nota FROM alumnos WHERE nota < 5";
        } else {
            System.out.println("Opción no válida");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            boolean any = false;
            while (rs.next()) {
                any = true;
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                double nota = rs.getDouble("nota");
                System.out.println(nombre + " - " + curso + " - Nota: " + nota);
            }
            if (!any) {
                System.out.println("No hay resultados para la consulta solicitada.");
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }

    // 5 Mejor estudiante (por nota más alta)
    public static void mejorEstudiante() {
        String sql = "SELECT nombre, curso, nota FROM alumnos ORDER BY nota DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                double nota = rs.getDouble("nota");
                System.out.println("Mejor estudiante: " + nombre + " - " + curso + " - Nota: " + nota);
            } else {
                System.out.println("No hay alumnos en la base de datos.");
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }

    // 6 Nueva matrícula
    public static void matricular() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nombre del alumno: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Curso del alumno: ");
        String curso = sc.nextLine().trim();

        double nota;
        try {
            System.out.print("Nota del alumno (ej. 7.5): ");
            nota = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException nfe) {
            System.out.println("Nota no válida.");
            return;
        }

        if (nombre.isEmpty() || curso.isEmpty()) {
            System.out.println("Nombre o curso vacío.");
            return;
        }

        String sql = "INSERT INTO alumnos (nombre, curso, nota) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, curso);
            stmt.setDouble(3, nota);

            int filas = stmt.executeUpdate();
            System.out.println("Alumno matriculado correctamente. Filas insertadas: " + filas);

        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }

    // 7 Actualizar nota
    public static void actualizarNota() {
        Scanner sc = new Scanner(System.in);
        int id;
        try {
            System.out.print("Dame el ID (alumno): ");
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException nfe) {
            System.out.println("ID no válido.");
            return;
        }

        double nuevaNota;
        try {
            System.out.print("Pon su nueva nota (X.Y): ");
            nuevaNota = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException nfe) {
            System.out.println("Nota no válida.");
            return;
        }

        String sql = "UPDATE alumnos SET nota = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, nuevaNota);
            stmt.setInt(2, id);

            int filas = stmt.executeUpdate();
            System.out.println("Filas actualizadas: " + filas);

        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }

    // 8 Dar de baja
    public static void darBaja() {
        Scanner sc = new Scanner(System.in);
        int id;
        try {
            System.out.print("Dame el ID del alumno que vamos a borrar: ");
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException nfe) {
            System.out.println("ID no válido.");
            return;
        }

        String sql = "DELETE FROM alumnos WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();
            System.out.println("Filas borradas: " + filas);

        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido: " + e.getMessage());
        }
    }
}
