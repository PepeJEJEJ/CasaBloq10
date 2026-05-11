import java.sql.*;
import java.util.Scanner;

public class App {
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

    /*
     * 1 Listado básico: la primera opción del menú será mostrar todo el listado de
     * alumnos completo, pero solo mostramos nombre y curso, no mostramos la nota.
     */
    public static void mostrarListadoBasico() {
        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";

        String sql = "SELECT nombre, curso FROM alumnos";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);// PILLA LAS CREDENCIALES
            PreparedStatement stmt = conn.prepareStatement(sql);// LA ACCION SQL
            ResultSet rs = stmt.executeQuery();// EJECUTAR LA ACCION SQL

            while (rs.next()) {// CURSOR QUE AVANZA MIENTRAS DEVUELVAN INFO
                String nombre = rs.getString("nombre");// SI HAY UN NOMBRE, DEVUELVE EL NOMBRE
                String curso = rs.getString("curso");// SI HAY CURSO, TAMBIEN LO DEVUELVE
                System.out.println(nombre + " - " + curso);
            }

            rs.close();// CERRAR TODO
            stmt.close();
            conn.close();

        } catch (SQLException e) {// EXCEPCIONES
            System.out.println("Erorr de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido" + e.getMessage());
        }
    }

    /*
     * 2 Buscar por curso: la segunda opción del menú consiste en solicitar al
     * usuario
     * un curso, y mostrar todos los alumnos de dicho curso.
     */
    public static void BuscarCurso() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Mete el curso que vamos a buscar: ");
        String cursoBuscado = sc.nextLine();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";

        String sql = "SELECT nombre, curso FROM alumnos WHERE curso = ?"; // "?" es un parametro (va a ser reemplazado)

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cursoBuscado);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                System.out.println(nombre + " - " + curso);
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

    /*
     * 3. Calcular la media de cada curso: la tercera opción del menú primero
     * pregunta al usuario de qué curso quiere saber la nota. Cuando el usuario haya
     * especificado un curso, el programa muestra la nota media de todos los alumnos
     * del curso indicado.
     */
    public static void MediaCursos() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Dame el curso: ");
        String cursoBuscado = sc.nextLine();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";

        String sql = "SELECT AVG(nota) AS media FROM alumnos WHERE curso = ?";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cursoBuscado);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double media = rs.getDouble("media");// ESTO ES LA MEDIA, ESTO GENERA UNA COLUMNA LLAMADA "MEDIA"
                System.out.println("Media del curso " + cursoBuscado + ": " + media); // DEVUELVE UNA SOLA FILA
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

    /*
     * 4. Aprobados o suspensos: la cuarta opción del menú pregunta al usuario si
     * quiere ver la lista de todos los aprobados o la lista de todos los suspensos,
     * y una vez conteste el usuario, se muestra la lista correspondiente (de ambos
     * cursos).
     */
    public static void AprobSus() {
        Scanner sc = new Scanner(System.in);
        System.out.print("A aprobados o S suspensos: ");
        String opcion = sc.nextLine().toUpperCase();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";

        String sql = "";// EL COMANDO SQL EN BLANCO (EL PQ ESTA ABAJO:)

        if (opcion.equals("A")) {// A (POR TECLAO) NOS DA LOS APROBADOS (SI SUS NOTAS SUPERAN O SON 5)
            sql = "SELECT nombre, curso, nota FROM alumnos WHERE nota >= 5";
        } else if (opcion.equals("S")) {// B (POR TECLAO) NOS DA LOS SUSPENSOS (SI SUS NOTAS NO SUPERAN EL 5)
            sql = "SELECT nombre, curso, nota FROM alumnos WHERE nota < 5";
        } else {
            System.out.println("Opción no válida");// SI PONES BABOSADAS, SALE ESTO
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {// LAS COLUMNAS PA MOSTRAR
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                double nota = rs.getDouble("nota");
                System.out.println(nombre + " - " + curso + " - Nota: " + nota);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        }
    }

    /*
     * 5 El mejor estudiante: la quinta opción del menú consiste en mostrar el mejor
     * estudiante de todos
     */
    public static void SpesialOne() {
        System.out.print("VER AL TOP DE TOPS: ");

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";

        String sql = "Select nombre, curso, max(nota_media)FROM alumnos";// MAXIMO

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {// LAS COLUMNAS PA MOSTRAR SIEMPRE QUE HAYA INFORMACION
                String nombre = rs.getString("nombre");
                String curso = rs.getString("curso");
                double nota = rs.getDouble("nota");
                System.out.println(nombre + " - " + curso + " - Nota: " + nota);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        }
    }

    /*
     * 6 Nueva matrícula: esta sexta opción del menú consiste en solicitar todos los
     * datos de un nuevo estudiante y matricularlo (añadirlo a la bbdd).
     */
    public static void Matricular() {
        Scanner sc = new Scanner(System.in);// VALORES PA PONER
        System.out.print("Nombre del alumno: ");
        String nombre = sc.nextLine();
        System.out.print("Curso del alumno: ");
        String curso = sc.nextLine();
        System.out.print("Nota del alumno: ");
        double nota = sc.nextDouble();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";

        String sql = "INSERT INTO alumnos (nombre, curso, nota) VALUES (?, ?, ?)";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            stmt.setString(1, nombre);// PA PODER AÑADIR LO QUE HEMOS PUESTO ANTES (PARAMETROS Y SU ORDEN AL PONERLOS)
            stmt.setString(2, curso);
            stmt.setDouble(3, nota);

            int filas = stmt.executeUpdate();
            System.out.println("Alumno matriculado correctamente. Filas insertadas: " + filas);

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        }
    }

    /*
     * 7 Actualizar nota tras la recuperación o subida de nota: la séptima opción
     * consiste en pedir al usuario un ID de un alumno y la nueva nota que ha
     * obtenido, modificando la nota al alumno correspondiente en la tabla de la
     * BBDD.
     */
    public static void actualizarNota() {
        Scanner sc = new Scanner(System.in);// VALORES PA PONER
        System.out.print("Dame el ID (alumno): ");
        int id = sc.nextInt();

        System.out.print("Pon su nueva nota (X.Y): ");
        double nuevaNota = sc.nextDouble();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";

        String sql = "UPDATE alumnos SET nota = ? WHERE id = ?";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setDouble(1, nuevaNota);
            stmt.setInt(2, id);

            int filas = stmt.executeUpdate();
            System.out.println("Filas actualizadas: " + filas);

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        }
    }

    /*
     * 8 Baja de un alumno: la octava opción del menú consiste en solicitar un ID y
     * borrar dicho alumno de la BBDD.
     */
    public static void DarBaja() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Dame el ID del alumno que vamos a Borrar: ");
        int id = sc.nextInt();

        String url = "jdbc:mysql://localhost:3306/instituto";
        String user = "root";
        String password = "12345";

        String sql = "DELETE FROM alumnos WHERE id = ?";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            int filas = stmt.executeUpdate();// EJECUTAR EL BORRADO
            System.out.println("Filas borradas: " + filas);

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        }
    }
}
/* ANEXOS NO ENTRAN */
/* 12345 Casa */
/* Med@c Clase */