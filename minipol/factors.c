#include <stdlib.h>
#include <stdio.h>
#include <gsl/gsl_vector_int.h>

int factorScalarToVector(const gsl_vector_int* f, int s, int* a) {
    int i;
    int fi;
    for (i = f->size - 1; i >= 0; i--) {
        fi = f->data[i];
        a[i] = s % fi;
        s /= fi;
    }
    return s; //error if not zero
}

int factorVectorToScalar(const gsl_vector_int* f, int* a) {
    int i;
    int retval = 0;

    for (i = 0; i < f->size; i++) {
        retval *= f->data[i];
        retval += a[i];
    }
    return retval;
}

int factorSize(const gsl_vector_int* f) {
    int i;
    int retval = 1;

    for (i = 0; i < f->size; i++) {
        retval *= f->data[i];
    }
    return retval;
}

int factorTest(void) {
    int i;
    gsl_vector_int* f = gsl_vector_int_alloc(3);
    gsl_vector_int_set(f, 0, 2);
    gsl_vector_int_set(f, 1, 4);
    gsl_vector_int_set(f, 2, 5);

    int n = 3;
    int m = factorSize(f);
    printf("m:%d\r\n", m);

    int a[3];
    int s = 1;
    factorScalarToVector(f, s, a);
    printf("s:%d\r\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\r\n", i, a[i]);
    }
    s = factorVectorToScalar(f, a);
    printf("s:%d\r\n\r\n", s);

    s = 39;
    factorScalarToVector(f, s, a);
    printf("s:%d\r\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\r\n", i, a[i]);
    }
    s = factorVectorToScalar(f, a);
    printf("s:%d\r\n\r\n", s);

    s = 25;
    factorScalarToVector(f, s, a);
    printf("s:%d\r\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\r\n", i, a[i]);
    }
    s = factorVectorToScalar(f, a);
    printf("s:%d\r\n\r\n", s);
}
