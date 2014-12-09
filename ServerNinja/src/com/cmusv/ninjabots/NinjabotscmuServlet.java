package com.cmusv.ninjabots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmusv.ninjabots.shared.MobileApp;

@SuppressWarnings("serial")
public class NinjabotscmuServlet extends HttpServlet {
	private static final Logger logger = 
			Logger.getLogger(NinjabotscmuServlet.class.getName());
	private List<MobileApp> appList = new ArrayList<MobileApp>();
	private Map<Double, String> timeStampKeyInput= new TreeMap<Double,String>();
		
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String appName = req.getParameter("appName");
		String keyInputs = req.getParameter("keyInput");
		String timeStamp = req.getParameter("timeStamp");
		String srcIP = req.getParameter("srcIP");
		StringBuilder receivedParams = new StringBuilder();
		resp.setContentType("text/plain");
		generateTimestampKeyInputsMap(keyInputs);
		MobileApp app = new MobileApp();
		app.setSrcIP(srcIP);
		app.setTimeStamp(timeStamp);
		
		if(keyInputs != null && keyInputs.isEmpty() == false)
		{
			app.setAppName("tempApp");
			processKeyLoggerRequest(app);
		}
		//TODO: from python script send something like firstApp when you send the AppName for the first time instead of sending blankAppName
		else
		{
			app.setAppName(appName);
			processNinjaClassiferAppRequest(app);
		}
		logger.info(receivedParams.toString());
	}

	private void processNinjaClassiferAppRequest(MobileApp app) {
		if(appList.size()==0)
		{
			appList.add(app);				
		}
		else
		{
			MobileApp latestApp = appList.get(appList.size()-1);
			if(latestApp.getAppName().equals("tempApp"))
			{
				int appIndex = appList.lastIndexOf(app);
				if(appIndex !=-1)
				{
					MobileApp existingApp = appList.get(appIndex);
					appList.remove(latestApp);
					existingApp.setKeyLog(existingApp.getKeyLog()+latestApp.getKeyLog());
					existingApp.setTimeStamp(latestApp.getTimeStamp());
				}
				else
				{
					latestApp.setAppName(app.getAppName());
				}
			}
			else
			{					
				int appIndex = appList.lastIndexOf(app);
				if(appIndex !=-1)
				{
					MobileApp existingApp = appList.get(appIndex);
					existingApp.setTimeStamp(latestApp.getTimeStamp());						
				}
				else
				{
					appList.add(app);
				}
			}
		}
	}

	private void processKeyLoggerRequest(MobileApp app) {
		if (appList.size() == 0)
		{
			Collection<String> allKeyInputs = timeStampKeyInput.values();
			Iterator<String> iAppKeys = allKeyInputs.iterator();
			StringBuilder keysBuilder = new StringBuilder();
			while(iAppKeys.hasNext())
			{
				keysBuilder.append(iAppKeys.next());
			}
			app.setKeyLog(keysBuilder.toString());
			appList.add(app);
		}
		else
		{
			Set<Double> allKeyTimestamps = timeStampKeyInput.keySet();
			Iterator<Double> iKeyTimestamps = allKeyTimestamps.iterator();
			while(iKeyTimestamps.hasNext())
			{
				double keyTimeStamp = iKeyTimestamps.next();
				for(int i = 1;i<appList.size();i++)
				{
					MobileApp oldApp = appList.get(i-1);
					MobileApp recentApp = appList.get(i);
					double oldAppTimestamp = Double.parseDouble(oldApp.getTimeStamp());
					double recentAppTimestamp = Double.parseDouble(recentApp.getTimeStamp());
					if(keyTimeStamp > oldAppTimestamp 
							&& keyTimeStamp < recentAppTimestamp)
					{
						recentApp.setKeyLog(recentApp.getKeyLog()+timeStampKeyInput.get(keyTimeStamp));
					}
				}
				MobileApp latestApp = appList.get(appList.size()-1);
				if(keyTimeStamp > Double.parseDouble(latestApp.getTimeStamp()))
				{
					if(latestApp.getAppName().equals("tempApp"))
					{
						latestApp.setKeyLog(latestApp.getKeyLog()
								+ timeStampKeyInput.get(keyTimeStamp));
					}
					else
					{
						app.setKeyLog(timeStampKeyInput.get(keyTimeStamp));
						appList.add(app);
					}
				}
			}
		}
	}

	private void generateTimestampKeyInputsMap(String keyInputs) {
		String keyInput[] = keyInputs.split("\n");
		for(int i = 0; i<keyInput.length;i++)
		{
			String[] timeKeyPair=keyInput[i].split(":");
			if( timeKeyPair.length == 2)
			{
				timeStampKeyInput.put(Double.parseDouble(timeKeyPair[0]),timeKeyPair[1]);
			}
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException {
		
		doGet(req, resp);
	}
}
