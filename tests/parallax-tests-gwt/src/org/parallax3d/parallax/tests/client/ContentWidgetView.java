/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution 
 * 3.0 Unported License. for more details.
 * 
 * You should have received a copy of the the Creative Commons Attribution 
 * 3.0 Unported License along with Parallax. 
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.parallax.tests.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.platforms.gwt.widgets.debugger.Debugger;

/**
 * A view of a {@link ContentWidget}.
 */
public class ContentWidgetView extends ResizeComposite
{

	interface ContentWidgetViewUiBinder extends UiBinder<Widget, ContentWidgetView>{
	}

	private static ContentWidgetViewUiBinder uiBinder = GWT.create(ContentWidgetViewUiBinder.class);

	/**
	 * Used to show description of an example
	 */
	@UiField
	Element descriptionField;

	/**
	 * Main panel where will be RenderingPanel located
	 */
	@UiField(provided = true)
	SimpleLayoutPanel examplePanel;

	@UiField(provided = true)
	SimpleLayoutPanel debuggerPanel;

	/**
	 * Used to show a name of an example
	 */
	@UiField(provided = true)
	SimpleLayoutPanel nameField;
	
	/**
	 * Toggle button to on/off animation. Just for fun
	 */
	@UiField(provided = true)
	public ToggleButton switchAnimation;
	
	@UiField(provided = true)
	public ToggleButton switchFullScreen;

	@UiField(provided = true)
	public ToggleButton switchEffectNone;
	
	@UiField(provided = true)
	public ToggleButton switchEffectAnaglyph;
	
	@UiField(provided = true)
	public ToggleButton switchEffectStereo;
	
	@UiField(provided = true)
	public ToggleButton switchEffectParallaxBarrier;
	
	@UiField(provided = true)
	public ToggleButton switchEffectOculusRift;
	
	List<ToggleButton> effectButtons;
		
	private Debugger debugger;
	
	private ClickHandler handler = new ClickHandler(){
        @Override
        public void onClick(ClickEvent event) {

        	for(ToggleButton button: effectButtons) {
        		if(event.getSource().equals(button)) {
                    button.setDown(true);
                } else {
                	button.setDown(false);
                }
        	}
        }
    };

	public ContentWidgetView()
	{
		this.examplePanel = new SimpleLayoutPanel();
		this.debuggerPanel = new SimpleLayoutPanel();
		this.nameField = new SimpleLayoutPanel();
		
		switchAnimation = new ToggleButton(new Image(WebApp.resources.switchAnimation()));
		switchFullScreen = new ToggleButton(new Image(WebApp.resources.switchFullscreen()));
		
		switchEffectNone = new ToggleButton(new Image(WebApp.resources.switchEffectNone()));
		switchEffectAnaglyph = new ToggleButton(new Image(WebApp.resources.switchEAnaglyph()));
		switchEffectStereo = new ToggleButton(new Image(WebApp.resources.switchEStereo()));
		switchEffectParallaxBarrier = new ToggleButton(new Image(WebApp.resources.switchEParallaxBarrier()));
		switchEffectOculusRift = new ToggleButton(new Image(WebApp.resources.switchEOculusRift()));
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.effectButtons = new ArrayList<ToggleButton>();
		for(ToggleButton button: Arrays.asList(switchEffectNone, switchEffectAnaglyph, switchEffectStereo, switchEffectParallaxBarrier, switchEffectOculusRift)) {
			button.addClickHandler(handler);
			this.effectButtons.add(button);
		}
	}
	
	public void setDescription(HTML html)
	{
		this.descriptionField.setInnerHTML(html.getHTML());
	}

	public void setName(String text)
	{
		this.nameField.getElement().setInnerText(text);
	}
	
	public void setEnableEffectSwitch(boolean enabled) {
		for(ToggleButton button: this.effectButtons)
			button.setEnabled(enabled);
	}

	public SimpleLayoutPanel getRenderingPanel()
	{
		return this.examplePanel;
	}

	public void setDebugger(GLRenderer renderer)
	{
		this.debugger = new Debugger(renderer.getInfo());
		this.debuggerPanel.setWidget(this.debugger);
	}

	public Debugger getDebugger() {
		return this.debugger;
	}
}
