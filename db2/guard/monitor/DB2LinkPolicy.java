package db2.guard.monitor;

import java.sql.ResultSet;
import java.sql.SQLException;

import db2.guard.tools.DatabaseBean;

public class DB2LinkPolicy {
	
	
	public InfoStatus analyDB2Conn( DatabaseBean oDB){
		InfoStatus oIS = new InfoStatus();
		//Handle DB link message
		
		if(oDB.getElapsedTime() >  15){
			oIS.setiStatus(0);
			oIS.setStrDesc("��ǰ�������ݿ���Ҫ" + oDB.getElapsedTime()+"��,��������ʱ���쳣(����������ʱ�������3����).\n");
		}
		
		if(oDB.getElapsedTime() > 10 && oDB.getElapsedTime()  <= 15){
			oIS.setiStatus(60);
			oIS.setStrDesc("��ǰ�������ݿ���Ҫ" + oDB.getElapsedTime()+"��,��������ʱ��ܳ�(����������ʱ�������3����). \n");
		}
		
		if(oDB.getElapsedTime() > 4 && oDB.getElapsedTime()  <= 10){
			oIS.setiStatus(85);
			oIS.setStrDesc("��ǰ�������ݿ���Ҫ" + oDB.getElapsedTime()+"��,��������ʱ��ϳ�(����������ʱ�������3����).\n");
		}
		
		if(oDB.getElapsedTime() > 2 && oDB.getElapsedTime()  <= 4){
			oIS.setiStatus(95);
			oIS.setStrDesc("��ǰ�������ݿ���Ҫ" + oDB.getElapsedTime()+"��,��������ʱ������(��ѵ�����ʱ�������1����). \n");		
		}
		
		if(oDB.getElapsedTime() <= 2){
			oIS.setiStatus(100);
			oIS.setStrDesc("��ǰ�������ݿ���Ҫ" + oDB.getElapsedTime()+"��,��������ʱ�����(��ѵ�����ʱ�������1����). \n");
			
		}
		
		return oIS;
	}
	
	public InfoStatus analyIntance(ResultSet oRS) {
		InfoStatus oIS = new InfoStatus();
		//Handle Instance list
		try {
			if(oRS==null ) {
				oIS.setiStatus(0);
				oIS.setStrDesc("���ݿ�����δ�ɹ����������,���������ݿ�");
				return oIS;
			}
			//oRS.last();  
			String strInstLevel=oRS.getString("SERVICE_LEVEL");

			if(strInstLevel.indexOf("9.5") >0  || strInstLevel.indexOf("9.1") >0 ){
				oIS.setiStatus(3);
				oIS.setStrDesc("��ǰ�������ݿ�汾̫�ͣ�����������DB2 V10.1/10.5�����ϰ汾��");	
			}
			
						if(strInstLevel.indexOf("9.7") >0 ){
				oIS.setiStatus(4);
				oIS.setStrDesc("��ǰ�������ݿ�汾�ϵͣ�����������DB2 V10.1/10.5�����ϰ汾��");
			}
					
			if(strInstLevel.indexOf("10.1") >0 || strInstLevel.indexOf("10.5") >0 ){
				oIS.setiStatus(5);
				oIS.setStrDesc("��ǰ�������ݿ�汾���")	;
			}
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			//oIS.setiStatus(0);			
			e.printStackTrace();
			//return oIS;
		}
		
		return oIS;
	}

}
