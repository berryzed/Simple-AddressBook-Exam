package kr.berryz.addressbook;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * @author Berryzed
 * 
 */
public class MainFrame extends JFrame
{
	public static MainFrame instance = new MainFrame();

	private JPanel mainPanel = null;
	private JPanel southPanel = new JPanel();
	private JTable table = new JTable();
	private JButton addBtn = new JButton("Add");
	private JButton removeBtn = new JButton("Remove");
	private JButton detailBtn = new JButton("Detail");
	private JButton picSelBtn = new JButton("Select Image");

	private JTextField nameTxtFld = new JTextField(6);
	private JTextField ageTxtFld = new JTextField(3);
	private JComboBox<String> sexComBox = new JComboBox<String>();
	private JTextField phoneTxtFld = new JTextField(9);
	private JTextField emailTxtFld = new JTextField(14);

	private String[] colNames = null;
	private DefaultTableModel model = null;

	private String currentDir = System.getProperty("user.dir");

	private JFileChooser fileChooser = new JFileChooser(currentDir);
	private JLabel picLbl = new JLabel("None");
	private File picFile = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{

	}

	public MainFrame()
	{
		init();
		setUI();
	}

	public static MainFrame getInstance()
	{
		if (instance == null)
			instance = new MainFrame();
		return instance;
	}

	private void init()
	{
		colNames = new String[] { "Name", "Age", "Sex", "Phone", "E-mail" };
		model = new DefaultTableModel(colNames, 0);

		sexComBox.addItem("Male");
		sexComBox.addItem("Female");

		picSelBtn.addActionListener(new PicSelActionEvent());
		addBtn.addActionListener(new AddActionEvent());
		removeBtn.addActionListener(new RemoveActionEvent());
		detailBtn.addActionListener(new DetailActionEvent());

		ageTxtFld.addKeyListener(new NumberKeyEvent());
		phoneTxtFld.addKeyListener(new NumberKeyEvent());
	}

	private void setUI()
	{
		setTitle("Address Book 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 400);
		mainPanel = (JPanel) this.getContentPane();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout(0, 0));

		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(1);
		table.getColumnModel().getColumn(1).setPreferredWidth(1);
		table.getColumnModel().getColumn(2).setPreferredWidth(1);
		mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel addPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) addPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		addPanel.add(new JLabel("Name"));
		addPanel.add(nameTxtFld);
		addPanel.add(new JLabel("Age"));
		addPanel.add(ageTxtFld);
		addPanel.add(new JLabel("Sex"));
		addPanel.add(sexComBox);
		addPanel.add(new JLabel("Phone"));
		addPanel.add(phoneTxtFld);
		addPanel.add(new JLabel("E-mail"));
		addPanel.add(emailTxtFld);
		addPanel.add(picSelBtn);
		addPanel.add(picLbl);

		JPanel commandPanel = new JPanel();

		commandPanel.add(addBtn);
		commandPanel.add(removeBtn);
		commandPanel.add(detailBtn);

		southPanel.setLayout(new BorderLayout(0, 0));

		southPanel.add(addPanel, BorderLayout.CENTER);
		southPanel.add(commandPanel, BorderLayout.SOUTH);

		mainPanel.add(southPanel, BorderLayout.SOUTH);

		setVisible(true);
		requestFocus();
	}

	public String[] getRowData(int row)
	{
		int colCnt = table.getColumnCount();
		String[] arr = new String[colCnt];

		for (int i = 0; i < colCnt; i++)
		{
			arr[i] = (String) model.getValueAt(row, i);
		}

		return arr;
	}

	public String getCurrentDir()
	{
		return currentDir;
	}

	private void moveAndRenameFile(String fileName)
	{
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		BufferedInputStream bin = null;
		BufferedOutputStream bout = null;

		try
		{
			inputStream = new FileInputStream(picFile);
			outputStream = new FileOutputStream(new File(currentDir, fileName));

			bin = new BufferedInputStream(inputStream);
			bout = new BufferedOutputStream(outputStream);

			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = bin.read(buffer, 0, 1024)) != -1)
			{
				bout.write(buffer, 0, bytesRead);
			}
			bout.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				outputStream.close();
				inputStream.close();
				bin.close();
				bout.close();
			}
			catch (IOException ioe)
			{
			}
		}
	}

	private class PicSelActionEvent implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			int returnVal = fileChooser.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				picFile = fileChooser.getSelectedFile();
				picLbl.setText("OK");
			}
			else
			{
				picFile = null;
				picLbl.setText("None");
			}
		}

	}

	private class AddActionEvent implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			String[] addr = new String[5];

			addr[0] = nameTxtFld.getText();
			if ((addr[0].length() < 2))
			{
				JOptionPane.showMessageDialog(null, "Name is empty or too short");
				nameTxtFld.requestFocus();
				return;
			}

			addr[1] = ageTxtFld.getText();
			if ((addr[1].equals("")))
			{
				JOptionPane.showMessageDialog(null, "Age is empty");
				ageTxtFld.requestFocus();
				return;
			}

			addr[2] = sexComBox.getSelectedItem().toString();

			addr[3] = phoneTxtFld.getText();
			if ((addr[3].length() < 2))
			{
				JOptionPane.showMessageDialog(null, "Phone is empty or too short");
				phoneTxtFld.requestFocus();
				return;
			}

			addr[4] = emailTxtFld.getText();
			if ((addr[4].length() < 2))
			{
				JOptionPane.showMessageDialog(null, "E-mail is empty or too short");
				emailTxtFld.requestFocus();
				return;
			}

			if (picFile != null)
			{
				moveAndRenameFile(addr[0] + "_" + addr[3]);
				picFile = null;
				picLbl.setText("None");
			}
			model.addRow(addr);

			nameTxtFld.setText("");
			ageTxtFld.setText("");
			sexComBox.setSelectedIndex(0);
			phoneTxtFld.setText("");
			emailTxtFld.setText("");
		}

	}

	private class RemoveActionEvent implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			int row = table.getSelectedRow();
			int dialogResult;

			if (row == -1)
			{
				JOptionPane.showMessageDialog(null, "Please Select Data");
				return;
			}

			dialogResult = JOptionPane.showConfirmDialog(null, "Remove OK?", "Message", JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION)
			{
				String name = (String) model.getValueAt(row, 0);
				String phone = (String) model.getValueAt(row, 3);

				File file = new File(currentDir, name + "_" + phone);
				if (file.exists())
				{
					file.delete();
				}

				model.removeRow(row);
			}

			requestFocus();
		}
	}

	private class DetailActionEvent implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			int row = table.getSelectedRow();
			if (row != -1)
				new DetailDialog(row);
			else
				JOptionPane.showMessageDialog(null, "Please Select Data");
		}

	}

	private class NumberKeyEvent implements KeyListener
	{

		@Override
		public void keyTyped(KeyEvent e)
		{
			// TODO Auto-generated method stub
			char c = e.getKeyChar();

			if (!Character.isDigit(c))
			{
				e.consume();
				return;
			}
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			// TODO Auto-generated method stub

		}

	}

}
