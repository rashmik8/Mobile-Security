package com.cmusv.ninjabots;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.log.AppLogLine;
import com.google.appengine.api.log.LogQuery;
import com.google.appengine.api.log.LogService;
import com.google.appengine.api.log.LogServiceFactory;
import com.google.appengine.api.log.RequestLogs;

public class LogHandler {
	private static final Logger logger = 
			Logger.getLogger(LogHandler.class.getName());
	
	public void parseLogs() {
		LogQuery lq = LogQuery.Builder.withDefaults();
		lq.includeAppLogs(true);
//		logger.info("Inside");
		
		LogService ls = LogServiceFactory.getLogService();
		Iterator<RequestLogs> requestLogIterator = ls.fetch(lq).iterator();
		RequestLogs req = requestLogIterator.next();
		
		//Assuming that there is atleast one request that has been sent to the
		//server. This is acceptable because when we deploy code to the server
		//the first request is automatically made
		long latestEntryTime = req.getEndTimeUsec();

		while (requestLogIterator.hasNext()) {
			req = requestLogIterator.next();
//			logger.info("Inside while"+"latest"+latestEntryTime + " " +"second latest"+req.getEndTimeUsec());
			//Note - getEndTimeUsec returns time in micro secs since Epoch
			if (latestEntryTime - req.getEndTimeUsec() <= 10*1000*1000) {
//				logger.info("inside if");
				List<AppLogLine> appLogLines = req.getAppLogLines();
				for (AppLogLine line : appLogLines) {
//					logger.info("inside logline for");
					String message[] = line.getLogMessage().split("\n");
					if (message.length < 3)
						continue;
					for(String s: message)
					{
						logger.info("Output:"+s+"\t");
					}
				}
				
			}			
			
		}
//		String dataToSave = stringBuilder.toString();
		//logger.info(dataToSave);
	}
}