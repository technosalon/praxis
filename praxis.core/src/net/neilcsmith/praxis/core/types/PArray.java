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
package net.neilcsmith.praxis.core.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.ArgumentFormatException;
import net.neilcsmith.praxis.core.CallArguments;
import net.neilcsmith.praxis.core.info.ArgumentInfo;
import net.neilcsmith.praxis.core.syntax.Token;
import net.neilcsmith.praxis.core.syntax.Tokenizer;

/**
 *
 * @author Neil C Smith
 */
public final class PArray extends Value implements Iterable<Value> {

    public final static PArray EMPTY = new PArray(new Value[0], "");
    private final Value[] data;
    private volatile String str;

    private PArray(Value[] data, String str) {
        this.data = data;
        this.str = str;
    }

    public Value get(int index) {
        int count = data.length;
        if (count > 0) {
            index %= count;
            return index < 0 ? data[index + count] : data[index];
        } else {
            return this;
        }
    }

    public Value[] getAll() {
        return data.clone();
    }

    public int getSize() {
        return data.length;
    }

    @Override
    public String toString() {
        if (str == null) {
            if (data.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Argument entry : data) {
                    if (sb.length() > 0) {
                        sb.append(' ');
                    }
                    if (entry instanceof PArray || entry instanceof PMap) {
                        sb.append('{')
                                .append(entry.toString())
                                .append('}');
                    } else {
                        sb.append(SyntaxUtils.escape(String.valueOf(entry)));
                    }
                }
                str = sb.toString();
            } else {
                str = "";
            }
        }

        return str;

    }

    @Override
    public boolean isEmpty() {
        return data.length == 0;
    }

    @Override
    public boolean equivalent(Value arg) {
        try {
            if (arg == this) {
                return true;
            }
            PArray other = PArray.coerce(arg);
            int size = data.length;
            if (size != other.data.length) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!Argument.equivalent(null, data[i], other.data[i])) {
                    return false;
                }
            }
            return true;
        } catch (ArgumentFormatException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PArray) {
            PArray o = (PArray) obj;
            return Arrays.equals(data, o.data);
        }
        return false;
    }

    public Iterator<Value> iterator() {
        return new Itr();
    }

    public Stream<Value> stream() {
        return Stream.of(data);
    }

    private class Itr implements Iterator<Value> {

        int cursor = 0;

        public boolean hasNext() {
            return cursor < data.length;
        }

        public Value next() {
            Value arg = data[cursor];
            cursor++;
            return arg;
        }

        public void remove() {
            throw new UnsupportedOperationException("PArrays are immutable");
        }
    }

    public static PArray valueOf(Collection<? extends Argument> collection) {
        Value[] values = collection.stream()
                .map(arg -> arg instanceof Value ? (Value) arg : PString.valueOf(arg))
                .toArray(Value[]::new);
        return new PArray(values, null);
    }

    @Deprecated
    public static PArray valueOf(Argument... args) {
        int size = args.length;
        if (size == 0) {
            return PArray.EMPTY;
        }
        Value[] copy = new Value[size];
        for (int i = 0; i < size; i++) {
            Argument arg = args[i];
            if (arg == null) {
                throw new NullPointerException();
            }
            copy[i] = arg instanceof Value ? (Value) arg : PString.valueOf(arg);
        }
        return new PArray(copy, null);
    }

    public static PArray valueOf(Value... args) {
        int size = args.length;
        if (size == 0) {
            return PArray.EMPTY;
        }
        Value[] copy = new Value[size];
        for (int i = 0; i < size; i++) {
            Value arg = args[i];
            if (arg == null) {
                throw new NullPointerException();
            }
            copy[i] = arg;
        }
        return new PArray(copy, null);
    }

    @Deprecated
    public static PArray valueOf(CallArguments args) {
        return valueOf(args.getAll());
    }

    public static PArray valueOf(String str) throws ArgumentFormatException {
        if (str.length() == 0) {
            return PArray.EMPTY;
        }
        try {
            Tokenizer tk = new Tokenizer(str);
            List<PString> list = new ArrayList<PString>();
            tokenize:
            for (Token t : tk) {
                Token.Type type = t.getType();
                switch (type) {
                    case PLAIN:
                    case QUOTED:
                        list.add(PString.valueOf(t.getText()));
                        break;
                    case BRACED:
                        String s = t.getText();
                        list.add(PString.valueOf(s));
                        break;
                    case EOL:
                        break tokenize;
                    default:
                        throw new ArgumentFormatException();
                }
            }
            int size = list.size();
            if (size == 0) {
                return PArray.EMPTY;
            } else {
                return new PArray(list.toArray(new Value[size]), str);
            }
        } catch (Exception ex) {
            throw new ArgumentFormatException(ex);
        }

    }

    public static PArray coerce(Argument arg) throws ArgumentFormatException {
        if (arg instanceof PArray) {
            return (PArray) arg;
        } else {
            return valueOf(arg.toString());
        }
    }

    public static Optional<PArray> from(Argument arg) {
        try {
            return Optional.of(coerce(arg));
        } catch (ArgumentFormatException ex) {
            return Optional.empty();
        }
    }

    public static ArgumentInfo info() {
        return ArgumentInfo.create(PArray.class, null);
    }

    public static <T extends Argument> Collector<T, ?, PArray> collector() {

        return Collector.<T, List<T>, PArray>of(
                ArrayList::new,
                List::add,
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                },
                PArray::valueOf
        );
    }

    public static PArray concat(PArray a, PArray b) {
        Value[] values = new Value[a.data.length + b.data.length];
        System.arraycopy(a.data, 0, values, 0, a.data.length);
        System.arraycopy(b.data, 0, values, a.data.length, b.data.length);
        return new PArray(values, null);
    }

    public static PArray subset(PArray array, int start, int count) {
        Value[] values = new Value[count];
        System.arraycopy(array.data, start, values, 0, count);
        return new PArray(values, null);
    }

    public static PArray insert(PArray array, int index, Value value) {
        Value[] values = new Value[array.data.length + 1];
        System.arraycopy(array.data, 0, values, 0, index);
        values[index] = value;
        System.arraycopy(array.data, index, values, index + 1,
                array.data.length - index);
        return new PArray(values, null);
    }
    
    public static PArray append(PArray array, Value value) {
        Value[] values = new Value[array.data.length + 1];
        System.arraycopy(array.data, 0, values, 0, array.data.length);
        values[values.length - 1] = value;
        return new PArray(values, null);
    }

}
