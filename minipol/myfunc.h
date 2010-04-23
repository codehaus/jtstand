/* 
 * File:   myfunc.h
 * Author: albert_kurucz
 *
 * Created on April 22, 2010, 11:26 PM
 */

#ifndef _MYFUNC_H
#define	_MYFUNC_H

#ifdef	__cplusplus
extern "C" {
#endif

    extern int my_optimize(double *par, gsl_vector *x);
    extern double my_f(const gsl_vector *v, void *params);
#ifdef	__cplusplus
}
#endif

#endif	/* _MYFUNC_H */

