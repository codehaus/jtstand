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
                continue;
            }
        }
    }
    free(a);
    for (s = 0; s < f_size; s++) {
        params[s] /= y;
    }
    return 0;
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
