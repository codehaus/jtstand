#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=
AS=as

# Macros
CND_PLATFORM=Cygwin-Windows
CND_CONF=DebugWin32
CND_DISTDIR=dist

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/polifunc.o \
	${OBJECTDIR}/factors.o \
	${OBJECTDIR}/main.o \
	${OBJECTDIR}/measurements.o

# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=C:/MagIC3SDK/utils/root/lib/libgsl.a C:/MagIC3SDK/utils/root/lib/libgslcblas.a C:/MagIC3SDK/utils/root/lib/libm.a

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-DebugWin32.mk dist/DebugWin32/Cygwin-Windows/minipol.exe

dist/DebugWin32/Cygwin-Windows/minipol.exe: /cygdrive/C/MagIC3SDK/utils/root/lib/libgsl.a

dist/DebugWin32/Cygwin-Windows/minipol.exe: /cygdrive/C/MagIC3SDK/utils/root/lib/libgslcblas.a

dist/DebugWin32/Cygwin-Windows/minipol.exe: /cygdrive/C/MagIC3SDK/utils/root/lib/libm.a

dist/DebugWin32/Cygwin-Windows/minipol.exe: ${OBJECTFILES}
	${MKDIR} -p dist/DebugWin32/Cygwin-Windows
	${LINK.c} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/minipol ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/polifunc.o: nbproject/Makefile-${CND_CONF}.mk polifunc.c 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/polifunc.o polifunc.c

${OBJECTDIR}/factors.o: nbproject/Makefile-${CND_CONF}.mk factors.c 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/factors.o factors.c

${OBJECTDIR}/main.o: nbproject/Makefile-${CND_CONF}.mk main.c 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/main.o main.c

${OBJECTDIR}/measurements.o: nbproject/Makefile-${CND_CONF}.mk measurements.c 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.c) -g -MMD -MP -MF $@.d -o ${OBJECTDIR}/measurements.o measurements.c

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r build/DebugWin32
	${RM} dist/DebugWin32/Cygwin-Windows/minipol.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
