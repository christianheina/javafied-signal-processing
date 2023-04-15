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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import com.christianheina.communication.javafied.signalprocessing.util.ComplexMath;

/**
 * Class for handling frequency domain signals.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class FrequencyDomainSignal extends Signal {

    FrequencyDomainSignal(List<Complex> iqDataList, int sampleRate) {
        super(iqDataList, sampleRate);
    }

    /**
     * Convert IQ list to magnitude
     * 
     * @return magnitude list
     */
    public List<Double> toMagnitude() {
        List<Double> spectrumList = new ArrayList<>();
        for (Complex iqEntry : iqDataList) {
            spectrumList.add(iqEntry.abs());
        }
        return spectrumList;
    }

    /**
     * Convert IQ list to Watt power
     * 
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return power list
     */
    public List<Double> toPower(double resistance) {
        List<Double> spectrumList = toMagnitude();
        for (int i = 0; i < spectrumList.size(); i++) {
            spectrumList.set(i, Math.pow(spectrumList.get(i), 2) / resistance);
        }
        return spectrumList;
    }

    /**
     * Convert IQ list to dBm power
     * 
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return power list
     */
    public List<Double> toPowerDbm(double resistance) {
        List<Double> spectrumList = toPower(resistance);
        for (int i = 0; i < spectrumList.size(); i++) {
            spectrumList.set(i, 10 * Math.log10(spectrumList.get(i) / 0.001));
        }
        return spectrumList;
    }

    /**
     * Convert IQ list to dBm power sum
     * 
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return sum power
     */
    public double toSumPowerDbm(double resistance) {
        List<Double> spectrumList = toPower(resistance);
        double sum = 0;
        for (double power : spectrumList) {
            sum += power;
        }
        return 10 * Math.log10((sum) / 0.001);
    }

    /**
     * Converts {@link FrequencyDomainSignal} to {@link TimeDomainSignal}
     * 
     * @return {@link TimeDomainSignal}
     */
    public TimeDomainSignal asTimeDomainSignal() {
        return new TimeDomainSignal(ComplexMath.scaleBySizeOfList(ComplexMath.ifftShift(ComplexMath.ifft(iqDataList))),
                sampleRate);
    }

}
