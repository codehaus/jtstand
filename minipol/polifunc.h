/* 
 * File:   polifunc.h
 * Author: albert
 *
 * Created on April 22, 2010, 6:08 PM
 */

#ifndef _POLIFUNC_H
#define	_POLIFUNC_H

#ifdef	__cplusplus
extern "C" {
#endif

    extern double polifunc_f(const gsl_vector *v, void *params);
    extern void polifunc_fdf(const gsl_vector *v, void *params,
            double *f, gsl_vector *df);
    extern void polifunc_df(const gsl_vector *v, void *params,
            gsl_vector *df);
    
#ifdef	__cplusplus
}
#endif

#endif	/* _POLIFUNC_H */

