/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, VolatileImageBackgroundPainterStrategy.java is part of JTStand.
 *
 * JTStand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JTStand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fife.ui.rtextarea;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.VolatileImage;


/**
 * A strategy for painting the background of an <code>RTextAreaBase</code>
 * as an image.  The image is always stretched to completely fill the
 * <code>RTextAreaBase</code>.<p>
 *
 * A <code>java.awt.image.VolatileImage</code> is used for rendering;
 * theoretically, this should be the best image format for performance.<p>
 *
 * You can set the scaling hint used when stretching/skewing the image
 * to fit in the <code>RTextAreaBase</code>'s background via the
 * <code>setScalingHint</code> method, but keep in mind the more
 * accurate the scaling hint, the less responsive your application will
 * be when stretching the window (as that's the only time the image's
 * size is recalculated).
 *
 * @author Robert Futrell
 * @version 0.1
 * @see org.fife.ui.rtextarea.ImageBackgroundPainterStrategy
 * @see org.fife.ui.rtextarea.BufferedImageBackgroundPainterStrategy
 */
public class VolatileImageBackgroundPainterStrategy
					extends ImageBackgroundPainterStrategy {

	private VolatileImage bgImage;


	/**
	 * Constructor.
	 *
	 * @param ta The text area whose background we'll be painting.
	 */
	public VolatileImageBackgroundPainterStrategy(RTextAreaBase ta) {
		super(ta);
	}


	/**
	 * Paints the image at the specified location.  This method assumes
	 * scaling has already been done, and simply paints the background
	 * image "as-is."
	 *
	 * @param g The graphics context.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 */
	protected void paintImage(Graphics g, int x, int y) {
		if (bgImage != null) {
			do {
				int rc = bgImage.validate(null);//getGraphicsConfiguration());
				if (rc==VolatileImage.IMAGE_RESTORED) {
					// FIXME:  If the image needs to be restored are its width
					// and height still valid??  If not, we'll need to cache
					// these values...
					renderImage(bgImage.getWidth(), bgImage.getHeight(),
						getScalingHint());
				}
				g.drawImage(bgImage, x,y, null);
			} while (bgImage.contentsLost());
		}
	}


	/**
	 * Renders the image at the proper size into <code>bgImage</code>.
	 * This method assumes that <code>bgImage</code> is not
	 * <code>null</code>.
	 *
	 * @param width The width of the volatile image to render into.
	 * @param height The height of the volatile image to render into.
	 * @param hint The scaling hint to use.
	 */
	protected void renderImage(int width, int height, int hint) {
		Image master = getMasterImage();
		if (master!=null) {
			do {
				Image i = master.getScaledInstance(width,height, hint);
				tracker.addImage(i, 1);
				try {
					tracker.waitForID(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					bgImage = null;
					return;
				} finally {
					tracker.removeImage(i, 1);
				}
				bgImage.getGraphics().drawImage(i, 0,0, null);
				tracker.addImage(bgImage, 0);
				try {
					tracker.waitForID(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
					bgImage = null;
					return;
				} finally {
					tracker.removeImage(bgImage, 0);
				}
			} while (bgImage.contentsLost());
		} // End of if (master!=null).
		else {
			bgImage = null;
		}
	}


	/**
	 * Rescales the displayed image to be the specified size.
	 *
	 * @param width The new width of the image.
	 * @param height The new height of the image.
	 * @param hint The scaling hint to use.
	 */
	protected void rescaleImage(int width, int height, int hint) {
		bgImage = getRTextAreaBase().createVolatileImage(width, height);
		if (bgImage!=null)
			renderImage(width, height, hint);
	}


}