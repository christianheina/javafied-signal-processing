/*
 * Copyright 2023 Christian Heina
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

package com.christianheina.communication.javafied.signalprocessing.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for {@link PowerUtil}.
 * 
 * @author Christian Heina (developer@christianheina.com)
 * 
 * @deprecated As of 2025-02-05 this class is deprecated. This class will be supported until 2025-05-05 and removed
 *             2023-08-05.
 */
@Deprecated
public class PowerUtilTest {

    private static final Complex COMPLEX_DATA_VALUE = new Complex(1, 0);
    private static final List<Complex> COMPLEX_LIST = Arrays.asList(new Complex[] { COMPLEX_DATA_VALUE });
    private static final List<Double> MAGNITUDE_LIST = PowerUtil.complexListToMagnitudeList(COMPLEX_LIST);
    private static final double RESISTANCE = 50;

    @Test
    public void complexListToMagnitudeListTest() {
        List<Double> magnitudeList = PowerUtil.complexListToMagnitudeList(COMPLEX_LIST);
        for (int i = 0; i < magnitudeList.size(); i++) {
            Assert.assertEquals(PowerUtil.complexToMagnitude(COMPLEX_LIST.get(i)), magnitudeList.get(i), 1e-9);
        }
    }

    @Test
    public void complexToMagnitudeTest() {
        Assert.assertEquals(1, PowerUtil.complexToMagnitude(Complex.ONE), 1e-9);
        Assert.assertEquals(1, PowerUtil.complexToMagnitude(Complex.ONE.multiply(-1)), 1e-9);
        Assert.assertEquals(2, PowerUtil.complexToMagnitude(new Complex(2, 0)), 1e-9);
        Assert.assertEquals(5, PowerUtil.complexToMagnitude(new Complex(3, 4)), 1e-9);
    }

    @Test
    public void complexListToPowerListTest() {
        List<Double> powerList = PowerUtil.complexListToPowerList(COMPLEX_LIST, RESISTANCE);
        for (int i = 0; i < powerList.size(); i++) {
            Assert.assertEquals(
                    PowerUtil.magnitudeToPower(PowerUtil.complexToMagnitude(COMPLEX_LIST.get(i)), RESISTANCE),
                    powerList.get(i), 1e-9);
        }
    }

    @Test
    public void magnitudeToPowerTest() {
        Assert.assertEquals(1, PowerUtil.magnitudeToPower(1, 1), 1e-9);
        Assert.assertEquals(50, PowerUtil.magnitudeToPower(50, RESISTANCE), 1e-9);
        Assert.assertEquals(8, PowerUtil.magnitudeToPower(20, RESISTANCE), 1e-9);
        Assert.assertEquals(0.02, PowerUtil.magnitudeToPower(1, RESISTANCE), 1e-9);
    }

    @Test
    public void complexListToPowerDbmListTest() {
        List<Double> powerList = PowerUtil.complexListToPowerDbmList(COMPLEX_LIST, RESISTANCE);
        for (int i = 0; i < powerList.size(); i++) {
            Assert.assertEquals(
                    PowerUtil.wattsToDbm(
                            PowerUtil.magnitudeToPower(PowerUtil.complexToMagnitude(COMPLEX_LIST.get(i)), RESISTANCE)),
                    powerList.get(i), 1e-9);
        }
    }

    @Test
    public void magnitudeListToPowerDbmListTest() {
        List<Double> powerList = PowerUtil.magnitudeListToPowerDbmList(MAGNITUDE_LIST, RESISTANCE);
        for (int i = 0; i < powerList.size(); i++) {
            Assert.assertEquals(PowerUtil.wattsToDbm(PowerUtil.magnitudeToPower(MAGNITUDE_LIST.get(i), RESISTANCE)),
                    powerList.get(i), 1e-9);
        }
    }

    @Test
    public void complexListToSumPowerDbmTest() {
        double sum = PowerUtil.complexListToSumPowerDbm(COMPLEX_LIST, RESISTANCE);
        Assert.assertEquals(
                PowerUtil.wattsToDbm(
                        PowerUtil.magnitudeToPower(PowerUtil.complexToMagnitude(COMPLEX_DATA_VALUE), RESISTANCE)),
                sum, 1e-9);
    }

    @Test
    public void magnitudeListToSumPowerDbmTest() {
        double sum = PowerUtil.magnitudeListToSumPowerDbm(MAGNITUDE_LIST, RESISTANCE);
        Assert.assertEquals(
                PowerUtil.wattsToDbm(
                        PowerUtil.magnitudeToPower(PowerUtil.complexToMagnitude(COMPLEX_DATA_VALUE), RESISTANCE)),
                sum, 1e-9);
    }

    @Test
    public void complexListToAveragePowerDbmTest() {
        double mean = PowerUtil.complexListToAveragePowerDbm(COMPLEX_LIST, RESISTANCE);
        Assert.assertEquals(
                PowerUtil.wattsToDbm(
                        PowerUtil.magnitudeToPower(PowerUtil.complexToMagnitude(COMPLEX_DATA_VALUE), RESISTANCE)),
                mean, 1e-9);
    }

    @Test
    public void magnitudeListToAveragePowerDbmTest() {
        double mean = PowerUtil.magnitudeListToAveragePowerDbm(MAGNITUDE_LIST, RESISTANCE);
        Assert.assertEquals(
                PowerUtil.wattsToDbm(
                        PowerUtil.magnitudeToPower(PowerUtil.complexToMagnitude(COMPLEX_DATA_VALUE), RESISTANCE)),
                mean, 1e-9);
    }

    @Test
    public void wattsToDbmTest() {
        Assert.assertEquals(Double.NEGATIVE_INFINITY, PowerUtil.wattsToDbm(0), 1e-9);
        Assert.assertEquals(30, PowerUtil.wattsToDbm(1), 1e-9);
        Assert.assertEquals(46.020599913279625, PowerUtil.wattsToDbm(40), 1e-9);
    }

    @Test
    public void dbmToWattsTest() {
        Assert.assertEquals(0, PowerUtil.dBmToWatts(Double.NEGATIVE_INFINITY), 1e-9);
        Assert.assertEquals(1, PowerUtil.dBmToWatts(30), 1e-9);
        Assert.assertEquals(40, PowerUtil.dBmToWatts(46.020599913279625), 1e-9);
    }

}
