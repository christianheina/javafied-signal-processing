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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link ComplexMath}.
 * 
 * @author Christian Heina (developer@christianheina.com)
 * 
 * @deprecated As of 2025-02-05 this class is deprecated. This class will be supported until 2025-05-05 and removed
 *             2023-08-05.
 */
@Deprecated
public class ComplexMathTest {

    private static final Complex NEGATIVE_ONE = new Complex(-1, 0);
    private static final List<Complex> EMPTY_LIST = new ArrayList<>();

    private List<Complex> complexList1;
    private List<Complex> complexList2;
    private List<Complex> fftResultList;
    private List<Complex> ifftResultList;

    @BeforeMethod
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

        fftResultList = new ArrayList<>();
        fftResultList.add(new Complex(0, 1));
        fftResultList.add(new Complex(1, 2));
        fftResultList.add(new Complex(0, -1));
        fftResultList.add(new Complex(3, -2));

        ifftResultList = new ArrayList<>();
        ifftResultList.add(new Complex(0, 0.25));
        ifftResultList.add(new Complex(0.75, -0.5));
        ifftResultList.add(new Complex(0, -0.25));
        ifftResultList.add(new Complex(0.25, 0.5));
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
        Assert.assertEquals(1.5833333333333333, varA, 1e-9);
        Assert.assertEquals(Math.pow(ComplexMath.standardDeviation(complexList1), 2), varA, 1e-9);

        double varB = ComplexMath.variance(complexList2);
        Assert.assertEquals(1.5833333333333333, varB, 1e-9);
        Assert.assertEquals(Math.pow(ComplexMath.standardDeviation(complexList2), 2), varA, 1e-9);
    }

    @Test
    public void standardDeviationTest() {
        double stdA = ComplexMath.standardDeviation(complexList1);
        Assert.assertEquals(1.2583057392117916, stdA, 1e-9);
        Assert.assertEquals(Math.sqrt(ComplexMath.variance(complexList1)), stdA, 1e-9);

        double stdB = ComplexMath.standardDeviation(complexList2);
        Assert.assertEquals(1.2583057392117916, stdB, 1e-9);
        Assert.assertEquals(Math.sqrt(ComplexMath.variance(complexList2)), stdB, 1e-9);
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

    @Test
    public void fftTest() {
        List<Complex> fftList = ComplexMath.fft(complexList1);
        Assert.assertEquals(fftList.size(), complexList1.size());
        for (int i = 0; i < fftList.size(); i++) {
            Assert.assertEquals(fftList.get(i), fftResultList.get(i));
        }

    }

    @Test
    public void fftShiftTest() {
        List<Complex> evenFftShiftList = ComplexMath.fftShift(complexList1);
        Assert.assertEquals(evenFftShiftList.size(), complexList1.size());
        for (int i = 0; i < complexList1.size(); i++) {
            Assert.assertEquals(complexList1.get(i),
                    evenFftShiftList.get((i + (complexList1.size()) / 2) % complexList1.size()));
        }

        complexList1.add(NEGATIVE_ONE);
        List<Complex> oddFftShiftList = ComplexMath.fftShift(complexList1);
        Assert.assertEquals(oddFftShiftList.size(), complexList1.size());
        for (int i = 0; i < complexList1.size(); i++) {
            Assert.assertEquals(complexList1.get(i),
                    oddFftShiftList.get((i + (complexList1.size()) / 2) % complexList1.size()));
        }
    }

    @Test
    public void ifftTest() {
        List<Complex> ifftList = ComplexMath.ifft(complexList1);
        Assert.assertEquals(ifftList.size(), complexList1.size());
        for (int i = 0; i < ifftList.size(); i++) {
            Assert.assertEquals(ifftList.get(i), ifftResultList.get(i));
        }
    }

    @Test
    public void ifftShiftTest() {
        List<Complex> evenIfftShiftList = ComplexMath.ifftShift(complexList1);
        Assert.assertEquals(evenIfftShiftList.size(), complexList1.size());
        for (int i = 0; i < complexList1.size(); i++) {
            Assert.assertEquals(complexList1.get(i),
                    evenIfftShiftList.get((i + (complexList1.size() + 1) / 2) % complexList1.size()));
        }

        complexList1.add(NEGATIVE_ONE);
        List<Complex> oddfftShiftList = ComplexMath.ifftShift(complexList1);
        Assert.assertEquals(oddfftShiftList.size(), complexList1.size());
        for (int i = 0; i < complexList1.size(); i++) {
            Assert.assertEquals(complexList1.get(i),
                    oddfftShiftList.get((i + (complexList1.size() + 1) / 2) % complexList1.size()));
        }

    }

}
