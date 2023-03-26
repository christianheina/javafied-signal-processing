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

import java.util.List;

import org.apache.commons.math3.complex.Complex;

/**
 * Utility class providing math functionality for complex numbers.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class ComplexMath {

    private ComplexMath() {
        /* Hidden Constructor */ }

    /**
     * Calculate pearson correlation between two complex lists.
     * 
     * @param complexList1
     *            first list to calculate correlation
     * @param complexList2
     *            second list to calculate correlation
     * 
     * @return Correlation. Value is between -1 and 1 with -1 being negatively correlated, 0 being uncorrelated and 1
     *         correlated.
     * 
     * @exception IllegalArgumentException
     *                if complexList1 and complexList2 is not the same size
     */
    public static Complex pearsonCorrelation(List<Complex> complexList1, List<Complex> complexList2) {
        Complex coveriance = covariance(complexList1, complexList2);

        double stdVector1 = standardDeviation(complexList1);
        double stdVector2 = standardDeviation(complexList2);

        return coveriance.divide(stdVector1 * stdVector2);
    }

    /**
     * Calculate dot product (sum of elementwise product) of two complex lists.
     * 
     * @param complexList1
     *            first list to perform dot product
     * @param complexList2
     *            second list to perform dot product
     * 
     * @return dot product
     * 
     * @exception IllegalArgumentException
     *                if complexList1 and complexList2 is not the same size
     */
    public static Complex dotProduct(List<Complex> complexList1, List<Complex> complexList2) {
        // Check vectors are same size
        if (complexList1.size() != complexList2.size()) {
            throw new IllegalArgumentException("complexList1 and complexList2 needs to be of equal size");
        }

        Complex sum = Complex.ZERO;
        for (int i = 0; i < complexList1.size(); i++) {
            sum = sum.add(complexList1.get(i).multiply(complexList2.get(i)));
        }

        return sum;
    }

    /**
     * Calculate covariance [cov(A,B)] of two complex lists
     * 
     * @param complexList1
     *            first complex list in covariance calculation
     * @param complexList2
     *            second complex list in covariance calculation
     * 
     * @return covariance [cov(complexList1, complexList2)]
     * 
     * @exception IllegalArgumentException
     *                if complexList1 and complexList2 is not the same size
     */
    public static Complex covariance(List<Complex> complexList1, List<Complex> complexList2) {
        // Check vectors are same size
        if (complexList1.size() != complexList2.size()) {
            throw new IllegalArgumentException("complexList1 and complexList2 needs to be of equal size");
        }
        Complex u1 = mean(complexList1);
        Complex u2 = mean(complexList2);
        Complex sum = Complex.ZERO;
        for (int i = 0; i < complexList1.size(); i++) {
            sum = sum.add(complexList1.get(i).subtract(u1).multiply(complexList2.get(i).subtract(u2).conjugate()));
        }
        return sum.divide(complexList1.size() - 1);
    }

    /**
     * Calculate variance [var(A)] of complex list
     * 
     * @param complexList
     *            complex list to calculate variance
     * 
     * @return variance [var(complexList)]
     */
    public static double variance(List<Complex> complexList) {
        Complex u = mean(complexList); // Calculate mean (u)
        double sum = 0;

        for (Complex sample : complexList) {
            // Add |sample - mean|^2

            sum += Math.pow(sample.subtract(u).abs(), 2);
        }
        return sum / (complexList.size() - 1);
    }

    /**
     * Calculate standard deviation of complex list
     * 
     * @param complexList
     *            complex list to calculate standard deviation
     * 
     * @return standard deviation
     */
    public static double standardDeviation(List<Complex> complexList) {
        // square root of variance
        return Math.sqrt(variance(complexList));
    }

    /**
     * Calculate sum each element in complex list
     * 
     * @param listToSum
     *            complex list to sum
     * 
     * @return sum value
     */
    public static Complex sum(List<Complex> listToSum) {
        Complex sum = Complex.ZERO;

        // Loop and add each value
        for (Complex sample : listToSum) {
            sum = sum.add(sample);
        }

        return sum;
    }

    /**
     * Calculate mean of complex list
     * 
     * @param listToMean
     *            complex list to mean
     * 
     * @return mean value
     */
    public static Complex mean(List<Complex> listToMean) {
        // Divide sum of all entries by the number of entries
        return sum(listToMean).divide(listToMean.size());
    }

}
