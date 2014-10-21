package edu.Example.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.Example.client.service.ExampleService;

public class ExampleServerImpl extends RemoteServiceServlet implements ExampleService{
	String name;
	@Override
	public String sayHello() {
		return "Hello "+name;
	}

	@Override
	public void HelloWho(String name) {
		this.name=name;		
	}

}
