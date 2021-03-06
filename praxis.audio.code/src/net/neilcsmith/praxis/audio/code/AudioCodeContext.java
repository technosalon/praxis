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
package net.neilcsmith.praxis.audio.code;

import net.neilcsmith.praxis.audio.AudioContext;
import net.neilcsmith.praxis.code.CodeComponent;
import net.neilcsmith.praxis.code.CodeContext;
import net.neilcsmith.praxis.core.ExecutionContext;
import net.neilcsmith.praxis.logging.LogLevel;
import org.jaudiolibs.pipes.Pipe;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class AudioCodeContext<D extends AudioCodeDelegate> extends CodeContext<D> {

    private final UGenDescriptor[] ugens;
    private final AudioInPort.Descriptor[] ins;
    private final AudioOutPort.Descriptor[] outs;

    private AudioContext audioCtxt;

    public AudioCodeContext(AudioCodeConnector<D> connector) {
        super(connector, true);
        ugens = connector.extractUGens();
        ins = connector.extractIns();
        outs = connector.extractOuts();
    }

    @Override
    protected void configure(CodeComponent<D> cmp, CodeContext<D> oldCtxt) {
        super.configure(cmp, oldCtxt);
        // audio ins and outs attached in super call because they're ports
        for (UGenDescriptor ugd : ugens) {
            ugd.attach(this, oldCtxt);
        }
    }

    @Override
    protected void hierarchyChanged() {
        audioCtxt = getLookup().get(AudioContext.class);
    }

    @Override
    protected void starting(ExecutionContext source) {
        setupDelegate();
    }

    @Override
    protected void stopping(ExecutionContext source) {
        resetPorts();
    }

    @Override
    protected void tick(ExecutionContext source) {
        updateDelegate();
    }

    private void setupDelegate() {
        setupPorts();
        setupUGens();
        AudioCodeDelegate delegate = getDelegate();
        if (audioCtxt != null) {
            delegate.sampleRate = audioCtxt.getSampleRate();
            delegate.blockSize = audioCtxt.getBlockSize();
        } else {
            delegate.sampleRate = 48000;
            delegate.blockSize = 64;
        }
        try {
            delegate.setup();
        } catch (Exception e) {
            getLog().log(LogLevel.ERROR, e, "Exception thrown during setup()");
        }
    }

    private void updateDelegate() {
        try {
            getDelegate().update();
        } catch (Exception e) {
            getLog().log(LogLevel.ERROR, e, "Exception thrown during update()");
        }
    }
    
    private void setupUGens() {
        for (UGenDescriptor ugd : ugens) {
            Pipe ug = ugd.getUGen();
            Utils.disconnect(ug);
            if (ug instanceof Resettable) {
                ((Resettable)ug).reset();
            }
        }
    }
    
    private void setupPorts() {
        for (AudioInPort.Descriptor aipd : ins) {
            Utils.disconnectSinks(aipd.getPort().getPipe());
        }
        for (AudioOutPort.Descriptor aopd : outs) {
            AudioOutPort.AudioOutPipe pipe = aopd.getPort().getPipe();
            Utils.disconnectSources(pipe);
            pipe.triggerSwitch();
        }
    }
    
    private void resetPorts() {
        for (AudioOutPort.Descriptor aopd : outs) {
            AudioOutPort.AudioOutPipe pipe = aopd.getPort().getPipe();
            pipe.resetSwitch();
        }
    }

}
