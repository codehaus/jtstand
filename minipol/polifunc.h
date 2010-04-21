/* 
 * File:   polifunc.h
 * Author: albert_kurucz
 *
 * Created on April 21, 2010, 5:58 AM
 */

#ifndef _POLIFUNC_H
#define	_POLIFUNC_H

#ifdef	__cplusplus
extern "C" {
#endif

    typedef struct {
        double* x;
        int n;
        double y;
        double* f;
    } Polifunc;


#ifdef	__cplusplus
}
#endif

#endif	/* _POLIFUNC_H */

