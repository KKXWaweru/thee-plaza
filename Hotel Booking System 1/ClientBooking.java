import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ClientBooking extends JDialog {
    private JLabel lblClientF_Name;
    private JTextField tfClientF_Name;
    private JTextField tfClientS_Name;
    private JLabel lblClientS_Name;
    private JTextField tfEmail;
    private JLabel lblEmail;
    private JTextField tfTelNo;
    private JLabel lblTelNo;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel ClientBookingPanel;

    public ClientBooking(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(ClientBookingPanel);
        setMinimumSize(new Dimension(800, 650));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerClient();
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

    public void registerClient() {
        String F_Name = tfClientF_Name.getText();
        String S_Name = tfClientS_Name.getText();
        String Email = tfEmail.getText();
        String TelNo = tfTelNo.getText();
        client = addClientToDatabase(F_Name, S_Name,Email,TelNo);
        if (client != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }



    public Client client;

    private Client addClientToDatabase(String F_Name, String S_Name, String Email, String TelNo) {
        Client client = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/thee Plaza";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Class.forName("com.mysql.cj.jdbc.Driver");

            //CONNECTION TO THE DB

            Statement statement = connection.createStatement();
            String sql = "INSERT INTO clients (ClientF_Name, ClientS_Name,Email,Tel_No,) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, F_Name);
            preparedStatement.setString(2, S_Name);
            preparedStatement.setString(3, Email);
            preparedStatement.setString(4, TelNo);



            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                client = new Client();
                client.ClientF_Name = F_Name;
                client.ClientS_Name = S_Name;
                client.TelNo = TelNo;
                client.email = Email;

            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }


    public static void main(String[] args) {
        ClientBooking clientBooking = new ClientBooking(null);
        Client client=clientBooking.client;

        if (client != null) {
            System.out.println("You have registered: " +client.ClientF_Name+"\t"+client.ClientS_Name);
        } else {
            System.out.println("Registration cancelled");
        }
    }
}