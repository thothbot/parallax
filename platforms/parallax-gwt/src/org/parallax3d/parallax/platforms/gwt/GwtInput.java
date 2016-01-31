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

package org.parallax3d.parallax.platforms.gwt;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import org.parallax3d.parallax.Input;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.input.*;
import org.parallax3d.parallax.system.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GwtInput implements Input {

    static final int MAX_TOUCHES = 20;

    List<Integer> pressedButtons = new ArrayList<>();

    private Map<Integer, Integer> touchMap = new HashMap<>();

    boolean justTouched = false;
    boolean keyJustPressed = false;
    boolean[] justPressedKeys = new boolean[256];

    int pressedKeyCount = 0;
    boolean[] pressedKeys = new boolean[256];

    private boolean[] touched = new boolean[MAX_TOUCHES];

    private int[] touchX = new int[MAX_TOUCHES];
    private int[] touchY = new int[MAX_TOUCHES];
    private int[] deltaX = new int[MAX_TOUCHES];
    private int[] deltaY = new int[MAX_TOUCHES];

    long currentEventTimeStamp;
    final CanvasElement canvas;
    boolean hasFocus = true;

    // Handlers
    KeyDownHandler keyDownHandler;
    KeyTypedHandler keyTypedHandler;
    KeyUpHandler keyUpHandler;
    ScrolledHandler scrolledHandler;
    TouchDownHandler touchDownHandler;
    TouchDraggedHandler touchDraggedHandler;
    TouchUpHandler touchUpHandler;

    public GwtInput (CanvasElement canvas) {
        this.canvas = canvas;
        hookEvents();
    }

    void reset () {
        justTouched = false;
        if (keyJustPressed) {
            keyJustPressed = false;
            for (int i = 0; i < justPressedKeys.length; i++) {
                justPressedKeys[i] = false;
            }
        }
    }

    @Override
    public void setInputHandler (InputHandler handler) {
        if(handler instanceof KeyDownHandler)
            keyDownHandler = (KeyDownHandler) handler;
        else if(handler instanceof KeyTypedHandler)
            keyTypedHandler = (KeyTypedHandler) handler;
        else if(handler instanceof KeyUpHandler)
            keyUpHandler = (KeyUpHandler) handler;
        else if(handler instanceof ScrolledHandler)
            scrolledHandler = (ScrolledHandler) handler;
        else if(handler instanceof TouchDownHandler)
            touchDownHandler = (TouchDownHandler) handler;
        else if(handler instanceof TouchDraggedHandler)
            touchDraggedHandler = (TouchDraggedHandler) handler;
        else if(handler instanceof TouchUpHandler)
            touchUpHandler = (TouchUpHandler) handler;
        else
            Log.error("Unknown handler: ", handler.getClass());
    }

    @Override
    public float getAccelerometerX () {
        return 0;
    }

    @Override
    public float getAccelerometerY () {
        return 0;
    }

    @Override
    public float getAccelerometerZ () {
        return 0;
    }

    @Override
    public int getX () {
        return touchX[0];
    }

    @Override
    public int getX (int pointer) {
        return touchX[pointer];
    }

    @Override
    public int getDeltaX () {
        return deltaX[0];
    }

    @Override
    public int getDeltaX (int pointer) {
        return deltaX[pointer];
    }

    @Override
    public int getY () {
        return touchY[0];
    }

    @Override
    public int getY (int pointer) {
        return touchY[pointer];
    }

    @Override
    public int getDeltaY () {
        return deltaY[0];
    }

    @Override
    public int getDeltaY (int pointer) {
        return deltaY[pointer];
    }

    @Override
    public boolean isTouched () {
        for (int pointer = 0; pointer < MAX_TOUCHES; pointer++) {
            if (touched[pointer]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isButtonPressed (int button) {
        return button == Buttons.LEFT && touched[0];
    }

    @Override
    public boolean isKeyPressed (int key) {
        if (key < 0 || key > 255) {
            return false;
        }
        return pressedKeys[key];
    }

    @Override
    public Orientation getNativeOrientation () {
        return Orientation.Landscape;
    }

    @Override
    public boolean isPeripheralAvailable (Peripheral peripheral) {
        if (peripheral == Peripheral.Accelerometer) return false;
        if (peripheral == Peripheral.Compass) return false;
        if (peripheral == Peripheral.HardwareKeyboard) return true;
        if (peripheral == Peripheral.MultitouchScreen) return isTouchScreen();
        if (peripheral == Peripheral.OnscreenKeyboard) return false;
        if (peripheral == Peripheral.Vibrator) return false;
        return false;
    }

    @Override
    public boolean isCursorCatched () {
        return isCursorCatchedJSNI();
    }

    /** Kindly borrowed from PlayN. **/
    protected int getRelativeX (NativeEvent e, CanvasElement target) {
        float xScaleRatio = target.getWidth() * 1f / target.getClientWidth(); // Correct for canvas CSS scaling
        return Math.round(xScaleRatio
                * (e.getClientX() - target.getAbsoluteLeft() + target.getScrollLeft() + target.getOwnerDocument().getScrollLeft()));
    }

    /** Kindly borrowed from PlayN. **/
    protected int getRelativeY (NativeEvent e, CanvasElement target) {
        float yScaleRatio = target.getHeight() * 1f / target.getClientHeight(); // Correct for canvas CSS scaling
        return Math.round(yScaleRatio
                * (e.getClientY() - target.getAbsoluteTop() + target.getScrollTop() + target.getOwnerDocument().getScrollTop()));
    }

    protected int getRelativeX (Touch touch, CanvasElement target) {
        float xScaleRatio = target.getWidth() * 1f / target.getClientWidth(); // Correct for canvas CSS scaling
        return Math.round(xScaleRatio * touch.getRelativeX(target));
    }

    protected int getRelativeY (Touch touch, CanvasElement target) {
        float yScaleRatio = target.getHeight() * 1f / target.getClientHeight(); // Correct for canvas CSS scaling
        return Math.round(yScaleRatio * touch.getRelativeY(target));
    }

    private void hookEvents () {
        addEventListener(canvas, "mousedown", this, true);
        addEventListener(Document.get(), "mousedown", this, true);
        addEventListener(canvas, "mouseup", this, true);
        addEventListener(Document.get(), "mouseup", this, true);
        addEventListener(canvas, "mousemove", this, true);
        addEventListener(Document.get(), "mousemove", this, true);
        addEventListener(canvas, getMouseWheelEvent(), this, true);
        addEventListener(Document.get(), "keydown", this, false);
        addEventListener(Document.get(), "keyup", this, false);
        addEventListener(Document.get(), "keypress", this, false);

        addEventListener(canvas, "touchstart", this, true);
        addEventListener(canvas, "touchmove", this, true);
        addEventListener(canvas, "touchcancel", this, true);
        addEventListener(canvas, "touchend", this, true);

    }

    private int getButton (int button) {
        if (button == NativeEvent.BUTTON_LEFT) return Buttons.LEFT;
        if (button == NativeEvent.BUTTON_RIGHT) return Buttons.RIGHT;
        if (button == NativeEvent.BUTTON_MIDDLE) return Buttons.MIDDLE;
        return Buttons.LEFT;
    }

    private void handleEvent (NativeEvent e) {
        if (e.getType().equals("mousedown")) {
            if (!e.getEventTarget().equals(canvas) || touched[0]) {
                float mouseX = getRelativeX(e, canvas);
                float mouseY = getRelativeY(e, canvas);
                if (mouseX < 0 || mouseX > canvas.getWidth() || mouseY < 0 || mouseY > canvas.getHeight()) {
                    hasFocus = false;
                }
                return;
            }
            hasFocus = true;
            this.justTouched = true;
            this.touched[0] = true;
            this.pressedButtons.add(getButton(e.getButton()));
            this.deltaX[0] = 0;
            this.deltaY[0] = 0;
            if (isCursorCatched()) {
                this.touchX[0] += getMovementXJSNI(e);
                this.touchY[0] += getMovementYJSNI(e);
            } else {
                this.touchX[0] = getRelativeX(e, canvas);
                this.touchY[0] = getRelativeY(e, canvas);
            }
            this.currentEventTimeStamp = Duration.nanoTime();
            if (touchDownHandler != null)
                touchDownHandler.onTouchDown(touchX[0], touchY[0], 0, getButton(e.getButton()));
        }

        if (e.getType().equals("mousemove")) {
            if (isCursorCatched()) {
                this.deltaX[0] = (int)getMovementXJSNI(e);
                this.deltaY[0] = (int)getMovementYJSNI(e);
                this.touchX[0] += getMovementXJSNI(e);
                this.touchY[0] += getMovementYJSNI(e);
            } else {
                this.deltaX[0] = getRelativeX(e, canvas) - touchX[0];
                this.deltaY[0] = getRelativeY(e, canvas) - touchY[0];
                this.touchX[0] = getRelativeX(e, canvas);
                this.touchY[0] = getRelativeY(e, canvas);
            }
            this.currentEventTimeStamp = Duration.nanoTime();

            if (touchDraggedHandler != null && touched[0])
                touchDraggedHandler.onTouchDragged(touchX[0], touchY[0], 0);

        }

        if (e.getType().equals("mouseup")) {
            if (!touched[0]) return;
            this.pressedButtons.remove(getButton(e.getButton()));
            this.touched[0] = pressedButtons.size() > 0;
            if (isCursorCatched()) {
                this.deltaX[0] = (int)getMovementXJSNI(e);
                this.deltaY[0] = (int)getMovementYJSNI(e);
                this.touchX[0] += getMovementXJSNI(e);
                this.touchY[0] += getMovementYJSNI(e);
            } else {
                this.deltaX[0] = getRelativeX(e, canvas) - touchX[0];
                this.deltaY[0] = getRelativeY(e, canvas) - touchY[0];
                this.touchX[0] = getRelativeX(e, canvas);
                this.touchY[0] = getRelativeY(e, canvas);
            }
            this.currentEventTimeStamp = Duration.nanoTime();
            this.touched[0] = false;
            if (touchUpHandler != null) touchUpHandler.onTouchUp(touchX[0], touchY[0], 0, getButton(e.getButton()));
        }
        if (e.getType().equals(getMouseWheelEvent())) {
            if (scrolledHandler != null) {
                scrolledHandler.onScrolled((int)getMouseWheelVelocity(e));
            }
            this.currentEventTimeStamp = Duration.nanoTime();
            e.preventDefault();
        }
        if (e.getType().equals("keydown") && hasFocus) {
            // System.out.println("keydown");
            int code = e.getKeyCode();
            if (code == 67) {
                e.preventDefault();
                if(keyDownHandler != null) keyDownHandler.onKeyDown(code);
                if(keyTypedHandler != null) keyTypedHandler.onKeyTyped('\b');
            } else {
                if (!pressedKeys[code]) {
                    pressedKeyCount++;
                    pressedKeys[code] = true;
                    keyJustPressed = true;
                    justPressedKeys[code] = true;
                    if (keyDownHandler != null)
                        keyDownHandler.onKeyDown(code);
                }
            }
        }

        if (e.getType().equals("keypress") && hasFocus) {
            // System.out.println("keypress");
            char c = (char)e.getCharCode();
            if (keyTypedHandler != null) keyTypedHandler.onKeyTyped(c);
        }

        if (e.getType().equals("keyup") && hasFocus) {
            // System.out.println("keyup");
            int code = e.getKeyCode();
            if (pressedKeys[code]) {
                pressedKeyCount--;
                pressedKeys[code] = false;
            }

            if (keyUpHandler != null) keyUpHandler.onKeyUp(code);
        }

        if (e.getType().equals("touchstart")) {
            this.justTouched = true;
            JsArray<Touch> touches = e.getChangedTouches();
            for (int i = 0, j = touches.length(); i < j; i++) {
                Touch touch = touches.get(i);
                int real = touch.getIdentifier();
                int touchId;
                touchMap.put(real, touchId = getAvailablePointer());
                touched[touchId] = true;
                touchX[touchId] = getRelativeX(touch, canvas);
                touchY[touchId] = getRelativeY(touch, canvas);
                deltaX[touchId] = 0;
                deltaY[touchId] = 0;

                if (touchDownHandler != null)
                    touchDownHandler.onTouchDown(touchX[touchId], touchY[touchId], touchId, Buttons.LEFT);
            }
            this.currentEventTimeStamp = Duration.nanoTime();
            e.preventDefault();
        }
        if (e.getType().equals("touchmove")) {
            JsArray<Touch> touches = e.getChangedTouches();
            for (int i = 0, j = touches.length(); i < j; i++) {
                Touch touch = touches.get(i);
                int real = touch.getIdentifier();
                int touchId = touchMap.get(real);
                deltaX[touchId] = getRelativeX(touch, canvas) - touchX[touchId];
                deltaY[touchId] = getRelativeY(touch, canvas) - touchY[touchId];
                touchX[touchId] = getRelativeX(touch, canvas);
                touchY[touchId] = getRelativeY(touch, canvas);
                if (touchDraggedHandler != null)
                    touchDraggedHandler.onTouchDragged(touchX[touchId], touchY[touchId], touchId);
            }
            this.currentEventTimeStamp = Duration.nanoTime();
            e.preventDefault();
        }
        if (e.getType().equals("touchcancel")) {
            JsArray<Touch> touches = e.getChangedTouches();
            for (int i = 0, j = touches.length(); i < j; i++) {
                Touch touch = touches.get(i);
                int real = touch.getIdentifier();
                int touchId = touchMap.get(real);
                touchMap.remove(real);
                touched[touchId] = false;
                deltaX[touchId] = getRelativeX(touch, canvas) - touchX[touchId];
                deltaY[touchId] = getRelativeY(touch, canvas) - touchY[touchId];
                touchX[touchId] = getRelativeX(touch, canvas);
                touchY[touchId] = getRelativeY(touch, canvas);
                if (touchUpHandler != null)
                    touchUpHandler.onTouchUp(touchX[touchId], touchY[touchId], touchId, Buttons.LEFT);
            }
            this.currentEventTimeStamp = Duration.nanoTime();
            e.preventDefault();
        }
        if (e.getType().equals("touchend")) {
            JsArray<Touch> touches = e.getChangedTouches();
            for (int i = 0, j = touches.length(); i < j; i++) {
                Touch touch = touches.get(i);
                int real = touch.getIdentifier();
                int touchId = touchMap.get(real);
                touchMap.remove(real);
                touched[touchId] = false;
                deltaX[touchId] = getRelativeX(touch, canvas) - touchX[touchId];
                deltaY[touchId] = getRelativeY(touch, canvas) - touchY[touchId];
                touchX[touchId] = getRelativeX(touch, canvas);
                touchY[touchId] = getRelativeY(touch, canvas);
                if (touchUpHandler != null)
                    touchUpHandler.onTouchUp(touchX[touchId], touchY[touchId], touchId, Buttons.LEFT);
            }
            this.currentEventTimeStamp = Duration.nanoTime();
            e.preventDefault();
        }
// if(hasFocus) e.preventDefault();
    }

    private int getAvailablePointer () {
        for (int i = 0; i < MAX_TOUCHES; i++) {
            if (!touchMap.containsValue(i)) return i;
        }
        return -1;
    }

    /** Kindly borrowed from PlayN. **/
    protected static native String getMouseWheelEvent () /*-{
        if (navigator.userAgent.toLowerCase().indexOf('firefox') != -1) {
            return "DOMMouseScroll";
        } else {
            return "mousewheel";
        }
    }-*/;

    static native void addEventListener (JavaScriptObject target, String name, GwtInput handler, boolean capture) /*-{
        target
            .addEventListener(
                name,
                function(e) {
                    handler.@org.parallax3d.parallax.platforms.gwt.GwtInput::handleEvent(Lcom/google/gwt/dom/client/NativeEvent;)(e);
                }, capture);
    }-*/;

    /** from https://github.com/toji/game-shim/blob/master/game-shim.js
     * @return is Cursor catched */
    private native boolean isCursorCatchedJSNI () /*-{
        if (!navigator.pointer) {
            navigator.pointer = navigator.webkitPointer || navigator.mozPointer;
        }
        if (navigator.pointer) {
            if (typeof (navigator.pointer.isLocked) === "boolean") {
                // Chrome initially launched with this interface
                return navigator.pointer.isLocked;
            } else if (typeof (navigator.pointer.isLocked) === "function") {
                // Some older builds might provide isLocked as a function
                return navigator.pointer.isLocked();
            } else if (typeof (navigator.pointer.islocked) === "function") {
                // For compatibility with early Firefox build
                return navigator.pointer.islocked();
            }
        }
        return false;
    }-*/;

    /** from https://github.com/toji/game-shim/blob/master/game-shim.js
     * @param event
     * @return
     */
    private native float getMovementXJSNI (NativeEvent event) /*-{
        return event.movementX || event.webkitMovementX || 0;
    }-*/;

    /** from https://github.com/toji/game-shim/blob/master/game-shim.js
     * @param event
     * @return
     */
    private native float getMovementYJSNI (NativeEvent event) /*-{
        return event.movementY || event.webkitMovementY || 0;
    }-*/;

    private static native boolean isTouchScreen () /*-{
        return (('ontouchstart' in window) || (navigator.msMaxTouchPoints > 0));
    }-*/;

    private static native float getMouseWheelVelocity (NativeEvent evt) /*-{
        var delta = 0.0;
        var agentInfo = @org.parallax3d.parallax.platforms.gwt.GwtApp::agentInfo()();

        if (agentInfo.isFirefox) {
            if (agentInfo.isMacOS) {
                delta = 1.0 * evt.detail;
            } else {
                delta = 1.0 * evt.detail / 3;
            }
        } else if (agentInfo.isOpera) {
            if (agentInfo.isLinux) {
                delta = -1.0 * evt.wheelDelta / 80;
            } else {
                // on mac
                delta = -1.0 * evt.wheelDelta / 40;
            }
        } else if (agentInfo.isChrome || agentInfo.isSafari) {
            delta = -1.0 * evt.wheelDelta / 120;
            // handle touchpad for chrome
            if (Math.abs(delta) < 1) {
                if (agentInfo.isWindows) {
                    delta = -1.0 * evt.wheelDelta;
                } else if (agentInfo.isMacOS) {
                    delta = -1.0 * evt.wheelDelta / 3;
                }
            }
        }
        return delta;
    }-*/;
}
