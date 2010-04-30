#include <stdlib.h>
#include <gsl/gsl_sys.h>
#include <gsl/gsl_nan.h>
#include <gsl/gsl_multimin.h>

#include "factors.h"
#include "polifunc.h"
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
    if (*meas) {
        if ((*meas)->x->size != x->size) {
            return -4;
        }
    }
    Measurement* m = (Measurement*) malloc(sizeof (Measurement));
    if (!m) {
        return -5;
    }
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

double* measurement_allparams_alloc(Measurement* meas,
        const gsl_vector_int* f) {
    int i = 0;
    double* allparams;
    double* params;
    int f_size;
    int m_size;

    if ((m_size = measurement_size(meas))) {
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
        }
        return allparams;
    }
    return NULL;
}

double measurement_f(const gsl_vector *v, void *params) {
    Measurement *meas = (Measurement *) params;
    double f = 0.0;
    while (meas) {
        f += polifunc_f(v, meas->params);
        meas = meas->next;
    }
    return f;
}

void measurement_fdf(const gsl_vector *v, void *params,
        double *f, gsl_vector *df) {
    Measurement *p = (Measurement *) params;
    double pf;
    gsl_vector* pdf = gsl_vector_alloc(df->size);
    if (pdf) {
        *f = 0.0;
        gsl_vector_set_zero(df);
        while (p) {
            polifunc_fdf(v, p->params, &pf, pdf);
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

void measurement_df(const gsl_vector *v,
        void *params,
        gsl_vector *df) {
    double f;
    measurement_fdf(v, params, &f, df);
}

void measurement_print(Measurement* meas, const gsl_vector_int* f) {
    printf("measSize %d\n", measurement_size(meas));

    printf("x0 %f\n", gsl_vector_get(meas->x, 0));
    printf("x1 %f\n", gsl_vector_get(meas->x, 1));
    printf("y %f\n", meas->y);

    if (meas->params) {
        int f_size = factor_size(f);
        int i;
        for (i = 0; i < f_size; i++) {
            printf("param%d %f\n", i, meas->params[i]);
        }
    }
}

int measurement_optimize(//measurements
        Measurement* meas,
        //factors
        const gsl_vector_int* f,
        //start vector
        gsl_vector* x) {
    int i;
    int iter = 0;
    int status;
    printf("optimizing...\n");
    const gsl_multimin_fdfminimizer_type* T;

    gsl_multimin_fdfminimizer *fdfminimizer;
    gsl_multimin_function_fdf measurement_func;
    measurement_func.n = x->size;
    measurement_func.f = measurement_f;
    measurement_func.df = measurement_df;
    measurement_func.fdf = measurement_fdf;
    measurement_func.params = meas;


    double* allparams = measurement_allparams_alloc(meas, f);

    printf("computing error...\n");
    double error = measurement_f(x, meas);
    printf("error %f\n", error);

    //measurement_print(meas, f);
    //measurement_print(meas->next, f);


    //T = gsl_multimin_fdfminimizer_conjugate_fr;
    //T = gsl_multimin_fdfminimizer_conjugate_pr;
    //T = gsl_multimin_fdfminimizer_vector_bfgs;
    T = gsl_multimin_fdfminimizer_vector_bfgs2;
    //T = gsl_multimin_fminimizer_nmsimplex;
    fdfminimizer = gsl_multimin_fdfminimizer_alloc(T, x->size);
    gsl_multimin_fdfminimizer_set(fdfminimizer, &measurement_func, x, 0.1, 0.1);

    do {
        iter++;
        status = gsl_multimin_fdfminimizer_iterate(fdfminimizer);
        printf("status: %d\n", status);
        if (status)
            break;

        status = gsl_multimin_test_gradient(fdfminimizer->gradient, 1e-3);

        if (status == GSL_SUCCESS) {
            printf("Minimum found at:\n");
        }
        printf("%5d %10.5f\n", iter, fdfminimizer->f);
        for (i = 0; i < fdfminimizer->x->size; i++) {
            printf("x[%d] %.5f\n", i, gsl_vector_get(fdfminimizer->x, i));
        }

    } while (status == GSL_CONTINUE && iter < 100);


    gsl_multimin_fdfminimizer_free(fdfminimizer);
    free(allparams);
    printf("optimization done\n");
    return (EXIT_SUCCESS);
}

int measurement_optimize_f(//measurements
        Measurement* meas,
        //factors
        const gsl_vector_int* f,
        //start vector
        gsl_vector* x) {
    int i;
    int iter = 0;
    int status;
    printf("optimizing...\n");
    const gsl_multimin_fminimizer_type* T;


    gsl_multimin_fminimizer *fminimizer;
    gsl_multimin_function minex_func;
    minex_func.n = x->size;
    minex_func.f = measurement_f;
    minex_func.params = meas;

    double* allparams = measurement_allparams_alloc(meas, f);

    printf("computing error...\n");
    double error = measurement_f(x, meas);
    printf("error %f\n", error);

    //measurement_print(meas, f);
    //measurement_print(meas->next, f);

    T = gsl_multimin_fminimizer_nmsimplex;
    fminimizer = gsl_multimin_fminimizer_alloc(T, x->size);
    gsl_vector* ss = gsl_vector_alloc(x->size);
    gsl_vector_set_all(ss, 1.0);
    gsl_multimin_fminimizer_set(fminimizer, &minex_func, x, ss);

    do {
        iter++;
        status = gsl_multimin_fminimizer_iterate(fminimizer);
        //printf("status: %d\n", status);
        if (status)
            break;

        double size = gsl_multimin_fminimizer_size(fminimizer);
        status = gsl_multimin_test_size(size, 1e-10);

        if (status == GSL_SUCCESS) {
            printf("Minimum found at:\n");
        }
        printf("%5d\n", iter);

    } while (status == GSL_CONTINUE && iter < 100000000);

    for (i = 0; i < fminimizer->x->size; i++) {
        printf("x[%d] %.5f\n", i, gsl_vector_get(fminimizer->x, i));
    }

    gsl_multimin_fminimizer_free(fminimizer);
    free(allparams);
    printf("optimization done\n");
    return (EXIT_SUCCESS);
}
