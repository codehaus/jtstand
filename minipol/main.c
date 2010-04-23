/*
 * File:   main.c
 * Author: albert_kurucz
 *
 * Created on April 14, 2010, 10:32 PM
 */

#include <stdio.h>
#include <stdlib.h>
#include <gsl/gsl_vector_double.h>
#include "factors.h"
#include "myfunc.h"
#include "measurements.h"

int my_test(void) {
    /* Position of the minimum (1,2), scale factors
       10,20, height 30. */
    double par[5] = {1.0, 2.0, 10.0, 20.0, 30.0};
    gsl_vector *x = gsl_vector_alloc(2);
    /* Starting point, x = (5,7) */
    //gsl_vector_set(x, 0, 5.0);
    //gsl_vector_set(x, 1, 7.0);

    my_optimize(par, x);
    gsl_vector_free(x);
}

Measurement* meas_test_data(void) {
    Measurement* meas = NULL;
    double xx, xy;
    double y;
    double par[5] = {1.0, 2.0, 10.0, 20.0, 30.0};    
    for (xx = 0.8; xx < 0.93; xx += 0.01) {
        for (xy = 0.8; xy < 0.93; xy += 0.01) {
            gsl_vector *x = gsl_vector_alloc(2);
            gsl_vector_set(x, 0, xx);
            gsl_vector_set(x, 1, xy);
            y = my_f(x, par);
            printf("%.2f %.2f %.3f\n", xx, xy, y);
            measurement_add(&meas, x, y);
        }
    }
    return meas;
}

int meas_test(void) {
    Measurement* meas = meas_test_data();
    gsl_vector_int* f = gsl_vector_int_alloc(2);
    if (!f) {
        return -1;
    }
    gsl_vector_int_set(f, 0, 4);
    gsl_vector_int_set(f, 1, 5);

    gsl_vector *x = gsl_vector_alloc(factor_size(f));

    measurement_optimize(meas, f, x);

    gsl_vector_free(x);
    gsl_vector_int_free(f);
}


int factor_test(void) {
    int i;
    gsl_vector_int* f = gsl_vector_int_alloc(3);
    if (!f) {
        return -1;
    }
    gsl_vector_int_set(f, 0, 2);
    gsl_vector_int_set(f, 1, 4);
    gsl_vector_int_set(f, 2, 5);

    int n = 3;
    int m = factor_size(f);
    printf("m:%d\n", m);

    int a[3];
    int s = 1;
    factor_scalar_to_vector(f, s, a);
    printf("s:%d\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\n", i, a[i]);
    }
    s = factor_vector_to_scalar(f, a);
    printf("s:%d\n\n", s);

    s = 39;
    factor_scalar_to_vector(f, s, a);
    printf("s:%d\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\n", i, a[i]);
    }
    s = factor_vector_to_scalar(f, a);
    printf("s:%d\n\n", s);

    s = 25;
    factor_scalar_to_vector(f, s, a);
    printf("s:%d\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\n", i, a[i]);
    }
    s = factor_vector_to_scalar(f, a);
    printf("s:%d\n\n", s);

    gsl_vector_int_free(f);
    return 0;
}

int factor_test2(void) {
    int i;
    gsl_vector_int* f = gsl_vector_int_alloc(2);
    if (!f) {
        return -1;
    }
    gsl_vector_int_set(f, 0, 3);
    gsl_vector_int_set(f, 1, 3);

    gsl_vector* v = gsl_vector_alloc(2);
    gsl_vector_set(v, 0, 2.0);
    gsl_vector_set(v, 1, 3.0);

    double params[9];
    factor_compute_params(f, v, 1.0, params);

    for (i = 0; i < 9; i++) {
        printf("%.0f\n", params[i]);
    }
}
/*
 *
 */
int main(int argc, char** argv) {
    my_test();
    factor_test();
    factor_test2();
    meas_test();
    return (EXIT_SUCCESS);
}
