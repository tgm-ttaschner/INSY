package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import output.format.Process;
import connection.PostgresConnection;

@SuppressWarnings("serial")
public class Segelverein_GUI extends JFrame implements ActionListener, ItemListener, ListSelectionListener {

	DefaultTableModel dtm;

	private JPanel p_combo, p_tables, p_buttons, p_all;
	private JScrollPane sp_content;
	private JTable t_data;
	private JComboBox<String> cb_dropdown;
	private JButton b_addCell, b_addDB, b_update, b_delete, b_help;

	private PostgresConnection c;

	private int currentRow;

	private String[] box_values = {"boot", "sportboot", "tourenboot", "mannschaft", "wettfahrt"};

	private boolean isSelected = true;

	public Segelverein_GUI(PostgresConnection c) throws SQLException	{

		this.c = c;

		c.query("*", "boot", null, null, null);
		this.initTable(c.getColumncount());

		cb_dropdown = new JComboBox<String>();

		for (int i = 0; i < box_values.length; i++) {
			cb_dropdown.addItem(box_values[i]);
		}

		cb_dropdown.addItemListener(this);

		b_addCell = new JButton("Create new row");
		b_addDB = new JButton("Add current row to database");
		b_update = new JButton("Update cell");
		b_delete = new JButton("Delete selected cells");
		b_help = new JButton("Help!");

		b_addCell.setName("addCell");
		b_addDB.setName("addDB");
		b_update.setName("update");
		b_delete.setName("delete");
		b_help.setName("help");

		b_addCell.addActionListener(this);
		b_addDB.addActionListener(this);
		b_update.addActionListener(this);
		b_delete.addActionListener(this);
		b_help.addActionListener(this);


		p_combo = new JPanel();
		p_combo.setLayout(new BorderLayout());

		p_tables = new JPanel();
		p_tables.setLayout(new GridLayout());

		p_buttons = new JPanel();

		p_all = new JPanel();
		p_all.setLayout(new BorderLayout());
		p_all.add(p_combo, BorderLayout.NORTH);
		p_all.add(p_tables, BorderLayout.CENTER);
		p_all.add(p_buttons, BorderLayout.SOUTH);


		sp_content = new JScrollPane(p_all, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(sp_content);
		this.setVisible(true);
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	public void initTable(int columns)	{

		Object[] o = new Object[columns];

		for (int i = 0; i < columns; i++)	{
			o[i] = i + "";
		}

		dtm = new DefaultTableModel(o, columns);
		t_data = new JTable(dtm);
		t_data.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		t_data.getSelectionModel().addListSelectionListener(this); 
	}

	public void setTableColumnCount(int size)	{
		dtm.setColumnCount(size);
	}

	public void addComboBox()	{
		p_combo.add(cb_dropdown, BorderLayout.NORTH);
	}

	public void addTables()	{
		p_tables.add(t_data, BorderLayout.CENTER);
	}

	public void addButtons()	{
		p_buttons.add(b_addCell);
		p_buttons.add(b_addDB);
		p_buttons.add(b_update);
		p_buttons.add(b_delete);
		p_buttons.add(b_help);
	}

	public void addJTableRowData(Object[] data)	{
		dtm.addRow(data);
	}

	public void addEmptyJTableRow()	{
		int row_count = t_data.getRowCount();
		this.refresh();
		dtm.setRowCount(row_count+1);
	}

	public void clearJTable()	{		
		dtm.setRowCount(0);
	}

	public String getCurrentComboBoxEntry()	{
		return (String) cb_dropdown.getSelectedItem();
	}

	public void refresh()	{
		try {
			c.query("*", this.getCurrentComboBoxEntry(), null, null, null);
			Process p = new Process(c.getResultSet(), ",");

			this.clearJTable();
			this.setTableColumnCount(c.getColumncount());

			for (int i = 0; i < p.readAll().size(); i++)	{
				this.addJTableRowData(p.readAll().toArray()[i].toString().split(","));
			}

		} catch (SQLException e) {

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JButton b = (JButton) e.getSource();

		if (b.getName().equals("addCell"))	{
			this.addEmptyJTableRow();
		}

		if (b.getName().equals("addDB"))	{
			try {

				c.query("*", this.getCurrentComboBoxEntry(), null, null, null);

				c.query("BEGIN");

				if (cb_dropdown.getSelectedIndex() == 0)	{
					c.query("INSERT INTO " + this.getCurrentComboBoxEntry() + " VALUES ('" + (c.getResultSetSize(c.getResultSet())+1) + "', '" + dtm.getValueAt(currentRow, 1).toString().trim() + "', '" + dtm.getValueAt(currentRow, 2).toString().trim() + "', '" + dtm.getValueAt(currentRow, 3).toString().trim() + "');");
				} else {

					if (dtm.getColumnCount() == 2)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null)	{
							c.query("INSERT INTO " + this.getCurrentComboBoxEntry() + " VALUES ('" + dtm.getValueAt(currentRow, 0).toString().trim() + "', '" + dtm.getValueAt(currentRow, 1).toString().trim() + "');");
						} else {
							JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}

					if (dtm.getColumnCount() == 3)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null)	{
							c.query("INSERT INTO " + this.getCurrentComboBoxEntry() + " VALUES ('" + dtm.getValueAt(currentRow, 0).toString().trim() + "', '" + dtm.getValueAt(currentRow, 1).toString().trim() + "', '" + dtm.getValueAt(currentRow, 2).toString().trim() + "');");
						} else {
							JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}

					if (dtm.getColumnCount() == 4)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null && dtm.getValueAt(currentRow, 3) != null)	{
							c.query("INSERT INTO " + this.getCurrentComboBoxEntry() + " VALUES ('" + dtm.getValueAt(currentRow, 0).toString().trim() + "', '" + dtm.getValueAt(currentRow, 1).toString().trim() + "', '" + dtm.getValueAt(currentRow, 2).toString().trim() + "', '" + dtm.getValueAt(currentRow, 3).toString().trim() + "');");
						} else {
							JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}
				}

				c.query("COMMIT");
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				try {
					c.query("ROLLBACK");
				} catch (SQLException e2) {

				}
			}
		}

		if (b.getName().equals("update"))	{
			
			String val1 = null;
			
			if (!isSelected)	{
				JOptionPane.showMessageDialog(null, "Waehlen Sie bitte einen Datensatz zum Aktualisieren aus");
			}
			
			try {

				if (dtm.getColumnCount() == 2)	{
					if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null)	{
						val1 = dtm.getValueAt(currentRow, 0).toString().trim();
						c.query("UPDATE " + this.getCurrentComboBoxEntry() + " SET " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "' WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + val1 + "';");
					} else {
						JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
					}
				}

				if (dtm.getColumnCount() == 3)	{
					if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null)	{
						val1 = dtm.getValueAt(currentRow, 0).toString().trim();
						c.query("UPDATE " + this.getCurrentComboBoxEntry() + " SET " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[2] + "='" + dtm.getValueAt(currentRow, 2).toString().trim() + "' WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + val1 + "';");
					} else {
						JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
					}
				}

				if (dtm.getColumnCount() == 4)	{
					if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null && dtm.getValueAt(currentRow, 3) != null)	{
						val1 = dtm.getValueAt(currentRow, 0).toString().trim();
						c.query("UPDATE " + this.getCurrentComboBoxEntry() + " SET " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[2] + "='" + dtm.getValueAt(currentRow, 2).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[3] + "='" + dtm.getValueAt(currentRow, 3).toString().trim() + "' WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + val1 + "';");
					} else {
						JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
					}
				}

			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			
			this.refresh();
		}

		if (b.getName().equals("delete"))	{
			if (isSelected)	{

				try {

					if (dtm.getColumnCount() == 2)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null)	{
							c.query("DELETE FROM " + this.getCurrentComboBoxEntry() + " WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "';");
						} else {
							JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}

					if (dtm.getColumnCount() == 3)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null)	{
							c.query("DELETE FROM " + this.getCurrentComboBoxEntry() + " WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[2] + "='" + dtm.getValueAt(currentRow, 2).toString().trim() + "';");
						} else {
							JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}

					if (dtm.getColumnCount() == 4)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null && dtm.getValueAt(currentRow, 3) != null)	{
							c.query("DELETE FROM " + this.getCurrentComboBoxEntry() + " WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[2] + "='" + dtm.getValueAt(currentRow, 2).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[3] + "='" + dtm.getValueAt(currentRow, 3).toString().trim() + "';");
						} else {
							JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}

				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

				dtm.removeRow(currentRow);
				this.refresh();
			} else	{
				JOptionPane.showMessageDialog(null, "Waehlen Sie bitte eine Zeile aus");
			}
		}

		if (b.getName().equals("help"))	{
			JOptionPane.showMessageDialog(null, "");
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.refresh();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		isSelected = !lsm.isSelectionEmpty();

		currentRow = e.getLastIndex();

	}
}