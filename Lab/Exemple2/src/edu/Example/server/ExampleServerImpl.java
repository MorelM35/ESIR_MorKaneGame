package edu.Example.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.Example.client.service.ExampleService;

public class ExampleServerImpl extends RemoteServiceServlet implements ExampleService{

	@Override
	public String sayHello(String name) {
		System.out.println("Serveur:HeyHello");
		return "Hello "+name;
	}

}
