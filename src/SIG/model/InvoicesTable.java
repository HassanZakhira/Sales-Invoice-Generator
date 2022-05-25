/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIG.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author PClaptop
 */
public class InvoicesTable extends AbstractTableModel {
    private ArrayList<InvoiceHeader> invoices;
    private String[] columns = {"No.", "Date", "Customer", "Total"};
    
    public InvoicesTable(ArrayList<InvoiceHeader> invoices) {
        this.invoices = invoices;
    }
    
    @Override
    public int getRowCount() {
        return invoices.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader invoice = invoices.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return invoice.getNum();
            case 1: return invoice.getDate();
            case 2: return invoice.getCustomer();
            case 3: return invoice.getInvoiceTotal();
            default : return "";
        }
    }
}
