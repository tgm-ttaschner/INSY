package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import output.format.Process;
import connection.PostgresConnection;

/**
 * @author Thomas Taschner
 * 
 * Realisiert eine einfache GUI zum Erstellen, Veraendern und Loeschen von Datenbankeintraegen von gewaehlen Tabellen.
 *
 */
@SuppressWarnings("serial")
public class Segelverein_GUI extends JFrame implements ActionListener, ItemListener, ListSelectionListener, WindowListener {

	private DefaultTableModel dtm;

	private JPanel p_combo, p_tables, p_buttons, p_all;
	private JScrollPane sp_content;
	private JTable t_data;
	private JComboBox<String> cb_dropdown;
	private JButton b_addCell, b_addDB, b_update, b_delete, b_help;

	private PostgresConnection c;



	private int currentRow;

	private String[] box_values = {"boot", "sportboot", "tourenboot", "mannschaft", "wettfahrt"};
	//private Object[] box_values;

	private boolean isSelected = true;

	/**
	 * @param c die Datenbankverbindung
	 * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank hergestellt werden kann
	 */
	public Segelverein_GUI(PostgresConnection c) throws SQLException	{

		this.c = c;

		//box_values = c.getTableNames().toArray();

		// Durchfuehren einer SELECT * Query zum Fuellen des Tables
		c.selectQuery("*", "boot", null, null, null);
		this.initTable(c.getColumncount());

		cb_dropdown = new JComboBox<String>();

		// Befuellen der Combobox mit den Tablenamen
		for (int i = 0; i < box_values.length; i++) {
			cb_dropdown.addItem((String) box_values[i]);
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

		// Scrollbars nur dann sichtbar, wenn benoetigt
		sp_content = new JScrollPane(p_all, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(sp_content);
		this.setVisible(true);
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
	}

	/**
	 * @param columns die Anzahl der Spalten der Tabelle
	 * 
	 * Erstellt einen neuen JTable mit einer gewissen Spaltenanzahl
	 */
	public void initTable(int columns)	{

		Object[] o = new Object[columns];

		for (int i = 0; i < columns; i++)	{
			o[i] = i + "";
		}

		// Setzen eines Tablemodels und, dass nur eine Einfachauswahl erfolgen kann
		dtm = new DefaultTableModel(o, columns);
		t_data = new JTable(dtm);
		t_data.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		t_data.getSelectionModel().addListSelectionListener(this); 
	}

	/**
	 * @param size die Anzahl der Spalten der Tabelle
	 */
	public void setTableColumnCount(int size)	{
		dtm.setColumnCount(size);
	}

	/**
	 * Fuegt die Combobox dem Panel hinzu
	 */
	public void addComboBox()	{
		p_combo.add(cb_dropdown, BorderLayout.NORTH);
	}

	/**
	 * Fuegt das JTable Objekt dem Panel hinzu
	 */
	public void addTables()	{
		p_tables.add(t_data, BorderLayout.CENTER);
	}

	/**
	 * Fuegt die Buttons dem Panel hinzu
	 */
	public void addButtons()	{
		p_buttons.add(b_addCell);
		p_buttons.add(b_addDB);
		p_buttons.add(b_update);
		p_buttons.add(b_delete);
		p_buttons.add(b_help);
	}

	/**
	 * @param data Daten, mit denen die neue Zeile befuellt werden soll
	 * 
	 * Erstellt eine neue Zeile und befuellt diese mit Daten.
	 */
	public void addJTableRowData(Object[] data)	{
		dtm.addRow(data);
	}

	/**
	 * Erstellt eine neue leere Zeile
	 */
	public void addEmptyJTableRow()	{
		int row_count = t_data.getRowCount();
		this.refresh();
		dtm.setRowCount(row_count+1);
	}

	/**
	 * Löscht alle Inhalte des Tables
	 */
	public void clearJTable()	{		
		dtm.setRowCount(0);
	}

	/**
	 * @return den aktuellen ausgewaehlten Wert der Combobox
	 */
	public String getCurrentComboBoxEntry()	{
		return (String) cb_dropdown.getSelectedItem();
	}

	/**
	 * Fuehrt ein SELECT * aus, verarbeitet die Daten und fuellt den JTable mit diesen
	 */
	public void refresh()	{
		try {
			c.selectQuery("*", this.getCurrentComboBoxEntry(), null, null, null);
			Process p = new Process(c.getResultSet(), ",");

			this.clearJTable();
			this.setTableColumnCount(c.getColumncount());

			for (int i = 0; i < p.readAll().size(); i++)	{
				this.addJTableRowData(p.readAll().toArray()[i].toString().split(","));
			}

		} catch (SQLException e) {
			System.out.println("Es ist ein Fehler beim Ausfuehren der Query aufgetreten.");
		}
	}

	public void addDataBaseEntry()	{
		try {
			c.selectQuery("*", this.getCurrentComboBoxEntry(), null, null, null);
			c.query("BEGIN");
			if (cb_dropdown.getSelectedItem().equals("boot"))	{
				try {
				c.query("INSERT INTO " + this.getCurrentComboBoxEntry() + " VALUES ('" + (c.getResultSetSize(c.getResultSet())+1) + "', '" + dtm.getValueAt(currentRow, 1).toString().trim() + "', '" + dtm.getValueAt(currentRow, 2).toString().trim() + "', '" + dtm.getValueAt(currentRow, 3).toString().trim() + "');");
				} catch (NullPointerException e)	{
					System.err.println("Eines der Felder scheint leer zu sein");
				}
			} else {

				if (dtm.getRowCount() == 0)	{
					//JOptionPane.showMessageDialog(null, "Sie muessen zuerst eine neue Zeile anlegen");
					System.err.println("Sie muessen zuerst eine neue Zeile anlegen");
				} else	{

					if (dtm.getColumnCount() == 2)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null)	{
							c.query("INSERT INTO " + this.getCurrentComboBoxEntry() + " VALUES ('" + dtm.getValueAt(currentRow, 0).toString().trim() + "', '" + dtm.getValueAt(currentRow, 1).toString().trim() + "');");
						} else {
							//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}

					if (dtm.getColumnCount() == 3)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null)	{
							c.query("INSERT INTO " + this.getCurrentComboBoxEntry() + " VALUES ('" + dtm.getValueAt(currentRow, 0).toString().trim() + "', '" + dtm.getValueAt(currentRow, 1).toString().trim() + "', '" + dtm.getValueAt(currentRow, 2).toString().trim() + "');");
						} else {
							//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}

					if (dtm.getColumnCount() == 4)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null && dtm.getValueAt(currentRow, 3) != null)	{
							c.query("INSERT INTO " + this.getCurrentComboBoxEntry() + " VALUES ('" + dtm.getValueAt(currentRow, 0).toString().trim() + "', '" + dtm.getValueAt(currentRow, 1).toString().trim() + "', '" + dtm.getValueAt(currentRow, 2).toString().trim() + "', '" + dtm.getValueAt(currentRow, 3).toString().trim() + "');");
						} else {
							//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						}
					}
				}
			}

			c.query("COMMIT");
		} catch (SQLException e1) {
			//JOptionPane.showMessageDialog(null, e1.getMessage());
			System.err.println(e1.getMessage());
			try {
				c.query("ROLLBACK");
			} catch (Exception e2) {
				System.out.println("Es ist ein Fehler beim Ausfuehren der Query aufgetreten. Versuchen Sie es erneut.");
			}
		}
	}

	public void updateDataBaseEntry()	{
		String val1 = null;

		if (!isSelected)	{
			//JOptionPane.showMessageDialog(null, "Waehlen Sie bitte einen Datensatz zum Aktualisieren aus");
			System.err.println("Waehlen Sie bitte einen Datensatz zum Aktualisieren aus");
		}

		try {
			c.query("BEGIN");

			c.query("SELECT * FROM " + this.getCurrentComboBoxEntry());

			if (dtm.getRowCount() == 0)	{
				//JOptionPane.showMessageDialog(null, "Sie muessen zuerst eine neue Zeile anlegen");
				System.err.println("Sie muessen zuerst eine neue Zeile anlegen");
			} else	{

				if (dtm.getColumnCount() == 2)	{
					if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null)	{
						val1 = dtm.getValueAt(currentRow, 0).toString().trim();
						c.query("UPDATE " + this.getCurrentComboBoxEntry() + " SET " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "' WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + val1 + "';");
						c.query("COMMIT");
					} else {
						//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						c.query("ROLLBACK");
					}
				}

				if (dtm.getColumnCount() == 3)	{
					if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null)	{
						val1 = dtm.getValueAt(currentRow, 0).toString().trim();
						c.query("UPDATE " + this.getCurrentComboBoxEntry() + " SET " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[2] + "='" + dtm.getValueAt(currentRow, 2).toString().trim() + "' WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + val1 + "';");
						c.query("COMMIT");
					} else {
						//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						c.query("ROLLBACK");
					}
				}

				if (dtm.getColumnCount() == 4)	{
					if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null && dtm.getValueAt(currentRow, 3) != null)	{
						val1 = dtm.getValueAt(currentRow, 0).toString().trim();
						c.query("UPDATE " + this.getCurrentComboBoxEntry() + " SET " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[2] + "='" + dtm.getValueAt(currentRow, 2).toString().trim() + "', " + c.getColumnNames(this.getCurrentComboBoxEntry())[3] + "='" + dtm.getValueAt(currentRow, 3).toString().trim() + "' WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + val1 + "';");
						c.query("COMMIT");
					} else {
						//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
						c.query("ROLLBACK");
					}
				}
			}

		} catch (SQLException e1) {
			//JOptionPane.showMessageDialog(null, e1.getMessage());
			System.err.println(e1.getMessage());
			try {
				c.disconnect();
				c.connect();
			} catch (Exception e2) {
				System.out.println("Es ist ein Fehler beim Ausfuehren der Query aufgetreten. Versuchen Sie es erneut.");
			}
		}

		this.refresh();
	}

	public void deleteDataBaseEntry()	{
		if (isSelected)	{

			try {
				c.query("BEGIN");

				if (dtm.getRowCount() == 0)	{
					JOptionPane.showMessageDialog(null, "Sie muessen zuerst eine neue Zeile anlegen");
					System.err.println("Sie muessen zuerst eine neue Zeile anlegen");
				} else	{

					if (dtm.getColumnCount() == 2)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null)	{
							c.query("DELETE FROM " + this.getCurrentComboBoxEntry() + " WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "';");
							c.query("COMMIT");
						} else {
							//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							c.query("ROLLBACK");
						}
					}

					if (dtm.getColumnCount() == 3)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null)	{
							c.query("DELETE FROM " + this.getCurrentComboBoxEntry() + " WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[2] + "='" + dtm.getValueAt(currentRow, 2).toString().trim() + "';");
							c.query("COMMIT");
						} else {
							//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							c.query("ROLLBACK");
						}
					}

					if (dtm.getColumnCount() == 4)	{
						if (dtm.getValueAt(currentRow, 0) != null && dtm.getValueAt(currentRow, 1) != null && dtm.getValueAt(currentRow, 2) != null && dtm.getValueAt(currentRow, 3) != null)	{
							c.query("DELETE FROM " + this.getCurrentComboBoxEntry() + " WHERE " + c.getColumnNames(this.getCurrentComboBoxEntry())[0] + "='" + dtm.getValueAt(currentRow, 0).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[1] + "='" + dtm.getValueAt(currentRow, 1).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[2] + "='" + dtm.getValueAt(currentRow, 2).toString().trim() + "' AND " + c.getColumnNames(this.getCurrentComboBoxEntry())[3] + "='" + dtm.getValueAt(currentRow, 3).toString().trim() + "';");
							c.query("COMMIT");
						} else {
							//JOptionPane.showMessageDialog(null, "Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							System.err.println("Eines der Textfelder scheint leer zu sein, bitte vergewissern Sie sich, dass jedes Textfeld befuellt wurde");
							c.query("ROLLBACK");
						}
					}
					dtm.removeRow(currentRow);
				}

			} catch (SQLException e1) {
				//JOptionPane.showMessageDialog(null, e1.getMessage());
				System.err.println(e1.getMessage());
				try {
					c.disconnect();
					c.connect();
				} catch (Exception e2) {
					System.out.println("Es ist ein Fehler beim Ausfuehren der Query aufgetreten. Versuchen Sie es erneut.");
				}
			}

			this.refresh();
		} else	{
			//JOptionPane.showMessageDialog(null, "Waehlen Sie bitte eine Zeile aus");
			System.err.println("Waehlen Sie bitte eine Zeile aus");
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		JButton b = (JButton) e.getSource();

		// Fuege neue leere Zeile hinzu
		if (b.getName().equals("addCell"))	{
			this.addEmptyJTableRow();
		}

		// Hole Werte der aktuellen Zeile, fuehre Checks durch und schicke diese an die Datenbank
		if (b.getName().equals("addDB"))	{
			this.addDataBaseEntry();
		}

		// Hole die Werte der aktuellen Zeile, fuehre Checks durch und schicke an die Datenbank
		if (b.getName().equals("update"))	{
			this.updateDataBaseEntry();
		}

		// Hole Wert der aktuellen Zeile, loesche, wenn moeglich und schicke an die Datenbank
		if (b.getName().equals("delete"))	{
			this.deleteDataBaseEntry();
		}

		// Gibt nicht vorhandene Hilfe aus
		if (b.getName().equals("help"))	{
			JOptionPane.showMessageDialog(null, "");
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 * 
	 * Zeichne den JTable neu, wenn sich der aktuelle Eintrag der Combobox aendert
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		this.refresh();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 * 
	 * Checke, ob eine Zeile ausgewaehlt wurde und setze den Werte des zuletzt ausgewaehlten Indexes
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {

		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		isSelected = !lsm.isSelectionEmpty();

		currentRow = e.getLastIndex();

	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			c.disconnect();
		} catch (SQLException e1) {
			System.out.println("Beim Versuch die Verbindung zur Datenbank zu schliessen, ist ein Fehler aufgetreten.");
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
}