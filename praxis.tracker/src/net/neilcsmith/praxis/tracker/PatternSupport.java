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
 */
package net.neilcsmith.praxis.tracker;

import java.lang.annotation.Annotation;
import net.neilcsmith.praxis.code.TypeConverter;
import org.praxislive.tracker.Pattern;
import org.praxislive.tracker.Patterns;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class PatternSupport implements TypeConverter.Provider {
    
    public final static String MIME = "text/x-praxis-tracker";

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeConverter<T> getTypeConverter(Class<T> type, Annotation... annotations) {
        if (Patterns.class.isAssignableFrom(type)) {
            return (TypeConverter<T>) new PatternsTypeConverter();
        } else if (Pattern.class.isAssignableFrom(type)) {
            return (TypeConverter<T>) new PatternTypeConverter();
        }
        return null;
    }
    
}
