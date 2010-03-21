package com.jtstand.spectrum;

public class FFT {

    public static double[] powerHalf(Complex[] x) {
        return powerN(x, x.length / 2);
    }

    public static double phase(Complex[] x, double delta) {
        int pos = (int) Math.round(delta);
        return x[pos].phasePlus(Math.PI * (pos - delta));
    }

    public static double[] powerN(Complex[] x, int N) {
        double[] out = new double[N];
        for (int i = 0; i < N; i++) {
            out[i] = x[i].power();
        }
        return out;
    }

    public static Complex[] fftHann(double[] x, int start, int N) {
        double[] valmod = new double[N];
        for (int i = 0; i < N; i++) {
            valmod[i] = x[start + i] * Math.sqrt(2.0) * (1.0 - Math.cos((2.0 * Math.PI * (i + 0.5)) / N)) / N;
        }
        return fft(valmod);
    }

    public static Complex[] fft(double[] x) {
        int N = x.length;
        Complex[] x_complex = new Complex[N];
        for (int i = 0; i < N; i++) {
            x_complex[i] = new Complex(x[i], 0);
        }
        return fft(x_complex);
    }

    // compute the FFT of x[], assuming its length is a power of 2
    public static Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) {
            return new Complex[]{x[0]};
        }

        // radix 2 Cooley-Tukey FFT
        int i;
        for (i = 2; i < N; i *= 2) {
        }
        if (i != N) {
            throw new IllegalArgumentException("N:" + N);
        }

        // fft of even terms
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd = even;  // reuse the array
        for (int k = 0; k < N / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    // compute the inverse FFT of x[], assuming its length is a power of 2
    public static Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].times(1.0 / N);
        }

        return y;

    }

    // compute the circular convolution of x and y
    public static Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }

        int N = x.length;

        // compute FFT of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }

    // compute the linear convolution of x and y
    public static Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2 * x.length];
        for (int i = 0; i < x.length; i++) {
            a[i] = x[i];
        }
        for (int i = x.length; i < 2 * x.length; i++) {
            a[i] = ZERO;
        }

        Complex[] b = new Complex[2 * y.length];
        for (int i = 0; i < y.length; i++) {
            b[i] = y[i];
        }
        for (int i = y.length; i < 2 * y.length; i++) {
            b[i] = ZERO;
        }

        return cconvolve(a, b);
    }

    // test client
    public static void main(String[] args) {
        int N = 2048;
        double delta = 100;



        Complex[] x = new Complex[N];

        // original data
        for (int i = 0; i < N; i++) {
            x[i] = new Complex(i, 0);
            x[i] = new Complex(-2 * Math.random() + 1, 0);
        }
        for (int i = 0; i < N; i++) {
            System.out.println(x[i]);
        }
        System.out.println();

        // FFT of original data
        Complex[] y = fft(x);
        for (int i = 0; i < N; i++) {
            System.out.println(y[i]);
        }
        System.out.println();

        // take inverse FFT
        Complex[] z = ifft(y);
        for (int i = 0; i < N; i++) {
            System.out.println(z[i]);
        }
        System.out.println();

        // circular convolution of x with itself
        Complex[] c = convolve(x, x);
        for (int i = 0; i < N; i++) {
            System.out.println(c[i]);
        }
        System.out.println();

        // linear convolution of x with itself
        Complex[] d = convolve(x, x);
        for (int i = 0; i < d.length; i++) {
            System.out.println(d[i]);
        }
        System.out.println();

    }
}

