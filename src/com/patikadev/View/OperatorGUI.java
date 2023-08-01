package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Patika;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {

    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scroll_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JTextField fld_user_password;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_search_user_name;
    private JTextField fld_search_uname;
    private JComboBox cmb_search_user_type;
    private JButton btn_user_search;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private final Operator operator;
    private JPopupMenu patikaMenu;



    public OperatorGUI(Operator operator){
        this.operator = operator;
        add(wrapper);
        setSize(700,500);
        setLocation(Helper.screenCenterPoint("x",getSize()), Helper.screenCenterPoint("y",getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.OPERATOR_TITLE);
        setVisible(true);

        lbl_welcome.setText("Welcome: "+ operator.getName());

        //Model User List
        mdl_user_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {  //It prevents the id column from being mutable.
                if (column == 0)
                    return false;

                return super.isCellEditable(row, column);
            }
        };

        Object[] col_user_list ={"ID","Ad Soyad", "Kullanıcı Adı","Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list = new Object[col_user_list.length];
        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false); //Prevents columns from being moved by the user.
        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String selected_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString();
                fld_user_id.setText(selected_user_id);
            }catch (Exception exception){

            }

        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE){
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),0).toString());
                String user_name =tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),1).toString();
                String user_uname =tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),2).toString();
                String user_password =tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),3).toString();
                String user_type =tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(),4).toString();

                if (User.update(user_id,user_name, user_uname,user_password,user_type)){
                    Helper.showMessage("done");
                    loadUserModel();
                }
                loadUserModel();
            }
        });

        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID","Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list); //adds the table to the model!
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false); // table adjustments + prevented movement of table columns
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(100);
        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });



        btn_user_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_uname)|| Helper.isFieldEmpty(fld_user_password)){
                Helper.showMessage("fill");
            }else {
                String name = fld_user_name.getText();
                String userName = fld_user_uname.getText();
                String password = fld_user_password.getText();
                String type = cmb_user_type.getSelectedItem().toString();
                if (User.add(name,userName,password,type)){
                    Helper.showMessage("done");
                    loadUserModel();
                    fld_user_name.setText(null);  //textField is cleared after the operation is done.
                    fld_user_uname.setText(null);
                    fld_user_password.setText(null);
                }
            }
        });


        btn_user_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_id)){
                Helper.showMessage("fill");
            }else {
                int user_id = Integer.parseInt(fld_user_id.getText());
                if (User.delete(user_id)){
                    Helper.showMessage("done");
                    loadUserModel();
                }else{
                    Helper.showMessage("error");
                }
            }
        });



        btn_user_search.addActionListener(e -> {
            String name = fld_search_user_name.getText();
            String uname = fld_search_uname.getText();
            String type = cmb_search_user_type.getSelectedItem().toString();
            String query = User.searchQuery(name,uname,type);
            loadUserModel(User.searchUserList(query));
        });


        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)){
                Helper.showMessage("fill");

            }else {
                if (Patika.add(fld_patika_name.getText())){
                    Helper.showMessage("done");
                    loadPatikaModel();
                    fld_patika_name.setText(null);
                }else {
                    Helper.showMessage("error");

                }

            }

        });
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i= 0;
        for (Patika obj : Patika.getList()){
            i = 0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);


        }

    }

    public void loadUserModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (User obj : User.getList()){
            i = 0;

            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getUserTypee();
            mdl_user_list.addRow(row_user_list);
        }
    }


    public void loadUserModel(ArrayList<User> list){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : list){
            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getUserTypee();
            mdl_user_list.addRow(row_user_list);
        }
    }


    public static void main(String[] args) {
        Operator op = new Operator();
        op.setId(1);
        op.setName("Beril");
        op.setUsername("beril");
        op.setPassword("1234");
        op.setUserTypee("Operator");
        OperatorGUI opGUI = new OperatorGUI(op);
    }
}
