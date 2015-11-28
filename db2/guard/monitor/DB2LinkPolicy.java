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
			oIS.setStrDesc("当前连接数据库需要" + oDB.getElapsedTime()+"秒,本次连接时间异常(正常的连接时间控制在3秒内).\n");
		}
		
		if(oDB.getElapsedTime() > 10 && oDB.getElapsedTime()  <= 15){
			oIS.setiStatus(60);
			oIS.setStrDesc("当前连接数据库需要" + oDB.getElapsedTime()+"秒,本次连接时间很长(正常的连接时间控制在3秒内). \n");
		}
		
		if(oDB.getElapsedTime() > 4 && oDB.getElapsedTime()  <= 10){
			oIS.setiStatus(85);
			oIS.setStrDesc("当前连接数据库需要" + oDB.getElapsedTime()+"秒,本次连接时间较长(正常的连接时间控制在3秒内).\n");
		}
		
		if(oDB.getElapsedTime() > 2 && oDB.getElapsedTime()  <= 4){
			oIS.setiStatus(95);
			oIS.setStrDesc("当前连接数据库需要" + oDB.getElapsedTime()+"秒,本次连接时间正常(最佳的连接时间控制在1秒内). \n");		
		}
		
		if(oDB.getElapsedTime() <= 2){
			oIS.setiStatus(100);
			oIS.setStrDesc("当前连接数据库需要" + oDB.getElapsedTime()+"秒,本次连接时间最佳(最佳的连接时间控制在1秒内). \n");
			
		}
		
		return oIS;
	}
	
	public InfoStatus analyIntance(ResultSet oRS) {
		InfoStatus oIS = new InfoStatus();
		//Handle Instance list
		try {
			if(oRS==null ) {
				oIS.setiStatus(0);
				oIS.setStrDesc("数据库连接未成功，检查网络,主机与数据库");
				return oIS;
			}
			//oRS.last();  
			String strInstLevel=oRS.getString("SERVICE_LEVEL");

			if(strInstLevel.indexOf("9.5") >0  || strInstLevel.indexOf("9.1") >0 ){
				oIS.setiStatus(3);
				oIS.setStrDesc("当前配置数据库版本太低（建议升级到DB2 V10.1/10.5及以上版本）");	
			}
			
						if(strInstLevel.indexOf("9.7") >0 ){
				oIS.setiStatus(4);
				oIS.setStrDesc("当前配置数据库版本较低（建议升级到DB2 V10.1/10.5及以上版本）");
			}
					
			if(strInstLevel.indexOf("10.1") >0 || strInstLevel.indexOf("10.5") >0 ){
				oIS.setiStatus(5);
				oIS.setStrDesc("当前配置数据库版本最佳")	;
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
