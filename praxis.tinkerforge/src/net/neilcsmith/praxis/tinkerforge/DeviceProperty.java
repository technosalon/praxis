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
 *
 */
package net.neilcsmith.praxis.tinkerforge;

import net.neilcsmith.praxis.code.AbstractBasicProperty;
import net.neilcsmith.praxis.code.CodeContext;
import net.neilcsmith.praxis.code.ControlDescriptor;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.Control;
import net.neilcsmith.praxis.core.info.ArgumentInfo;
import net.neilcsmith.praxis.core.info.ControlInfo;
import net.neilcsmith.praxis.core.types.PMap;
import net.neilcsmith.praxis.core.types.PString;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
class DeviceProperty extends AbstractBasicProperty {

    private final static ControlInfo INFO = ControlInfo.createPropertyInfo(
            new ArgumentInfo[]{ArgumentInfo.create(PString.class,
                        PMap.create(ArgumentInfo.KEY_SUGGESTED_VALUES,
                                TFCodeContext.AUTO))},
            new Argument[]{PString.EMPTY},
            PMap.EMPTY
    );

    private TFCodeContext context;
    
    @Override
    protected void set(long time, Argument arg) throws Exception {
        context.setUID(arg.toString());
    }

    @Override
    protected Argument get() {
        return PString.valueOf(context.getUID());
    }

    @Override
    public ControlInfo getInfo() {
        return INFO;
    }
    

    static class Descriptor extends ControlDescriptor {

        private final DeviceProperty control;

        Descriptor(String id, int index) {
            super(id, Category.Property, index);
            control = new DeviceProperty();
        }

        @Override
        public ControlInfo getInfo() {
            return control.getInfo();
        }

        @Override
        public void attach(CodeContext<?> context, Control previous) {
            control.context = (TFCodeContext) context;
        }

        @Override
        public Control getControl() {
            return control;
        }

    }

}
