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

double measurement_f(const gsl_vector *v, void *params) {
    double f = 0.0;
    Measurement *p = (Measurement *) params;
    while (p) {
        f += polifunc_f(v, p->params);
        p = p->next;
    }
    return f;
}
