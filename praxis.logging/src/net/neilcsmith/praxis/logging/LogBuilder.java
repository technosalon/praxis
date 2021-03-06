/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2015 Neil C Smith.
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
package net.neilcsmith.praxis.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.CallArguments;
import net.neilcsmith.praxis.core.types.PError;
import net.neilcsmith.praxis.core.types.PString;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public final class LogBuilder {

    private final List<Argument> log;

    private LogLevel level;

    public LogBuilder(LogLevel level) {
        this.level = Objects.requireNonNull(level);
        this.log = new ArrayList<Argument>();
    }

    public void log(LogLevel level, String msg) {
        if (isLoggable(level)) {
            PString m = PString.valueOf(msg);
            log.add(level.asPString());
            log.add(m);
        }
    }

    public void log(LogLevel level, PError err) {
        if (isLoggable(level)) {
            log.add(level.asPString());
            log.add(err);
        }
    }
    
    public void log(LogLevel level, Exception ex) {
        if (isLoggable(level)) {
            PError e = PError.create(ex);
            log.add(level.asPString());
            log.add(e);
        }
    }
    
    public void log(LogLevel level, Exception ex, String msg) {
        if (isLoggable(level)) {
            PError e = PError.create(ex, msg);
            log.add(level.asPString());
            log.add(e);
        }
    }
    
    public void log(LogLevel level, Class<? extends Exception> type, String msg) {
        if (isLoggable(level)) {
            PError e = PError.create(type, msg);
            log.add(level.asPString());
            log.add(e);
        }
    }

    public void setLevel(LogLevel level) {
        this.level = Objects.requireNonNull(level);
    }

    public LogLevel getLevel() {
        return level;
    }

    public boolean isLoggable(LogLevel level) {
        return this.level.isLoggable(level);
    }

    public CallArguments toCallArguments() {
        return CallArguments.create(log);
    }

    public void clear() {
        log.clear();
    }

    public boolean isEmpty() {
        return log.isEmpty();
    }
}
