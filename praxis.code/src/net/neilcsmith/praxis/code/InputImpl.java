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
 */
package net.neilcsmith.praxis.code;

import java.lang.reflect.Field;
import net.neilcsmith.praxis.code.userapi.AuxIn;
import net.neilcsmith.praxis.code.userapi.In;
import net.neilcsmith.praxis.code.userapi.Input;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.core.info.PortInfo;
import net.neilcsmith.praxis.core.types.PString;
import net.neilcsmith.praxis.core.types.Value;
import net.neilcsmith.praxis.logging.LogLevel;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
class InputImpl extends Input {
    
    private CodeContext<?> context;
    
    @Override
    protected void attach(CodeContext<?> context) {
        super.attach(context);
        this.context = context;
    }

    private void update(long time, double value) {
        if (context.checkActive()) {
            context.update(time);
            super.updateLinks(value);
        }
    }
    
    private void update(long time, Value value) {
        if (context.checkActive()) {
            context.update(time);
            super.updateLinks(value);
        }
    }
    
    
    static Descriptor createDescriptor(CodeConnector<?> connector,
            In ann, Field field) {
        return createDescriptor(connector, PortDescriptor.Category.In, ann.value(), field);
    }

    static Descriptor createDescriptor(CodeConnector<?> connector,
            AuxIn ann, Field field) {
        return createDescriptor(connector, PortDescriptor.Category.AuxIn, ann.value(), field);
    }
    
    private static Descriptor createDescriptor(CodeConnector<?> connector,
            PortDescriptor.Category category, int index, Field field) {
        if (!Input.class.isAssignableFrom(field.getType())) {
            return null;
        }
        String id = connector.findID(field);
        return new Descriptor(id, category, index, field);
    }
    
    static class Descriptor extends PortDescriptor 
            implements ControlInput.Link {

        private final Field field;
        private final InputImpl input;
        private ControlInput port;

        private Descriptor(String id, Category category, int index, Field field) {
            super(id, category, index);
            this.input = new InputImpl();
            this.field = field;
        }
        
        @Override
        public void attach(CodeContext<?> context, Port previous) {
            if (previous instanceof ControlInput) {
                port = (ControlInput) previous;
                port.setLink(this);
            } else {
                if (previous != null) {
                    previous.disconnectAll();
                }
                port = new ControlInput(this);
            }
            try {
                field.setAccessible(true);
                field.set(context.getDelegate(), input);
            } catch (Exception ex) {
                context.getLog().log(LogLevel.ERROR, ex);
            }
            input.attach(context);
        }

        @Override
        public Port getPort() {
            return port;
        }

        @Override
        public PortInfo getInfo() {
            return ControlInput.INFO;
        }

        @Override
        public void receive(long time, double value) {
            input.update(time, value);
        }

        @Override
        public void receive(long time, Argument value) {
            Value v = value instanceof Value ? (Value) value : PString.valueOf(value);
            input.update(time, v);
        }
        
    }
}
