package db2.guard.monitor;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import db2.guard.tools.XMLOpt;

public class SummaryReport {
	
public void DailyMonthInfo(String strPath,Document doc){
		
		Document oDailyReport  = DocumentFactory.getInstance()
				.createDocument();
				
		// 创建ProcessingInstruction
		Map<String, String> inMap = new HashMap<String, String>();
		inMap.put("type", "text/xsl");
		inMap.put("href", "simple.xsl");
		oDailyReport.addProcessingInstruction("xml-stylesheet", inMap);
		
		Element root = oDailyReport.addElement("report");
		Element elink = root.addElement("linkInfo"); 
		Node oNode = doc.selectSingleNode("/report/linkInfo/dbInfo");
				
		elink.add((Element)oNode.clone());
		
		XMLOpt oXML = new XMLOpt("dailyReport");
		oXML.dumpXML(oDailyReport);
		
	}
	
    private Document appendReportInfo(Document oScanDoc, Document oSumReport){
    	
    	Element root = oSumReport.getRootElement();
		Element elink = root.addElement("linkInfo"); 
		Node oNode = oScanDoc.selectSingleNode("/report/linkInfo/dbInfo");
			
		elink.add((Element)oNode.clone());
		//elink.appendAttributes(arg0)
    	
    	return oSumReport;
    }
    
    private Document appendMonthInfo(Document oScanDoc, Document oSumReport){
    	Element root = oSumReport.getRootElement();
		Element elink = root.addElement("linkInfo"); 
		
		Node oNode = oScanDoc.selectSingleNode("/report/linkInfo/dbInfo");
		
		elink.add((Element)oNode.clone());
		
		//del lkDesc
		String strListNodes[]={"/report/linkInfo/dbInfo/lkDesc",
				               "/report/linkInfo/dbInfo/tbDesc",
				               "/report/linkInfo/dbInfo/dlDesc",
				               "/report/linkInfo/dbInfo/erDesc"};
		int i=0;
		
		Node delNode = null;
		while(i< strListNodes.length){
			delNode=	elink.selectSingleNode(strListNodes[i]);
			if(delNode != null){
				elink.remove(delNode);
			}
			i++;
		}
				
    	return oSumReport;
    }
    
    public void MonthReportInfo(Document doc){
	String strPathfile ="monthReport.xml";
		
		File fd = null;
		try {
			fd = new File(strPathfile);
			if (fd.exists()) {
				System.out.println("Append File");
				SAXReader reader = new SAXReader();
				Document oSumReport = reader.read(strPathfile);
				Document oAppendReport = appendMonthInfo(doc,oSumReport);
				
				XMLOpt oXML = new XMLOpt(strPathfile);
				oXML.dumpXML(oAppendReport);
			}else{
				System.out.println("Create MonthReport File");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	public void DailyReportInfo(Document doc){
		
		String strPathfile ="dailyReport.xml";
		
		File fd = null;
		try {
			fd = new File(strPathfile);
			if (fd.exists()) {
				System.out.println("Append File");
				SAXReader reader = new SAXReader();
				Document oSumReport = reader.read(strPathfile);
				Document oAppendReport = appendReportInfo(doc,oSumReport);
				
				XMLOpt oXML = new XMLOpt(strPathfile);
				oXML.dumpXML(oAppendReport);
			}else{
				System.out.println("Create DailyReport File");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		/*
		
		Document oDailyReport  = DocumentFactory.getInstance()
				.createDocument();
				
		// 创建ProcessingInstruction
		Map<String, String> inMap = new HashMap<String, String>();
		inMap.put("type", "text/xsl");
		inMap.put("href", "simple.xsl");
		oDailyReport.addProcessingInstruction("xml-stylesheet", inMap);
		
		Element root = oDailyReport.addElement("report");
		Element elink = root.addElement("linkInfo"); 
		Node oNode = doc.selectSingleNode("/report/linkInfo/dbInfo");
				
		elink.add((Element)oNode.clone());
		
		XMLOpt oXML = new XMLOpt("dailyReport");
		oXML.dumpXML(oDailyReport);
		
		*/
	}
	
	private void createReport(int iType,String strPath){
		
	}
}
