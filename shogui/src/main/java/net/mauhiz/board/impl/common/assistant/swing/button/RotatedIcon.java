package net.mauhiz.board.impl.common.assistant.swing.button;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

/**
*  The RotatedIcon allows you to change the orientation of an Icon by
*  rotating the Icon before it is painted. This class supports the following
*  orientations:
*
* <ul>
* <li>DOWN - rotated 90 degrees
* <li>UP (default) - rotated -90 degrees
* <li>UPSIDE_DOWN - rotated 180 degrees
* <li>ABOUT_CENTER - the icon is rotated a specfic angle about its center. The
*  angle of rotation is specified when the class is created.
* </ul>
*/
public class RotatedIcon implements Icon {

	private double angle;

	private Icon icon;

	private Rotation rotate;

	/**
	 *  Convenience constructor to create a RotatedIcon that is rotated DOWN.
	 *
	 *  @param icon  the Icon to rotate
	 */
	public RotatedIcon(Icon icon) {
		this(icon, Rotation.UP);
	}

	/**
	 *  Create a RotatedIcon. The icon will rotate about its center. This
	 *  constructor will automatically set the Rotate enum to ABOUT_CENTER.
	 *  For rectangular icons the icon will be clipped before the rotation
	 *  to make sure it doesn't paint over the rest of the component.
	 *
	 *  @param icon    the Icon to rotate
	 *  @param angle   the angle of rotation
	 */
	public RotatedIcon(Icon icon, double angle) {
		this(icon, Rotation.ABOUT_CENTER);
		this.angle = angle;
	}

	/**
	 *  Create a RotatedIcon
	 *
	 *  @param icon    the Icon to rotate
	 *  @param rotate  the direction of rotation
	 */
	public RotatedIcon(Icon icon, Rotation rotate) {
		this.icon = icon;
		this.rotate = rotate;
	}

	/**
	 *  Gets the angle of rotation. Only use for Rotate.ABOUT_CENTER.
	 *
	 *  @return the angle of rotation
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 *  Gets the Icon to be rotated
	 *
	 *  @return the Icon to be rotated
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 *  Gets the height of this icon.
	 *
	 *  @return the height of the icon in pixels.
	 */
	public int getIconHeight() {
		if (rotate == Rotation.UPSIDE_DOWN || rotate == Rotation.ABOUT_CENTER) {
			return icon.getIconHeight();
		}
		return icon.getIconWidth();
	}

	//
	//  Implement the Icon Interface
	//

	/**
	 *  Gets the width of this icon.
	 *
	 *  @return the width of the icon in pixels.
	 */
	public int getIconWidth() {
		if (rotate == Rotation.UPSIDE_DOWN || rotate == Rotation.ABOUT_CENTER) {
			return icon.getIconWidth();
		}
		return icon.getIconHeight();
	}

	/**
	 *  Gets the Rotate enum which indicates the direction of rotation
	 *
	 *  @return the Rotate enum
	 */
	public Rotation getRotate() {
		return rotate;
	}

	/**
	 *  Paint the icons of this compound icon at the specified location
	 *
	 *  @param c The component on which the icon is painted
	 *  @param g the graphics context
	 *  @param x the X coordinate of the icon's top-left corner
	 *  @param y the Y coordinate of the icon's top-left corner
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();

		int cWidth = icon.getIconWidth() / 2;
		int cHeight = icon.getIconHeight() / 2;
		int xAdjustment = icon.getIconWidth() % 2 == 0 ? 0 : -1;
		int yAdjustment = icon.getIconHeight() % 2 == 0 ? 0 : -1;

		if (rotate == Rotation.DOWN) {
			g2.translate(x + cHeight, y + cWidth);
			g2.rotate(Math.toRadians(90));
			icon.paintIcon(c, g2, -cWidth, yAdjustment - cHeight);
		} else if (rotate == Rotation.UP) {
			g2.translate(x + cHeight, y + cWidth);
			g2.rotate(Math.toRadians(-90));
			icon.paintIcon(c, g2, xAdjustment - cWidth, -cHeight);
		} else if (rotate == Rotation.UPSIDE_DOWN) {
			g2.translate(x + cWidth, y + cHeight);
			g2.rotate(Math.toRadians(180));
			icon.paintIcon(c, g2, xAdjustment - cWidth, yAdjustment - cHeight);
		} else if (rotate == Rotation.ABOUT_CENTER) {
			Rectangle r = new Rectangle(x, y, icon.getIconWidth(), icon.getIconHeight());
			g2.setClip(r);
			AffineTransform original = g2.getTransform();
			AffineTransform at = new AffineTransform();
			at.concatenate(original);
			at.rotate(Math.toRadians(angle), x + cWidth, y + cHeight);
			g2.setTransform(at);
			icon.paintIcon(c, g2, x, y);
			g2.setTransform(original);
		}
	}
}
