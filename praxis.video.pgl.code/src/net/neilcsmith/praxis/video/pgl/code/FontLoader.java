/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2017 Neil C Smith.
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
 *
 *
 * Parts of the API of this package, as well as some of the code, is derived from
 * the Processing project (http://processing.org)
 *
 * Copyright (c) 2004-09 Ben Fry and Casey Reas
 * Copyright (c) 2001-04 Massachusetts Institute of Technology
 *
 */
package net.neilcsmith.praxis.video.pgl.code;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import net.neilcsmith.praxis.code.ResourceProperty;
import net.neilcsmith.praxis.video.pgl.PGLContext;
import net.neilcsmith.praxis.video.pgl.code.userapi.PFont;
import net.neilcsmith.praxis.video.render.utils.FontUtils;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
class FontLoader extends ResourceProperty.Loader<PFont> {
    
    private final static FontLoader INSTANCE = new FontLoader();

    private FontLoader() {
        super(PFont.class);
    }
    
    @Override
    public PFont load(URI uri) throws IOException {
        try {
            Font baseFont = FontUtils.load(uri);
            return new PFontImpl(baseFont);
        } catch (FontFormatException ex) {
            throw new IOException(ex);
        }
    }
    
    static FontLoader getDefault() {
        return INSTANCE;
    }
    
    private static class PFontImpl extends PFont {
        
        private final Font baseFont;
        
        private PFontImpl(Font baseFont) {
            this.baseFont = baseFont;
        }

        @Override
        protected processing.core.PFont unwrap(PGLContext context, double size) {
            return context.asPFont(baseFont.deriveFont((float) size));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(baseFont);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PFontImpl) {
                return ((PFontImpl) obj).baseFont.equals(baseFont);
            } else {
                return false;
            }
        }
        
        
        
    }
    
}
