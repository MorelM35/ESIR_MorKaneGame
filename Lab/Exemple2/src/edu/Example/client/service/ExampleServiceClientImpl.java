package edu.Example.client.service;

import edu.Example.client.gui.MainGUI;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ExampleServiceClientImpl implements ExampleServiceClientInt {
private ExampleServiceAsync service;
private MainGUI main;
	
	
	public ExampleServiceClientImpl(String url){
		System.out.println("url : "+url);
		this.service = GWT.create(ExampleService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget) this.service;
		endPoint.setServiceEntryPoint(url);
		this.main = new MainGUI(this);
	}
	
	public MainGUI getMain() {
		return main;
	}

	@Override
	public void sayHello(String name) {
		this.service.sayHello(name, new DefaultCallBack());
	}
	
	private class DefaultCallBack implements AsyncCallback{

		@Override
		public void onFailure(Throwable caught) {
			System.err.println("An Error has occured \n *** "+caught.getMessage()+" ***");
		}

		@Override
		public void onSuccess(Object result) {
			if(result instanceof String){
				main.updateLabel((String)result);
			}
		}
		
		
	}

}
