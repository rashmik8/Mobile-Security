package com.cmusv.ninjabots;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class NinjabotscmuServlet extends HttpServlet {
	private static final Logger logger = 
			Logger.getLogger(NinjabotscmuServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String appName = req.getParameter("appName");
		String keyInput = req.getParameter("keyInput");
		String timeStamp = req.getParameter("timeStamp");
		String srcIP = req.getParameter("srcIP");
		
		StringBuilder receivedParams = new StringBuilder();

		receivedParams.append("\n" + "timeStamp : " + timeStamp + "\n");
		receivedParams.append("srcIP : " + srcIP + "\n");		

		if (appName != null)
			receivedParams.append("appName : " + appName);
		
		if (keyInput != null)
			receivedParams.append("keyInput : " + keyInput);
		
		logger.info(receivedParams.toString());
		
//		LogHandler lh = new LogHandler();
//		lh.parseLogs();
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Scotty <3 secret data");
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException {
		
		doGet(req, resp);
	}
}
