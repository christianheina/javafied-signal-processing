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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import com.christianheina.communication.javafied.signalprocessing.enums.BinaryIqFormat;
import com.christianheina.communication.javafied.signalprocessing.exceptions.SignalProcessingException;
import com.christianheina.langx.half4j.Half;

/**
 * Factory for {@link Signal}
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class SignalFactory {

    private SignalFactory() {
        /* Hidden Constructor */
    }

    /**
     * Create new {@link TimeDomainSignal} instance
     * 
     * @param iqDataList
     *            {@link Complex} {@link List} in time domain
     * @param sampleRate
     *            the sample rate of IQ data
     * 
     * @return new {@link TimeDomainSignal} instance
     */
    public static TimeDomainSignal newTimeDomainSignal(List<Complex> iqDataList, int sampleRate) {
        return new TimeDomainSignal(iqDataList, sampleRate);
    }

    /**
     * Create new {@link TimeDomainSignal} instance from CSV string containing I and Q pairs separated by comma.
     * 
     * @param csvString
     *            CSV to convert.
     * @param sampleRate
     *            the sample rate of IQ data
     * 
     * @return new {@link TimeDomainSignal} instance containing the converted CSV
     */
    public static TimeDomainSignal newTimeDomainSignal(String csvString, int sampleRate) {
        String[] csvArray = csvString.split(",");
        if (csvArray.length % 2 != 0) {
            throw new SignalProcessingException("CSV String needs to be in I and Q pairs");
        }
        List<Complex> iqValueList = new ArrayList<Complex>();
        for (int i = 0; i < csvArray.length; i += 2) {
            iqValueList.add(new Complex(Double.parseDouble(csvArray[i]), Double.parseDouble(csvArray[i + 1])));
        }
        return new TimeDomainSignal(iqValueList, sampleRate);
    }

    /**
     * Create new {@link TimeDomainSignal} instance from byte array containing I and Q pairs.<br>
     * <strong>NOTE!</strong> Method uses default byte order of {@link ByteOrder#BIG_ENDIAN}.
     * 
     * @param iqBytes
     *            byte array containing I and Q pairs.
     * @param format
     *            the binary format of conversion
     * @param sampleRate
     *            the sample rate of IQ data
     * 
     * @return new {@link TimeDomainSignal} instance containing the converted byte array
     * 
     * @deprecated As of 2023-07-09 this is replaced by
     *             {@link com.christianheina.communication.javafied.signalprocessing.data.SignalFactory#newTimeDomainSignal(byte[], BinaryIqFormat, ByteOrder, int)
     *             newTimeDomainSignal(byte[] iqBytes, BinaryIqFormat format, ByteOrder byteOrder, int sampleRate)}.
     *             This method will be supported until 2023-10-09 and removed 2024-01-09.
     */
    @Deprecated
    public static TimeDomainSignal newTimeDomainSignal(byte[] iqBytes, BinaryIqFormat format, int sampleRate) {
        return newTimeDomainSignal(iqBytes, format, ByteOrder.BIG_ENDIAN, sampleRate);
    }

    /**
     * Create new {@link TimeDomainSignal} instance from byte array containing I and Q pairs.
     * 
     * @param iqBytes
     *            byte array containing I and Q pairs.
     * @param format
     *            the binary format of conversion
     * @param byteOrder
     *            the iqBytes byte order
     * @param sampleRate
     *            the sample rate of IQ data
     * 
     * @return new {@link TimeDomainSignal} instance containing the converted byte array
     */
    public static TimeDomainSignal newTimeDomainSignal(byte[] iqBytes, BinaryIqFormat format, ByteOrder byteOrder,
            int sampleRate) {
        if (iqBytes.length % format.getByteLength() != 0) {
            throw new SignalProcessingException(
                    "IQ byte array and format does not match. Please make sure expected format matches byte array");
        }
        int chunks = iqBytes.length / format.getByteLength();
        if (chunks % 2 != 0) {
            throw new SignalProcessingException("IQ byte values need to be in I and Q pairs");
        }
        List<Complex> iqValueList = new ArrayList<Complex>();
        ByteBuffer buf = ByteBuffer.wrap(iqBytes).order(byteOrder);
        for (int i = 0; i < chunks; i += 2) {
            double inPhase = 0;
            double quadrature = 0;
            if (format == BinaryIqFormat.FLOAT_16) {
                inPhase = Half.shortBitsToHalf(buf.getShort()).doubleValue();
                quadrature = Half.shortBitsToHalf(buf.getShort()).doubleValue();
            } else if (format == BinaryIqFormat.FLOAT_32) {
                inPhase = buf.getFloat();
                quadrature = buf.getFloat();
            } else if (format == BinaryIqFormat.FLOAT_64) {
                inPhase = buf.getDouble();
                quadrature = buf.getDouble();
            } else {
                throw new SignalProcessingException(format + " is currently not supported");
            }
            iqValueList.add(new Complex(inPhase, quadrature));
        }
        return new TimeDomainSignal(iqValueList, sampleRate);
    }

    /**
     * Create new {@link TimeDomainSignal} instance from byte array containing I and Q pairs.
     * 
     * @param iqBytes
     *            byte array containing I and Q pairs.
     * @param format
     *            the binary format of conversion
     * @param sampleRate
     *            the sample rate of IQ data
     * 
     * @return new {@link TimeDomainSignal} instance containing the converted byte array
     * 
     * @deprecated As of 2023-04-16 this is replaced by
     *             {@link com.christianheina.communication.javafied.signalprocessing.data.SignalFactory#newTimeDomainSignal(byte[], BinaryIqFormat, int)
     *             newTimeDomainSignal(byte[] iqBytes, BinaryIqFormat format, int sampleRate)}. This method will be
     *             supported until 2023-07-16 and removed 2023-10-16.
     */
    @Deprecated
    public static TimeDomainSignal newTimeDomainSignal(byte[] iqBytes,
            com.christianheina.communication.javafied.signalprocessing.data.BinaryIqFormat format, int sampleRate) {
        if (format == com.christianheina.communication.javafied.signalprocessing.data.BinaryIqFormat.FLOAT_16) {
            return newTimeDomainSignal(iqBytes, BinaryIqFormat.FLOAT_16, sampleRate);
        } else if (format == com.christianheina.communication.javafied.signalprocessing.data.BinaryIqFormat.FLOAT_32) {
            return newTimeDomainSignal(iqBytes, BinaryIqFormat.FLOAT_32, sampleRate);
        } else {
            return newTimeDomainSignal(iqBytes, BinaryIqFormat.FLOAT_64, sampleRate);
        }
    }

    /**
     * Create new {@link FrequencyDomainSignal} instance
     * 
     * @param iqDataList
     *            {@link Complex} {@link List} in frequency domain
     * @param sampleRate
     *            the sample rate of IQ data
     * 
     * @return new {@link FrequencyDomainSignal} instance
     */
    public static FrequencyDomainSignal newFrequencyDomainSignal(List<Complex> iqDataList, int sampleRate) {
        return new FrequencyDomainSignal(iqDataList, sampleRate);
    }

}
