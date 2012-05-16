/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2012 Neil C Smith.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details.
 * 
 * You should have received a copy of the GNU General Public License version 3
 * along with this work; if not, see http://www.gnu.org/licenses/
 * 
 * 
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */
package net.neilcsmith.praxis.video.components.analysis.blobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import net.neilcsmith.praxis.core.ControlPort;
import net.neilcsmith.praxis.core.ExecutionContext;
import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.impl.AbstractClockComponent;
import net.neilcsmith.praxis.impl.BooleanProperty;
import net.neilcsmith.praxis.impl.DefaultControlOutputPort;
import net.neilcsmith.praxis.video.impl.DefaultVideoInputPort;
import net.neilcsmith.praxis.video.impl.DefaultVideoOutputPort;
import net.neilcsmith.ripl.PixelData;
import net.neilcsmith.ripl.Surface;
import net.neilcsmith.ripl.SurfaceOp;
import net.neilcsmith.ripl.components.Delegator;
import net.neilcsmith.ripl.delegates.AbstractDelegate;
import net.neilcsmith.ripl.ops.Blend;
import net.neilcsmith.ripl.ops.GraphicsOp;
import net.neilcsmith.ripl.ops.RectFill;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public class BlobTracker extends AbstractClockComponent {

    private ImageBlobs imageBlobs;
    private BlobDelegate blobDelegate;
    private BooleanProperty debug;
    private ControlPort.Output x;
    private ControlPort.Output y;
    private ControlPort.Output width;
    private ControlPort.Output height;
    private double xd;
    private double yd;
    private double widthd;
    private double heightd;

    public BlobTracker() {
        blobDelegate = new BlobDelegate();
        imageBlobs = new ImageBlobs();
        Delegator del = new Delegator(blobDelegate);
        debug = BooleanProperty.create(this, false);
        registerControl("debug", debug);
        registerPort(Port.IN, new DefaultVideoInputPort(this, del));
        registerPort(Port.OUT, new DefaultVideoOutputPort(this, del));
        x = new DefaultControlOutputPort(this);
        registerPort("x", x);
        y = new DefaultControlOutputPort(this);
        registerPort("y", y);
        width = new DefaultControlOutputPort(this);
        registerPort("width", width);
        height = new DefaultControlOutputPort(this);
        registerPort("height", height);
    }

    public void tick(ExecutionContext source) {
        long time = source.getTime();
        x.send(time, xd);
        y.send(time, yd);
        width.send(time, widthd);
        height.send(time, heightd);
    }

    private class BlobDelegate extends AbstractDelegate implements SurfaceOp {

        public void process(Surface surface) {
            surface.process(this);
            imageBlobs.dotracking();
            if (imageBlobs.trackedblobs.isEmpty()) {
                widthd = 0;
                heightd = 0;
            } else {
                ABlob b = imageBlobs.trackedblobs.get(0);
                xd = (double) b.boxcenterx / surface.getWidth();
                yd = (double) b.boxcentery / surface.getHeight();
                widthd = (double) b.dimx / surface.getWidth();
                heightd = (double) b.dimy / surface.getHeight();
            }
            if (debug.getValue()) {
                for (int i = 0; i < imageBlobs.trackedblobs.size(); i++) {
                    final ABlob b = imageBlobs.trackedblobs.get(i);
                    final int idx = i;
                    if (idx == 0) {

                        surface.process(RectFill.op(Color.MAGENTA, Blend.NORMAL.opacity(0.4),
                                b.boxminx, b.boxminy, b.boxdimx, b.boxdimy));
                    } else {
                        surface.process(RectFill.op(Color.CYAN, Blend.NORMAL.opacity(0.4),
                                b.boxminx, b.boxminy, b.boxdimx, b.boxdimy));
                    }

                    surface.process(new GraphicsOp(new GraphicsOp.Callback() {

                        public void draw(Graphics2D g2d, Image[] images) {
                            g2d.drawString("" + idx, b.boxcenterx, b.boxcentery);
                        }
                    }));
                }
            }
        }

        public void process(PixelData output, PixelData... inputs) {
            imageBlobs.calc(output);
        }
    }
}