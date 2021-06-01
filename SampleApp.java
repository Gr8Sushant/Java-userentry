import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import Helpers.DBUtils;
import  java.sql.Connection;

public class SampleApp extends JFrame {
    String dbURL = "jdbc:mysql://localhost:3306/videogamestation";
    String username = "root";
    String password = "";
    JMenuBar menuBar;
    JPanel dataPanel;
    JPanel buttonPanel;
    JPanel secondPanel;
    JTextField txtFirstname, txtLastname, txtPhonenumber;
    JCheckBox chPositive;
    JButton btnAdd, btnUpdate, btnRemove, btnClear;
    JTable table;
    DefaultTableModel tableModel;
    Connection con;


    public SampleApp() {
        try {
            con = DBUtils.getDbConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setTitle("Videogame Station");
        setMinimumSize(new Dimension ( 300,300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(getMenu());
        setVisible(true);
        setLayout(new GridLayout(1, 2));
        JPanel subPanel = new JPanel(new GridLayout(3, 1));
        subPanel.add(dataUI());
        subPanel.add(fileMethod());
        subPanel.add(buttonUI());
        add(secondUI());
        add(subPanel);
        pack();
        refreshTable();
        setLocationRelativeTo(null);

    }

    private JMenuBar getMenu(){            //menu items
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);    //mnemonics
        editMenu.setMnemonic(KeyEvent.VK_E);
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setToolTipText("Closes the app");  //tooltips
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));   //keystrokes
        exitItem.setMnemonic(KeyEvent.VK_E);

        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.setToolTipText("Click to clear the input fields");
        clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        clearItem.setMnemonic(KeyEvent.VK_C);

        JMenuItem updateItem = new JMenuItem("Update");
        updateItem.setToolTipText("Update a specific row");
        updateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        updateItem.setMnemonic(KeyEvent.VK_S);

        JMenuItem addItem = new JMenuItem("Add");
        addItem.setToolTipText("Add data into the table");
        addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        addItem.setMnemonic(KeyEvent.VK_A);

        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.setToolTipText("Remove selected item");
        removeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        removeItem.setMnemonic(KeyEvent.VK_R);

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setToolTipText("About the Application");
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        aboutItem.setMnemonic(KeyEvent.VK_B);

        exitItem.addActionListener(new ActionListener() {   //menu functionality
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(menuBar, "It is still in trial version!!");
            }
        });

        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    int rowNo = table.getSelectedRow();
                    tableModel.removeRow(rowNo);
                }catch (Exception e1){
                    JOptionPane.showMessageDialog(menuBar, "No items selected");
                }
            }
        });

        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked!");
                System.out.println(txtFirstname.getText());
                System.out.println(txtLastname.getText());
                System.out.println(txtPhonenumber.getText());

                String textFirstName = txtFirstname.getText();
                String textLastName = txtLastname.getText();
                String textPhone = txtPhonenumber.getText();
                String type = chPositive.isSelected() ? "Private" : "Public";

                if (textFirstName.isEmpty() || textLastName.isEmpty() || textPhone.isEmpty()) {
                    JOptionPane.showMessageDialog(buttonPanel, "Please fill up the form.");
                } else {

                    String info = null;
                    if (chPositive.isSelected()) {
                        info = "Private";
                    } else if (!chPositive.isSelected()) {
                        info = "Public";
                    }

                    String insert = "INSERT INTO users (textFname, textLname, textPhn, type) Values (?,?,?,?)";
                    try{
                        PreparedStatement statement = con.prepareStatement(insert);
                        statement.setString(1,textFirstName);
                        statement.setString(2, textLastName);
                        statement.setString(3, textPhone);
                        statement.setString(4, info);

                        statement.executeUpdate();
                        refreshTable();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    btnClear.doClick();
                    JOptionPane.showMessageDialog(buttonPanel, "The details have been added.","Success", JOptionPane.INFORMATION_MESSAGE);


                }
            }
        });

        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (txtFirstname.getText().isEmpty() && txtLastname.getText().isEmpty() && txtPhonenumber.getText().isEmpty()){
                    JOptionPane.showMessageDialog(menuBar,"The input fields are empty");
                }else {
                    txtFirstname.setText("");
                    txtLastname.setText("");
                    txtPhonenumber.setText("");
                    table.clearSelection();
                    chPositive.setSelected(false);
                }
            }
        });

        updateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                String textFirstName = txtFirstname.getText().trim();
                String textLastName = txtLastname.getText().trim();
                String textPhone = txtPhonenumber.getText().trim();

                String positive = chPositive.isSelected() ? "Private" : "Public";
                if (textFirstName.isEmpty() || textLastName.isEmpty()) {
                    JOptionPane.showMessageDialog(buttonPanel, "Some of the fields are empty", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    System.out.println(id);
                    String query = "UPDATE users SET textFname = ?, textLname = ? , textPhn = ? , type = ? WHERE id = ?";
                    try {
                        PreparedStatement statement = con.prepareStatement(query);
                        statement.setString(1,textFirstName);
                        statement.setString(2, textLastName);
                        statement.setString(3, textPhone);
                        statement.setString(4, positive);
                        statement.setInt(5, id);

                        statement.executeUpdate();
                        refreshTable();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    btnClear.doClick();
                    JOptionPane.showMessageDialog(buttonPanel, "Record at row " + id + " is updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        fileMenu.add(exitItem);

        editMenu.add(clearItem);
        editMenu.add(updateItem);
        editMenu.addSeparator();
        editMenu.add(addItem);
        editMenu.add(removeItem);

        helpMenu.add(aboutItem);

        setJMenuBar(menuBar);
        return menuBar;
    }

    private JPanel dataUI() {
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridBagLayout());
        dataPanel.setBorder(BorderFactory.createTitledBorder("Info: "));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dataPanel.add(new JLabel("First Name"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        dataPanel.add(new JLabel("Last Name"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        dataPanel.add(new JLabel("Phone Number"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        dataPanel.add(new JLabel("Private"), gbc);


        gbc.gridx = 1;
        gbc.gridy = 0;
        txtFirstname = new JTextField(20);
        dataPanel.add(txtFirstname, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtLastname = new JTextField(20);
        dataPanel.add(txtLastname, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        txtPhonenumber = new JTextField(20);
        dataPanel.add(txtPhonenumber, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        chPositive = new JCheckBox();
        dataPanel.add(chPositive, gbc);

        return dataPanel;
    }

    private JPanel buttonUI() {
        buttonPanel = new JPanel(new GridLayout(2, 2));
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnRemove = new JButton("Remove");
        btnClear = new JButton("Clear");
        buttonPanel.add(btnClear);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRemove);


        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String row = JOptionPane.showInputDialog(buttonPanel, "Please enter ID number to delete?", "Queries", JOptionPane.QUESTION_MESSAGE);
                int confirm = JOptionPane.showConfirmDialog(buttonPanel, "Are you sure want to delete row?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        int rowDelete = Integer.parseInt(row);
                        String query = "DELETE FROM users WHERE id = ?";

                        try {
                            PreparedStatement statement = con.prepareStatement(query);
                            statement.setInt(1, rowDelete);
                            statement.execute();
                            refreshTable();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

//                        model.removeRow(rowDelete - 1);
                    } catch (NumberFormatException exception) {
                        JOptionPane.showMessageDialog(buttonPanel, "You must enter valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ArrayIndexOutOfBoundsException exception) {
                        JOptionPane.showMessageDialog(buttonPanel, "Provided row doesn't exist. Please enter valid row number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (txtFirstname.getText().isEmpty() && txtLastname.getText().isEmpty() && txtPhonenumber.getText().isEmpty()){
                    JOptionPane.showMessageDialog(menuBar,"The input fields are empty");
                }else {
                    txtFirstname.setText("");
                    txtLastname.setText("");
                    txtPhonenumber.setText("");
                    table.clearSelection();
                    chPositive.setSelected(false);
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                String textFirstName = txtFirstname.getText().trim();
                String textLastName = txtLastname.getText().trim();
                String textPhone = txtPhonenumber.getText().trim();
                String positive = chPositive.isSelected() ? "Private" : "Public";
                if (textFirstName.isEmpty() || textLastName.isEmpty()) {
                    JOptionPane.showMessageDialog(buttonPanel, "Some of the fields are empty", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    System.out.println(id);
                    String query = "UPDATE users SET textFname = ?, textLname = ? , textPhn = ? , type = ? WHERE id = ?";
                    try {
                        PreparedStatement statement = con.prepareStatement(query);
                        statement.setString(1,textFirstName);
                        statement.setString(2, textLastName);
                        statement.setString(3, textPhone);
                        statement.setString(4, positive);
                        statement.setInt(5, id);

                        statement.executeUpdate();
                        refreshTable();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    btnClear.doClick();
                    JOptionPane.showMessageDialog(buttonPanel, "Record at row " + id + " is updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked!");
                System.out.println(txtFirstname.getText());
                System.out.println(txtLastname.getText());
                System.out.println(txtPhonenumber.getText());

                String textFirstName = txtFirstname.getText();
                String textLastName = txtLastname.getText();
                String textPhone = txtPhonenumber.getText();
                String type = chPositive.isSelected() ? "Private" : "Public";

                if (textFirstName.isEmpty() || textLastName.isEmpty() || textPhone.isEmpty()) {
                    JOptionPane.showMessageDialog(buttonPanel, "Please fill up the form.");
                } else {

                    String info = null;
                    if (chPositive.isSelected()) {
                        info = "Private";
                    } else if (!chPositive.isSelected()) {
                        info = "Public";
                    }

                String insert = "INSERT INTO users (textFname, textLname, textPhn, type) Values (?,?,?,?)";
                try{
                    PreparedStatement statement = con.prepareStatement(insert);
                    statement.setString(1,textFirstName);
                    statement.setString(2, textLastName);
                    statement.setString(3, textPhone);
                    statement.setString(4, info);

                    statement.executeUpdate();
                    refreshTable();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                    btnClear.doClick();
                    JOptionPane.showMessageDialog(buttonPanel, "The details have been added.","Success", JOptionPane.INFORMATION_MESSAGE);


                }
            }
        });
        return buttonPanel;
    }

    public JPanel fileMethod (){   //fileMethod
        JPanel filePanel = new JPanel();
        filePanel.setBorder(BorderFactory.createTitledBorder("File as: "));
        filePanel.setLayout(new GridLayout(2,0));

        JRadioButton first = new JRadioButton("Forename, Surname");
        JRadioButton last = new JRadioButton("Surname, Forename");
        ButtonGroup btngroup = new ButtonGroup();
        btngroup.add(first);
        btngroup.add(last);
        filePanel.add(first);
        filePanel.add(last);

        first.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.moveColumn(1,2);
                first.setForeground(Color.GRAY);
                last.setForeground(Color.BLACK);
            }
        });

        last.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.moveColumn(2,1);
                first.setForeground(Color.GRAY);
                last.setForeground(Color.BLACK);
            }
        });
        return filePanel;

    }



    private JPanel secondUI() {
        secondPanel = new JPanel(new BorderLayout());
        secondPanel.setBorder(BorderFactory.createTitledBorder("Name: "));

        table = new JTable();
        tableModel = new DefaultTableModel();
        table.setModel(tableModel);



        String [] columnNames = {
                "ID",
                "First Name",
                "Last Name",
                "Phone Number",
                "Privacy"
        };

        tableModel.setColumnIdentifiers(columnNames);

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                System.out.println("Cell Selected");
                int rowNo = table.getSelectedRow();
                System.out.println("Row No "+ rowNo);
                System.out.println(table.getValueAt(rowNo,0));
                System.out.println(table.getValueAt(rowNo,1));
                System.out.println(table.getValueAt(rowNo,2));
                System.out.println(table.getValueAt(rowNo,3));
                System.out.println(table.getValueAt(rowNo,4));

                String name = (String) table.getValueAt(rowNo,0);


                txtFirstname.setText(name);
                txtLastname.setText(table.getValueAt(rowNo,1).toString());
                txtPhonenumber.setText(table.getValueAt(rowNo, 3).toString());
                String info = table.getValueAt(rowNo,3).toString();

                if (info.equals("Private"))
                {
                    chPositive.setSelected(true);
                }
                if (info.equals("Public"))
                {
                    chPositive.setSelected(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }


        });

        table.setDefaultEditor(Object.class,null);

        JScrollPane scrollPane = new JScrollPane(table);
        secondPanel.add(scrollPane);

        return secondPanel;
    }
    public void connect() {

        try {
            Connection conn = DriverManager.getConnection(dbURL,username,password);//DriverManager.getConnection method lai call gareko
            if(conn!=null) {
                System.out.println("Connected to database");
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("ID"),
                        resultSet.getString("textFname"),
                        resultSet.getString("textLname"),
                        resultSet.getString("textPhn"),
                        resultSet.getString("type"),
                });
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SampleApp newobj = new SampleApp();
        newobj.connect();
    }
}
