package com.hopto.dtanjp.particles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.hopto.dtanjp.tictactoe.GameController;
/**
 * FallingParticle.java
 * 
 * @author David Tan
 **/
public class FallingParticle extends Particle {

	/** Constructor **/
	public FallingParticle(BufferedImage image) {
		sprite = image;
		
		int size = (int)(10 + (Math.random()*100));
		width = size;
		height = size;
		y = (float) (-size + ((Math.random() * size) * -1));
		transform = toBufferedImage(sprite.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH));
		dy = (float) (0.15f * 1+ (Math.random() * 5));
		dx = 0.15f * (-5 + (int)(Math.random() * 5));
	}
	
	@Override
	public void render(Graphics2D g) {
		float alpha = 0.1f * width;
		if(alpha < 0.01f)
			alpha = 0.01f;
		else if(alpha > 1f)
			alpha = 1f;
		transform = setAlpha(transform, alpha);
		if(sprite != null)
			g.drawImage(transform, (int)x, (int)y, null);
	}

	@Override
	public void process() {
		if(y < GameController.MAIN_MENU.getHeight()) {
			y += dy;
			x += dx;
		} else {
			int size = (int)(10 + (Math.random()*100));
			width = size;
			height = size;
			y = -size;
			transform = toBufferedImage(sprite.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH));
			dy = (float) (0.15f * 1+ (Math.random() * 5));
			dx = 0.15f * (1 + (int)(Math.random() * 5));
		}
	}
	
	/** Direction **/
	private float dx = 0, dy = 0.05f;
	
	/** Image sprite **/
	private BufferedImage sprite = null, transform = null;
}
