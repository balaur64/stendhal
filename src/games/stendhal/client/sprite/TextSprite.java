package games.stendhal.client.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Outlined text representation of a string.
 */
public class TextSprite extends ImageSprite {
	// needed only because there's no other reliable way to calculate 
	// string widths other than having a Graphics object
	private static final Graphics graphics = (new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)).getGraphics(); 
	
	protected TextSprite(Image image) {
		super(image);
	}
	
	/**
	 * Create a new <code>TextSprite</code>
	 * 
	 * @param text The text to be rendered 
	 * @param textColor Color of the text
	 * @return TextSprite with the wanted text
	 */
	public static TextSprite createTextSprite(String text, final Color textColor) {
		final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		final Image image = gc.createCompatibleImage(graphics.getFontMetrics().stringWidth(
				text) + 2, 16, Transparency.BITMASK);
		final Graphics g2d = image.getGraphics();

		drawOutlineString(g2d, textColor, text, 1, 10);

		return new TextSprite(image);
	}
	
	/**
	 * Draw a text string (like <em>Graphics</em><code>.drawString()</code>)
	 * only with an outline border. The area drawn extends 1 pixel out on all
	 * side from what would normal be drawn by drawString().
	 * 
	 * @param g The graphics context.
	 * @param textColor The text color.
	 * @param outlineColor The outline color.
	 * @param text The text to draw.
	 * @param x X position.
	 * @param y Y position.
	 */
	private static void drawOutlineString(final Graphics g, final Color textColor,
			final String text, final int x, final int y) {
		/*
		 * Use light gray as outline for colors < 25% bright. Luminance = 0.299R +
		 * 0.587G + 0.114B
		 */
		final int lum = ((textColor.getRed() * 299) + (textColor.getGreen() * 587) + (textColor.getBlue() * 114)) / 1000;

		Color outlineColor;
		if (lum >= 64) {
			outlineColor = Color.black;
		} else {
			outlineColor = Color.lightGray;
		}
		drawOutlineString(g, textColor, outlineColor, text, x, y);
	}
	
	/**
	 * Draw a text string (like <em>Graphics</em><code>.drawString()</code>)
	 * only with an outline border. The area drawn extends 1 pixel out on all
	 * side from what would normal be drawn by drawString().
	 *
	 * @param g
	 *            The graphics context.
	 * @param textColor
	 *            The text color.
	 * @param outlineColor
	 *            The outline color.
	 * @param text
	 *            The text to draw.
	 * @param x
	 *            The X position.
	 * @param y
	 *            The Y position.
	 */
	private static void drawOutlineString(final Graphics g, final Color textColor,
			final Color outlineColor, final String text, final int x,
			final int y) {
		g.setColor(outlineColor);
		g.drawString(text, x - 1, y - 1);
		g.drawString(text, x - 1, y + 1);
		g.drawString(text, x + 1, y - 1);
		g.drawString(text, x + 1, y + 1);

		g.setColor(textColor);
		g.drawString(text, x, y);
	}
}
