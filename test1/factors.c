#include <stdlib.h>

int factorScalarToVector(int* f, int n, int s, int* a) {
    int i;
    
    for (i = n - 1; i >= 0; i--) {
        a[i] = s % f[i];
        s /= f[i];
    }
    return s; //error if not zero
}

int factorVectorToScalar(int* f, int n, int* a) {
    int i;
    int retval = 0;

    for (i = 0; i < n; i++) {
        retval *= f[i];
        retval += a[i];
    }
    return retval;
}

int factorMulti(int* f, int n) {
    int retval = 1;

    for (n--; n >= 0; n--) {
        retval *= f[n];
    }
    return retval;
}

int factorTest(void) {
    int i;
    int f[] = {2, 4, 5};
    int n = 3;
    int m = factorMulti(f, 3);
    printf("m:%d\r\n", m);

    int a[3];
    int s = 1;
    factorScalarToVector(f, n, s, a);
    printf("s:%d\r\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\r\n", i, a[i]);
    }
    s = factorVectorToScalar(f, n, a);
    printf("s:%d\r\n\r\n", s);

    s = 39;
    factorScalarToVector(f, n, s, a);
    printf("s:%d\r\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\r\n", i, a[i]);
    }
    s = factorVectorToScalar(f, n, a);
    printf("s:%d\r\n\r\n", s);

    s = 25;
    factorScalarToVector(f, n, s, a);
    printf("s:%d\r\n", s);
    for (i = 0; i < n; i++) {
        printf("a[%d]:%d\r\n", i, a[i]);
    }
    s = factorVectorToScalar(f, n, a);
    printf("s:%d\r\n\r\n", s);
}
