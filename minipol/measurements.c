#include <stdlib.h>
#include "measurements.h"

Measurement* measurement_add(
        Measurement* next,
        gsl_vector* x,
        double y) {
    Measurement* m = (Measurement*) malloc(sizeof (Measurement));
    m->x = x;
    m->y = y;
    m->next = next;
    return m;
}
