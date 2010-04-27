/* 
 * File:   measurements.h
 * Author: albert_kurucz
 *
 * Created on April 21, 2010, 9:29 AM
 */

#ifndef _MEASUREMENTS_H
#define	_MEASUREMENTS_H

#include <gsl/gsl_vector_double.h>
#include <gsl/gsl_vector_int.h>

#ifdef	__cplusplus
extern "C" {
#endif

    typedef struct measurement_type{
        gsl_vector* x;
        double y;
        double* params;
        void* next;
    } Measurement;

    extern int measurement_add(
                    Measurement** meas,
                    gsl_vector* x,
                    double y);
    extern int measurement_optimize(//measurements
                         Measurement* meas,
                         //factors
                         const gsl_vector_int* f,
                         //start vector
                         gsl_vector* x);

#ifdef	__cplusplus
}
#endif

#endif	/* _MEASUREMENTS_H */

