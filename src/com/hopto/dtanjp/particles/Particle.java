package com.hopto.dtanjp.particles;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Particle.java
 * 
 * @author David Tan
 **/
public abstract class Particle {

	/** Positions **/
	public float x, y;
	
	/** Size **/
	public int width, height;
	
	public abstract void render(Graphics2D g);
	public abstract void process();
	
	/** Set the alpha of the image **/
	protected BufferedImage setAlpha(BufferedImage image, float alpha) {
		if(image == null) return null;
	    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = result.createGraphics();
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    return result;
	}
	
	/** Recolors the image **/
	protected BufferedImage recolor(BufferedImage image, Color color) {
		if(image == null) return null;
	    int w = image.getWidth();
	    int h = image.getHeight();
	    BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = result.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.setComposite(AlphaComposite.SrcAtop);
	    g.setColor(color);
	    g.fillRect(0, 0, w, h);
	    g.dispose();
	    return result;
	}
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	protected BufferedImage toBufferedImage(Image img) {
	    if (img instanceof BufferedImage) {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
}