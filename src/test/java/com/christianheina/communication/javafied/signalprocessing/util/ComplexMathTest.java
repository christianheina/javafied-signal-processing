/*
 * Copyright 2022 Christian Heina
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ComplexMathTest {

    private static final Complex NEGATIVE_ONE = new Complex(-1, 0);
    private static final List<Complex> EMPTY_LIST = new ArrayList<>();

    private List<Complex> complexList1;
    private List<Complex> complexList2;

    @BeforeTest
    public void prepare() {
        Complex c = new Complex(1, 1);
        complexList1 = new ArrayList<>();
        complexList1.add(Complex.ONE);
        complexList1.add(NEGATIVE_ONE);
        complexList1.add(NEGATIVE_ONE);
        complexList1.add(c);

        complexList2 = new ArrayList<>();
        complexList2.add(NEGATIVE_ONE);
        complexList2.add(Complex.ONE);
        complexList2.add(NEGATIVE_ONE);
        complexList2.add(c);
    }

    @Test
    public void pearsonCorrelationTest() {
        Complex corrAB = ComplexMath.pearsonCorrelation(complexList1, complexList2);
        Assert.assertTrue(Complex.equals(corrAB, new Complex(0.15789473684210528, 0.0), 1e-9));

        Complex corrBA = ComplexMath.pearsonCorrelation(complexList2, complexList1);
        Assert.assertTrue(Complex.equals(corrBA, new Complex(0.15789473684210528, 0.0), 1e-9));

        Assert.assertEquals(corrAB, corrBA);

        Complex corrAA = ComplexMath.pearsonCorrelation(complexList1, complexList1);
        Assert.assertTrue(Complex.equals(corrAA, Complex.ONE, 1e-9));

        Complex corrBB = ComplexMath.pearsonCorrelation(complexList2, complexList2);
        Assert.assertTrue(Complex.equals(corrBB, Complex.ONE, 1e-9));
    }

    @Test
    public void dotProductTest() {
        Complex productAB = ComplexMath.dotProduct(complexList1, complexList2);
        Assert.assertTrue(Complex.equals(productAB, new Complex(-1, 2), 1e-9));

        Complex productBA = ComplexMath.dotProduct(complexList1, complexList2);
        Assert.assertTrue(Complex.equals(productBA, new Complex(-1, 2), 1e-9));

        Complex productAA = ComplexMath.dotProduct(complexList1, complexList2);
        Assert.assertTrue(Complex.equals(productAA, new Complex(-1, 2), 1e-9));

        Complex productBB = ComplexMath.dotProduct(complexList1, complexList2);
        Assert.assertTrue(Complex.equals(productBB, new Complex(-1, 2), 1e-9));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void dotProductExceptionTest() {
        ComplexMath.dotProduct(complexList1, EMPTY_LIST);
    }

    @Test
    public void covarianceTest() {
        // cov(A,B)
        Complex covAB = ComplexMath.covariance(complexList1, complexList2);
        Assert.assertTrue(Complex.equals(covAB, new Complex(0.25, 0.0), 1e-9));

        // cov(B,A)
        Complex covBA = ComplexMath.covariance(complexList2, complexList1);
        Assert.assertTrue(Complex.equals(covBA, new Complex(0.25, 0.0), 1e-9));

        // cov(A,B) == cov(B,A)
        Assert.assertEquals(covAB, covBA);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void covarianceExceptionTest() {
        ComplexMath.covariance(complexList1, EMPTY_LIST);
    }

    @Test
    public void varianceTest() {
        double varA = ComplexMath.variance(complexList1);
        Assert.assertEquals(varA, 1.5833333333333333, 1e-9);
        Assert.assertEquals(varA, Math.pow(ComplexMath.standardDeviation(complexList1), 2), 1e-9);

        double varB = ComplexMath.variance(complexList2);
        Assert.assertEquals(varB, 1.5833333333333333, 1e-9);
        Assert.assertEquals(varA, Math.pow(ComplexMath.standardDeviation(complexList2), 2), 1e-9);
    }

    @Test
    public void standardDeviationTest() {
        double stdA = ComplexMath.standardDeviation(complexList1);
        Assert.assertEquals(stdA, 1.2583057392117916, 1e-9);
        Assert.assertEquals(stdA, Math.sqrt(ComplexMath.variance(complexList1)));

        double stdB = ComplexMath.standardDeviation(complexList2);
        Assert.assertEquals(stdB, 1.2583057392117916, 1e-9);
        Assert.assertEquals(stdB, Math.sqrt(ComplexMath.variance(complexList2)));
    }

    @Test
    public void sumTest() {
        Complex sumA = ComplexMath.sum(complexList1);
        Assert.assertTrue(sumA.equals(new Complex(0, 1)));

        Complex sumB = ComplexMath.sum(complexList2);
        Assert.assertTrue(sumB.equals(new Complex(0, 1)));
    }

    @Test
    public void meanTest() {
        Complex meanA = ComplexMath.mean(complexList1);
        Assert.assertTrue(meanA.equals(new Complex(0, 0.25)));

        Complex meanB = ComplexMath.mean(complexList1);
        Assert.assertTrue(meanB.equals(new Complex(0, 0.25)));
    }

}
