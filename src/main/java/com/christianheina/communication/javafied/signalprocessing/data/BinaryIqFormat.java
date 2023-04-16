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

/**
 * Enum for handling supported binary formats of IQ data
 * 
 * @author Christian Heina (developer@christianheina.com)
 * 
 * @deprecated As of 2023-04-16 this is replaced by
 *             {@link com.christianheina.communication.javafied.signalprocessing.enums.BinaryIqFormat BinaryIqFormat}.
 *             This class will be supported until 2023-07-16 and removed 2023-10-16.
 */
@Deprecated
public enum BinaryIqFormat {
    /**
     * 16 bit float
     */
    FLOAT_16(2),
    /**
     * 32 bit float
     */
    FLOAT_32(4),
    /**
     * 64 bit float
     */
    FLOAT_64(8);

    private int byteLength;

    BinaryIqFormat(int byteLength) {
        this.byteLength = byteLength;
    }

    /**
     * Retrieve byte length
     * 
     * @return byte length
     */
    public int getByteLength() {
        return byteLength;
    }

}
