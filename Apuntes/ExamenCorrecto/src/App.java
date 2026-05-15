import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        // EL MENU
        do {
            System.out.println("1. Listado basico");
            System.out.println("2. Reservar");
            System.out.println("3. Añadir Libro");
            System.out.println("4. Salir");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    MostrarTodo();
                    break;
                case 2:
                    Reservar();
                    break;
                case 3:
                    Añadir();
                    break;
                case 4:
                    System.out.println("Chau");
                    break;
            }

        } while (opcion != 4);
    }

    public static void MostrarTodo() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ver Disponibles (D)");
        String opcion = sc.nextLine().toUpperCase();

        String url = "jdbc:mysql://localhost:3306/biblioteca";//LA URL PA CONECTARNOS
        String user = "root";//EL USUARIO CON EL QUE VAMOS A CONECTARNOS
        String password = "Med@c";//LA CONTRASEÑA

        String sql = "";// EL COMANDO SQL EN BLANCO (EL PORQUE ESTA ABAJO:)

        if (opcion.equals("D")) {// D (POR TECLAO) NOS DA LOS Disponibles (SI DISPONIBLE ES 1)
            sql = "SELECT titulo, autor, disponible FROM libros WHERE disponible = 1";
        } else {
            System.out.println("Opcion invalida");// SI PONES LO QUE SEA, SALE ESTO
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {// LAS COLUMNAS PA MOSTRAR
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                boolean disponible = rs.getBoolean("disponible");
                System.out.println(titulo + " - " + autor + " - Disponible: " + disponible);
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


    public static void Reservar() {
        Scanner sc = new Scanner(System.in);// VALORES PA PONER
        System.out.print("Dame el ID (Libro): ");
        int id = sc.nextInt();

        System.out.print("Pon pa reservar (1): ");
        int Reservar = sc.nextInt();

        String url = "jdbc:mysql://localhost:3306/biblioteca";//LA URL PA CONECTARNOS
        String user = "root";//EL USUARIO CON EL QUE VAMOS A CONECTARNOS
        String password = "Med@c";//LA CONTRASEÑA

        String sql = "UPDATE libros SET disponible = ? WHERE id = ?";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, Reservar);
            stmt.setInt(2, id);

            int libroReservado = stmt.executeUpdate();
            System.out.println("Filas actualizadas: " + libroReservado);

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error de SQL " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error desconocido " + e.getMessage());
        }
    }

public static void Añadir() {//ARREGLADO
    Scanner sc = new Scanner(System.in);

    System.out.print("Nombre del libro: ");
    String nombre = sc.nextLine();

    System.out.print("Nombre del autor: ");
    String autor = sc.nextLine();

    String url = "jdbc:mysql://localhost:3306/biblioteca";
    String user = "root";
    String password = "Med@c";

    String sql = "INSERT INTO libros (titulo, autor, disponible) VALUES (?, ?, ?)";

    try {
        Connection conn = DriverManager.getConnection(url, user, password);
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, nombre);
        stmt.setString(2, autor);
        stmt.setBoolean(3, true);

        int filas = stmt.executeUpdate();
        System.out.println("Libro insertado correctamente. Filas afectadas: " + filas);

        stmt.close();
        conn.close();

    } catch (SQLException e) {
        System.out.println("Error de SQL " + e.getMessage());
    } catch (Exception e) {
        System.out.println("Error desconocido " + e.getMessage());
    }
}

//JOSE A.