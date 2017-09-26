package ru.home.diary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class DataBase 
{
	private static String strConnect="jdbc:sqlite:";

/**
 * =======================
 * Процедура создания базы
 * =======================	
 */
	public static void create(String database)
	{
		strConnect+=database;
		Connection conn=null;
		Statement stmt=null;
		String strCommand;
		FileInputStream fis=null;
		BufferedReader br=null;

		try 
		{
			Class.forName("org.sqlite.JDBC");
			conn=DriverManager.getConnection(strConnect);
			stmt=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY);
			fis=new FileInputStream("database.sql");
			br=new BufferedReader(new InputStreamReader(fis));
			while((strCommand=br.readLine())!=null)
				stmt.addBatch(strCommand);
			stmt.executeBatch();
		} catch (ClassNotFoundException | SQLException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} catch (FileNotFoundException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} finally
		{
			try 
			{
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
				if(fis!=null) fis.close();
				if(br!=null) br.close();
			} catch (SQLException e) 
			{
				JOptionPane.showMessageDialog(null, e,"Ошибка!",
					JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null, e,"Ошибка!",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
/**
 * ===============================
 * Процедура чтения данных из базы
 * ===============================	
 */
	public static ArrayList<Object[]> select(String strSelect)
	{
		ArrayList<Object[]> listobj=null;
		listobj=new ArrayList<Object[]>();
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
				
	    try 
	    {
			Class.forName("org.sqlite.JDBC");
			conn=DriverManager.getConnection(strConnect);
			stmt=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY);
			rs=stmt.executeQuery(strSelect);
			int nobj=rs.getMetaData().getColumnCount();
			if(rs!=null)
				while(rs.next())
				{
					Object[] obj=null;
					obj=new Object[nobj];
					int j;
					for(j=0;j<nobj;j++)
						if(rs.getMetaData().getColumnType(j+1)==Types.BLOB)
							obj[j]=rs.getBytes(j+1);
						else
							obj[j]=rs.getObject(j+1);
					listobj.add(obj);
				}
		} catch (ClassNotFoundException | SQLException e) 
	    {
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} finally
	    {
			try 
			{
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) 
			{
				JOptionPane.showMessageDialog(null, e,"Ошибка!",
					JOptionPane.ERROR_MESSAGE);
			}
	    }
	    return listobj;
	}
		
/**
 * ===========================
 * Процедура добавления в базу
 * ===========================
 */
	public static void insert(Object obj[], String strInsert)
	{
		Connection conn=null;
		PreparedStatement pstmt=null;

		try 
		{
			Class.forName("org.sqlite.JDBC");
			conn=DriverManager.getConnection(strConnect);
			pstmt=conn.prepareStatement(strInsert);
			int i;
			for(i=0;i<obj.length;i++)
				if(obj[i] instanceof byte[])
					pstmt.setBytes(i+1, (byte[])obj[i]);
				else
					pstmt.setObject(i+1, obj[i]);
			pstmt.executeUpdate();
		} catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} finally
		{
			try 
			{
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) 
			{
				JOptionPane.showMessageDialog(null, e,"Ошибка!",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}

/**
 * ==========================
 * Процедура изменения в базе
 * ==========================
 */
	public static void update(Object[] obj, String strUpdate)
	{
		Connection conn=null;
		PreparedStatement pstmt=null;

		try 
		{
			Class.forName("org.sqlite.JDBC");
			conn=DriverManager.getConnection(strConnect);
			pstmt=conn.prepareStatement(strUpdate);
			int i;
			for(i=0;i<obj.length;i++)
				if(obj[i] instanceof byte[])
					pstmt.setBytes(i+1, (byte[])obj[i]);
				else
					pstmt.setObject(i+1, obj[i]);
			pstmt.executeUpdate();
		} catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} finally
		{
			try 
			{
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) 
			{
				JOptionPane.showMessageDialog(null, e,"Ошибка!",
					JOptionPane.ERROR_MESSAGE);
			}
		}		
	}
		
/**
 * ==========================
 * Процедура удаления из базы
 * ========================== 
 */
	public static void delete(int ID, String strDelete)
	{
		Connection conn=null;
		PreparedStatement pstmt=null;

		try 
		{
			Class.forName("org.sqlite.JDBC");
			conn=DriverManager.getConnection(strConnect);
			pstmt=conn.prepareStatement(strDelete);
			pstmt.setInt(1, ID);
			pstmt.executeUpdate();
		} catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) 
		{
			JOptionPane.showMessageDialog(null, e,"Ошибка!",
				JOptionPane.ERROR_MESSAGE);
		} finally
		{
			try 
			{
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) 
			{
				JOptionPane.showMessageDialog(null, e,"Ошибка!",
					JOptionPane.ERROR_MESSAGE);
			}
		}				
	}	
}
