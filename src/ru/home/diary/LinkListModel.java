package ru.home.diary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.AbstractListModel;


/**
 * ============================
 * Класс данных связей с тэгами
 * ============================
 */
public class LinkListModel extends AbstractListModel<String>
{
	public class SData 
	{
		public String Title;
		public int index, ID_t;
		public SData(int index, String Title, int ID_t)
		{
			this.index=index;
			this.Title=Title;
			this.ID_t=ID_t;
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
	public LinkListModel()
	{
		super();
		DataList=null;
		DataList=new HashMap<Integer,SData>();
		KeyList=null;
		KeyList=new ArrayList<Integer>();
	}

/**
 * ===========================
 * Процедура обновления данных
 * ===========================	
 */
	public Integer refresh(int ID_d)
	{
		ArrayList<Object[]> listobj=null;
		String strSelect="SELECT t.Title, t.ID, l.ID FROM "+
			"tags t, link l WHERE l.ID2=t.ID and l.ID1="+String.valueOf(ID_d)+
			" ORDER BY t.Title";
		listobj=new ArrayList<Object[]>();
		listobj=DataBase.select(strSelect);		
		int i=0;
		DataList.clear();
		KeyList.clear();
		for(Object[] obj:listobj)
		{
			KeyList.add((Integer)obj[2]);
			DataList.put((Integer)obj[2],new SData(i++,(String)obj[0],(Integer)obj[1]));		
		}
		fireContentsChanged(this, 0, getSize());		
		return (KeyList.size()>0)?Collections.max(KeyList):-1;
	}

/**
 * =================================
 * Процедура добавления связи в базу
 * =================================
 */
	public void insert(int ID_d, int ID_t)
	{
		String strInsert="INSERT INTO link(ID1,ID2) VALUES(?,?)";
		Object[] obj={ID_d,ID_t};
		DataBase.insert(obj, strInsert);
	}

	public void insert(int ID, int ID_d, int ID_t)
	{
		String strInsert="INSERT INTO link(ID,ID1,ID2) VALUES(?,?,?)";
		Object[] obj={ID,ID_d,ID_t};
		DataBase.insert(obj, strInsert);
	}
				
/**
 * ================================
 * Процедура изменения связи в базе
 * ================================
 */
	public void update(int ID, int ID_d, int ID_t)
	{
		String strUpdate="UPDATE link SET ID1=?, ID2=? WHERE ID=?";
		Object[] obj={ID_d,ID_t,ID};
		DataBase.update(obj, strUpdate);
	}
						
/**
 * ================================
 * Процедура удаления связи из базы
 * ================================ 
 */
	public void delete(int ID)
	{
		String strDelete="DELETE FROM link WHERE ID=?";
		DataBase.delete(ID, strDelete);
	}
		
/**
 * ==================================
 * Процедуры передачи данных в список
 * ==================================
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
				return DataList.get(key).Title;
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
				if(j==0) return DataList.get(key).Title;
				else if(j==1) return DataList.get(key).ID_t;
				else return null;
			}
			else return null;
		}
		else return null;
	}

	@Override
	public int getSize(){return DataList.size();}
	
/**
 * ==========================================
 * Процедура получения всех статусов в строке
 * ==========================================	
 */
	public String getLineText()
	{
		String line="";
		for(int key:KeyList)
			line+=DataList.get(key).Title+";";
		return line;
	}
}
