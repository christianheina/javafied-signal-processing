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
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for {@link MathUtil}.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class MathUtilTest {

    private static final List<Double> EMPTY_LIST = new ArrayList<>();
    private static final List<Double> DOUBLE_LIST_1 = Arrays.asList(new Double[] { 10.0, 15.0, 5.0 });
    private static final List<Double> DOUBLE_LIST_2 = Arrays.asList(new Double[] { 10.0, -15.0, 5.0 });
    private static final List<Double> DOUBLE_LIST_3 = Arrays.asList(new Double[] { 1.0, -1.0, -1.0, 1.0 });
    private static final List<Double> DOUBLE_LIST_4 = Arrays.asList(new Double[] { -1.0, 1.0, -1.0, 1.0 });

    @Test
    public void pearsonCorrelationTest() {
        double corrAB = MathUtil.pearsonCorrelation(DOUBLE_LIST_3, DOUBLE_LIST_4);
        Assert.assertEquals(0.0, corrAB, 1e-9);

        double corrBA = MathUtil.pearsonCorrelation(DOUBLE_LIST_4, DOUBLE_LIST_3);
        Assert.assertEquals(0.0, corrAB, 1e-9);

        Assert.assertEquals(corrAB, corrBA, 1e-9);

        double corrAA = MathUtil.pearsonCorrelation(DOUBLE_LIST_3, DOUBLE_LIST_3);
        Assert.assertEquals(1.0, corrAA, 1e-9);

        double corrBB = MathUtil.pearsonCorrelation(DOUBLE_LIST_4, DOUBLE_LIST_4);
        Assert.assertEquals(1.0, corrBB, 1e-9);
    }

    @Test
    public void dotProductTest() {
        double productAB = MathUtil.dotProduct(DOUBLE_LIST_3, DOUBLE_LIST_4);
        Assert.assertEquals(0.0, productAB, 1e-9);

        double productBA = MathUtil.dotProduct(DOUBLE_LIST_4, DOUBLE_LIST_3);
        Assert.assertEquals(0.0, productBA, 1e-9);

        double productAA = MathUtil.dotProduct(DOUBLE_LIST_3, DOUBLE_LIST_3);
        Assert.assertEquals(4.0, productAA, 1e-9);

        double productBB = MathUtil.dotProduct(DOUBLE_LIST_4, DOUBLE_LIST_4);
        Assert.assertEquals(4.0, productBB, 1e-9);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void dotProductExceptionTest() {
        MathUtil.dotProduct(DOUBLE_LIST_3, EMPTY_LIST);
    }

    @Test
    public void covarianceTest() {
        // cov(A,B)
        double covAB = MathUtil.covariance(DOUBLE_LIST_3, DOUBLE_LIST_4);
        Assert.assertEquals(0.0, covAB, 1e-9);

        // cov(B,A)
        double covBA = MathUtil.covariance(DOUBLE_LIST_4, DOUBLE_LIST_3);
        Assert.assertEquals(0.0, covBA, 1e-9);

        // cov(A,B) == cov(B,A)
        Assert.assertEquals(covAB, covBA, 1e-9);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void covarianceExceptionTest() {
        MathUtil.covariance(DOUBLE_LIST_3, EMPTY_LIST);
    }

    @Test
    public void varianceTest() {
        double varA = MathUtil.variance(DOUBLE_LIST_3);
        Assert.assertEquals(1.3333333333333333, varA, 1e-9);
        Assert.assertEquals(Math.pow(MathUtil.standardDeviation(DOUBLE_LIST_3), 2), varA, 1e-9);

        double varB = MathUtil.variance(DOUBLE_LIST_4);
        Assert.assertEquals(1.3333333333333333, varB, 1e-9);
        Assert.assertEquals(Math.pow(MathUtil.standardDeviation(DOUBLE_LIST_4), 2), varA, 1e-9);
    }

    @Test
    public void standardDeviationTest() {
        double stdA = MathUtil.standardDeviation(DOUBLE_LIST_3);
        Assert.assertEquals(1.1547005383792515, stdA, 1e-9);
        Assert.assertEquals(Math.sqrt(MathUtil.variance(DOUBLE_LIST_3)), stdA, 1e-9);

        double stdB = MathUtil.standardDeviation(DOUBLE_LIST_4);
        Assert.assertEquals(1.1547005383792515, stdB, 1e-9);
        Assert.assertEquals(Math.sqrt(MathUtil.variance(DOUBLE_LIST_4)), stdB, 1e-9);
    }

    @Test
    public void sumTest() {
        Assert.assertEquals(30.0, MathUtil.sum(DOUBLE_LIST_1), 1e-9);
        Assert.assertEquals(0.0, MathUtil.sum(DOUBLE_LIST_2), 1e-9);
    }

    @Test
    public void meanTest() {
        Assert.assertEquals(10.0, MathUtil.mean(DOUBLE_LIST_1), 1e-9);
        Assert.assertEquals(0.0, MathUtil.mean(DOUBLE_LIST_2), 1e-9);
    }

}
