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
 *
 */
package net.neilcsmith.praxis.core.code;

import net.neilcsmith.praxis.code.CodeContext;
import net.neilcsmith.praxis.core.ExecutionContext;
import net.neilcsmith.praxis.logging.LogLevel;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public class CoreCodeContext extends CodeContext<CoreCodeDelegate> {

    public CoreCodeContext(CoreCodeConnector connector) {
        super(connector);
    }

    @Override
    protected void starting(ExecutionContext source, boolean fullStart) {
        try {
            getDelegate().setup();
        } catch (Exception e) {
            getLog().log(LogLevel.ERROR, e, "Exception thrown during setup()");
        }
        if (fullStart) {
            try {
                getDelegate().starting();
            } catch (Exception e) {
                getLog().log(LogLevel.ERROR, e, "Exception thrown during starting()");
            }
        }
    }

    @Override
    protected void stopping(ExecutionContext source, boolean fullStop) {
        if (fullStop) {
            try {
                getDelegate().stopping();
            } catch (Exception e) {
                getLog().log(LogLevel.ERROR, e, "Exception thrown during stopping()");
            }
        }
    }

    @Override
    protected void tick(ExecutionContext source) {
        try {
            getDelegate().update();
        } catch (Exception e) {
            getLog().log(LogLevel.ERROR, e, "Exception thrown during update()");
        }
    }

}
