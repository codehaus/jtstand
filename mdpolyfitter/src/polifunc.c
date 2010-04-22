#include <gsl/gsl_vector_double.h>
#include <math.h>

double polifunc_f(const gsl_vector *v, void *params) {
	int i;
	double f = -1.0;

	double *p = (double *) params;

	for (i = 0; i < v->size; i++) {
		f += gsl_vector_get(v, i) * p[i];
	}
	return (f >= 0) ? f : -f; //abs
}

void polifunc_fdf(const gsl_vector *v, void *params, double *f, gsl_vector *df) {
	int i;
	double *p = (double *) params;
	if ((*f = polifunc_f(v, params)) >= 0) {
		for (i = 0; i < v->size; i++) {
			gsl_vector_set(df, i, p[i]);
		}

	} else {
		for (i = 0; i < v->size; i++) {
			gsl_vector_set(df, i, -p[i]);
		}
	}
}

void polifunc_df(const gsl_vector *v, void *params, gsl_vector *df) {
	double f;
	polifunc_fdf(v, params, &f, df);
}

