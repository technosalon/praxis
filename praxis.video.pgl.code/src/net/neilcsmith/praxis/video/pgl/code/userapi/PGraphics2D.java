/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2014 Neil C Smith.
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
package net.neilcsmith.praxis.video.pgl.code.userapi;

import net.neilcsmith.praxis.video.pgl.PGLGraphics;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public abstract class PGraphics2D extends PGraphics {
    

    protected final void initGraphics(PGLGraphics graphics) {
        this.g = graphics;
        this.context = graphics.getContext();
    }
    
    protected final PGLGraphics releaseGraphics() {
        PGLGraphics ret = (PGLGraphics) g;
        this.g = null;
        this.context = null;
        return ret;
    }

}
