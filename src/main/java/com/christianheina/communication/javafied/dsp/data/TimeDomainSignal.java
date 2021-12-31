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

package com.christianheina.communication.javafied.dsp.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.jtransforms.fft.DoubleFFT_1D;

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
     * Convert IQ list to magnitude
     * 
     * @return magnitude list
     */
    public List<Double> toMagnitude() {
        List<Double> magnitudeList = new ArrayList<>();
        for (Complex iqEntry : iqDataList) {
            magnitudeList.add(iqEntry.abs());
        }
        return magnitudeList;
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
        List<Double> powerList = new ArrayList<>();
        List<Double> magnitudeList = toMagnitude();
        for (double magnitude : magnitudeList) {
            powerList.add(Math.pow(magnitude, 2) / resistance);
        }
        return powerList;
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
        List<Double> powerDbmList = new ArrayList<>();
        List<Double> powerList = toPower(resistance);
        for (double power : powerList) {
            powerDbmList.add(10 * Math.log10(power / 0.001));
        }
        return powerDbmList;
    }

    /**
     * Convert IQ list to mean dBm power
     * 
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return mean power
     */
    public double toAveragePowerDbm(double resistance) {
        List<Double> powerList = toPower(resistance);
        double sum = 0;
        for (double power : powerList) {
            sum += power;
        }
        return 10 * Math.log10((sum / powerList.size()) / 0.001);
    }

    /**
     * Converts {@link TimeDomainSignal} to {@link FrequencyDomainSignal}
     * 
     * @return {@link FrequencyDomainSignal}
     */
    public FrequencyDomainSignal asFrequencyDomainSignal() {
        DoubleFFT_1D fft = new DoubleFFT_1D(iqDataList.size());
        double[] complexPairs = new double[iqDataList.size() * 2];
        for (int i = 0; i < iqDataList.size(); i++) {
            complexPairs[2 * i] = iqDataList.get(i).getReal();
            complexPairs[2 * i + 1] = iqDataList.get(i).getImaginary();
        }
        fft.complexForward(complexPairs);
        List<Complex> complexSpectrumList = new ArrayList<>();
        for (int i = 0; i < iqDataList.size(); i++) {
            complexSpectrumList
                    .add(new Complex(complexPairs[2 * i], complexPairs[2 * i + 1]).divide(iqDataList.size()));
        }
        // List<Complex> tempList = new ArrayList<>(complexSpectrumList);
        // complexSpectrumList.clear();
        // complexSpectrumList.addAll(tempList.subList(tempList.size()/2, tempList.size()));
        // complexSpectrumList.addAll(tempList.subList(0, tempList.size()/2));
        // System.out.println(tempList.size());
        // System.out.println(complexSpectrumList.size());
        return new FrequencyDomainSignal(complexSpectrumList, sampleRate);
    }

}
