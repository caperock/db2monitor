package db2.guard.tools;

public class DatabaseBean {
	public String getIPAddr() {
		return IPAddr;
	}
	public void setIPAddr(String iPAddr) {
		IPAddr = iPAddr;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getDBName() {
		return DBName;
	}
	public void setDBName(String dBName) {
		DBName = dBName;
	}
	String IPAddr;
	public String getPort() {
		return Port;
	}
	public void setPort(String port) {
		Port = port;
	}
	String Port;
	String User;
	String Password;
	String DBName;
	public String getCurTime() {
		return CurTime;
	}
	public void setCurTime(String curTime) {
		CurTime = curTime;
	}
	public float getElapsedTime() {
		return ElapsedTime;
	}
	public void setElapsedTime(float elapsedTime) {
		ElapsedTime = elapsedTime;
	}
	String CurTime;
	float ElapsedTime;
	
	public String getWebPath() {
		return WebPath;
	}
	public void setWebPath(String webPath) {
		WebPath =  webPath;		
	}
	public String getLogPath() {
		return LogPath;
	}
	public void setLogPath(String logPath) {
		LogPath = logPath;
	}
	String WebPath;
	String LogPath;
	
	public void toPrint(){
		System.out.println("IPAddr=" + IPAddr  + ";Port=" + Port +";User=" + User + ";Password=" + Password + ";DBName=" + DBName);
	}
	
}
