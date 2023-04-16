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

import org.apache.commons.math3.complex.Complex;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.christianheina.communication.javafied.signalprocessing.data.FrequencyDomainSignal;
import com.christianheina.communication.javafied.signalprocessing.data.SignalFactory;
import com.christianheina.communication.javafied.signalprocessing.data.TimeDomainSignal;
import com.christianheina.communication.javafied.signalprocessing.exceptions.SignalProcessingException;

/**
 * Unit test for {@link SignalProcessing}.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class SignalProcessingTest {

    private static final double THRESHOLD = 1E-10;
    private static final double REAL = -0.0044;
    private static final double IMAG = 0.0021;

    private TimeDomainSignal iqData;

    @BeforeTest
    public void createIqData() {
        List<Complex> iqDataList = new ArrayList<>();
        for (int i = 0; i < 1228800; i++) {
            iqDataList.add(new Complex(REAL, IMAG));
        }
        iqData = SignalFactory.newTimeDomainSignal(iqDataList, 122880000);
    }

    @Test
    public void powerForTimeIntervalUsingOnlyIntervalTest() {
        List<Double> powerForIntervalList = SignalProcessing.powerForTimeInterval(iqData, 50.0, 0.002);
        Assert.assertEquals(powerForIntervalList.size(), 5);
        for (double meanPower : powerForIntervalList) {
            Assert.assertTrue(Math.abs(meanPower - 10 * Math
                    .log10(Math.pow(Math.sqrt(Math.pow(REAL, 2) + Math.pow(IMAG, 2)), 2) / 50 / 0.001)) < THRESHOLD);
        }
    }

    @Test
    public void powerForTimeIntervalUsingOffsetAndIntervalTest() {
        List<Double> powerForIntervalList = SignalProcessing.powerForTimeInterval(iqData, 50.0, 0.002, 0.002);
        Assert.assertEquals(powerForIntervalList.size(), 4);
        for (double meanPower : powerForIntervalList) {
            Assert.assertTrue(Math.abs(meanPower - 10 * Math
                    .log10(Math.pow(Math.sqrt(Math.pow(REAL, 2) + Math.pow(IMAG, 2)), 2) / 50 / 0.001)) < THRESHOLD);
        }
    }

    @Test
    public void powerForTimeIntervalUsingOffsetIntervalAndPeriodTest() {
        List<Double> powerForIntervalList = SignalProcessing.powerForTimeInterval(iqData, 50, 0, 0.002, 0.005);
        Assert.assertEquals(powerForIntervalList.size(), 2);
        for (double meanPower : powerForIntervalList) {
            Assert.assertTrue(Math.abs(meanPower - 10 * Math
                    .log10(Math.pow(Math.sqrt(Math.pow(REAL, 2) + Math.pow(IMAG, 2)), 2) / 50 / 0.001)) < THRESHOLD);
        }
    }

    @Test
    public void splitIqDataForTimeIntervalUsingIntervalTest() {
        List<TimeDomainSignal> powerForIntervalList = SignalProcessing.splitIqDataForTimeInterval(iqData, 0.002);
        Assert.assertEquals(powerForIntervalList.size(), 5);
    }

    @Test
    public void splitIqDataForTimeIntervalUsingOffsetAndIntervalTest() {
        List<TimeDomainSignal> powerForIntervalList = SignalProcessing.splitIqDataForTimeInterval(iqData, 0.002, 0.002);
        Assert.assertEquals(powerForIntervalList.size(), 4);
    }

    @Test
    public void splitIqDataForTimeIntervalUsingOffsetIntervalAndPeriodTest() {
        List<TimeDomainSignal> powerForIntervalList = SignalProcessing.splitIqDataForTimeInterval(iqData, 0, 0.002,
                0.005);
        Assert.assertEquals(powerForIntervalList.size(), 2);
    }

    @Test
    public void iqDataSubsetForFrequencyRangeTest() {
        List<Complex> data = new ArrayList<>();
        data.add(Complex.ONE.multiply(-0.5));
        data.add(Complex.ONE);
        data.add(Complex.ONE.multiply(-1));
        data.add(Complex.ZERO.multiply(0.5));
        FrequencyDomainSignal freqSig = SignalFactory.newFrequencyDomainSignal(data, 4);
        FrequencyDomainSignal filteredFreqSig = SignalProcessing.iqDataSubsetForFrequencyRange(freqSig, 2);
        Assert.assertNotEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), 2);
        Assert.assertEquals(filteredFreqSig.getIqDataList().get(0), Complex.ONE);
        Assert.assertEquals(filteredFreqSig.getIqDataList().get(1), Complex.ONE.multiply(-1));

        data.add(Complex.ONE.multiply(-1));
        data.add(Complex.ZERO.multiply(0.5));
        filteredFreqSig = SignalProcessing.iqDataSubsetForFrequencyRange(freqSig, 2);
        Assert.assertNotEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), 2);
        Assert.assertEquals(filteredFreqSig.getIqDataList().get(0), Complex.ONE.multiply(-1));
        Assert.assertEquals(filteredFreqSig.getIqDataList().get(1), Complex.ZERO.multiply(0.5));

        data.add(Complex.ONE.multiply(-1));
        data.add(Complex.ZERO.multiply(0.5));
        filteredFreqSig = SignalProcessing.iqDataSubsetForFrequencyRange(freqSig, 2);
        Assert.assertNotEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), 4);
        Assert.assertEquals(filteredFreqSig.getIqDataList().get(0), Complex.ONE.multiply(-1));
        Assert.assertEquals(filteredFreqSig.getIqDataList().get(1), Complex.ZERO.multiply(0.5));
        Assert.assertEquals(filteredFreqSig.getIqDataList().get(2), Complex.ONE.multiply(-1));
        Assert.assertEquals(filteredFreqSig.getIqDataList().get(3), Complex.ZERO.multiply(0.5));
    }

    @Test(expectedExceptions = SignalProcessingException.class)
    public void iqDataSubsetForFrequencyRangeExceptionTest() {
        List<Complex> data = new ArrayList<>();
        data.add(Complex.ONE.multiply(-0.5));
        data.add(Complex.ONE);
        data.add(Complex.ONE.multiply(-1));
        data.add(Complex.ZERO.multiply(0.5));
        FrequencyDomainSignal freqSig = SignalFactory.newFrequencyDomainSignal(data, 4);
        // Should throw exception
        SignalProcessing.iqDataSubsetForFrequencyRange(freqSig, 10);
    }

    @Test
    public void filterReplaceWithZeroTimeDomainTest() {
        List<Complex> data = new ArrayList<>();
        data.add(Complex.ONE.multiply(-0.5));
        data.add(Complex.ONE);
        data.add(new Complex(-1, 0));
        data.add(Complex.ZERO.multiply(0.5));
        FrequencyDomainSignal freqSig = SignalFactory.newFrequencyDomainSignal(data, 4);
        FrequencyDomainSignal filteredFreqSig = SignalProcessing.filterReplaceWithZero(freqSig.asTimeDomainSignal(), 2)
                .asFrequencyDomainSignal();
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(0), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(1), data.get(1)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(2), data.get(2)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(3), Complex.ZERO));

        data.add(new Complex(-1, 0));
        data.add(Complex.ZERO.multiply(0.5));
        filteredFreqSig = SignalProcessing.filterReplaceWithZero(freqSig.asTimeDomainSignal(), 2)
                .asFrequencyDomainSignal();
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(0), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(1), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(2), data.get(2)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(3), data.get(3)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(4), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(5), Complex.ZERO));

        data.add(new Complex(-1, 0));
        data.add(Complex.ZERO.multiply(0.5));
        filteredFreqSig = SignalProcessing.filterReplaceWithZero(freqSig.asTimeDomainSignal(), 2)
                .asFrequencyDomainSignal();
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(0), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(1), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(2), data.get(2)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(3), data.get(3)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(4), data.get(4)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(5), data.get(5)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(6), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(7), Complex.ZERO));
    }

    @Test
    public void filterReplaceWithZeroFrequencyDomainTest() {
        List<Complex> data = new ArrayList<>();
        data.add(Complex.ONE.multiply(-0.5));
        data.add(Complex.ONE);
        data.add(new Complex(-1, 0));
        data.add(Complex.ZERO.multiply(0.5));
        FrequencyDomainSignal freqSig = SignalFactory.newFrequencyDomainSignal(data, 4);
        FrequencyDomainSignal filteredFreqSig = SignalProcessing.filterReplaceWithZero(freqSig, 2)
                .asFrequencyDomainSignal();
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(0), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(1), data.get(1)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(2), data.get(2)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(3), Complex.ZERO));

        data.add(new Complex(-1, 0));
        data.add(Complex.ZERO.multiply(0.5));
        filteredFreqSig = SignalProcessing.filterReplaceWithZero(freqSig, 2).asFrequencyDomainSignal();
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(0), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(1), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(2), Complex.ONE.multiply(-1)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(3), Complex.ZERO.multiply(0.5)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(4), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(5), Complex.ZERO));

        data.add(new Complex(-1, 0));
        data.add(Complex.ZERO.multiply(0.5));
        filteredFreqSig = SignalProcessing.filterReplaceWithZero(freqSig, 2).asFrequencyDomainSignal();
        Assert.assertEquals(filteredFreqSig.getIqDataList().size(), freqSig.getIqDataList().size());
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(0), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(1), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(2), Complex.ONE.multiply(-1)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(3), Complex.ZERO.multiply(0.5)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(4), Complex.ONE.multiply(-1)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(5), Complex.ZERO.multiply(0.5)));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(6), Complex.ZERO));
        Assert.assertTrue(complexEquals(filteredFreqSig.getIqDataList().get(7), Complex.ZERO));
    }

    @Test(expectedExceptions = SignalProcessingException.class)
    public void filterReplaceWithZeroFreqRangeExceptionTest() {
        List<Complex> data = new ArrayList<>();
        data.add(Complex.ONE.multiply(-0.5));
        data.add(Complex.ONE);
        data.add(new Complex(-1, 0));
        data.add(Complex.ZERO.multiply(0.5));
        FrequencyDomainSignal freqSig = SignalFactory.newFrequencyDomainSignal(data, 4);
        // Should throw exception
        SignalProcessing.filterReplaceWithZero(freqSig, 10).asFrequencyDomainSignal();
    }

    private boolean complexEquals(Complex complex1, Complex complex2) {
        return Math.abs(complex1.getReal() - complex2.getReal()) < THRESHOLD
                && Math.abs(complex1.getImaginary() - complex2.getImaginary()) < THRESHOLD;
    }

}
