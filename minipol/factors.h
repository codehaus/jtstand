/* 
 * File:   factors.h
 * Author: albert_kurucz
 *
 * Created on April 21, 2010, 10:57 AM
 */

#ifndef _FACTORS_H
#define	_FACTORS_H

#include <gsl/gsl_vector_int.h>
#include <gsl/gsl_vector_double.h>

#ifdef	__cplusplus
extern "C" {
#endif

    extern int factor_size(const gsl_vector_int* f);

    extern int factor_compute_params(
            const gsl_vector_int* f,
            const gsl_vector* x,
            double y,
            double* params);
    extern int factorTest(void);

#ifdef	__cplusplus
}
#endif

#endif	/* _FACTORS_H */

