/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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
package org.parallax3d.parallax.animation;

import org.parallax3d.parallax.math.Quaternion;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float64Array;

/**
 * Buffered scene graph property that allows weighted accumulation.
 *
 * @author Ben Houston / http://clara.io/
 * @author David Sarno / http://lighthaus.us/
 * @author tschw
 * @author thothbot
 */
@ThreejsObject("THREE.PropertyMixer")
public class PropertyMixer {

    public interface MixFunctions {
        void run(Float64Array buffer, int dstOffset, int srcOffset, double t, int stride);
    }

    public class SelectFunction implements MixFunctions {

        @Override
        public void run(Float64Array buffer, int dstOffset, int srcOffset, double t, int stride) {
            if (t >= 0.5) {

                for (int i = 0; i != stride; ++i) {

                    buffer.set(dstOffset + i, buffer.get(srcOffset + i));

                }

            }
        }
    }

    public class SlerpFunction implements MixFunctions {

        @Override
        public void run(Float64Array buffer, int dstOffset, int srcOffset, double t, int stride) {
            Quaternion.slerpFlat(buffer, dstOffset,
                    buffer, dstOffset, buffer, srcOffset, t);
        }
    }

    public class LerpFunction implements MixFunctions {

        @Override
        public void run(Float64Array buffer, int dstOffset, int srcOffset, double t, int stride) {
            double s = 1. - t;

            for (int i = 0; i != stride; ++i) {

                int j = dstOffset + i;

                buffer.set(j, buffer.get(j) * s + buffer.get(srcOffset + i) * t);

            }
        }
    }

    PropertyBinding binding;
    int valueSize;

    Float64Array buffer;

    MixFunctions _mixBufferRegion;

    double cumulativeWeight = 0;

    int useCount = 0;
    int referenceCount = 0;

    public PropertyMixer(PropertyBinding binding, String typeName, int valueSize) {

        this.binding = binding;
        this.valueSize = valueSize;

        switch (typeName) {

            case "quaternion":
                _mixBufferRegion = new SlerpFunction();
                break;

            case "string":
            case "bool":

                _mixBufferRegion = new SelectFunction();
                break;

            default:
                _mixBufferRegion = new LerpFunction();

        }

        this.buffer = Float64Array.create(valueSize * 4);

        // layout: [ incoming | accu0 | accu1 | orig ]
        //
        // interpolators can use .buffer as their .result
        // the data then goes to 'incoming'
        //
        // 'accu0' and 'accu1' are used frame-interleaved for
        // the cumulative result and are compared to detect
        // changes
        //
        // 'orig' stores the original state of the property

        this.cumulativeWeight = 0;

        this.useCount = 0;
        this.referenceCount = 0;

    }

    /**
     * accumulate data in the 'incoming' region into 'accu<i>'
     */
    public void accumulate(int accuIndex, double weight) {

        // note: happily accumulating nothing when weight = 0, the caller knows
        // the weight and shouldn't have made the call in the first place

        Float64Array buffer = this.buffer;
        int stride = this.valueSize;
        int offset = accuIndex * stride + stride;

        double currentWeight = this.cumulativeWeight;

        if (currentWeight == 0) {

            // accuN := incoming * weight

            for (int i = 0; i != stride; ++i) {

                buffer.set(offset + i, buffer.get(i));

            }

            currentWeight = weight;

        } else {

            // accuN := accuN + incoming * weight

            currentWeight += weight;
            double mix = weight / currentWeight;
            this._mixBufferRegion.run(buffer, offset, 0, mix, stride);

        }

        this.cumulativeWeight = currentWeight;

    }

    /**
     * apply the state of 'accu<i>' to the binding when accus differ
     *
     * @param accuIndex
     */
    public void apply(int accuIndex) {

        int stride = this.valueSize;
        Float64Array buffer = this.buffer;
        int offset = accuIndex * stride + stride;

        double weight = this.cumulativeWeight;

        this.cumulativeWeight = 0;

        if (weight < 1) {

            // accuN := accuN + original * ( 1 - cumulativeWeight )

            int originalValueOffset = stride * 3;

            this._mixBufferRegion.run(
                    buffer, offset, originalValueOffset, 1 - weight, stride);

        }

        for (int i = stride, e = stride + stride; i != e; ++i) {

            if (buffer.get(i) != buffer.get(i + stride)) {

                // value has changed -> update scene graph

                binding.setValue(buffer, offset);
                break;

            }
        }

    }

    /**
     * remember the state of the bound property and copy it to both accus
     */
    public void saveOriginalState() {

        Float64Array buffer = this.buffer;
        int stride = this.valueSize;

        int originalValueOffset = stride * 3;

        binding.getValue(buffer, originalValueOffset);

        // accu[0..1] := orig -- initially detect changes against the original
        for (int i = stride, e = originalValueOffset; i != e; ++i) {

            buffer.set(i, buffer.get(originalValueOffset + (i % stride)));

        }

        this.cumulativeWeight = 0;

    }

    /**
     * apply the state previously taken via 'saveOriginalState' to the binding
     */
    public void restoreOriginalState() {

        int originalValueOffset = this.valueSize * 3;
        this.binding.setValue(this.buffer, originalValueOffset);

    }

}
