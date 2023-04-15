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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import com.christianheina.communication.javafied.signalprocessing.exceptions.SignalProcessingException;

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
     */
    public static TimeDomainSignal newTimeDomainSignal(byte iqBytes[], BinaryIqFormat format, int sampleRate) {
        if (iqBytes.length % format.getByteLength() != 0) {
            throw new SignalProcessingException(
                    "IQ byte array and format does not match. Please make sure expected format matches byte array");
        }
        int chunks = iqBytes.length / format.getByteLength();
        if (chunks % 2 != 0) {
            throw new SignalProcessingException("IQ byte values need to be in I and Q pairs");
        }
        List<Complex> iqValueList = new ArrayList<Complex>();
        for (int i = 0; i < chunks; i += 2) {
            double inPhase = 0;
            double quadrature = 0;
            if (format == BinaryIqFormat.FLOAT_16) {
                throw new SignalProcessingException("16 bit float is currently not supported");
            } else if (format == BinaryIqFormat.FLOAT_32) {
                inPhase = ByteBuffer.wrap(iqBytes, i * format.getByteLength(), format.getByteLength()).getFloat();
                quadrature = ByteBuffer.wrap(iqBytes, (i + 1) * format.getByteLength(), format.getByteLength())
                        .getFloat();
            } else if (format == BinaryIqFormat.FLOAT_64) {
                inPhase = ByteBuffer.wrap(iqBytes, i * format.getByteLength(), format.getByteLength()).getDouble();
                quadrature = ByteBuffer.wrap(iqBytes, (i + 1) * format.getByteLength(), format.getByteLength())
                        .getDouble();
            }
            iqValueList.add(new Complex(inPhase, quadrature));
        }
        return new TimeDomainSignal(iqValueList, sampleRate);
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
