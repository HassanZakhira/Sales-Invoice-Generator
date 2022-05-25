/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIG.sales.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author PClaptop
 */

   
public class LineDialog extends JDialog{
    private JTextField itemNameField;
    private JTextField itemCountField;
    private JTextField itemPriceField;
    private JLabel itemNameLbl;
    private JLabel itemCountLbl;
    private JLabel itemPriceLbl;
    private JButton okBtn;
    private JButton cancelBtn;
    
    public LineDialog(InvoiceFrame frame) {
        itemNameField = new JTextField(20);
        itemNameLbl = new JLabel("New Item Name");
        
        itemCountField = new JTextField(20);
        itemCountLbl = new JLabel("New Item Count");
        
        itemPriceField = new JTextField(20);
        itemPriceLbl = new JLabel("New Item Price");
        
        okBtn = new JButton("Add Item ");
        cancelBtn = new JButton("Cancel");
        
        okBtn.setActionCommand("ActionLineOK");
        cancelBtn.setActionCommand("ActionLineCancel");
        
        okBtn.addActionListener(frame.getController());
        cancelBtn.addActionListener(frame.getController());
        setLayout(new GridLayout(4, 2));
        
        add(itemNameLbl);
        add(itemNameField);
        add(itemCountLbl);
        add(itemCountField);
        add(itemPriceLbl);
        add(itemPriceField);
        add(okBtn);
        add(cancelBtn);
        
        pack();
    }

    public JTextField getItemNameField() {
        return itemNameField;
    }

    public JTextField getItemCountField() {
        return itemCountField;
    }

    public JTextField getItemPriceField() {
        return itemPriceField;
    }
}