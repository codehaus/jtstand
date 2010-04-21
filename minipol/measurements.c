#include <stdlib.h>
#include "factors.h"
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
    if (0.0 == y) {
        return -3;
    }
    if (*meas && ((*meas)->x->size != x->size)) {
        return -4;
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

int measurement_compute_params(Measurement* meas, const gsl_vector_int* f, double* allparams) {
    int i = 0;
    double* params;
    int f_size;
    int m_size;
    if (m_size = measurement_size(meas)) {
        f_size = factor_size(f);
        allparams = (double*) malloc(f_size * m_size * sizeof (double));
        if (allparams) {
            while (meas) {
                params = &allparams[i];
                factor_compute_params(f, meas->x, meas->y, params);
                meas->params = params;
                i += f_size;
                meas = meas->next;
            }
        } else
            return -1;
    }
    return m_size;
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
