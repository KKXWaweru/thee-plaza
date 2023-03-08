import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginForm extends JDialog {
    JTextField tfEmail;
    JPasswordField pfPassword;
    JButton btnOK;
    JButton btnCancel;
    JPanel LoginPanel;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(700, 610));
        setModal(true);

        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String pass = String.valueOf(pfPassword.getPassword());
                user = getAuthenticatedUser(email, pass);

                if (user != null) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password invalid",
                            "TRY AGAIN",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }


    public User user;

    private User getAuthenticatedUser(String email, String pass) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/thee Plaza";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //CONNECTION TO THE DB
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND pass=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);


            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.UF_name = resultSet.getString("UF_Name");
                user.US_name = resultSet.getString("US_Name");
                user.email = resultSet.getString("Email");
                user.Pass = resultSet.getString("Pass");
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return user;
    }





             public static void main(String[] args) {
                LoginForm loginForm = new LoginForm(null);
                User user=loginForm.user;
                if (user!= null){
                    System.out.println("Successful Authentication of:"+user.UF_name+"\t"+user.US_name);
                    System.out.println("Email\t"+user.email);
                }
                else{
                    System.out.println("Authentication Cancelled");
                }
            }
        }
