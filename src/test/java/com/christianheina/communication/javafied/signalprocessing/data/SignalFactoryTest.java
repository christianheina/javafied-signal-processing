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

import java.nio.ByteOrder;
import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.christianheina.communication.javafied.signalprocessing.enums.BinaryIqFormat;
import com.christianheina.communication.javafied.signalprocessing.exceptions.SignalProcessingException;

/**
 * Unit test for {@link SignalFactory}.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class SignalFactoryTest {

    @Test
    public void newIqDataFromIqListTest() {
        TimeDomainSignal iqData = SignalFactory.newTimeDomainSignal(new ArrayList<>(), 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), TimeDomainSignal.class);
        Assert.assertTrue(iqData.getIqDataList().isEmpty());
    }

    @Test
    public void newIqDataFromCsvTest() {
        String iqString = "1,0";
        TimeDomainSignal iqData = SignalFactory.newTimeDomainSignal(iqString, 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), TimeDomainSignal.class);
        Assert.assertEquals(iqData.getIqDataList().get(0), new Complex(1, 0));
    }

    @Test(expectedExceptions = SignalProcessingException.class)
    public void newIqDataFromIncompleteCsvTest() {
        String iqString = "1";
        TimeDomainSignal iqData = SignalFactory.newTimeDomainSignal(iqString, 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), TimeDomainSignal.class);
        Assert.assertEquals(iqData.getIqDataList().get(0), new Complex(1, 0));
    }

    @Test
    public void newIqDataFromByteArrayHalfTest() {
        byte iqBytes[] = new byte[] { 60, 0, 0, 0 };
        TimeDomainSignal iqData = SignalFactory.newTimeDomainSignal(iqBytes, BinaryIqFormat.FLOAT_16,
                ByteOrder.BIG_ENDIAN, 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), TimeDomainSignal.class);
        Assert.assertEquals(iqData.getIqDataList().get(0), new Complex(1, 0));
    }

    @Test
    public void newIqDataFromByteArrayFloatTest() {
        byte iqBytes[] = new byte[] { 63, -128, 0, 0, 0, 0, 0, 0 };
        TimeDomainSignal iqData = SignalFactory.newTimeDomainSignal(iqBytes, BinaryIqFormat.FLOAT_32,
                ByteOrder.BIG_ENDIAN, 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), TimeDomainSignal.class);
        Assert.assertEquals(iqData.getIqDataList().get(0), new Complex(1, 0));
    }

    @Test
    public void newIqDataFromByteArrayDoubleTest() {
        byte iqBytes[] = new byte[] { 63, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        TimeDomainSignal iqData = SignalFactory.newTimeDomainSignal(iqBytes, BinaryIqFormat.FLOAT_64,
                ByteOrder.BIG_ENDIAN, 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), TimeDomainSignal.class);
        Assert.assertEquals(iqData.getIqDataList().get(0), new Complex(1, 0));
    }

    @Test(expectedExceptions = SignalProcessingException.class)
    public void newIqDataFromIncompleteByteArrayTest() {
        byte iqBytes[] = new byte[] { 63, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        TimeDomainSignal iqData = SignalFactory.newTimeDomainSignal(iqBytes, BinaryIqFormat.FLOAT_32,
                ByteOrder.BIG_ENDIAN, 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), TimeDomainSignal.class);
        Assert.assertEquals(iqData.getIqDataList().get(0), new Complex(1, 0));
    }

    @Test(expectedExceptions = SignalProcessingException.class)
    public void newIqDataFromNonPairByteArrayTest() {
        byte iqBytes[] = new byte[] { 63, -128, 0, 0, 0, 0, 0, 0, 0, 0 };
        TimeDomainSignal iqData = SignalFactory.newTimeDomainSignal(iqBytes, BinaryIqFormat.FLOAT_32,
                ByteOrder.BIG_ENDIAN, 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), TimeDomainSignal.class);
        Assert.assertEquals(iqData.getIqDataList().get(0), new Complex(1, 0));
    }

    @Test
    public void newFrequencyDomainSignalTest() {
        FrequencyDomainSignal iqData = SignalFactory.newFrequencyDomainSignal(new ArrayList<>(), 122800000);
        Assert.assertNotEquals(iqData, null);
        Assert.assertEquals(iqData.getClass(), FrequencyDomainSignal.class);
        Assert.assertTrue(iqData.getIqDataList().isEmpty());
    }

}
