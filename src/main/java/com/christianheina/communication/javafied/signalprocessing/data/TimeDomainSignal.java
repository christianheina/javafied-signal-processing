/*
 * Copyright 2021 Christian Heina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.christianheina.communication.javafied.signalprocessing.data;

import java.util.List;

import org.apache.commons.math3.complex.Complex;

import com.christianheina.communication.javafied.signalprocessing.util.ComplexMath;

/**
 * Class for handling time domain signals.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class TimeDomainSignal extends Signal {

    TimeDomainSignal(List<Complex> iqDataList, int sampleRate) {
        super(iqDataList, sampleRate);
    }

    /**
     * Converts {@link TimeDomainSignal} to {@link FrequencyDomainSignal}
     * 
     * @return {@link FrequencyDomainSignal}
     */
    public FrequencyDomainSignal asFrequencyDomainSignal() {
        return new FrequencyDomainSignal(
                ComplexMath.normalizeBySizeOfList(ComplexMath.fftShift(ComplexMath.fft(iqDataList))), sampleRate);
    }

}
