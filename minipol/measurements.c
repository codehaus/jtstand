#include <stdlib.h>
#include "measurements.h"

int measurement_add(
        Measurement** meas,
        gsl_vector* x,
        double y) {
    if (!meas) {
        return -1;
    }
    if (!x) {
        return -2;
    }
    if (*meas && ((*meas)->x->size != x->size)) {
        return -3;
    }
    Measurement* m = (Measurement*) malloc(sizeof (Measurement));
    m->x = x;
    m->y = y;
    m->next = *meas;
    *meas = m;
    return 0;
}

int measurement_size(Measurement* meas) {
    int size = 0;
    while (meas) {
        size++;
        meas = meas->next;
    }
    return size;
}

int measurement_compute_factors(Measurement* meas, const gsl_vector_int* f, double* factors) {
    int i=0;
    int f_size;
    int m_size;
    if(!meas){
        return 0;
    }
    int f_size = factor_size(f);
    int m_size = measurement_size(meas);
    factors = double* malloc(f_size * m_size * sizeof (double));
    while(meas){
        //TBD
        meas=meas->next;
        i++;
    }
}

double measurement_f(const gsl_vector *v, void *params) {
    double f = 0.0;
    Measurement *p = (Measurement *) params;
    while (p) {
        f += polifunc_f(v, p->params);
        p = p->next;
    }
    return f;
}
