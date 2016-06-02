package com.deh.b2r.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class ErrorLogger{
	private static Logger logger;
	private final String logName = "Error.log";
	private final String logLocation = "./resources";

	public ErrorLogger() {
		File dir = new File(logLocation);
		dir.mkdir();
		File log = new File(logLocation + "/" + logName);
		File log4j = new File(logLocation + "/log4j.properties");
		try {
			log.createNewFile();
			if(!log4j.exists()){
				log4j.createNewFile();
				writeLogProperties(log4j);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger = Logger.getLogger(Updater.class);
		String log4jConfigFile = System.getProperty("user.dir") + File.separator + "resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
	}
	
	/**
	 * 
	 * @param log4j
	 * @throws IOException
	 */
	private static void writeLogProperties(File log4j) throws IOException {
		FileWriter log4jWriter = new FileWriter(log4j);
		BufferedWriter writer = new BufferedWriter(log4jWriter);
		
		//Root logger option
		writer.write("log4j.rootLogger=INFO, file, stdout\n\n");
		
		//Direct log messages to a log file
		writer.write("log4j.appender.file=org.apache.log4j.RollingFileAppender\n");
		writer.write("log4j.appender.file.File=resources/Error.log\n");
		writer.write("log4j.appender.file.MaxFileSize=10MB\n");
		writer.write("log4j.appender.file.MaxBackupIndex=10\n");
		writer.write("log4j.appender.file.layout=org.apache.log4j.PatternLayout\n");
		writer.write("log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n\n\n");
		
		//Direct log messages to stdout
		writer.write("log4j.appender.stdout=org.apache.log4j.ConsoleAppender\n");
		writer.write("log4j.appender.stdout.Target=System.out\n");
		writer.write("log4j.appender.stdout.layout=org.apache.log4j.PatternLayout\n");
		writer.write("log4j.appender.stdout.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p: %m%n\n");
		writer.close();
	}

	public void error(String string) {
		logger.error(string);
	}

	public void warn(String string) {
		logger.error(string);
		
	}

}
