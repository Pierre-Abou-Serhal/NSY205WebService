import javax.xml.ws.Endpoint;

import utils.ConfigManager;
import ws.BankService;

public class JaxWSServer {
	public static void main(String[] args) {
		// Retrieve the singleton
        ConfigManager config = ConfigManager.getInstance();
		Endpoint.publish(config.getWebserviceUrl(), new BankService());
		System.out.println("Web service deployed on: " + config.getWebserviceUrl());
	}
}