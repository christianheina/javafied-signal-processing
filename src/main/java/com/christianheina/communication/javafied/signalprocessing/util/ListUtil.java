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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

/**
 * Utility class providing functionality for lists.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class ListUtil {

    private ListUtil() {
        /* Hidden Constructor */
    }

    /**
     * Creates an identical list with a deep copy of every complex list element
     * 
     * @param complexListToDeepCopy
     *            list to deep copy
     * 
     * @return new deep copied list
     */
    public static List<Complex> deepCopyList(List<Complex> complexListToDeepCopy) {
        List<Complex> deepCopyList = new ArrayList<>(complexListToDeepCopy.size());
        for (Complex sample : complexListToDeepCopy) {
            deepCopyList.add(new Complex(sample.getReal(), sample.getImaginary()));
        }
        return deepCopyList;
    }

}
