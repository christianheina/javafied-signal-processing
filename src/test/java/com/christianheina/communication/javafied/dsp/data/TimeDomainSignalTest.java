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
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TimeDomainSignalTest {

    private static final int SAMPLE_RATE = 120;
    private static final double RESISTANCE = 50;
    private static final Complex IQ_DATA_VALUE = new Complex(1, 0);

    private TimeDomainSignal iqData;
    private List<Complex> iqDataList;

    @BeforeMethod
    public void create() {
        iqDataList = new ArrayList<>();
        iqDataList.add(IQ_DATA_VALUE);
        iqData = new TimeDomainSignal(iqDataList, SAMPLE_RATE);
    }

    @Test
    public void getSampleRateTest() {
        int sampleRate = iqData.getSampleRate();
        Assert.assertEquals(sampleRate, SAMPLE_RATE);
    }

    @Test
    public void getIqDataListTest() {
        List<Complex> iqDataList = iqData.getIqDataList();
        Assert.assertEquals(iqDataList.size(), 1);
        Assert.assertEquals(iqDataList.get(0), IQ_DATA_VALUE);
    }

    @Test
    public void getMagnitudeTest() {
        List<Double> magnitudeList = iqData.toMagnitude();
        for (int i = 0; i < magnitudeList.size(); i++) {
            Assert.assertTrue(magnitudeList.get(i) == Math
                    .sqrt(Math.pow(iqDataList.get(i).getReal(), 2) + Math.pow(iqDataList.get(i).getImaginary(), 2)));
        }
    }

    @Test
    public void getPowerTest() {
        List<Double> powerList = iqData.toPower(RESISTANCE);
        for (int i = 0; i < powerList.size(); i++) {
            Assert.assertTrue(powerList.get(i) == Math.pow(
                    Math.sqrt(Math.pow(iqDataList.get(i).getReal(), 2) + Math.pow(iqDataList.get(i).getImaginary(), 2)),
                    2) / RESISTANCE);
        }
    }

    @Test
    public void getPowerDbmTest() {
        List<Double> powerList = iqData.toPowerDbm(RESISTANCE);
        for (int i = 0; i < powerList.size(); i++) {
            Assert.assertTrue(powerList.get(i) == 10 * Math.log10(Math.pow(
                    Math.sqrt(Math.pow(iqDataList.get(i).getReal(), 2) + Math.pow(iqDataList.get(i).getImaginary(), 2)),
                    2) / RESISTANCE / 0.001));
        }
    }

    @Test
    public void meanPowerTest() {
        double meanPower = iqData.toAveragePowerDbm(RESISTANCE);
        Assert.assertTrue(meanPower == 10 * Math.log10(
                Math.pow(Math.sqrt(Math.pow(IQ_DATA_VALUE.getReal(), 2) + Math.pow(IQ_DATA_VALUE.getImaginary(), 2)), 2)
                        / RESISTANCE / 0.001));
    }

}
