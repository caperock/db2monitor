package db2.guard.monitor;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import db2.guard.tools.SQLHelper;

public class DB2BPCfg {
	 
	 public int db2checkDate(Connection oCon,Document doc){
		 int iFlag=0;
		 try {
				ResultSet oRS = SQLHelper.getResultSet(oCon,"SELECT  VARCHAR_FORMAT(CURRENT date,'YYYYMMDD') as curDate FROM SYSIBM.SYSDUMMY1");
				while(oRS.next()){
					String strValue = oRS.getString("curDate");
					System.out.println("#XDate=" + strValue);
					if(Integer.valueOf(strValue).intValue() < 20140301){
						iFlag=1;
					}else{
						iFlag=-1;
					}
					
				}
		 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				iFlag=0;
			}
		 return iFlag;
				
	 }
	
	  public Document db2BPInfo(Connection oCon,Document doc){
		//"SELECT SUBSTR(A.BP_NAME,1,20) AS BP_NAME,    A.TOTAL_HIT_RATIO_PERCENT, A.DATA_HIT_RATIO_PERCENT,  A.DATA_LOGICAL_READS,A.DATA_PHYSICAL_READS , A.INDEX_HIT_RATIO_PERCENT , A.INDEX_LOGICAL_READS,A.INDEX_PHYSICAL_READS ,(B.BP_CUR_BUFFSZ * c.PAGESIZE )/1048576 as BP_SIZE_MB FROM SYSIBMADM.BP_HITRATIO A, SYSIBMADM.SNAPBP_PART B, syscat.bufferpools C  WHERE a.bp_name = b.bp_name and b.bp_name = c.bpname"
		  try {
			ResultSet oRS = SQLHelper.getResultSet(oCon,DB2V97ESE.strDB2_BP_HITRATIO);
			  //REG_VAR_NAME        REG_VAR_VALUE 
			 Element root = doc.getRootElement();
			  Element elemDB2SET = root.addElement("bpInfo");
			  elemDB2SET.addElement("title").addText("缓冲池的监控信息");
			  int i=0,j=0,k=0;
			  float x=0.0f,y=0.0f,z=0.0f;
			while(oRS.next()){
			  Element elemValue = elemDB2SET.addElement("bp");
			  
			  elemValue.addElement("bpID").addText(Long.toString(oRS.getLong("BUFFERPOOLID")));
			  elemValue.addElement("bpName").addText(oRS.getString("BP_NAME") );
			  elemValue.addElement("bpSizeMB").addText(Long.toString(oRS.getLong("BP_SIZE_MB")));			  
			  elemValue.addElement("bpRatio").addText(Float.toString(oRS.getFloat("TOTAL_HIT_RATIO_PERCENT")));

			  elemValue.addElement("bpIdxRatio").addText(Float.toString(oRS.getFloat("INDEX_HIT_RATIO_PERCENT")));
			  elemValue.addElement("bpIdxLogRead").addText(Long.toString(oRS.getLong("INDEX_LOGICAL_READS")));
			  elemValue.addElement("bpIdxPhyRead").addText(Long.toString(oRS.getLong("INDEX_PHYSICAL_READS")));
			  
			  elemValue.addElement("bpDatRatio").addText(Float.toString(oRS.getFloat("DATA_HIT_RATIO_PERCENT")));
			  elemValue.addElement("bpDatLogRead").addText(Long.toString(oRS.getLong("DATA_LOGICAL_READS")));
			  elemValue.addElement("bpDatPhyRead").addText(Long.toString(oRS.getLong("DATA_PHYSICAL_READS")));			  
			  elemValue.addElement("bpTcbNum").addText(Long.toString(oRS.getLong("BP_TBSP_USE_COUNT")));
			  
			  //缓冲池的平均命中率、索引平均命中率、数据的平均命中率
			 if(oRS.getFloat("TOTAL_HIT_RATIO_PERCENT") >0.0f){
				 i++;
				 x += oRS.getFloat("TOTAL_HIT_RATIO_PERCENT");
				 
			 }
			 
			 if(oRS.getFloat("INDEX_HIT_RATIO_PERCENT") >0.0f){
				 j++;
				 y += oRS.getFloat("INDEX_HIT_RATIO_PERCENT");
				 
			 }
			 
			 if(oRS.getFloat("DATA_HIT_RATIO_PERCENT") >0.0f){
				 k++;
				 z += oRS.getFloat("DATA_HIT_RATIO_PERCENT");
				 
			 }			 
			  
			}
			oRS.close();
			
			 float fBPAvg=0.0f ,fIDXAvg=0.0f ,fDatAvg=0.0f ;
			if(i>0){				
				fBPAvg =x/i;
			}
			
			if(j>0){
				fIDXAvg=y/j;
			}
			
			if(k >0){
				fDatAvg=z/k;
			}
		   
		   
		   DecimalFormat format = new DecimalFormat("0.00");

			  Node oValue= doc.selectSingleNode("/report/linkInfo/dbInfo/status");
			  int db2Value =Integer.parseInt(oValue.getText());
			  
			  if(fBPAvg < 0.75){
				  db2Value -=15;
			  }
			  
			  if(fBPAvg>=.75 && fBPAvg<.85){
				  db2Value -=10;
			  }
			  
			  if(fBPAvg>=.85 && fBPAvg<.95){
				  db2Value -=5;
			  }
			  
			  oValue.setText(Integer.toString(db2Value));     
             
                //  doc.selectSingleNode("/report/linkInfo/dbInfo/desc").getText();
             String strDesc ="缓冲池平均命中率" + format.format(fBPAvg) + "%,其中索引缓冲池命中率" +format.format(fIDXAvg) + "%,数据缓冲池命中率" +format.format(fDatAvg) + "%.\n" 
	                    + "缓冲池的命中率不得低于95%，否则需要增加缓冲池容量,特别是要提高索引缓冲池与高频读写缓冲池的命中率.";
            		
             Node oNode = doc.selectSingleNode("/report/linkInfo/dbInfo/lkDesc");
             oNode.getParent().addElement("bpDesc").addText(strDesc);
             oNode.getParent().addElement("bpValue").addText(format.format(fBPAvg));
             	
		  	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  

		  
		  return doc;
	  }

}
