package edu.Example.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExampleServiceAsync {
	void HelloWho(String name,AsyncCallback callback);
	void sayHello(AsyncCallback callback);
}
