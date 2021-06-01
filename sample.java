
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;

public class sample extends JFrame
{
    JMenuBar menuBar;
    JPanel dataPanel;
    JPanel buttonPanel;
    JPanel secondPanel;
    // Reference variable for different fields, buttons, table and table model

    JTextField txtName, txtAddress;
    JRadioButton rdMale, rdFemale;
    ButtonGroup bgGroup;
    JCheckBox chkPositive;
    JButton btnSave, btnUpdate, btnDelete, btnClear;
    JTable table;
    DefaultTableModel tableModel;


    /**
     * Constructor of the class
     */
    public sample() {
        setTitle("Sample App");
        setVisible(true);
//        setMinimumSize(new Dimension(300, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(getMenu());
        setLayout(new GridLayout(1, 2));
        JPanel subPanel = new JPanel(new GridLayout(3, 1));
        subPanel.add(dataUI());
        subPanel.add(fileMethod());
        subPanel.add(buttonUI());
        add(secondUI());
        add(subPanel);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * This method is used for creating menus in the GUI.
     * You must call this method in constructor inside setJMenuBar() to display in menu in the frame.
     *
     * @return list of menus
     */
    private JMenuBar getMenu() {
        menuBar = new JMenuBar();

        // define top level menu
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");

        // creating sub menu
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem exitItem = new JMenuItem("Exit");

        // sub menu added inside file menu
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // top level menu added inside menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        return menuBar;
    }

    /**
     * This method defines interface for data entry section
     *
     * @return panel for data entry section
     */
    private JPanel dataUI() {
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridBagLayout());
        dataPanel.setBorder(BorderFactory.createTitledBorder("Data Entry"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dataPanel.add(new JLabel("Name"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        dataPanel.add(new JLabel("Address"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        dataPanel.add(new JLabel("Gender"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        dataPanel.add(new JLabel("Positive"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtName = new JTextField(20);
        dataPanel.add(txtName, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtAddress = new JTextField(20);
        dataPanel.add(txtAddress, gbc);

        // Created new panel to add both radio button at same place
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        rdMale = new JRadioButton("Male");
        rdFemale = new JRadioButton("Female");
        // added radio button inside button group so that one radio button is selected at a time
        bgGroup = new ButtonGroup();
        bgGroup.add(rdMale);
        bgGroup.add(rdFemale);
        radioPanel.add(rdMale);
        radioPanel.add(rdFemale);
        gbc.gridx = 1;
        gbc.gridy = 2;
        dataPanel.add(radioPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        chkPositive = new JCheckBox();
        dataPanel.add(chkPositive, gbc);

        return dataPanel;
    }

    /**
     * This method defines interface for button functionality section
     *
     * @return panel for button functionality section
     */
    private JPanel buttonUI() {
        buttonPanel = new JPanel(new GridLayout(2, 2));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Buttons Functionality"));
        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int rowNo = table.getSelectedRow();

                String name = txtName.getText();


                table.setValueAt(name,rowNo,0);

                // Add yourself
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked!!");
                System.out.println(txtName.getText());
                System.out.println(txtAddress.getText());


                String textName = txtName.getText();

                if (textName.isEmpty() || txtAddress.getText().toString().isEmpty()) {
                    System.out.println("Please fill up the form");
                } else {
                    String gender = null;

                    if (rdMale.isSelected()) {
                        gender = rdMale.getText();
                    } else if (rdFemale.isSelected()) {
                        gender = rdFemale.getText();
                    }


                    String covidResult = null;
                    if (chkPositive.isSelected()) {
                        covidResult = "Positive";
                    } else if (!chkPositive.isSelected()) {
                        covidResult = "Negative";
                    }


                    //addRow
                    Object[] formData = {
                            txtName.getText(),  // " "
                            txtAddress.getText(), //
                            gender,
                            covidResult,
                    };
                    tableModel.addRow(formData);

                }
            }


        });




        return buttonPanel;
    }

    public JPanel fileMethod (){
        JPanel filePanel = new JPanel();
        filePanel.setBorder(BorderFactory.createTitledBorder("File: "));
        filePanel.setLayout(new GridLayout(2,0));

        JRadioButton first = new JRadioButton("First Name, Last Name");
        JRadioButton last = new JRadioButton("Last Name, First Name");
        ButtonGroup btngroup = new ButtonGroup();
        btngroup.add(first);
        btngroup.add(last);
        filePanel.add(first);
        filePanel.add(last);

        first.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             table.moveColumn(1,0);
             first.setForeground(Color.GRAY);
             last.setForeground(Color.BLACK);
            }
        });

        last.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.moveColumn(1,0);
                first.setForeground(Color.GRAY);
                last.setForeground(Color.BLACK);
            }
        });
        return filePanel;

    }

    private JPanel secondUI() {
        secondPanel = new JPanel(new BorderLayout());
        secondPanel.setBorder(BorderFactory.createTitledBorder("List of data"));

        table = new JTable();
        tableModel = new DefaultTableModel();
        table.setModel(tableModel);



        String [] columnNames = {
                "Name",
                "Address",
                "Gender",
                "CovidResult"
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

                String name = (String) table.getValueAt(rowNo,0);


                txtName.setText(name);
                txtAddress.setText(table.getValueAt(rowNo,1).toString());

                String gender =  table.getValueAt(rowNo,2).toString(); //Male or Female

                if ( gender.equals("Male") )
                {
                    rdMale.setSelected(true);
                }

                if ( gender.equals("Female") )
                {
                    rdFemale.setSelected(true);
                }

                String covidResult = table.getValueAt(rowNo,3).toString();

                if (covidResult.equals("Positive"))
                {
                    chkPositive.setSelected(true);
                }
                if (covidResult.equals("Negative"))
                {
                    chkPositive.setSelected(false);
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

        //DefaultEditmode disable
        table.setDefaultEditor(Object.class,null);

        JScrollPane scrollPane = new JScrollPane(table);
        secondPanel.add(scrollPane);






        return secondPanel;
    }

    /**
     * Entry point of the application
     *
     * @param args
     */
    public static void main(String[] args) {
        new sample();
    }

}