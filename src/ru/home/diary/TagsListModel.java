package ru.home.diary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * =========================
 * Класс данных списка тегов
 * =========================
 */
public class TagsListModel extends AbstractListModel<String> implements ComboBoxModel<String>
{
	public class SData 
	{
		public String Title;
		public int index;
		public SData(int index, String Title)
		{
			this.index=index;
			this.Title=Title;
		}
	}		
	private static final long serialVersionUID = 1L;
	private HashMap<Integer,SData> DataList;
	private ArrayList<Integer> KeyList;
	private Object selectedItem;

/**
 * ==================
 * Конструктор класса
 * ==================	
 */
	public TagsListModel()
	{
		super();
		DataList=null;
		DataList=new HashMap<Integer,SData>();
		KeyList=null;
		KeyList=new ArrayList<Integer>();
		refresh();
	}
	
/**
 * ===========================
 * Процедура обновления данных
 * ===========================	
 */
	public Integer refresh()
	{
		ArrayList<Object[]> listobj=null;
		String strSelect="SELECT Title, ID FROM tags ORDER BY Title";
		listobj=new ArrayList<Object[]>();
		listobj=DataBase.select(strSelect);		
		int i=0;
		DataList.clear();
		KeyList.clear();
		for(Object[] obj:listobj)
		{
			KeyList.add((Integer)obj[1]);
			DataList.put((Integer)obj[1],new SData(i++,(String)obj[0]));		
		}
		fireContentsChanged(this, 0, getSize());
		return (KeyList.size()>0)?Collections.max(KeyList):-1;
	}

/**
 * ================================
 * Процедура добавления тега в базу
 * ================================
 */
	public void insert(String title)
	{
		String strInsert="INSERT INTO tags(Title) VALUES(?)";
		Object[] obj={title};
		DataBase.insert(obj, strInsert);
	}

	public void insert(int ID, String title)
	{
		String strInsert="INSERT INTO tags(ID,Title) VALUES(?,?)";
		Object[] obj={ID,title};
		DataBase.insert(obj, strInsert);
	}
	
/**
 * =============================================
 * Процедура добавления записи без записи в базу
 * =============================================	
 */
	public void insertNoBase(String title)
	{
		DataList.put(-1, new SData(KeyList.size()+1,"Все записи"));		
	}
			
/**
 * ===============================
 * Процедура изменения тега в базе
 * ===============================
 */
	public void update(int ID, String title)
	{
		String strUpdate="UPDATE tags SET Title=? WHERE ID=?";
		Object[] obj={title,ID};
		DataBase.update(obj, strUpdate);
	}
					
/**
 * ===============================
 * Процедура удаления тега из базы
 * =============================== 
 */
	public void delete(int ID)
	{
		String strDelete="DELETE FROM tags WHERE ID=?";
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

	@Override
	public int getSize(){return DataList.size();}

	@Override
	public Object getSelectedItem() 
	{
		return selectedItem;
	}

	@Override
	public void setSelectedItem(Object selectedItem)
	{
		this.selectedItem=selectedItem;
	}
}
