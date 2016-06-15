package server.service;

public class SystemService {
	public String restartDIServer() {
		try {
			System.out.println(System.getProperty("user.dir"));
			Runtime.getRuntime().exec("../jre/bin/java -cp * com.drillinginfo.rest.DIServer");
			//Runtime.getRuntime().exec("export CLASSPATH=$CLASSPATH:*");
			Runtime.getRuntime().exec("../jre/bin/java com.drillinginfo.rest.DIServer");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
