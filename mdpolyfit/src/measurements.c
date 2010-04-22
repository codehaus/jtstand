#include <stdlib.h>
#include <gsl/gsl_nan.h>
#include "factors.h"
#include "measurements.h"

int measurement_add(Measurement** meas, gsl_vector* x, double y) {
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
	Measurement* m = (Measurement*) malloc(sizeof(Measurement));
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

int measurement_compute_params(Measurement* meas, const gsl_vector_int* f,
		double* allparams) {
	int i = 0;
	double* params;
	int f_size;
	int m_size;
	if (m_size = measurement_size(meas)) {
		f_size = factor_size(f);
		allparams = (double*) malloc(f_size * m_size * sizeof(double));
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
	Measurement *p = (Measurement *) params;
	double f = 0.0;
	while (p) {
		f += polifunc_f(v, p->params);
		p = p->next;
	}
	return f;
}

void measurement_df(const gsl_vector *v, void *params, gsl_vector *df) {
	double f;
	my_fdf(v, params, &f, df);
}

void measurement_fdf(const gsl_vector *v, void *params, double *f,
		gsl_vector *df) {
	Measurement *p = (Measurement *) params;
	double pf;
	gsl_vector* pdf = gsl_vector_alloc(df->size);
	if (pdf) {
		*f = 0.0;
		gsl_vector_set_zero(df);
		while (p) {
			df += polifunc_fdf(v, p->params, &pf, pdf);
			*f += pf;
			gsl_vector_add(df, pdf);
			p = p->next;
		}
		gsl_vector_free(pdf);
	} else {
		*f = GSL_NAN;
		gsl_vector_set_all(df, GSL_NAN);
	}
}
