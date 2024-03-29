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

package com.christianheina.communication.javafied.signalprocessing.exceptions;

import org.testng.annotations.Test;

/**
 * Unit test for {@link SignalProcessingException}.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class SignalProcessingExceptionTest {

    @Test(expectedExceptions = SignalProcessingException.class)
    public void PhasedArrayAntennaExceptionWithMessageTest() {
        throw new SignalProcessingException("Test");
    }

    @Test(expectedExceptions = SignalProcessingException.class)
    public void PhasedArrayAntennaExceptionWithThrowableTest() {
        throw new SignalProcessingException(new RuntimeException());
    }

    @Test(expectedExceptions = SignalProcessingException.class)
    public void PhasedArrayAntennaExceptionWithMessageAndThrowableTest() {
        throw new SignalProcessingException("Test", new RuntimeException());
    }

}
