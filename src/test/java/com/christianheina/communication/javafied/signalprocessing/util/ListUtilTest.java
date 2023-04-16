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
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ListUtil}.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class ListUtilTest {

    private static final Complex NEGATIVE_ONE = new Complex(-1, 0);

    private List<Complex> complexList;

    @BeforeMethod
    public void prepare() {
        complexList = new ArrayList<>();
        complexList.add(Complex.ONE);
        complexList.add(NEGATIVE_ONE);
        complexList.add(NEGATIVE_ONE);
        complexList.add(new Complex(1, 1));
    }

    @Test
    public void deepCopyListTest() {
        List<Complex> deepCopiedList = ListUtil.deepCopyList(complexList);
        Assert.assertEquals(complexList.size(), deepCopiedList.size());
        for (int i = 0; i < deepCopiedList.size(); i++) {
            Assert.assertEquals(complexList.get(i), deepCopiedList.get(i));
        }
    }

}
