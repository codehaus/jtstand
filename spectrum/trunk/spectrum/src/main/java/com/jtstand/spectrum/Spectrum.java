package com.jtstand.spectrum;

public class Spectrum {

    double[] spect;
    double ratePerN;
    int avrN = 1;

    public void average(Spectrum s) {
        if (this.spect.length != s.spect.length) {
            throw new IllegalArgumentException("Spectrum length does not match!");
        }
        if (this.ratePerN != s.ratePerN) {
            throw new IllegalArgumentException("Spectrum rate does not match!");
        }
        int newAvrN = avrN + 1;
        for (int i = 0; i < spect.length; i++) {
            this.spect[i] = (avrN * this.spect[i] + s.spect[i]) / newAvrN;
        }
        avrN = newAvrN;
    }

    public Spectrum(double[] spect, double ratePerN) {
        this.spect = spect;
        this.ratePerN = ratePerN;
    }

    public int length() {
        return spect.length;
    }

    public double getRatePerN() {
        return ratePerN;
    }

    private void setRatePerN(double ratePerN) {
        this.ratePerN = ratePerN;
    }

    public double getFrequency() {
        return getDeltaFromSpectrum(spect) * ratePerN;
    }

    public double getFrequency(int nmin, int nmax) {
        return getDeltaFromSpectrum(spect, nmin, nmax) * ratePerN;
    }

    public static double[] getPowerSpectrumHalf(double[] val, int start, int N) {
        double[] valmod = new double[N];
        //Hann
        for (int i = 0; i < N; i++) {
            valmod[i] = val[start + i] * Math.sqrt(2.0) * (1.0 - Math.cos((2.0 * Math.PI * (i + 0.5)) / N)) / N;
        }
        return FFT.powerHalf(com.jtstand.spectrum.FFT.fft(valmod));
    }

    public static double[] getPowerSpectrumHalf(double[] val) {
        int N = val.length;
        double[] valmod = new double[N];
        //Hann
        for (int i = 0; i < N; i++) {
            valmod[i] = val[i] * Math.sqrt(2.0) * (1.0 - Math.cos((2.0 * Math.PI * (i + 0.5)) / N)) / N;
        }
        return FFT.powerHalf(com.jtstand.spectrum.FFT.fft(valmod));
    }

    public static double deltaFromTime(double[] val) {
        return getDeltaFromSpectrum(getPowerSpectrumHalf(val));
    }

    public double getDeltaFromSpectrum() {
        return getDeltaFromSpectrum(spect, 2, spect.length - 2);
    }

    public static double getDeltaFromSpectrum(double[] spect) {
        return getDeltaFromSpectrum(spect, 2, spect.length - 2);
    }

    public double getDeltaFromSpectrum(int nmin, int nmax) {
        return getDeltaFromSpectrum(spect, nmin, nmax);
    }

    public static int getPeakPos(double[] spect, int nmin, int nmax) {
        int pos = -1;
        double max = 0.0;
        if (nmin < 2) {
            nmin = 2;
        }
        if (nmax > spect.length - 3) {
            nmax = spect.length - 3;
        }
        for (int i = nmin; i < nmax; i++) {
            if (spect[i] > max && spect[i] > spect[i - 1] && spect[i] > spect[i + 1] && spect[i - 1] > spect[i - 2] && spect[i + 1] > spect[i + 2]) {
                max = spect[i];
                pos = i;
            }
        }
        return pos;
    }

    public static double getDeltaFromSpectrum(double[] spect, int nmin, int nmax) {
        double max = 0;
        int pos = getPeakPos(spect, nmin, nmax);
        if (pos < 0) {
            return 0.0;
        }
        double d = delta(Math.sqrt(spect[pos - 1]), Math.sqrt(spect[pos]), Math.sqrt(spect[pos + 1]));
        return pos + d;
    }

    public double getDFromSpectrum() {
        return getDFromSpectrum(spect, 2, spect.length - 2);
    }

    public static double getDFromSpectrum(double[] spect) {
        return getDFromSpectrum(spect, 2, spect.length - 2);
    }

    public double getDFromSpectrum(int nmin, int nmax) {
        return getDFromSpectrum(spect, nmin, nmax);
    }

    public static double getDFromSpectrum(double[] spect, int nmin, int nmax) {
        double max = 0;
        int pos = 0;
        for (int i = nmin; i < nmax; i++) {
            if (spect[i] > max && spect[i] > spect[i - 1] && spect[i] > spect[i + 1]) {
                max = spect[i];
                pos = i;
            }
        }
        if (pos == 0) {
            return 0;
        }
        return delta(Math.sqrt(spect[pos - 1]), Math.sqrt(spect[pos]), Math.sqrt(spect[pos + 1]));
    }

    public double getTHD() {
        return getTHD(spect);
    }

    public static double getTHD(double[] spect) {
        return getTHD(spect, getDeltaFromSpectrum(spect));
    }

    public double getTHD(double delta) {
        return getTHD(spect, delta);
    }

    public static double getTHD(double[] spect, double delta) {
        double base = getSumAround(spect, delta);
        //System.out.println("base:"+base);
        if (base == 0) {
            return -1;
        }
        double sum = 0;

        for (int i = 2; Math.round(delta * i) < spect.length; i++) {
            double harm = getSumAround(spect, delta * i);
            //System.out.println("harm["+i+"]"+harm);
            sum += harm;
        }
        return Math.sqrt(sum / base);
    }

    public double getHD2(double delta) {
        return getHD2(spect, delta);
    }

    public static double getHD2(double[] spect, double delta) {
        return getHDN(spect, delta, 2);
    }

    public double getHD3(double delta) {
        return getHD3(spect, delta);
    }

    public static double getHD3(double[] spect, double delta) {
        return getHDN(spect, delta, 3);
    }

    public double getHDN(double delta, int n) {
        return getHDN(spect, delta, n);
    }

    public static double getHDN(double[] spect, double delta, int n) {
        double base = getSumAround(spect, delta);
        //System.out.println("base:"+base);
        if (base == 0) {
            return -1;
        }
        double sum = 0;
        double d = n * delta;
        if (Math.round(d) < spect.length) {
            double harm = getSumAround(spect, d);
            //System.out.println("harm["+n+"]"+harm);
            sum += harm;
        }
        return Math.sqrt(sum / base);
    }

    public static double getSumAround(double[] spect, double delta) {
        int N = spect.length;
        int n = (int) Math.round(delta);
        //System.out.println("Computing sum around:"+delta);
        double sum = 0;
        for (int i = n - 2; i <= n + 2; i++) {
            if (i >= 0 && i < N) {
                sum += spect[i];
            }
        }
        return sum;
    }

    public static double getSum(double[] spect, int nmin, int nmax) {
        double sum = 0.0;
        if (nmin < 0) {
            nmin = 0;
        }
        if (nmax >= spect.length) {
            nmax = spect.length - 1;
        }
        for (int i = nmin; i <= nmax; i++) {
            sum += spect[i];
        }
        return sum;
    }

    public double getSum(int nmin, int nmax) {
        double sum = 0.0;
        if (nmin < 0) {
            nmin = 0;
        }
        if (nmax >= spect.length) {
            nmax = spect.length - 1;
        }
        for (int i = nmin; i <= nmax; i++) {
            sum += spect[i];
        }
        return sum;
    }

    public double getAmplitudeWithoutPeak(int nmin, int nmax) {
        return Math.sqrt(getSum(nmin, nmax) / 1.5);

    }

    public double getAmplitude(int nmin, int nmax) {
        return getAmplitude(this.spect, nmin, nmax);
    }

    public static double getAmplitude(double[] spect, double near) {
        return getAmplitude(spect, (int) Math.round(near) - 1, (int) Math.round(near) + 1);
    }

    public static double getAmplitude(double[] spect, int nmin, int nmax) {
        double max = 0;
        int pos = getPeakPos(spect, nmin, nmax);
        if (pos < 0) {
            return 0.0;
        }
        double d = delta(Math.sqrt(spect[pos - 1]), Math.sqrt(spect[pos]), Math.sqrt(spect[pos + 1]));
        if (Math.abs(d) < 0.001) {
            return Math.sqrt(getSum(spect, pos - 1, pos + 1) / 1.5);
        } else {
            return Math.sqrt(getSum(spect, pos - 2, pos + 2) / inst5sq(d));
        }
    }

    public static double inst5sq(double d) {
        double im2 = inst(d - 2);
        double im1 = inst(d - 1);
        double i = inst(d);
        double ip1 = inst(d + 1);
        double ip2 = inst(d + 2);
        return im2 * im2 + im1 * im1 + i * i + ip1 * ip1 + ip2 * ip2;
    }

    public static double inst(double d) {
        return d == 0.0 ? 1.0 : Math.sin(Math.PI * d) / (Math.PI * d * (1.0 - d * d));
    }

    public static double delta(double a, double b, double c) {
        return 0.5 * ((b - 2 * a) / (a + b) + (2 * c - b) / (b + c));
    }

    public static int getMaxPos(double[] spect) {
        double max = 0;
        int pos = 0;
        for (int i = 2; i < spect.length - 2; i++) {
            if (spect[i] > max && spect[i] > spect[i - 1] && spect[i] > spect[i + 1]) {
                max = spect[i];
                pos = i;
            }
        }
        return pos;
    }

    public static double computeDeltaFromSpectrum(double[] spect) {
        int pos = getMaxPos(spect);
        //System.out.println("pos:" + pos);
        double d = delta(Math.sqrt(spect[pos - 1]), Math.sqrt(spect[pos]), Math.sqrt(spect[pos + 1]));
        //System.out.println("delta:" + d);
        return pos + d;
    }

    public static double getPeakToPeak(double[] val) {
        double min = val[0];
        double max = val[0];
        for (int i = 1; i < val.length; i++) {
            if (val[i] < min) {
                min = val[i];
            }
            if (val[i] > max) {
                max = val[i];
            }
        }
        return max - min;
    }

    public static double getAcRms(double[] val) {
        double sum = 0;
        for (int i = 0; i < val.length; i++) {
            sum += val[i];
        }
        double dc = sum / val.length;
        double sum2 = 0;
        for (int i = 0; i < val.length; i++) {
            double ac = val[i] - dc;
            sum2 += ac * ac;
        }
        return Math.sqrt(sum2 / val.length);
    }

    public static double getRms(double[] val) {
        double sum2 = 0;
        for (int i = 0; i < val.length; i++) {
            sum2 += val[i] * val[i];
        }
        return Math.sqrt(sum2 / val.length);
    }

    public static void main(String[] args) {
        double[] timefunc = new double[128];
        int N = timefunc.length;
        double freqline = 8;
        double rate = 80E6;
        double df = rate / N;
        double maxerr = 0.0;
        double vrms = 52.0;
        double noiseLevel = 0.0;
        com.jtstand.spectrum.Spectrum spectAverage = null;
        java.text.DecimalFormat format = new java.text.DecimalFormat("#.######");
        for (double d = 0.0; d <= 0.4; d += 0.1) {
            double phase = Math.random() * Math.PI * 2.0;
            double delta = freqline + d;
            double c = 2 * Math.PI * (df * (delta)) / rate;
            spectAverage = null;
            for (int av = 0; av < 10; av++) {
                for (int i = 0; i < N; i++) {
                    double noise = 0;
                    for (int j = 0; j < 10; j++) {
                        noise += Math.random();
                    }
                    noise *= noiseLevel / 10;
                    timefunc[i] = vrms * Math.sqrt(2.0) * (Math.cos(c * i + phase) + noise);
                    //timefunc[i]=vrms*Math.sqrt(2.0)* Math.signum(0.5+Math.sin(c*i+phase)+noise);

                    //System.out.println("timefunc["+i+"]="+timefunc[i]);
                }
                Complex[] fft = FFT.fftHann(timefunc, 0, N);
                Spectrum spect = new com.jtstand.spectrum.Spectrum(FFT.powerHalf(fft), rate / timefunc.length);
                double spectDelta = spect.getDeltaFromSpectrum();
                double spectPhase = FFT.phase(fft, spectDelta);
                double spectPhaseError = Complex.normalizedPhase(spectPhase - phase);
                System.out.println("phaseError:" + spectPhaseError);
                if (spectAverage == null) {
                    spectAverage = spect;
                } else {
                    //System.out.println("sp"+av+" amp:"+nsp.getAmplitude(2,nsp.length()-3));
                    spectAverage.average(spect);
                }
            }
            //for(int i=0;i<spect.length();i++){
            //System.out.println("spect["+i+"[="+spect.spect[i]);
            //}

            double err = 100.0 * (spectAverage.getDeltaFromSpectrum() - delta) / delta;
            double amperr = 100.0 * (spectAverage.getAmplitude(2, spectAverage.length() - 3) - vrms) / vrms;
            System.out.println("d:" + format.format(d) + " frErr:" + format.format(err) + "% ampErr:" + format.format(amperr) + "%");
        }
        //System.out.println("maxerr:"+ format.format(maxerr));
    }
}
