package db2.guard.monitor;

public class InfoStatus {
	
	
	public String getStrStatus() {
		if(strStatus==null) return "";
		return strStatus;
	}
	public void setStrStatus(String strStatus) {
	
		this.strStatus = strStatus;
	}
	public String getStrDesc() {
		if(strDesc==null) return "";
		return strDesc;
	}
	public void setStrDesc(String strDesc) {
		this.strDesc =strDesc;
	}
	public int getiStatus() {
		return iStatus;
	}
	public void setiStatus(int iStatus) {
		//if(this.iStatus > iStatus) {
			this.iStatus = iStatus;
		//}
	}
	private String strStatus;
	private String strDesc="";
	private int iStatus=100;
	

}
