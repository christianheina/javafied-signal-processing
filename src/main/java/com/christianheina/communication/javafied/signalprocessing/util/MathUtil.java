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

import java.util.List;

/**
 * Provides math utilities.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class MathUtil {

    private MathUtil() {
        /* Hidden Constructor */ }

    /**
     * Calculate pearson correlation between two lists.
     * 
     * @param doubleList1
     *            first list to calculate correlation
     * @param doubleList2
     *            second list to calculate correlation
     * 
     * @return Correlation. Value is between -1 and 1 with -1 being negatively correlated, 0 being uncorrelated and 1
     *         correlated.
     * 
     * @exception IllegalArgumentException
     *                if complexList1 and complexList2 is not the same size
     */
    public static double pearsonCorrelation(List<Double> doubleList1, List<Double> doubleList2) {
        double coveriance = covariance(doubleList1, doubleList2);

        double stdVector1 = standardDeviation(doubleList1);
        double stdVector2 = standardDeviation(doubleList2);

        return coveriance / (stdVector1 * stdVector2);
    }

    /**
     * Calculate dot product (sum of elementwise product) of two lists.
     * 
     * @param doubleList1
     *            first list to perform dot product
     * @param doubleList2
     *            second list to perform dot product
     * 
     * @return dot product
     * 
     * @exception IllegalArgumentException
     *                if doubleList1 and doubleList2 is not the same size
     */
    public static double dotProduct(List<Double> doubleList1, List<Double> doubleList2) {
        // Check vectors are same size
        if (doubleList1.size() != doubleList2.size()) {
            throw new IllegalArgumentException("complexList1 and complexList2 needs to be of equal size");
        }

        double sum = 0;
        for (int i = 0; i < doubleList1.size(); i++) {
            sum += doubleList1.get(i) * doubleList2.get(i);
        }

        return sum;
    }

    /**
     * Calculate covariance [cov(A,B)] of two lists
     * 
     * @param doubleList1
     *            first list in covariance calculation
     * @param doubleList2
     *            second list in covariance calculation
     * 
     * @return covariance [cov(complexList1, complexList2)]
     * 
     * @exception IllegalArgumentException
     *                if doubleList1 and doubleList2 is not the same size
     */
    public static double covariance(List<Double> doubleList1, List<Double> doubleList2) {
        // Check vectors are same size
        if (doubleList1.size() != doubleList2.size()) {
            throw new IllegalArgumentException("doubleList1 and douybleList2 needs to be of equal size");
        }
        double u1 = mean(doubleList1);
        double u2 = mean(doubleList2);
        double sum = 0;
        for (int i = 0; i < doubleList1.size(); i++) {
            sum += (doubleList1.get(i) - u1) * (doubleList2.get(i) - u2);
        }
        return sum / (doubleList1.size() - 1);
    }

    /**
     * Calculate variance [var(A)] of list
     * 
     * @param doubleList
     *            list to calculate variance
     * 
     * @return variance [var(complexList)]
     */
    public static double variance(List<Double> doubleList) {
        double u = mean(doubleList); // Calculate mean (u)
        double sum = 0;

        for (double sample : doubleList) {
            // Add |sample - mean|^2
            sum += Math.pow(Math.abs(sample - u), 2);
        }
        return sum / (doubleList.size() - 1);
    }

    /**
     * Calculate standard deviation of list
     * 
     * @param doubleList
     *            list to calculate standard deviation
     * 
     * @return standard deviation
     */
    public static double standardDeviation(List<Double> doubleList) {
        // square root of variance
        return Math.sqrt(variance(doubleList));
    }

    /**
     * Calculate sum each element in list
     * 
     * @param listToSum
     *            list to sum
     * 
     * @return sum value
     */
    public static double sum(List<Double> listToSum) {
        double sum = 0;
        for (double power : listToSum) {
            sum += power;
        }
        return sum;
    }

    /**
     * Calculate mean of list
     * 
     * @param listToMean
     *            list to mean
     * 
     * @return mean value
     */
    public static double mean(List<Double> listToMean) {
        // Divide sum of all entries by the number of entries
        return sum(listToMean) / listToMean.size();
    }

}
