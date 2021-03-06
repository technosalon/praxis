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
 */
package net.neilcsmith.praxis.code.userapi;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
abstract class AbstractLinkable<IN, OUT> implements Consumer<IN>, Linkable<OUT> {
    
    private final Linkable<IN> source;
    private Consumer<OUT> sink;
    
    AbstractLinkable(Linkable<IN> source) {
        this.source = Objects.requireNonNull(source);
    }
    
    @Override
    public void accept(IN value) {
        process(value, sink);
    }

    @Override
    public void link(Consumer<OUT> consumer) {
        this.sink = Objects.requireNonNull(consumer);
        source.link(this);
    }
    
    abstract void process(IN value, Consumer<OUT> sink);
    
    abstract static class Double implements DoubleConsumer, Linkable.Double {
        
        private final Linkable.Double source;
        private DoubleConsumer sink;
        
        Double(Linkable.Double source) {
            this.source = source;
        }

        @Override
        public void accept(double value) {
            process(value, sink);
        }

        @Override
        public void link(DoubleConsumer consumer) {
            this.sink = consumer;
            source.link(this);
        }
        
        abstract void process(double value, DoubleConsumer sink);
        
    }
    
    abstract static class Int implements IntConsumer, Linkable.Int {
        
        private final Linkable.Int source;
        private IntConsumer sink;
        
        Int(Linkable.Int source) {
            this.source = source;
        }

        @Override
        public void accept(int value) {
            process(value, sink);
        }

        @Override
        public void link(IntConsumer consumer) {
            this.sink = consumer;
            source.link(this);
        }
        
        abstract void process(int value, IntConsumer sink);
        
    }
    
}
