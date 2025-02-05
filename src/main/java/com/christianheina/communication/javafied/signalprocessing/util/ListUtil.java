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
 * 
 * @deprecated As of 2025-02-05 this is replaced by {@link com.christianheina.common.utilities.ListUtil
 *             com.christianheina.common.utilities.ListUtil}. This class will be supported until 2025-05-05 and removed
 *             2023-08-05.
 */
@Deprecated
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
     * 
     * @deprecated As of 2025-02-05 this is replaced by
     *             {@link com.christianheina.common.utilities.ListUtil#deepCopyComplexList
     *             com.christianheina.common.utilities.ListUtil#deepCopyComplexList}. This class will be supported until
     *             2025-05-05 and removed 2023-08-05.
     */
    @Deprecated
    public static List<Complex> deepCopyList(List<Complex> complexListToDeepCopy) {
        List<Complex> deepCopyList = new ArrayList<>(complexListToDeepCopy.size());
        for (Complex sample : complexListToDeepCopy) {
            deepCopyList.add(new Complex(sample.getReal(), sample.getImaginary()));
        }
        return deepCopyList;
    }

}
