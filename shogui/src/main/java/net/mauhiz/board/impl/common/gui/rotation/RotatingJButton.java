package net.mauhiz.board.impl.common.gui.rotation;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RotatingJButton extends JButton {

	public void setText(String iconText, boolean flip) {
		TextIcon ti = new TextIcon(this, iconText);
		if (flip) {
			ImageIcon ii = new ImageIcon();
			CompoundIcon ci = new CompoundIcon(ii, ti);
			RotatedIcon ri = new RotatedIcon(ci, Rotation.UPSIDE_DOWN);
			this.setIcon(ri);
		} else {
			setIcon(ti);
		}
	}
}
