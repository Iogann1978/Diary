package ru.home.diary;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TagsDialog extends JDialog implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	private JFrame parent;	
	private static final int hx=300, hy=300;
	private int iResult;
	private JList<String> listTags;
	private TagsListModel lm=null;
	private JButton btnDel, btnEdit;

/**
 * ==================
 * Конструктор класса
 * ==================
*/
	public TagsDialog(JFrame parent, String Title)
	{
		super(parent,Title,true);
		this.parent=null;
		this.parent=parent;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);			
		iResult=JOptionPane.CANCEL_OPTION;
	}
			
/**
 * ==========================
 * Процедура загрузки значков
 * ==========================
*/
	private ImageIcon createIcon(String path)
	{
		ImageIcon icon=null;
		URL imgURL=TagsDialog.class.getResource(path);
		if(imgURL!=null)
			return icon=new ImageIcon(imgURL);
		else
			JOptionPane.showMessageDialog(parent, "Не найдена иконка","Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		return icon;
	}
	
/**
 * ============================
 * Процедура построения диалога
 * ============================
 */
	public int init()
	{
		Container c=getContentPane();
		c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));				
		
		JLabel lblList=null;
		lblList=new JLabel("Список тегов");
		lblList.setAlignmentX(CENTER_ALIGNMENT);
		c.add(lblList);
		
		lm=null;
		lm=new TagsListModel();
		listTags=null;
		listTags=new JList<String>(lm);
		listTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTags.getSelectionModel().addListSelectionListener(this);
		c.add(new JScrollPane(listTags));
		
		// Панель кнопок
		JPanel pnlButton=null;
		pnlButton=new JPanel();
		pnlButton.setLayout(new BoxLayout(pnlButton,BoxLayout.X_AXIS));
		pnlButton.add(Box.createHorizontalGlue());
		ImageIcon iconAdd24=createIcon("Images/add24.png");
		JButton btnAdd=null;
		btnAdd=new JButton();
		btnAdd.setIcon(iconAdd24);
		btnAdd.setActionCommand("Добавить");
		btnAdd.addActionListener(this);
		pnlButton.add(btnAdd);		
		ImageIcon iconEdit24=createIcon("Images/edit24.png");
		btnEdit=null;
		btnEdit=new JButton();
		btnEdit.setIcon(iconEdit24);
		btnEdit.setActionCommand("Удалить");
		btnEdit.addActionListener(this);
		btnEdit.setEnabled(false);
		pnlButton.add(btnEdit);
		ImageIcon iconDel24=createIcon("Images/delete24.png");
		btnDel=null;
		btnDel=new JButton();
		btnDel.setIcon(iconDel24);
		btnDel.setActionCommand("Удалить");
		btnDel.addActionListener(this);
		btnDel.setEnabled(false);
		pnlButton.add(btnDel);
		ImageIcon iconCancel24=createIcon("Images/cancel24.png");
		JButton btnCancel=null;
		btnCancel=new JButton();
		btnCancel.setIcon(iconCancel24);
		btnCancel.setActionCommand("Отмена");
		btnCancel.addActionListener(this);
		pnlButton.add(btnCancel);
		pnlButton.add(Box.createHorizontalGlue());
		c.add(pnlButton);
		
		setSize(hx,hy);
		setLocationRelativeTo(parent);
		setVisible(true);
		return iResult;
	}

/**
 * ==================================
 * Процедура обработки нажатия кнопок
 * ==================================	
 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String word;
		int SelRow;

		switch(e.getActionCommand())
		{
		case "Отмена":
			dispose();
			break;
		case "Добавить":
			word=JOptionPane.showInputDialog(parent, "Новый тег", 
				"Добавить",JOptionPane.OK_CANCEL_OPTION);
			if(word!=null && !word.equals(""))
			{
				lm.insert(word);
				int id=lm.refresh();
				listTags.setSelectedIndex(lm.getIndex(id));
			}			
			break;
		case "Изменить":
			SelRow=listTags.getSelectedIndex();
			word=(String)JOptionPane.showInputDialog(parent, "Изменить тег",
				"Изменить",JOptionPane.OK_CANCEL_OPTION,null,null,
				lm.getElementAt(SelRow));
			if(word!=null && !word.equals(""))
			{
				int id=lm.getID(SelRow);
				lm.update(lm.getID(SelRow),word);
				lm.refresh();
				listTags.setSelectedIndex(lm.getIndex(id));
			}
			break;
		case "Удалить":
			SelRow=listTags.getSelectedIndex();
			int result=JOptionPane.showConfirmDialog(parent,  
				"Удалить тег "+lm.getElementAt(SelRow)+"?",
				"Удаление",	JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION)
			{
				lm.delete(lm.getID(SelRow));
				lm.refresh();
			}
			break;
		default:
			break;
		}
	}

/**
 * ==================================================
 * Процедура изменения состояния кнопок при выделении
 * ==================================================	
 */	
	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		ListSelectionModel lsm=(ListSelectionModel)e.getSource();
		if(!lsm.isSelectionEmpty())
		{
			btnDel.setEnabled(true);
			btnEdit.setEnabled(true);
		}
		else
		{
			btnDel.setEnabled(false);
			btnEdit.setEnabled(false);
		}
	}

}
