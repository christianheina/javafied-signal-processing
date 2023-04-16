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
 * Provides power utilities.
 * 
 * @author Christian Heina (developer@christianheina.com)
 */
public class PowerUtil {

    private PowerUtil() {
        /* Hidden Constructor */
    }

    /**
     * Converts {@link Complex} list to {@link Double} list representing magnitude.
     * 
     * @param complexList
     *            list of {@link Complex} to convert
     * 
     * @return list of {@link Double} representing magnitude.
     */
    public static List<Double> complexListToMagnitudeList(List<Complex> complexList) {
        List<Double> spectrumList = new ArrayList<>();
        for (Complex iqEntry : complexList) {
            spectrumList.add(complexToMagnitude(iqEntry));
        }
        return spectrumList;
    }

    /**
     * Calculate magnitude
     * 
     * @param complex
     *            value to calculate magnitude
     * 
     * @return magnitude
     */
    public static double complexToMagnitude(Complex complex) {
        return complex.abs();
    }

    /**
     * Converts {@link Complex} list to {@link Double} list representing power in Watts.
     * 
     * @param complexList
     *            list of {@link Complex} to convert
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return list of {@link Double} representing power in Watts.
     */
    public static List<Double> complexListToPowerList(List<Complex> complexList, double resistance) {
        return magnitudeListToPowerList(complexListToMagnitudeList(complexList), resistance);
    }

    /**
     * Converts list of magnitudes to {@link Double} list representing power in Watts.
     * 
     * @param magnitudeList
     *            list of magnitudes to convert
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return list of {@link Double} representing power in Watts.
     */
    public static List<Double> magnitudeListToPowerList(List<Double> magnitudeList, double resistance) {
        List<Double> powerList = new ArrayList<>(magnitudeList.size());
        for (double magnitude : magnitudeList) {
            powerList.add(magnitudeToPower(magnitude, resistance));
        }
        return powerList;
    }

    /**
     * Calculate power in watts
     * 
     * @param magnitude
     *            value to calculate power
     * @param resistance
     *            the resistance on the receiving port (active impedance)
     * 
     * @return power in watts
     */
    public static double magnitudeToPower(double magnitude, double resistance) {
        return Math.pow(magnitude, 2) / resistance;
    }

    /**
     * Converts {@link Complex} list to {@link Double} list representing power in dBm.
     * 
     * @param complexList
     *            list of {@link Complex} to convert
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return list of {@link Double} representing power in dBm.
     */
    public static List<Double> complexListToPowerDbmList(List<Complex> complexList, double resistance) {
        List<Double> powerList = complexListToPowerList(complexList, resistance);
        return powerListToPowerDbmList(powerList);
    }

    /**
     * Converts list of magnitudes to {@link Double} list representing power in dBm.
     * 
     * @param magitudeList
     *            list to convert
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return list of {@link Double} representing power in dBm.
     */
    public static List<Double> magnitudeListToPowerDbmList(List<Double> magitudeList, double resistance) {
        List<Double> powerList = magnitudeListToPowerList(magitudeList, resistance);
        return powerListToPowerDbmList(powerList);
    }

    /**
     * Converts list of power to {@link Double} list representing power in dBm.
     * 
     * @param powerList
     *            list to convert
     * 
     * @return list of {@link Double} representing power in dBm.
     */
    public static List<Double> powerListToPowerDbmList(List<Double> powerList) {
        List<Double> dbmList = new ArrayList<>(powerList.size());
        for (int i = 0; i < powerList.size(); i++) {
            dbmList.add(wattsToDbm(powerList.get(i)));
        }
        return dbmList;
    }

    /**
     * Calculates power sum in Watts as a {@link Double}.
     * 
     * @param complexList
     *            list of {@link Complex} to calculate power sum on.
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return power in dBm as a {@link Double}
     */
    public static double complexListToSumPower(List<Complex> complexList, double resistance) {
        List<Double> spectrumList = complexListToPowerList(complexList, resistance);
        return MathUtil.sum(spectrumList);
    }

    /**
     * Calculates power sum in dBm as a {@link Double}.
     * 
     * @param complexList
     *            list of {@link Complex} to calculate power sum on.
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return power in dBm as a {@link Double}
     */
    public static double complexListToSumPowerDbm(List<Complex> complexList, double resistance) {
        double sum = complexListToSumPower(complexList, resistance);
        return wattsToDbm(sum);
    }

    /**
     * Calculates power sum in Watts as a {@link Double}.
     * 
     * @param magnitudeList
     *            list to calculate power sum on.
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return power in dBm as a {@link Double}
     */
    public static double magnitudeListToSumPower(List<Double> magnitudeList, double resistance) {
        List<Double> powerList = magnitudeListToPowerList(magnitudeList, resistance);
        return MathUtil.sum(powerList);
    }

    /**
     * Calculates power sum in dBm as a {@link Double}.
     * 
     * @param magnitudeList
     *            list to calculate power sum on.
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return power in dBm as a {@link Double}
     */
    public static double magnitudeListToSumPowerDbm(List<Double> magnitudeList, double resistance) {
        double sum = magnitudeListToSumPower(magnitudeList, resistance);
        return wattsToDbm(sum);
    }

    /**
     * Calculates average power in dBm as a {@link Double}.
     * 
     * @param complexList
     *            list of {@link Complex} to calculate average power on.
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return average power in dBm as a {@link Double}
     */
    public static double complexListToAveragePowerDbm(List<Complex> complexList, double resistance) {
        double sum = complexListToSumPower(complexList, resistance);
        return wattsToDbm(sum / complexList.size());
    }

    /**
     * Calculates average power in dBm as a {@link Double}.
     * 
     * @param magnitudeList
     *            list to calculate average power on.
     * @param resistance
     *            the resistance in ohms.
     * 
     * @return average power in dBm as a {@link Double}
     */
    public static double magnitudeListToAveragePowerDbm(List<Double> magnitudeList, double resistance) {
        double sum = magnitudeListToSumPower(magnitudeList, resistance);
        return wattsToDbm(sum / magnitudeList.size());
    }

    /**
     * Convert Watts to dBm
     * 
     * @param watts
     *            power to convert in Watts
     * 
     * @return watts in dBm
     */
    public static double wattsToDbm(double watts) {
        return 10 * Math.log10((watts) * 1000);
    }

    /**
     * Convert dBm to Watts
     * 
     * @param dBm
     *            power to convert in dBm
     * 
     * @return dBm in Watts
     */
    public static double dBmToWatts(double dBm) {
        return Math.pow(10, dBm * 0.1) * 0.001;
    }

}
