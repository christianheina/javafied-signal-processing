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

package com.christianheina.communication.javafied.signalprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.complex.Complex;

import com.christianheina.communication.javafied.signalprocessing.data.FrequencyDomainSignal;
import com.christianheina.communication.javafied.signalprocessing.data.SignalFactory;
import com.christianheina.communication.javafied.signalprocessing.data.TimeDomainSignal;
import com.christianheina.communication.javafied.signalprocessing.exceptions.SignalProcessingException;

/**
 * Signal processing with multiple outputs.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class SignalProcessing {

    private SignalProcessing() {
        /* Hidden Constructor */
    }

    /**
     * Splits IQ data using a given interval. Calculates power for each interval.<br>
     * <strong>Note!</strong> Only full intervals will be included. Any partial intervals will be excluded.
     * 
     * @param iqData
     *            IQ data to split and calculate power on.
     * @param resistance
     *            the resistance in ohms.
     * @param interval
     *            The interval in seconds to split of split IQ data
     * 
     * @return power in dBm for each interval.
     */
    public static List<Double> powerForTimeInterval(TimeDomainSignal iqData, double resistance, double interval) {
        return powerForTimeInterval(iqData, resistance, 0, interval, interval);
    }

    /**
     * Splits IQ data using a given interval. Calculates power for each interval.<br>
     * <strong>Note!</strong> Only full intervals will be included. Any partial intervals will be excluded.
     * 
     * @param iqData
     *            IQ data to split and calculate power on.
     * @param resistance
     *            the resistance in ohms.
     * @param offset
     *            the offset in seconds for the first interval
     * @param interval
     *            The interval in seconds to split of split IQ data
     * 
     * @return power in dBm for each interval.
     */
    public static List<Double> powerForTimeInterval(TimeDomainSignal iqData, double resistance, double offset,
            double interval) {
        return powerForTimeInterval(iqData, resistance, offset, interval, interval);
    }

    /**
     * Splits IQ data using a given interval and period. Calculates power for each interval.<br>
     * <strong>Note!</strong> Only full intervals will be included. Any partial intervals will be excluded.
     * 
     * @param iqData
     *            IQ data to split and calculate power on.
     * @param resistance
     *            the resistance in ohms.
     * @param offset
     *            the offset in seconds for the first interval
     * @param interval
     *            The interval in seconds to split of split IQ data
     * @param period
     *            The period in seconds in which this interval occurs
     * 
     * @return power in dBm for each interval.
     */
    public static List<Double> powerForTimeInterval(TimeDomainSignal iqData, double resistance, double offset,
            double interval, double period) {
        List<Double> powerList = new ArrayList<>();
        List<TimeDomainSignal> iqDataIntervalList = splitIqDataForTimeInterval(iqData, offset, interval, period);

        for (TimeDomainSignal iqDataEntry : iqDataIntervalList) {
            powerList.add(iqDataEntry.toAveragePowerDbm(resistance));
        }
        return powerList;
    }

    /**
     * Split {@link TimeDomainSignal} in to smaller {@link TimeDomainSignal} objects.
     * 
     * @param iqData
     *            IQ data to split and calculate power on.
     * @param interval
     *            The interval in seconds to split of split IQ data
     * 
     * @return {@link TimeDomainSignal} list containing splits
     */
    public static List<TimeDomainSignal> splitIqDataForTimeInterval(TimeDomainSignal iqData, double interval) {
        return splitIqDataForTimeInterval(iqData, 0, interval, interval);
    }

    /**
     * Split {@link TimeDomainSignal} in to smaller {@link TimeDomainSignal} objects.
     * 
     * @param iqData
     *            IQ data to split and calculate power on.
     * @param offset
     *            the offset in seconds for the first interval
     * @param interval
     *            The interval in seconds to split of split IQ data
     * 
     * @return {@link TimeDomainSignal} list containing splits
     */
    public static List<TimeDomainSignal> splitIqDataForTimeInterval(TimeDomainSignal iqData, double offset,
            double interval) {
        return splitIqDataForTimeInterval(iqData, offset, interval, interval);
    }

    /**
     * Split {@link TimeDomainSignal} in to smaller {@link TimeDomainSignal} objects.
     * 
     * @param iqData
     *            IQ data to split and calculate power on.
     * @param offset
     *            the offset in seconds for the first interval
     * @param interval
     *            The interval in seconds to split of split IQ data
     * @param period
     *            The period in seconds in which this interval occurs
     * 
     * @return {@link TimeDomainSignal} list containing splits
     */
    public static List<TimeDomainSignal> splitIqDataForTimeInterval(TimeDomainSignal iqData, double offset,
            double interval, double period) {
        int nOffsetSamples = (int) Math.ceil(iqData.getSampleRate() * offset);
        int nIntervalSamples = (int) (iqData.getSampleRate() * interval);
        int nPeriodSamples = (int) (iqData.getSampleRate() * period);
        List<TimeDomainSignal> iqDataList = new ArrayList<>();
        for (int i = nOffsetSamples; i + nIntervalSamples <= iqData.getIqDataList().size(); i += nPeriodSamples) {
            iqDataList.add(SignalFactory.newTimeDomainSignal(iqData.getIqDataList().subList(i, i + nIntervalSamples),
                    iqData.getSampleRate()));
        }
        return iqDataList;
    }

    /**
     * Select frequency range from {@link FrequencyDomainSignal}.
     * 
     * @param signal
     *            {@link FrequencyDomainSignal} to select frequency range from.
     * @param frequencyRange
     *            the frequency range to select in Hz relative to IQ center
     * 
     * @return {@link FrequencyDomainSignal} containing only frequency range
     */
    public static FrequencyDomainSignal iqDataSubsetForFrequencyRange(FrequencyDomainSignal signal,
            long frequencyRange) {
        if (frequencyRange > signal.getSampleRate()) {
            throw new SignalProcessingException("Requested frequency range is larger than data sample rate");
        }

        double[] freqs = IntStream.range(0, signal.getIqDataList().size()).mapToDouble(
                i -> (i - signal.getIqDataList().size() / 2.0) * signal.getSampleRate() / signal.getIqDataList().size())
                .toArray();

        long halfFrequencyRange = frequencyRange / 2;
        int[] indexes = IntStream.range(0, freqs.length)
                .filter(i -> freqs[i] >= -halfFrequencyRange && freqs[i] <= halfFrequencyRange).toArray();
        List<Complex> iqDataList = signal.getIqDataList().subList(indexes[0], indexes[indexes.length - 1]);

        return SignalFactory.newFrequencyDomainSignal(iqDataList, signal.getSampleRate());
    }

    /**
     * Filter signal replacing unwanted frequency range with zero
     * 
     * @param signal
     *            {@link TimeDomainSignal} to select frequency range from.
     * @param frequencyRange
     *            the frequency range to select in Hz relative to IQ center
     * 
     * @return {@link TimeDomainSignal} containing only filtered signal
     */
    public static TimeDomainSignal filterReplaceWithZero(TimeDomainSignal signal, long frequencyRange) {
        return filterReplaceWithZero(signal.asFrequencyDomainSignal(), frequencyRange);
    }

    /**
     * Filter signal replacing unwanted frequency range with zero
     * 
     * @param signal
     *            {@link FrequencyDomainSignal} to select frequency range from.
     * @param frequencyRange
     *            the frequency range to select in Hz relative to IQ center
     * 
     * @return {@link TimeDomainSignal} containing only filtered signal
     */
    public static TimeDomainSignal filterReplaceWithZero(FrequencyDomainSignal signal, long frequencyRange) {
        if (frequencyRange > signal.getSampleRate()) {
            throw new SignalProcessingException("Requested frequency range is larger than data sample rate");
        }
        int samples = (int) (frequencyRange / ((double) signal.getSampleRate() / signal.getIqDataList().size()));
        int lowIndex = (int) Math.ceil(samples / 2.0);
        int highIndex = signal.getIqDataList().size() - (int) Math.ceil(samples / 2.0);
        List<Complex> iqDataList = new ArrayList<>();
        for (int i = 0; i < signal.getIqDataList().size(); i++) {
            if (i < lowIndex || i >= highIndex) {
                iqDataList.add(Complex.ZERO);
            } else {
                iqDataList.add(signal.getIqDataList().get(i));
            }
        }
        return SignalFactory.newFrequencyDomainSignal(iqDataList, signal.getSampleRate()).asTimeDomainSignal();
    }

}
