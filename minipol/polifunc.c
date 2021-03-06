#include <gsl/gsl_vector_double.h>
#include <math.h>

double polifunc(const gsl_vector *v, void *params) {
    int i, j;
    double f = -1.0;
    double t;

    double *p = (double *) params;
    for (i = 0; i < v->size; i++) {
        t = gsl_vector_get(v, i) * p[i];
        f += t * t;
        for (j = 0; j < i; j++) {
            f += 2.0 * t * gsl_vector_get(v, j) * p[j];
        }
    }
    return f; //no abs
}

double polifunc_f(const gsl_vector *v, void *params) {
    double f = polifunc(v,params);
    return (f >= 0.0) ? f : -f; //abs
}

void polifunc_fdf(const gsl_vector *v, void *params,
                  double *f, gsl_vector *df) {
    int i, j;
    double *p = (double *) params;
    double t;

    for (i = 0; i < v->size; i++) {
        t = gsl_vector_get(v, i) * p[i];
        gsl_vector_set(df, i, 2.0 * t);
        for (j = 0; j < v->size; j++) {
            if (i != j) {
                gsl_vector_set(df, i, gsl_vector_get(v, j) * p[j] + gsl_vector_get(df, i));
            }
        }
    }
    //abs
    if ((*f = polifunc(v, params)) < 0.0) {
        *f = -*f;
        for (i = 0; i < v->size; i++) {
            gsl_vector_set(df, i, -gsl_vector_get(df, i));
        }
    }
}

void polifunc_df(const gsl_vector *v, void *params,
                 gsl_vector *df) {
    double f;
    polifunc_fdf(v, params, &f, df);
}
