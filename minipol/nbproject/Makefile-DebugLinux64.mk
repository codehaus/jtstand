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
CC=gcc.exe
CCC=g++.exe
CXX=g++.exe
FC=
AS=as.exe

# Macros
CND_PLATFORM=Cygwin_ofMagIC3SDK-Windows
CND_CONF=DebugLinux64
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
LDLIBSOPTIONS=/usr/lib64/libgsl.a /usr/lib64/libgslcblas.a /usr/lib64/libm.a

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-DebugLinux64.mk dist/DebugLinux64/Cygwin_ofMagIC3SDK-Windows/minipol.exe

dist/DebugLinux64/Cygwin_ofMagIC3SDK-Windows/minipol.exe: /usr/lib64/libgsl.a

dist/DebugLinux64/Cygwin_ofMagIC3SDK-Windows/minipol.exe: /usr/lib64/libgslcblas.a

dist/DebugLinux64/Cygwin_ofMagIC3SDK-Windows/minipol.exe: /usr/lib64/libm.a

dist/DebugLinux64/Cygwin_ofMagIC3SDK-Windows/minipol.exe: ${OBJECTFILES}
	${MKDIR} -p dist/DebugLinux64/Cygwin_ofMagIC3SDK-Windows
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
	${RM} -r build/DebugLinux64
	${RM} dist/DebugLinux64/Cygwin_ofMagIC3SDK-Windows/minipol.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
