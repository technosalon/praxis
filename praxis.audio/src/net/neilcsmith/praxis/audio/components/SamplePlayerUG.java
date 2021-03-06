/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2013 Neil C Smith.
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
package net.neilcsmith.praxis.audio.components;

import net.neilcsmith.praxis.audio.io.SampleTable;
import org.jaudiolibs.pipes.Buffer;
import org.jaudiolibs.pipes.Pipe;
import org.jaudiolibs.pipes.impl.SingleInOut;

/**
 *
 * @author Neil C Smith
 */
class SamplePlayerUG extends SingleInOut {
    private SampleTable table;
    private int channel;
    private int tableSize;
    private int in;
    private int out;
    private boolean playing;
    private boolean looping;
    private boolean recording;
//    private boolean wasRecording;
    private float position;
    private float speed = 1;

    public void setSampleTable(SampleTable table) {
        this.table = table;
        channel = 0;
        if (table == null) {
            tableSize = 0;
            in = 0;
            out = 0;
            position = 0;
//            playing = recording = wasRecording = false;
            playing = false; 
        } else {
            tableSize = table.getSize();
            in = 0;
            out = tableSize;
//            wasRecording = false;
        }
    }

    public SampleTable getSampleTable() {
        return table;
    }
    
    public void setChannel(int channel) {
        if (table == null) {
            throw new IllegalStateException();
        }
        if (channel < 0 || channel >= table.getChannelCount()) {
            throw new IllegalArgumentException();
        }
        this.channel = channel;
    }
    
    

    public int getChannel() {
        return channel;
    }

//    public void setRecording(boolean recording) {
//        if (recording && table != null) {
//            this.recording = true;
//        } else {
//            this.recording = false;
//        }
//    }
//
//    public boolean getRecording() {
//        return recording;
//    }

    public void setPlaying(boolean playing) {
        if (this.playing != playing) {
            this.playing = playing;
            smoothIndex = 0;
        }
        if (speed < 0) {
            setPosition(out - 1);
        } else {
            setPosition(in);
        }
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public boolean getLooping() {
        return looping;
    }

    public void setIn(int value) {
        if (value < 0) {
            in = 0;
        } else if (value > out) {
            in = out;
        } else {
            in = value;
        }
    }

    public int getIn() {
        return in;
    }

    public void setOut(int value) {
        if (value < in) {
            out = in;
        } else if (value > tableSize) {
            out = tableSize;
        } else {
            out = value;
        }
    }

    public int getOut() {
        return out;
    }

    public void setPosition(float value) {
        if (value < in || value >= out) {
            position = in;
        } else {
            position = value;
        }
        if (playing) {
            smoothIndex = 0;
        }
    }

    public float getPosition() {
        return position;
    }

    public void setSpeed(float value) {
        speed = value;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    protected void process(Buffer buffer, boolean rendering) {
        //        System.out.println("Process called");
        float[] data = buffer.getData();
//        if (recording) {
//            processRecording(data, 0, rendering);
//        } else 
        if (playing) {
            processPlaying(data, 0, rendering);
        } else {
            processStopped(data, 0, rendering);
        }
    }
//    private int recordIdx;
//    private int recordIn;
//    private int recordOut;

//    private void processRecording(float[] data, int offset, boolean rendering) {
//        if (!wasRecording) {
//            smoothIndex = 0;
//        }
//        int idx = (int) position;
//        int looplength = out - in;
//        while (idx >= out) {
//            idx -= looplength;
//        }
//        while (idx < in) {
//            idx += looplength;
//        }
//        //        boolean smoothEnds = false;
//        //        boolean smoothIdx = false;
//        if (idx != recordIdx) {
//            // smooth old in and out if was recording
//            if (wasRecording) {
//                fadeTo(recordIdx);
//                fadeFrom(recordIdx);
//            }
//            //            recordIdx = idx;
//            //            smoothIdx = true;
//        }
//        for (int i = offset, k = data.length; i < k; i++) {
//            float sample = data[i];
//            table.set(0, idx, sample);
//            idx++;
//            if (idx == out) {
//                idx = in;
//                //                smoothEnds = true;
//                smoothEnds();
//                if (!looping) {
//                    smoothIndex = 0;
//                    processStopped(data, i, rendering);
//                    return;
//                }
//            }
//            if (!playing) {
//                sample = 0;
//            }
//            if (smoothIndex >= 0) {
//                sample = smooth(sample);
//                smoothIndex++;
//                if (smoothIndex >= SMOOTH_SAMPLES) {
//                    smoothIndex = -1;
//                }
//            }
//            data[i] = sample;
//            previousSample = sample;
//        }
//        wasRecording = true;
//        position = idx;
//        recordIdx = idx;
//        recordIn = in;
//        recordOut = out;
//    }
//
//    private void smoothEnds() {
//        // smooth to out
//        // smooth from in
//        fadeTo(out);
//        fadeFrom(in);
//        if (recordIn != in) {
//            // smooth from out
//            fadeTo(in);
//            recordIn = in;
//        }
//        if (recordOut != out) {
//            // smooth to in
//            fadeFrom(out);
//            recordOut = out;
//        }
//    }
//
//    private void fadeTo(int idx) {
//        if (idx < SMOOTH_SAMPLES) {
//            for (int i = 0; i < idx; i++) {
//                table.set(0, i, 0);
//            }
//        } else {
//            float mult = 1;
//            float decrement = 1.0f / SMOOTH_SAMPLES;
//            for (int i = idx - SMOOTH_SAMPLES; i < idx; i++) {
//                mult -= decrement;
//                table.set(0, i, mult * table.get(0, i));
//            }
//        }
//    }
//
//    private void fadeFrom(int idx) {
//        int size = tableSize;
//        if (idx + SMOOTH_SAMPLES > size) {
//            for (int i = idx; i < size; i++) {
//                table.set(0, i, 0);
//            }
//        } else {
//            float mult = 0;
//            float increment = 1.0f / SMOOTH_SAMPLES;
//            size = idx + SMOOTH_SAMPLES;
//            for (int i = idx; i < size; i++) {
//                table.set(0, i, mult * table.get(0, i));
//                mult += increment;
//            }
//        }
//    }

    private void processStopped(float[] data, int offset, boolean rendering) {
//        if (wasRecording) {
//            fadeFrom(recordIdx);
//            fadeTo(recordIdx);
//            smoothIndex = 0;
//            wasRecording = false;
//        }
        if (!rendering) {
            smoothIndex = -1;
            return;
        }
        if (smoothIndex >= 0) {
            for (int i = offset, k = data.length; i < k; i++) {
                float sample = smooth(0);
                data[i] = sample;
                smoothIndex++;
                if (sample == 0 || smoothIndex >= SMOOTH_SAMPLES) {
                    smoothIndex = -1;
                    offset = i;
                    break;
                }
            }
        }
        for (int i = offset, k = data.length; i < k; i++) {
            data[i] = 0;
        }
    }
    private float previousSample = 0;
    private static final float MAX_DELTA = 0.2f;
    private static final int SMOOTH_SAMPLES = 8;
    private int smoothIndex = -1;

    private float smooth(float sample) {
        float delta = sample - previousSample;
        float max = MAX_DELTA;
        if (delta > max) {
            sample = previousSample + max;
        } else if (delta < -max) {
            sample = previousSample - max;
        }
        previousSample = sample;
        return sample;
    }

    private void processPlaying(float[] data, int offset, boolean rendering) {
//        if (wasRecording) {
//            // smooth tails
//            fadeFrom(recordIdx);
//            fadeTo(recordIdx);
//            smoothIndex = 0;
//            wasRecording = false;
//        }
        int bSize = data.length;
        int loopLength = out - in;
        if (loopLength > 0 && table != null) {
            if (rendering) {
                for (int i = 0; i < bSize; i++) {
                    if (position >= out) {
                        smoothIndex = 0;
                        if (looping) {
                            while (position >= out) {
                                position -= loopLength;
                            }
                        } else {
                            processStopped(data, i, rendering);
                            position = in;
                            playing = false;
                            return;
                        }
                    } else if (position < in) {
                        smoothIndex = 0;
                        if (looping) {
                            while (position < in) {
                                position += loopLength;
                            }
                        } else {
                            processStopped(data, i, rendering);
                            position = out - 1;
                            playing = false;
                            return;
                        }
                    }
                    float sample = getSample(channel, position);
                    if (smoothIndex >= 0) {
                        sample = smooth(sample);
                        smoothIndex++;
                        if (smoothIndex >= SMOOTH_SAMPLES) {
                            smoothIndex = -1;
                        }
                    }
                    data[i] = sample;
                    previousSample = sample;
                    position += speed;
                }
            } else {
                position += (speed * bSize);
                if (position > out) {
                    if (looping) {
                        while (position > out) {
                            position -= loopLength;
                        }
                    } else {
                        position = in;
                        playing = false;
                    }
                } else if (position < in) {
                    if (looping) {
                        while (position < in) {
                            position += loopLength;
                        }
                    } else {
                        position = out - 1;
                        playing = false;
                    }
                }
            }
        } else {
            if (previousSample != 0) {
                smoothIndex = 0;
            }
            processStopped(data, offset, rendering);
        }
    }

    private float getSample(int channel, float pos) {
        int iPos = (int) pos;
        //            if (iPos == pos) {
        //                return table.get(0, iPos);
        //            }
        float frac;
        float a;
        float b;
        float c;
        float d;
        float cminusb;
        if (iPos < (1)) {
            iPos = 1;
            frac = 0;
        } else if (iPos > (tableSize - 3)) {
            iPos = tableSize - 3;
            frac = 1;
        } else {
            frac = pos - iPos;
        }
        a = table.get(channel, iPos - 1);
        b = table.get(channel, iPos);
        c = table.get(channel, iPos + 1);
        d = table.get(channel, iPos + 2);
        cminusb = c - b;
        return b + frac * (cminusb - 0.5f * (frac - 1) * ((a - d + 3.0f * cminusb) * frac + (b - a - cminusb)));
    }

    @Override
    public boolean isRenderRequired(Pipe source, long time) {
        return recording;
    }
    
}
