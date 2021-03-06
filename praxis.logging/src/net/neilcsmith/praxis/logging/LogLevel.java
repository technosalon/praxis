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


package net.neilcsmith.praxis.logging;

import net.neilcsmith.praxis.core.types.PString;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public enum LogLevel {
    
    ERROR(1000), WARNING(750), INFO(500), DEBUG(250);

    private final int level;
    private final PString pstring;
    
    private LogLevel(int level) {
        this.level = level;
        this.pstring = PString.valueOf(name());
    }
    
    public boolean isLoggable(LogLevel other) {
        return other.level >= level;
    }
    
    public PString asPString() {
        return pstring;
    }
    
}
