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

import com.christianheina.common.math.ComplexAdditions;
import com.christianheina.common.utilities.PowerUtil;

/**
 * Class for handling information related so signals such as IQ data and sample rate.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
class Signal {

    /**
     * Complex representation of the I and Q values.
     */
    protected List<Complex> iqDataList;
    /**
     * Signal sample rate in Hertz.
     */
    protected int sampleRate;

    protected Signal(List<Complex> iqDataList, int sampleRate) {
        this.iqDataList = iqDataList;
        this.sampleRate = sampleRate;
    }

    /**
     * Retrieve IQ data sample rate in Hz
     * 
     * @return sample rate
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * Retrieve IQ data list
     * 
     * @return IQ data list
     */
    public List<Complex> getIqDataList() {
        return iqDataList;
    }

    /**
     * Convert IQ list to magnitude
     * 
     * @return magnitude list
     */
    public List<Double> toMagnitude() {
        return PowerUtil.complexListToMagnitudeList(iqDataList);
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
        return PowerUtil.complexListToPowerList(iqDataList, resistance);
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
        return PowerUtil.complexListToPowerDbmList(iqDataList, resistance);
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
        return PowerUtil.complexListToAveragePowerDbm(iqDataList, resistance);
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
        return PowerUtil.complexListToSumPowerDbm(iqDataList, resistance);
    }

    /**
     * The pearson correlation of this signal relative to some other signal
     * 
     * @param otherSignal
     *            other signal to calculate correlation
     * 
     * @return {@code Complex} correlation value
     * 
     * @exception IllegalArgumentException
     *                if this Signal.getIqDataList() and otherSignal.getIqDataList() is not the same size
     */
    public Complex correlationTo(Signal otherSignal) {
        return ComplexAdditions.pearsonCorrelation(iqDataList, otherSignal.getIqDataList());
    }

}
