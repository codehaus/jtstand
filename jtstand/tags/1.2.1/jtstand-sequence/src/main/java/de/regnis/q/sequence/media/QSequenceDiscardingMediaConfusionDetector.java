/*
 * ====================================================================
 * Copyright (c) 2000-2008 SyntEvo GmbH, info@syntevo.com
 * All rights reserved.
 *
 * This software is licensed as described in the file SEQUENCE-LICENSE,
 * which you should have received as part of this distribution. Use is
 * subject to license terms.
 * ====================================================================
 */

package de.regnis.q.sequence.media;

/**
 * @author Marc Strapetz
 */
public interface QSequenceDiscardingMediaConfusionDetector {

	void init(int symbolCount);

	boolean isAbsolute(int occurences);

	boolean isProvisional(int occurences);
}