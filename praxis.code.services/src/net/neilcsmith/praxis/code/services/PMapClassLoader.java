/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2016 Neil C Smith.
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
package net.neilcsmith.praxis.code.services;

import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.ArgumentFormatException;
import net.neilcsmith.praxis.core.types.PBytes;
import net.neilcsmith.praxis.core.types.PMap;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
class PMapClassLoader extends ClassLoader {
    
    private final PMap classes;
    
    PMapClassLoader(PMap classes, ClassLoader parent) {
        super(parent);
        this.classes = classes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Argument dataArg = classes.get(name);
        if (dataArg == null) {
            throw new ClassNotFoundException(name);
        }
        try {
            PBytes data = PBytes.coerce(dataArg);
            byte[] bytes = new byte[data.getSize()];
            data.read(bytes);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (ArgumentFormatException ex) {
            throw new ClassNotFoundException(name, ex);
        }
    }
    
    
    
}
