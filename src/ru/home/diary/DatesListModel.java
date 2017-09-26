package ru.home.diary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.apache.commons.codec.binary.Base64;

/**
 * ===========================
 * Класс данных списка записей
 * ===========================
 */
public class DatesListModel extends AbstractListModel<String>
{
	public class SData 
	{
		public String Date, Title;
		public byte[] Descript;
		public int index;
		public SData(int index, String Date, String Title, byte[] Descript)
		{
			this.index=index;
			this.Date=Date;
			this.Title=Title;
			this.Descript=null;
			this.Descript=Descript.clone();
		}
	}	
	private static final long serialVersionUID = 1L;
	private HashMap<Integer,SData> DataList;
	private ArrayList<Integer> KeyList;

/**
 * ==================
 * Конструктор класса
 * ==================	
 */
	public DatesListModel()
	{
		super();
		DataList=null;
		DataList=new HashMap<Integer,SData>();
		KeyList=null;
		KeyList=new ArrayList<Integer>();
		//loadXML("D:/anton/1/Дневник.xml");
		//loadXML_Notes("D:/anton/1/Заметки.xml");
		//loadXML("//home/anton/workspace/Diary/Дневник.xml");
		refresh();
	}
	
/**
 * ===========================
 * Процедура обновления данных
 * ===========================	
 */
	public Integer refresh()
	{
		String strSelect="SELECT Date, Title, Descript, ID FROM diary ORDER BY Date desc";
		select(strSelect);
		fireContentsChanged(this, 0, getSize());
		return (KeyList.size()>0)?Collections.max(KeyList):-1;
	}

	public Integer refresh(String find)
	{
		String strSelect="SELECT Date, Title, Descript, ID FROM diary "+
			"WHERE Descript like '%"+find+"%' ORDER BY Date";
		select(strSelect);
		fireContentsChanged(this, 0, getSize());
		return (KeyList.size()>0)?Collections.max(KeyList):-1;
	}

	public Integer refresh(int ID_t)
	{
		String strSelect="SELECT d.Date, d.Title, d.Descript, d.ID "+
			"FROM diary d, tags t, link l "+
			"WHERE d.ID=l.ID1 and t.ID=l.ID2 and t.ID="+String.valueOf(ID_t)+
			" ORDER BY d.Date";
		select(strSelect);
		fireContentsChanged(this, 0, getSize());
		return (KeyList.size()>0)?Collections.max(KeyList):-1;
	}
	
/**
 * ===============================
 * Процедура чтения данных из базы
 * ===============================	
 */
	private void select(String strSelect)
	{
		ArrayList<Object[]> listobj=null;
		listobj=new ArrayList<Object[]>();
		listobj=DataBase.select(strSelect);
		int i=0;
		DataList.clear();
		KeyList.clear();
		for(Object[] obj:listobj)
		{
			KeyList.add((Integer)obj[3]);
			DataList.put((Integer)obj[3],new SData(i++,(String)obj[0],(String)obj[1],(byte[])obj[2]));
		}
	}
	
/**
 * ==================================
 * Процедура добавления группы в базу
 * ==================================
 */
	public void insert(String date, String title, byte[] descript)
	{
		String strInsert="INSERT INTO diary(Date,Title,Descript) VALUES(?,?,?)";
		Object[] obj={date,title,descript};
		DataBase.insert(obj, strInsert);
	}

	public void insert(int ID, String date, String title, byte[] descript)
	{
		String strInsert="INSERT INTO diary(ID,Date,Title,Descript) VALUES(?,?,?,?)";
		Object[] obj={ID,date,title,descript};
		DataBase.insert(obj, strInsert);
	}
		
/**
 * =================================
 * Процедура изменения группы в базе
 * =================================
 */
	public void update(int ID, String date, String title, byte[] descript)
	{
		String strUpdate="UPDATE diary SET Date=?, Title=?, Descript=? WHERE ID=?";
		Object[] obj={date,title,descript,ID};
		DataBase.update(obj, strUpdate);
	}
				
/**
 * =================================
 * Процедура удаления группы из базы
 * ================================= 
 */
	public void delete(int ID)
	{
		String strDelete="DELETE FROM diary WHERE ID=?";
		DataBase.delete(ID, strDelete);
	}

/**
 * =================================
 * Процедура загрузки таблицы из XML
 * =================================
 */
	public void loadXML(String filename)
	{
		File fxml=null;
		fxml=new File(filename);
		if(fxml.exists()&&fxml.isFile())
		{
			DocumentBuilderFactory dbf=null;
			dbf=DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);
			try 
			{
				DocumentBuilder db=null;
				db=dbf.newDocumentBuilder();
				Document doc=null;
				doc=db.parse(fxml);
				doc.getDocumentElement().normalize();
								
				// Читаем корневой элемент
				Element elementRoot=null;
				elementRoot=(Element)doc.getElementsByTagName("dataroot").item(0);
								
				// Читаем данные
				NodeList nodelistDiary=null;
				nodelistDiary=elementRoot.getElementsByTagName("Дневник");
				int i;
				for(i=0;i<nodelistDiary.getLength();++i)
				{
					Node nodeDiary=null;
					nodeDiary=nodelistDiary.item(i);
					if(nodeDiary.getNodeType()==Node.ELEMENT_NODE)
					{
						Element elementDiary=null;
						elementDiary=(Element)nodeDiary;
						Node nodeDate=null;
						nodeDate=elementDiary.getElementsByTagName("Дата").item(0);
						Node nodeTitle=null;
						nodeTitle=elementDiary.getElementsByTagName("Название").item(0);
						Node nodeText=null;
						nodeText=elementDiary.getElementsByTagName("Запись").item(0);
						
						byte[] encoded=null, decoded=null, html=null;
						encoded=nodeText.getTextContent().getBytes();
						decoded=Base64.decodeBase64(encoded);
						html=TransformData.RTFtoHTML(decoded);
						insert(nodeDate.getTextContent(),nodeTitle.getTextContent(),html);													
					}
				}													
			} catch (SAXException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			} catch (ParserConfigurationException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null,"Файл данных не найден!","Ошибка",JOptionPane.ERROR_MESSAGE);
		}								
	}

/**
 * =================================
 * Процедура загрузки таблицы из XML
 * =================================
 */
	public void loadXML_Notes(String filename)
	{
		File fxml=null;
		fxml=new File(filename);
		if(fxml.exists()&&fxml.isFile())
		{
			DocumentBuilderFactory dbf=null;
			dbf=DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);
			try 
			{
				DocumentBuilder db=null;
				db=dbf.newDocumentBuilder();
				Document doc=null;
				doc=db.parse(fxml);
				doc.getDocumentElement().normalize();
								
				// Читаем корневой элемент
				Element elementRoot=null;
				elementRoot=(Element)doc.getElementsByTagName("dataroot").item(0);
									
				// Читаем данные
				NodeList nodelistNotes=null;
				nodelistNotes=elementRoot.getElementsByTagName("Заметки");
				int i;
				for(i=0;i<nodelistNotes.getLength();++i)
				{
					Node nodeNotes=null;
					nodeNotes=nodelistNotes.item(i);
					if(nodeNotes.getNodeType()==Node.ELEMENT_NODE)
					{
						Element elementNotes=null;
						elementNotes=(Element)nodeNotes;
						Node nodeDate=null;
						nodeDate=elementNotes.getElementsByTagName("Дата").item(0);
						Node nodeTitle=null;
						nodeTitle=elementNotes.getElementsByTagName("Название").item(0);
						Node nodeText=null;
						nodeText=elementNotes.getElementsByTagName("Заметка").item(0);
						insert(nodeDate.getTextContent(),nodeTitle.getTextContent(),nodeText.getTextContent().getBytes());													
					}
				}													
			} catch (SAXException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			} catch (ParserConfigurationException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null,"Файл данных не найден!","Ошибка",JOptionPane.ERROR_MESSAGE);
		}								
	}
	
/**
 * ===================================
 * Процедуры передачи данных в таблицу
 * ===================================
 */
	public int getIndex(Integer ID)
	{
		return (DataList.containsKey(ID))?
			DataList.get(ID).index:-1;
	}
				
	public Integer getID(int i){return (i>=0 && i<KeyList.size())?KeyList.get(i):-1;}
				
	@Override
	public String getElementAt(int i) 
	{
		if(i>=0 && i<DataList.size())
		{
			Integer key=getID(i);
			if(DataList.containsKey(key))
				return DataList.get(key).Date+" - "+DataList.get(key).Title;
			else 
				return null;
		}
		else return null;		
	}

	public Object getValueAt(int i, int j)
	{
		if(i>=0 && i<DataList.size())
		{
			Integer key=getID(i);
			if(DataList.containsKey(key))
			{
				if(j==0) return DataList.get(key).Date;
				else if(j==1) return DataList.get(key).Title;
				else if(j==2)
					return DataList.get(key).Descript;
				else return null;
			}
			else return null;
		}
		else return null;
	}
	
	@Override
	public int getSize(){return DataList.size();}
}
