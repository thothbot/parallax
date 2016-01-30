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

package org.parallax3d.parallax.input;

public interface InputProcessor {

    /**
     * Called when key is pressed
     * @param keycode
     * @return
     */
    boolean keyDown (int keycode);

    /**
     * Called when key is released
     * @param keycode
     * @return
     */
    boolean keyUp (int keycode);

    /**
     * Called when a key was typed
     * @param character
     * @return
     */
    boolean keyTyped (char character);

    boolean touchDown (int screenX, int screenY, int pointer, int button);

    /**
     * Called when a finger was lifted or a mouse button was released.
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    boolean touchUp (int screenX, int screenY, int pointer, int button);

    /**
     * Called when a finger or the mouse was dragged.
     * @param screenX
     * @param screenY
     * @param pointer
     * @return
     */
    boolean touchDragged (int screenX, int screenY, int pointer);

    boolean mouseMoved (int screenX, int screenY);

    /**
     * Called when the mouse wheel was scrolled.
     * @param amount
     * @return
     */
    boolean scrolled (int amount);
}
