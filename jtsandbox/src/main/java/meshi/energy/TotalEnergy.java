package meshi.energy;

import java.util.ArrayList;

/**
 * A generic class for all meshi energy functions. Typically only a single such object exists, which 
 * include several energy term objects (sub classes of AbstractEnergy).
 **/
public class TotalEnergy {

    /**
     * Number of times the energy function was evaluated. This is a simple machine-independent 
     * measure of convergence efficiency.
     **/
    protected double totalEnergy;

    /**
     * Finds the magnitude of the gradient vecor in coordinates ( coordinates[][1] ).
     **/
    public double getGradMagnitude() {
        return 0;
    }

    public double evaluate() {
        totalEnergy = 0;
        return totalEnergy;
    }

    /**
     * Returns the last enrgy value that was calculated
     **/

    public double energy() {
        return totalEnergy;
    }
}
	


