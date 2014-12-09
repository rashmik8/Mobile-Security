package com.cmusv.ninjabots.shared;

public class MobileApp {
	String appName;
	String timeStamp;
	String srcIP;
	String keyLog;

	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getSrcIP() {
		return srcIP;
	}
	public void setSrcIP(String srcIP) {
		this.srcIP = srcIP;
	}

	public String getKeyLog() {
		return keyLog;
	}
	public void setKeyLog(String keyLog) {
		this.keyLog = keyLog;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.appName.equals(((MobileApp)obj).appName);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return appName.hashCode();
	}
}
