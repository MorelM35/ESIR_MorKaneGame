package edu.Example.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import edu.Example.client.service.ExampleServiceClientImpl;

public class Exemple2 implements EntryPoint {

	public void onModuleLoad() {
		ExampleServiceClientImpl client = new ExampleServiceClientImpl(GWT.getModuleBaseURL()+"exampleservice");
		RootPanel.get().add(client.getMain());
	}

}
