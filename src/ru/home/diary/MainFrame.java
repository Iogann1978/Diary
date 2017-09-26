package ru.home.diary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainFrame extends JFrame implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	private JTextPane txtEdit, txtView;
	private DatesListModel lmd;
	private TagsListModel cmt;
	private LinkListModel lml;
	private JLabel lblDTitle;
	private JLabel lblDTags;
	private JList<String> listDates, listLinkTags;
	private JComboBox<String> cmbTags;
	private int hashCodeD, hashCodeL;
	private JButton btnEdit1, btnDel1;
	private JButton btnDel2;

/**
* ================		
* Загрузка значков 
* ================
*/
	private ImageIcon createIcon(String path)
	{
		ImageIcon icon=null;
		URL imgURL=MainFrame.class.getResource(path);
		if(imgURL!=null)
			return icon=new ImageIcon(imgURL);
		else
			JOptionPane.showMessageDialog(this,"Не найдена иконка","Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		return icon;
	}		
		
	public static void main(String[] args) 
	{
		if(args.length>0)
		{
			MainFrame mainFrame=null;
			mainFrame=new MainFrame(args[0]);
			mainFrame.init();
		}
		else
		{
			System.out.println("Не задан файл базы данных!");
			System.exit(0);
		}
	}
	
	public MainFrame(String database)
	{
		super();
		DataBase.create(database);
	}
	
/**
 * ================================
 * Процедура создания главного окна
 * ================================	
 */	
	private void init()
	{
		setTitle("Электронный дневник");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Container c=getContentPane();
		c.setLayout(new BorderLayout());
				
		// Панель кнопок дневника
		JPanel pnlButton1=null;
		pnlButton1=new JPanel();
		pnlButton1.setLayout(new BoxLayout(pnlButton1,BoxLayout.Y_AXIS));
		ImageIcon iconAdd64=createIcon("Images/add64.png");
		JButton btnAdd1=null;
		btnAdd1=new JButton("Добавить");
		btnAdd1.setIcon(iconAdd64);
		btnAdd1.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnAdd1.setHorizontalTextPosition(SwingConstants.CENTER);
		btnAdd1.setForeground(Color.WHITE);		
		btnAdd1.setActionCommand("Добавить запись");
		btnAdd1.addActionListener(this);
		pnlButton1.add(btnAdd1);
		ImageIcon iconEdit64=createIcon("Images/edit64.png");
		btnEdit1=null;
		btnEdit1=new JButton("Изменить");
		btnEdit1.setIcon(iconEdit64);
		btnEdit1.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnEdit1.setHorizontalTextPosition(SwingConstants.CENTER);
		btnEdit1.setForeground(Color.WHITE);		
		btnEdit1.setActionCommand("Изменить запись");
		btnEdit1.addActionListener(this);
		btnEdit1.setEnabled(false);
		btnEdit1.setMaximumSize(btnAdd1.getMaximumSize());
		pnlButton1.add(btnEdit1);	
		ImageIcon iconDel64=createIcon("Images/delete64.png");
		btnDel1=null;
		btnDel1=new JButton("Удалить");
		btnDel1.setIcon(iconDel64);
		btnDel1.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnDel1.setHorizontalTextPosition(SwingConstants.CENTER);
		btnDel1.setForeground(Color.WHITE);		
		btnDel1.setActionCommand("Удалить запись");
		btnDel1.addActionListener(this);
		btnDel1.setEnabled(false);
		btnDel1.setMaximumSize(btnAdd1.getMaximumSize());
		pnlButton1.add(btnDel1);
		JLabel lblSeparator=null;
		lblSeparator=new JLabel("Другое");
		pnlButton1.add(lblSeparator);
		ImageIcon iconFind64=createIcon("Images/find64.png");
		JButton btnFind1=null;
		btnFind1=new JButton("Найти");
		btnFind1.setIcon(iconFind64);
		btnFind1.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnFind1.setHorizontalTextPosition(SwingConstants.CENTER);
		btnFind1.setForeground(Color.WHITE);		
		btnFind1.setActionCommand("Найти");
		btnFind1.addActionListener(this);
		btnFind1.setMaximumSize(btnAdd1.getMaximumSize());
		pnlButton1.add(btnFind1);
		ImageIcon iconTags64=createIcon("Images/tags64.png");
		JButton btnTags1=null;
		btnTags1=new JButton("Теги");
		btnTags1.setIcon(iconTags64);
		btnTags1.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnTags1.setHorizontalTextPosition(SwingConstants.CENTER);
		btnTags1.setForeground(Color.WHITE);		
		btnTags1.setActionCommand("Редактировать теги");
		btnTags1.addActionListener(this);
		btnTags1.setMaximumSize(btnAdd1.getMaximumSize());
		pnlButton1.add(btnTags1);
		
		// Центральная панель
		JSplitPane pnlCenter=null;
		pnlCenter=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		// Текстовая панель
		JPanel pnlDiary=null;
		pnlDiary=new JPanel();
		pnlDiary.setLayout(new BoxLayout(pnlDiary,BoxLayout.Y_AXIS));
		lblDTitle=null;
		lblDTitle=new JLabel("Запись");
		lblDTitle.setAlignmentX(CENTER_ALIGNMENT);
		pnlDiary.add(lblDTitle);
		JTabbedPane pnlInfo=null;
		pnlInfo=new JTabbedPane();
		txtView=null;
		txtView=new JTextPane();
		txtView.setContentType("text/html");
		txtView.setEditable(false);
		pnlInfo.add(new JScrollPane(txtView), "Просмотр");
		txtEdit=null;
		txtEdit=new JTextPane();
		txtEdit.setContentType("text/plane");
		txtEdit.setEditable(true);
		pnlInfo.add(new JScrollPane(txtEdit), "Текст");
		
		JPanel pnlLinkTags=null;
		pnlLinkTags=new JPanel();
		pnlLinkTags.setLayout(new BoxLayout(pnlLinkTags,BoxLayout.Y_AXIS));
		JLabel lblLinkTags=null;
		lblLinkTags=new JLabel("Теги записи");
		lblLinkTags.setAlignmentX(CENTER_ALIGNMENT);
		pnlLinkTags.add(lblLinkTags);
		lml=null;
		lml=new LinkListModel();
		listLinkTags=null;
		listLinkTags=new JList<String>(lml);
		listLinkTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listLinkTags.getSelectionModel().addListSelectionListener(this);
		hashCodeL=listLinkTags.getSelectionModel().hashCode();
		pnlLinkTags.add(new JScrollPane(listLinkTags));
		
		// Панель кнопок тегов
		JPanel pnlButton2=null;
		pnlButton2=new JPanel();
		pnlButton2.setLayout(new BoxLayout(pnlButton2,BoxLayout.X_AXIS));
		pnlButton2.add(Box.createHorizontalGlue());
		ImageIcon iconAdd24=createIcon("Images/add24.png");
		JButton btnAdd2=null;
		btnAdd2=new JButton();
		btnAdd2.setIcon(iconAdd24);
		btnAdd2.setActionCommand("Добавить тег");
		btnAdd2.addActionListener(this);
		pnlButton2.add(btnAdd2);		
		ImageIcon iconDel24=createIcon("Images/delete24.png");
		btnDel2=null;
		btnDel2=new JButton();
		btnDel2.setIcon(iconDel24);
		btnDel2.setActionCommand("Удалить тег");
		btnDel2.addActionListener(this);
		btnDel2.setEnabled(false);
		pnlButton2.add(btnDel2);
		pnlButton2.add(Box.createHorizontalGlue());
		pnlLinkTags.add(pnlButton2);
		pnlInfo.add(pnlLinkTags,"Теги");
		
		pnlDiary.add(pnlInfo);
		
		// Панель дат
		JPanel pnlDates=null;
		pnlDates=new JPanel();
		pnlDates.setLayout(new BoxLayout(pnlDates,BoxLayout.Y_AXIS));
		
		JLabel lblTags=null;
		lblTags=new JLabel("Выбор тега");
		lblTags.setAlignmentX(CENTER_ALIGNMENT);
		pnlDates.add(lblTags);
		cmbTags=null;
		cmt=null;
		cmt=new TagsListModel();
		cmt.insertNoBase("Все записи");
		cmbTags=new JComboBox<String>(cmt);
		cmbTags.setMaximumSize(new Dimension(1000,20));
		cmbTags.setActionCommand("Выбор тега");
		cmbTags.addActionListener(this);
		pnlDates.add(cmbTags);
		
		JLabel lblDates=null;
		lblDates=new JLabel("Даты");
		lblDates.setAlignmentX(CENTER_ALIGNMENT);
		pnlDates.add(lblDates);
		listDates=null;
		lmd=null;
		lmd=new DatesListModel();
		listDates=new JList<String>(lmd);
		listDates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDates.getSelectionModel().addListSelectionListener(this);
		hashCodeD=listDates.getSelectionModel().hashCode();
		pnlDates.add(new JScrollPane(listDates));

		pnlCenter.add(pnlDiary);
		pnlCenter.add(pnlDates);
		pnlCenter.setDividerLocation(800);
		
		JPanel pnlStatus=null;
		pnlStatus=new JPanel();
		pnlStatus.setLayout(new BoxLayout(pnlStatus,BoxLayout.X_AXIS));
		lblDTags=null;
		lblDTags=new JLabel("Теги");
		pnlStatus.add(lblDTags);
		pnlStatus.setBorder(LineBorder.createGrayLineBorder());
		
		c.add(pnlButton1,BorderLayout.WEST);
		c.add(pnlCenter,BorderLayout.CENTER);
		c.add(pnlStatus,BorderLayout.SOUTH);

		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
	}

/**
 * ==================================
 * Диалог добавления записи в дневник
 * ==================================	
 */
	private void AddDiaryDialog()
	{
		JPanel pnlAdd=null;
		pnlAdd=new JPanel();
		pnlAdd.setLayout(new BoxLayout(pnlAdd,BoxLayout.Y_AXIS));

		JLabel lblDate=null;
		lblDate=new JLabel("Дата");
		lblDate.setAlignmentX(CENTER_ALIGNMENT);
		pnlAdd.add(lblDate);
		JSpinner date=null;
		date=new JSpinner(new SpinnerDateModel());
		date.setEditor(new JSpinner.DateEditor(date,"dd.MM.yyyy HH:mm:ss"));
		date.setAlignmentX(CENTER_ALIGNMENT);
		pnlAdd.add(date);
		
		JLabel lblTitle=null;
		lblTitle=new JLabel("Название");
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		pnlAdd.add(lblTitle);
		JTextField txtTitle=null;
		txtTitle=new JTextField();
		pnlAdd.add(txtTitle);
				
		int result=JOptionPane.showConfirmDialog(this, pnlAdd,
			"Новая запись",JOptionPane.OK_CANCEL_OPTION);
		if(result==JOptionPane.OK_OPTION)
		{
			DateFormat df=null;
			df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
			char ch=34;
			String strHTML="<html>\n"+
				"<head>\n"+
				"<meta content="+ch+"text/html;charset=UTF-8"+ch+"/>\n"+
				"</head>\n"+
				"<body>\n"+
				"<h1>"+txtTitle.getText()+"</h1>\n"+
				"<hr/>\n"+
				"</body>\n"+
				"</html>";
			try 
			{
				lmd.insert(df.format(date.getValue()),txtTitle.getText(),strHTML.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) 
			{
				JOptionPane.showMessageDialog(this,e,"Ошибка!",JOptionPane.ERROR_MESSAGE);					
			}
			int id=lmd.refresh();
			int SelRow=lmd.getIndex(id);
			lml.refresh(id);
			refreshText(SelRow);
			listDates.setSelectedIndex(SelRow);
		}					
	}
	
/**
 * ==============================================
 * Построение диалога изменения записи в дневнике
 * ==============================================	
 */
	private void EditDiaryDialog()
	{
		int SelRow=listDates.getSelectedIndex();
		
		JPanel pnlEdit=null;
		pnlEdit=new JPanel();
		pnlEdit.setLayout(new BoxLayout(pnlEdit,BoxLayout.Y_AXIS));

		JLabel lblDate=null;
		lblDate=new JLabel("Дата");
		lblDate.setAlignmentX(CENTER_ALIGNMENT);
		pnlEdit.add(lblDate);
		JSpinner date=null;
		date=new JSpinner(new SpinnerDateModel());
		date.setEditor(new JSpinner.DateEditor(date,"dd.MM.yyyy HH:mm:ss"));
		date.setAlignmentX(CENTER_ALIGNMENT);
		DateFormat df=null;
		df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try 
		{
			date.setValue(df.parse((String)lmd.getValueAt(SelRow,0)));
		} catch (ParseException e) 
		{
			JOptionPane.showMessageDialog(this,e,"Ошибка!",JOptionPane.ERROR_MESSAGE);					
		}
		pnlEdit.add(date);
		
		JLabel lblTitle=null;
		lblTitle=new JLabel("Название");
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		pnlEdit.add(lblTitle);
		JTextField txtTitle=null;
		txtTitle=new JTextField();
		txtTitle.setText((String)lmd.getValueAt(SelRow,1));
		pnlEdit.add(txtTitle);
				
		int result=JOptionPane.showConfirmDialog(this, pnlEdit,
			"Изменение записи",JOptionPane.OK_CANCEL_OPTION);
		if(result==JOptionPane.OK_OPTION)
		{
			int id=lmd.getID(SelRow);
			lmd.update(lmd.getID(SelRow),df.format(date.getValue()),txtTitle.getText(),
					TransformData.getPlaneBytes(txtEdit.getStyledDocument()));
			lmd.refresh();
			SelRow=lmd.getIndex(id);
			refreshText(SelRow);
			listDates.setSelectedIndex(SelRow);
		}					
	}

/**
 * ===============================
 * Диалог добавления тега в запись
 * ===============================	
 */
	private void AddTagDialog()
	{
		int SelRow=listDates.getSelectedIndex();

		JPanel pnlAdd=null;
		pnlAdd=new JPanel();
		pnlAdd.setLayout(new BoxLayout(pnlAdd,BoxLayout.Y_AXIS));
		
		JLabel lblTags=null;
		lblTags=new JLabel("Теги");
		lblTags.setAlignmentX(CENTER_ALIGNMENT);
		pnlAdd.add(lblTags);
		TagsListModel cmTags=null;
		cmTags=new TagsListModel();
		JComboBox<String> cmbTags=null;
		cmbTags=new JComboBox<String>(cmTags);
		pnlAdd.add(cmbTags);		
	
		int result=JOptionPane.showConfirmDialog(this, pnlAdd,
			"Добавить тег",JOptionPane.OK_CANCEL_OPTION);
		if(result==JOptionPane.OK_OPTION)
		{
			lml.insert(lmd.getID(listDates.getSelectedIndex()),
				cmTags.getID(cmbTags.getSelectedIndex()));
			int id=lml.refresh(lmd.getID(SelRow));
			listLinkTags.setSelectedIndex(lml.getIndex(id));
			refreshText(SelRow);
		}					
	}
	
/**
 * ===================================================
 * Процедура обновления текстовой информации на экране
 * ===================================================
 */
	private void refreshText(int SelRow)
	{
		if(SelRow>=0)
		{
			txtView.setDocument(
				TransformData.getHTMLDocument((byte[])lmd.getValueAt(SelRow, 2)));
			txtEdit.setDocument(
				TransformData.getPlaneDocument((byte[])lmd.getValueAt(SelRow, 2)));
			lblDTitle.setText((String)lmd.getValueAt(SelRow, 1));
			lblDTags.setText("Теги: "+lml.getLineText());
		}
		else
		{
			txtView.setText("");
			txtEdit.setText("");			
			lblDTitle.setText("Запись");
			lblDTags.setText("Теги");
		}
	}
	
/**
 * ==================================
 * Процедура обработки нажатия кнопок
 * ==================================
 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		int SelRowD=listDates.getSelectedIndex();
		int result;
		switch(e.getActionCommand())
		{
		case "Добавить запись":
			AddDiaryDialog();
			break;
		case "Изменить запись":
			EditDiaryDialog();
			break;
		case "Удалить запись":
			result=JOptionPane.showConfirmDialog(this,  
				"Удалить запись "+lmd.getElementAt(SelRowD)+"?",
				"Удаление",	JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION)
			{
				lmd.delete(lmd.getID(SelRowD));
				lmd.refresh();
				refreshText((lmd.getSize()>0)?((lmd.getSize()==SelRowD)?SelRowD-1:SelRowD):-1);
			}
			break;
		case "Выбор тега":
			String item=(String)cmt.getSelectedItem();
			if(!item.equals("") && !item.equals("Все записи"))
			{
				lmd.refresh(cmt.getID(cmbTags.getSelectedIndex()));
				refreshText(-1);
			}
			else
			{
				lmd.refresh();
				refreshText(-1);
			}
			listDates.clearSelection();
			break;
		case "Добавить тег":
			AddTagDialog();
			break;
		case "Удалить тег":
			int SelRowL=listLinkTags.getSelectedIndex();
			result=JOptionPane.showConfirmDialog(this,  
				"Удалить тег "+lml.getElementAt(SelRowL)+"?",
				"Удаление",	JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION)
			{
				lml.delete(lml.getID(SelRowL));
				lml.refresh(lmd.getID(SelRowD));
				refreshText(SelRowD);
			}
			break;
		case "Редактировать теги":
			TagsDialog tags=null;
			tags=new TagsDialog(this,"Список тегов");
			tags.init();
			break;
		case "Найти":
			String word=JOptionPane.showInputDialog(this, "Введите строку для поиска", 
				"Поиск",JOptionPane.OK_CANCEL_OPTION);
			if(word!=null && !word.equals(""))
			{
				lmd.refresh(word);
				listDates.clearSelection();
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
			int SelRow=lsm.getAnchorSelectionIndex();
			if(lsm.hashCode()==hashCodeD)
			{
				btnDel1.setEnabled(true);
				btnEdit1.setEnabled(true);
				refreshText(SelRow);
				lml.refresh(lmd.getID(SelRow));
			}
			else if(lsm.hashCode()==hashCodeL)
				btnDel2.setEnabled(true);				
		}
		else
		{
			if(lsm.hashCode()==hashCodeD)
			{
				btnDel1.setEnabled(false);
				btnEdit1.setEnabled(false);
				refreshText(-1);
			}
			else if(lsm.hashCode()==hashCodeL)
				btnDel2.setEnabled(false);			
		}
	}
}
