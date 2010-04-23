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
    gsl_vector_set(x, 0, 5.0);
    gsl_vector_set(x, 1, 7.0);

    my_optimize(par, x);
    gsl_vector_free(x);
}

Measurement* meas_test_data(void) {
    Measurement* meas = NULL;
    double xx, xy;
    double y;
    double par[5] = {1.0, 2.0, 10.0, 20.0, 30.0};
    gsl_vector *x = gsl_vector_alloc(2);
    for (xx = 10.0; xx < 23.0; xx += 1.0) {
        for (xy = 10.0; xy < 23.0; xy += 1.0) {
            gsl_vector_set(x, 0, xx);
            gsl_vector_set(x, 1, xy);
            y = my_f(x, par);
            printf("%.5f %.5f %10.5f\n", xx, xy, y);
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

/*
 *
 */
int main(int argc, char** argv) {
    my_test();
    factor_test();
    meas_test();
    return (EXIT_SUCCESS);
}
