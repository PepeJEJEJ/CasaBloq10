import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginMySQL {

    public static void main(String[] args) {

        JFrame ventana = new JFrame("Login");
        ventana.setSize(320, 200);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        ventana.add(panel);

        JTextField campoUsuario = new JTextField(12);
        JPasswordField campoPass = new JPasswordField(12);
        JButton botonLogin = new JButton("Iniciar sesión");
        JLabel resultado = new JLabel("");

        panel.add(new JLabel("Usuario:"));
        panel.add(campoUsuario);

        panel.add(new JLabel("Contraseña:"));
        panel.add(campoPass);

        panel.add(botonLogin);
        panel.add(resultado);

        botonLogin.addActionListener(e -> {
            String usuario = campoUsuario.getText();
            String pass = new String(campoPass.getPassword());

            String mensaje = validarLogin(usuario, pass);

            if (mensaje.startsWith("OK")) {
                resultado.setText("Inicio de sesión correcto. " + mensaje.substring(3));
                resultado.setForeground(Color.GREEN);
            } else {
                resultado.setText(mensaje);
                resultado.setForeground(Color.RED);
            }
        });

        ventana.setVisible(true);
    }

    private static String validarLogin(String usuario, String pass) {
        String url = "jdbc:mysql://localhost:3306/sistema_login";
        String user = "root";
        String password = ""; // tu contraseña aquí

        try (Connection con = DriverManager.getConnection(url, user, password)) {

            // 1. Comprobar si el usuario existe
            PreparedStatement st = con.prepareStatement(
                "SELECT password, nombre_completo FROM usuarios WHERE usuario = ?"
            );
            st.setString(1, usuario);

            ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                return "Usuario inexistente";
            }

            String passReal = rs.getString("password");
            String nombre = rs.getString("nombre_completo");

            // 2. Comprobar contraseña
            if (!passReal.equals(pass)) {
                return "Contraseña incorrecta";
            }

            return "OK " + "Bienvenido, " + nombre;

        } catch (SQLException e) {
            return "Error de conexión con la base de datos";
        }
    }
}
