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
package net.neilcsmith.praxis.code;

import net.neilcsmith.praxis.core.info.ArgumentInfo;
import net.neilcsmith.praxis.core.info.ControlInfo;
import net.neilcsmith.praxis.core.interfaces.Service;
import net.neilcsmith.praxis.core.types.PMap;
import net.neilcsmith.praxis.core.types.PReference;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class CodeCompilerService extends Service {

    public final static String COMPILE = "compile";
    public final static ControlInfo COMPILE_INFO = 
            ControlInfo.createFunctionInfo(
            new ArgumentInfo[]{PReference.info(PMap.class)},
            new ArgumentInfo[]{PReference.info(PMap.class)},
            PMap.EMPTY);

    // parameter keys
    public final static String KEY_CLASS_BODY_CONTEXT =
            "class-body-context";
    public final static String KEY_CODE =
            "code";
    public final static String KEY_LOG_LEVEL = 
            "log-level";
    
    // response keys
    public final static String KEY_CLASSES =
            "classes";
    public final static String KEY_LOG =
            "log";
    
    @Override
    public String[] getControls() {
        return new String[]{COMPILE};
    }

    @Override
    public ControlInfo getControlInfo(String control) {
        if (COMPILE.equals(control)) {
            return COMPILE_INFO;
        }
        throw new IllegalArgumentException();
    }
}
