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

typedef struct {
	gsl_vector* x;
	double y;
	double* params;
	void* next;
} Measurement;

#ifdef	__cplusplus
}
#endif

#endif	/* _MEASUREMENTS_H */

