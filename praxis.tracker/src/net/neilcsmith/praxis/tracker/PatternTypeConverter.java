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
package net.neilcsmith.praxis.tracker;

import net.neilcsmith.praxis.code.TypeConverter;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.ArgumentFormatException;
import net.neilcsmith.praxis.core.info.ArgumentInfo;
import net.neilcsmith.praxis.core.types.PMap;
import net.neilcsmith.praxis.core.types.PString;
import org.praxislive.tracker.Pattern;
import org.praxislive.tracker.Patterns;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
class PatternTypeConverter extends TypeConverter<Pattern> {

    private final static ArgumentInfo INFO
            = ArgumentInfo.create(PString.class,
                    PMap.create(PString.KEY_MIME_TYPE, PatternSupport.MIME));

    @Override
    public Argument toArgument(Pattern value) {
        assert false : "Not supported by non-realtime type converters yet";
        return PString.EMPTY;
    }

    @Override
    public Pattern fromArgument(Argument value) throws ArgumentFormatException {
        Patterns pats = PatternParser.parse(value.toString());
        return pats.getPatternCount() > 0 ? pats.getPattern(0) : Pattern.EMPTY;
    }

    @Override
    public Class<Pattern> getType() {
        return Pattern.class;
    }

    @Override
    public Pattern getDefaultValue() {
        return Pattern.EMPTY;
    }

    @Override
    public ArgumentInfo getInfo() {
        return INFO;
    }

}
