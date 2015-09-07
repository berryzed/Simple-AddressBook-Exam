package kr.berryz.addressbook;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author Berryzed
 * 
 */
public class DetailDialog extends JDialog implements ActionListener
{

	private JPanel mainPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel southPanel = new JPanel();

	private JButton okBtn = new JButton("OK");

	private JLabel imgLbl = null;
	private JLabel[] dataLbl = new JLabel[5];

	private int row = -1;
	private String[] data = null;

	private String[] dataText = new String[] { "Name", "Age", "Sex", "Phone", "E-mail" };

	private ImageIcon img = null;

	/**
	 * Create the dialog.
	 */
	public DetailDialog(int row)
	{
		this.row = row;
		init();
		setUI();
	}

	void init()
	{
		data = MainFrame.getInstance().getRowData(row);
		for (int i = 0; i < 5; i++)
		{
			dataLbl[i] = new JLabel();
			dataLbl[i].setText(" : " + data[i]);
		}

		String currentDir = MainFrame.getInstance().getCurrentDir();
		String fileName = data[0] + "_" + data[3];
		File file = new File(currentDir, fileName);

		if (file.exists())
		{
			img = new ImageIcon(currentDir + "\\" + fileName);
		}
		else
		{
			img = new ImageIcon(currentDir + "/default.jpg");
		}
		Image oldImage = img.getImage();
		Image newImage = oldImage.getScaledInstance(100, 150, Image.SCALE_SMOOTH);

		imgLbl = new JLabel(new ImageIcon(newImage));

		okBtn.addActionListener(this);
	}

	void setUI()
	{
		setTitle("Detail Data");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		setModal(true);

		mainPanel = (JPanel) this.getContentPane();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(new BorderLayout());

		JPanel leftPanel = new JPanel(new GridLayout(5, 1));
		JPanel rightPanel = new JPanel(new GridLayout(5, 1));

		for (int i = 0; i < 5; i++)
		{
			leftPanel.add(new JLabel(dataText[i]));
			rightPanel.add(dataLbl[i]);
		}

		centerPanel.add(leftPanel, BorderLayout.WEST);
		centerPanel.add(rightPanel, BorderLayout.CENTER);
		centerPanel.add(imgLbl, BorderLayout.EAST);

		mainPanel.add(centerPanel, BorderLayout.CENTER);

		southPanel.add(okBtn);
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		if (e.getSource() == okBtn)
		{
			dispose();
		}
	}

}
