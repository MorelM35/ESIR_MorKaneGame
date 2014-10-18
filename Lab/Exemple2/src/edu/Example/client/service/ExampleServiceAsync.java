package edu.Example.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExampleServiceAsync {
	void sayHello(String name,AsyncCallback callback);
}
