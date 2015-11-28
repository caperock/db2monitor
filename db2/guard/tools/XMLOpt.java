package db2.guard.tools;

import java.io.File;
import java.io.FileOutputStream;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XMLOpt {
	String strDir;
	String strFileName;
	String strCurPath;
	String strLogPath;
	
	public XMLOpt(String FileName){
		if(FileName.indexOf(".xml")>0){
			strFileName = FileName ;
		}else{
		strFileName = FileName + ".xml";
		}
		
	}
	
	public XMLOpt(String CurPath, String LogPath,String FileName){
		strCurPath="";
		strLogPath="";
		
		strCurPath = System.getProperty("user.dir") + "\\" +CurPath;
		System.out.println("#Dir=" + strCurPath);
		strLogPath = LogPath;		
		strFileName= FileName; 
		if(FileName.indexOf(".xml")>0){
			strFileName = FileName ;
		}else{
		strFileName = FileName + ".xml";
		}
	}
	
	public void dumpXMLog(Document oDoc){
		//Dump Current Report
     try {			
			FileOutputStream fos = new FileOutputStream(strCurPath + "\\"  + "report.xml");
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(fos, format);
			writer.write(oDoc);
			writer.flush();
			writer.close();
			fos.close();
			System.out.println("Succeedful write report.xml file");
		} catch (Exception e) {
			System.out.println(e.toString());
			// TODO: handle exception
		}

   //Dump Report Into Log Path
     try {
    	 File fd = null;
    	 try {
				fd = new File(strLogPath);
				if (!fd.exists()) {
					fd.mkdirs();
				}				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fd = null;
			}
    	 
			FileOutputStream fos = new FileOutputStream(strLogPath + "\\" +strFileName);
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(fos, format);
			writer.write(oDoc);
			writer.flush();
			writer.close();
			fos.close();
			System.out.println("Succeedful write " + strFileName +".xml file into log path");
		} catch (Exception e) {
			System.out.println(e.toString());
			// TODO: handle exception
		}


		
	}
	
	public void dumpXML(Document oDoc){
		try {
			
			FileOutputStream fos = new FileOutputStream(strFileName);
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(fos, format);
			writer.write(oDoc);
			writer.flush();
			writer.close();
			System.out.println("Succeed write xml file");
		} catch (Exception e) {
			System.out.println(e.toString());
			// TODO: handle exception
		}
		
	}
	
	// 创建文件上传路径
		public static void mkdir(String strDBName) {
									
			String strPath= strDBName + "\\"+TimeTools.getTimeDayPath();
		//	System.out.println(strPath);
			File fd = null;
			try {
				fd = new File(strPath);
				if (!fd.exists()) {
					fd.mkdirs();
				}				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fd = null;
			}
		}

}
