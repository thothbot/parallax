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

package thothbot.parallax.core.client.context;

import com.google.gwt.event.dom.client.DragDropEventBase;

/**
 * Represents a native webgl context lost event.
 *
 * @author hao1300@gmail.com
 */
public class Context3dLostEvent extends DragDropEventBase<Context3dLostHandler> 
{

  /**
   * Event type for webgl context lost events. Represents the meta-data associated with
   * this event.
   */
  private static final Type<Context3dLostHandler> TYPE = new Type<Context3dLostHandler>(
  		"webglcontextlost", new Context3dLostEvent());

  public static Type<Context3dLostHandler> getType() 
  {
    return TYPE;
  }

  protected Context3dLostEvent() 
  {
  }

  @Override
  public final Type<Context3dLostHandler> getAssociatedType() 
  {
    return TYPE;
  }

  @Override
  protected void dispatch(Context3dLostHandler handler) 
  {
    handler.onContextLost(this);
  }

}
