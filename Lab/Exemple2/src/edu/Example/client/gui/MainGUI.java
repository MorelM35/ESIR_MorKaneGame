package edu.Example.client.gui;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.Example.client.service.ExampleServiceClientImpl;

public class MainGUI extends Composite {
	private VerticalPanel vPanel = new VerticalPanel();
	private TextBox txt ;
	private Label label;
	
	private ExampleServiceClientImpl serviceImpl;
	
	public MainGUI(ExampleServiceClientImpl service){
		this.serviceImpl=service;
		
		initWidget(vPanel);
		
		this.txt= new TextBox();
		this.vPanel.add(txt);
		
		Button btn = new Button("SayHello");
		this.vPanel.add(btn);
		btn.addClickHandler(new BtnClickHandler());
		
		label = new Label();
		label.setText("Wait ...");
		this.vPanel.add(label);
		
	}
	
	public void updateLabel (String txt){
		this.label.setText(txt);
	}
	
	private class BtnClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			serviceImpl.sayHello(txt.getText());
		}
		
	}
}
