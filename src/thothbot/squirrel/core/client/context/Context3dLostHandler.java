/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the code written by Hao Nguyen.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.client.context;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link Context3dLostEvent} events.
 *
 * @author hao1300@gmail.com
 */
public interface Context3dLostHandler extends EventHandler 
{

  /**
   * Called when a {@link Context3dLostEvent} is fired.
   * 
   * @param event the {@link Context3dLostEvent} that was fired
   */
  void onContextLost(Context3dLostEvent event);
}