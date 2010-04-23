#include <stdlib.h>
#include <stdio.h>
#include "factors.h"

int factor_scalar_to_vector(const gsl_vector_int* f, int s, int* a) {
    int i;
    int fi;
    for (i = f->size - 1; i >= 0; i--) {
        fi = f->data[i];
        a[i] = s % fi;
        s /= fi;
    }
    return s; //error if not zero
}

int factor_vector_to_scalar(const gsl_vector_int* f, int* a) {
    int i;
    int retval = 0;

    for (i = 0; i < f->size; i++) {
        retval *= f->data[i];
        retval += a[i];
    }
    return retval;
}

int factor_size(const gsl_vector_int* f) {
    int i;
    int retval = 1;

    for (i = 0; i < f->size; i++) {
        retval *= f->data[i];
    }
    return retval;
}

int factor_compute_params(const gsl_vector_int* f,
        const gsl_vector* x,
        double y,
        double* params) {
    //TBD
    int s;
    int i;
    int f_size = factor_size(f);
    int* a = (int*) malloc(f->size * sizeof (int));
    if (!a) {
        return -1;
    }
    *params = 1.0;
    for (s = 1; s < f_size; s++) {
        factor_scalar_to_vector(f, s, a);
        for (i = 0; i < f->size; i++) {
            if (a[i]) {
                a[i]--;
                params[s] = params[factor_vector_to_scalar(f, a)]
                        * gsl_vector_get(x, i);
                break;
            }
        }
    }
    free(a);
    for (s = 0; s < f_size; s++) {
        params[s] /= y;
    }
    return 0;
}

