/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIG.controller;

import SIG.model.InvoiceHeader;
import SIG.model.InvoicesTable;
import SIG.model.InvoiceLine;
import SIG.model.LinesTable;
import SIG.sales.view.InvoiceDialog;
import SIG.sales.view.InvoiceFrame;
import SIG.sales.view.LineDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author PClaptop
 */
public class Controller implements ActionListener, ListSelectionListener {

    private InvoiceFrame frame;
     private InvoiceDialog invoiceDialog;
    private LineDialog lineDialog;
    
    public Controller(InvoiceFrame frame) {
        this.frame = frame;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
      //  System.out.println("Action is: " + actionCommand);
        switch (actionCommand) {
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create New Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
             case "ActionInvoiceOK":
                CreateInvoiceOK();
                break;
            case "ActionLineOK":
                CreateLineOK();
                break;
            case "ActionLineCancel":
                CreateLineCancel();
                break;
            case "ActionInvoiceCancel":
                CreateInvoiceCancel();
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if(selectedIndex!=-1){
        
        InvoiceHeader currentInvoice = frame.getInvoices().get(selectedIndex);
        frame.getInvoiceNumLabel().setText(""+currentInvoice.getNum());
        frame.getInvoiceDateLabel().setText(currentInvoice.getDate());
        frame.getCustomerNameLabel().setText(currentInvoice.getCustomer());
        frame.getInvoiceTotalLabel().setText(""+currentInvoice.getInvoiceTotal());
        LinesTable linesTableModel = new LinesTable(currentInvoice.getLines());
        frame.getLineTable().setModel(linesTableModel);
        linesTableModel.fireTableDataChanged();
        }
    }
    
    private void loadFile() {
        JFileChooser fch = new JFileChooser();
        try {
            int x= fch.showOpenDialog(frame);
            if (x == JFileChooser.APPROVE_OPTION) {
                File headerFile = fch.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                System.out.println("Invoices have been read");
                // 1,22-11-2020,Ali
                
                ArrayList<InvoiceHeader> invoicesArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    try{
                    String[] headerParts = headerLine.split(",");
                    int invoiceNum = Integer.parseInt(headerParts[0]);
                    String invoiceDate = headerParts[1];
                    String customerName = headerParts[2];
                    
                    InvoiceHeader invoice = new InvoiceHeader(invoiceNum, invoiceDate, customerName);
                    invoicesArray.add(invoice);
                
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error cant read line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                x = fch.showOpenDialog(frame);
                if(x == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fch.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    System.out.println("Lines have been read");
                    
                    for (String lineLine : lineLines) {
                        try{
                        String lineParts[] = lineLine.split(",");
                        int invoiceNum = Integer.parseInt(lineParts[0]);
                        String itemName = lineParts[1];
                        double itemPrice = Double.parseDouble(lineParts[2]);
                        int count = Integer.parseInt(lineParts[3]);
                        InvoiceHeader inv = null;
                        for (InvoiceHeader invoice : invoicesArray) {
                            if (invoice.getNum() == invoiceNum) {
                                inv = invoice;
                                break;
                            }
                        }
                        
                        InvoiceLine line = new InvoiceLine(itemName, itemPrice, count, inv);
                        inv.getLines().add(line);
                    
                        } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }     
                 
               
                frame.setInvoices(invoicesArray);
                InvoicesTable invoicesTable = new InvoicesTable(invoicesArray);
                frame.setInvoicesTableModel(invoicesTable);
                frame.getInvoiceTable().setModel(invoicesTable);
                frame.getInvoicesTableModel().fireTableDataChanged();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Cannot read  this file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFile() {
        ArrayList<InvoiceHeader> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceHeader invoice : invoices) {
            String invCSV = invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";

            for (InvoiceLine line : invoice.getLines()) {
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        
        try {
            JFileChooser fch = new JFileChooser();
            int x = fch.showSaveDialog(frame);
            if (x == JFileChooser.APPROVE_OPTION) {
                File headerFile = fch.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                hfw.write(headers);
                hfw.flush();
                hfw.close();
                x = fch.showSaveDialog(frame);
                if (x == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fch.getSelectedFile();
                    FileWriter lfw = new FileWriter(lineFile);
                    lfw.write(lines);
                    lfw.flush();
                    lfw.close();
                }
            }
        } catch (Exception ex) {

        }
    }

    private void createNewInvoice() {
        invoiceDialog = new InvoiceDialog(frame);
        invoiceDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int Row = frame.getInvoiceTable().getSelectedRow();
        if (Row != -1) {
            frame.getInvoices().remove(Row);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createNewItem() {
        lineDialog = new LineDialog(frame);
        lineDialog.setVisible(true);
    }

    private void deleteItem() {
       int Row = frame.getLineTable().getSelectedRow();

        if (Row != -1) {
            LinesTable linesTableModel = (LinesTable) frame.getLineTable().getModel();
            linesTableModel.getLines().remove(Row);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }
        
    
// to create new invoice
    private void CreateInvoiceOK() {
         //To change body of generated methods, choose Tools | Templates.
         String date = invoiceDialog.getInvDateField().getText();
        String customer = invoiceDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
        try {
            String[] dateParts = date.split("-");  // "25-05-2013" 
            if (dateParts.length < 3) {
                JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if (day > 31 || month > 12) {
                    JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
        
        InvoiceHeader invoice = new InvoiceHeader(num, date, customer);
        frame.getInvoices().add(invoice);
        frame.getInvoicesTableModel().fireTableDataChanged();
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null; }
    }
            } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // to create new line 
    private void CreateLineOK() {
         //To change body of generated methods, choose Tools | Templates.
         String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            InvoiceHeader invoice = frame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item, price, count, invoice);
            invoice.getLines().add(line);
            LinesTable linesTableModel = (LinesTable) frame.getLineTable().getModel();
            //linesTableModel.getLines().add(line);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void CreateLineCancel() {
        //To change body of generated methods, choose Tools | Templates.
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void CreateInvoiceCancel() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

}
