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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.parallax3d.parallax.tests.DemoAnimation;
import org.parallax3d.parallax.tests.geometries.GeometryCube;

public class Test1 extends ContentWidget  {

    public Test1()
    {
        super("STL loader", "This example based on the three.js example.");
    }

    @Override
    public DemoAnimation onInitialize()
    {
        return new GeometryCube();
    }

    @Override
    protected boolean isEnabledEffectSwitch() {
        return false;
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<DemoAnimation> callback)
    {
        GWT.runAsync(Test1.class, new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
