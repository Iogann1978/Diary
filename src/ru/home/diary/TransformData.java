package ru.home.diary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * ==========================
 * Класс трансформации данных
 * ==========================
 */
public class TransformData 
{
/**
 * ======================================
 * Процедура преобразования байтов в HTML
 * ======================================
 */
	public static HTMLDocument getHTMLDocument(byte[] html)
	{		
		HTMLEditorKit kitHTML=null;
		ByteArrayInputStream shtml=null;
		BufferedReader r=null;
		HTMLDocument docHTML=null;
	
		try 
		{
			kitHTML=new HTMLEditorKit();
			docHTML=(HTMLDocument)kitHTML.createDefaultDocument();
			shtml=new ByteArrayInputStream(html);
			r=new BufferedReader(new InputStreamReader(shtml,"UTF-8"));
			kitHTML.read(r, docHTML, 0);
		} catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		} catch (BadLocationException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try 
			{
				if(r!=null) r.close();
				if(shtml!=null) shtml.close();
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return docHTML;
	}
	
/**
 * =====================================	
 * Процедура преобразования HTML в байты
 * =====================================
 */
	public static byte[] getHTMLBytes(HTMLDocument docHTML)
	{
		byte[] html=null;
		ByteArrayOutputStream shtml=null;
		BufferedWriter w=null;
		HTMLEditorKit kit=null;
		try 
		{
			shtml=new ByteArrayOutputStream();
			w=new BufferedWriter(new OutputStreamWriter(shtml));			
			kit=new HTMLEditorKit();
			kit.write(w, docHTML, 0, docHTML.getLength());
			html=shtml.toByteArray();
		} catch (IOException | BadLocationException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try 
			{
				if(w!=null) w.close();
				if(shtml!=null) shtml.close();
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
		return html;
	}

/**
 * =====================================
 * Процедура преобразования байтов в RTF
 * =====================================
 */
	public static StyledDocument getRTFDocument(byte[] rtf)
	{		
		RTFEditorKit kitRTF=null;
		ByteArrayInputStream srtf=null;
		BufferedReader r=null;
		StyledDocument docRTF=null;
		
		try 
		{
			srtf=new ByteArrayInputStream(rtf);
			kitRTF=new RTFEditorKit();
			docRTF=(StyledDocument)kitRTF.createDefaultDocument();
			r=new BufferedReader(new InputStreamReader(srtf));
			kitRTF.read(r, docRTF, 0);
		} catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		} catch (BadLocationException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try 
			{
				if(r!=null) r.close();
				if(srtf!=null) srtf.close();
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
			
		return docRTF;
	}
	
/**
 * =======================================
 * Процедура преобразования байтов в Plane
 * =======================================
 */
	public static StyledDocument getPlaneDocument(byte[] plane)
	{		
		StyledEditorKit kitPlane=null;
		ByteArrayInputStream splane=null;
		BufferedReader r=null;
		StyledDocument docPlane=null;
		
		try 
		{
			kitPlane=new StyledEditorKit();
			docPlane=(StyledDocument)kitPlane.createDefaultDocument();
			splane=new ByteArrayInputStream(plane);
			r=new BufferedReader(new InputStreamReader(splane,"UTF-8"));
			kitPlane.read(r, docPlane, 0);
		} catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		} catch (BadLocationException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try 
			{
				if(r!=null) r.close();
				if(splane!=null) splane.close();
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return docPlane;
	}
	
/**
 * ====================================	
 * Процедура преобразования RTF в байты
 * ====================================
 */
	public static byte[] getRTFBytes(StyledDocument docRTF)
	{
		byte[] rtf=null;
		ByteArrayOutputStream srtf=null;
		BufferedWriter w=null;
		RTFEditorKit kit=null;
		try 
		{
			srtf=new ByteArrayOutputStream();
			w=new BufferedWriter(new OutputStreamWriter(srtf,"UTF-8"));			
			kit=new RTFEditorKit();
			kit.write(w, docRTF, 0, docRTF.getLength());
			rtf=srtf.toByteArray();
		} catch (IOException | BadLocationException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try 
			{
				if(w!=null) w.close();
				if(srtf!=null) srtf.close();
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
		return rtf;
	}

/**
 * ======================================	
 * Процедура преобразования Plane в байты
 * ======================================
 */
	public static byte[] getPlaneBytes(StyledDocument docPlane)
	{
		byte[] plane=null;
		ByteArrayOutputStream splane=null;
		BufferedWriter w=null;
		StyledEditorKit kit=null;
		try 
		{
			splane=new ByteArrayOutputStream();
			w=new BufferedWriter(new OutputStreamWriter(splane,"UTF-8"));			
			kit=new StyledEditorKit();
			kit.write(w, docPlane, 0, docPlane.getLength());
			plane=splane.toByteArray();
		} catch (IOException | BadLocationException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try 
			{
				if(w!=null) w.close();
				if(splane!=null) splane.close();
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
		return plane;
	}
	
/**
 * ================================	
 * Процедура конвертации RTF в HTML
 * ================================
 */
	public static byte[] RTFtoHTML(byte[] rtf)
	{
		byte[] html=null;
		ByteArrayOutputStream shtml=null;
		JTextPane txtPane=null;
		BufferedWriter w=null;
		EditorKit kitHTML=null;
		try 
		{
			txtPane=new JTextPane();
			txtPane.setContentType("text/rtf");
			txtPane.setDocument(getRTFDocument(rtf));
			kitHTML=txtPane.getEditorKitForContentType("text/html;charset=cp1252");
			shtml=new ByteArrayOutputStream();
			w=new BufferedWriter(new OutputStreamWriter(shtml,"Windows-1252"));
			kitHTML.write(w, txtPane.getDocument(), 0, txtPane.getDocument().getLength());
			html=new String(shtml.toByteArray(),"cp1251").getBytes();
		} catch (IOException | BadLocationException e) 
		{
			JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try 
			{
				if(w!=null) w.close();
				if(shtml!=null) shtml.close();
			} catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,e,"Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
		return html;
	}
}
