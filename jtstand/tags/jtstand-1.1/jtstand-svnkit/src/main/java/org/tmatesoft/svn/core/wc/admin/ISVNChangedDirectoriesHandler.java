/*
 * ====================================================================
 * Copyright (c) 2004-2009 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html.
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
package org.tmatesoft.svn.core.wc.admin;

import org.tmatesoft.svn.core.SVNException;


/**
 * The <b>ISVNChangedDirectoriesHandler</b> is used to process changed 
 * directory paths.
 * 
 * @version 1.3
 * @author  TMate Software Ltd.
 * @since   1.2
 */
public interface ISVNChangedDirectoriesHandler {
    /**
     * Handles changed directory path. <code>path</code> is always absolute.
     * @param  path             a changed dir path
     * @throws SVNException 
     */
    public void handleDir(String path) throws SVNException;
}
