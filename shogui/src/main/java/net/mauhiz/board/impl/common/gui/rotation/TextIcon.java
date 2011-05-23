package net.mauhiz.board.impl.common.gui.rotation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 *  The TextIcon will paint a String of text as an Icon. The Icon
 *  can be used by any Swing component that supports icons.
 *
 *  TextIcon supports two different layout styles:
 *  <ul>
 *  <li>Horizontally - does normal rendering of the text by using the
 *	  Graphics.drawString(...) method
 *  <li>Vertically - Each character is displayed on a separate line
 *  </ul>
 *
 *  TextIcon was designed to be rendered on a specific JComponent as it
 *  requires FontMetrics information in order to calculate its size and to do
 *  the rendering. Therefore, it should only be added to component it was
 *  created for.
 *
 *  By default the text will be rendered using the Font and foreground color
 *  of its associated component. However, this class does allow you to override
 *  these properties.
 */
public class TextIcon implements Icon, PropertyChangeListener {
	private JComponent component;

	private Font font;

	private Color foreground;

	private int iconHeight;

	private int iconWidth;

	private Layout layout;

	//  Used for the implementation of Icon interface

	private int padding;
	private String[] strings;

	//  Used for Layout.VERTICAL to save reparsing the text every time the
	//  icon is repainted

	private int[] stringWidths;
	private String text;

	/**
	 *  Convenience constructor to create a TextIcon with a HORIZONTAL layout.
	 *
	 *  @param component  the component to which the icon will be added
	 *  @param text       the text to be rendered on the Icon
	 */
	public TextIcon(JComponent component, String text) {
		this(component, text, Layout.HORIZONTAL);
	}

	/**
	 *  Create a TextIcon specifying all the properties.
	 *
	 *  @param component  the component to which the icon will be added
	 *  @param text       the text to be rendered on the Icon
	 *  @param layout     specify the layout of the text. Must be one of
	 *	                  the Layout enums: HORIZONTAL or VERTICAL
	 */
	public TextIcon(JComponent component, String text, Layout layout) {
		this.component = component;
		this.layout = layout;
		setText(text);

		component.addPropertyChangeListener("font", this);
	}

	/**
	 *  Calculate the size of the Icon using the FontMetrics of the Font.
	 */
	private void calculateIconDimensions() {
		Font lfont = getFont();
		FontMetrics fm = component.getFontMetrics(lfont);

		if (layout == Layout.HORIZONTAL) {
			iconWidth = fm.stringWidth(text) + padding * 2;
			iconHeight = fm.getHeight();
		} else if (layout == Layout.VERTICAL) {
			int maxWidth = 0;
			strings = new String[text.length()];
			stringWidths = new int[text.length()];

			//  Find the widest character in the text string

			for (int i = 0; i < text.length(); i++) {
				strings[i] = text.substring(i, i + 1);
				stringWidths[i] = fm.stringWidth(strings[i]);
				maxWidth = Math.max(maxWidth, stringWidths[i]);
			}

			//  Add a minimum of 2 extra pixels, plus the leading value,
			//  on each side of the character.

			iconWidth = maxWidth + (fm.getLeading() + 2) * 2;

			//  Decrease then normal gap betweens lines of text by taking into
			//  account the descent.

			iconHeight = (fm.getHeight() - fm.getDescent()) * text.length();
			iconHeight += padding * 2;
		}

		component.revalidate();
	}

	/**
	 *  Get the Font used to render the text. This will default to the Font
	 *  of the component unless the Font has been overridden by using the
	 *  setFont() method.
	 *
	 *  @return the Font used to render the text
	 */
	public Font getFont() {
		if (font == null) {
			return component.getFont();
		}
		return font;
	}

	/**
	 *  Get the foreground Color used to render the text. This will default to
	 *  the foreground Color of the component unless the foreground Color has
	 *  been overridden by using the setForeground() method.
	 *
	 *  @return the Color used to render the text
	 */
	public Color getForeground() {
		if (foreground == null) {
			return component.getForeground();
		}
		return foreground;
	}

	/**
	 *  Gets the height of this icon.
	 *
	 *  @return the height of the icon in pixels.
	 */
	public int getIconHeight() {
		return iconHeight;
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
		return iconWidth;
	}

	/**
	 *  Get the Layout enum
	 *
	 *  @return the Layout enum
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 *  Get the padding used when rendering the text
	 *
	 *  @return the padding specified in pixels
	 */
	public int getPadding() {
		return padding;
	}

	/**
	 *  Get the text String that will be rendered on the Icon
	 *
	 *  @return the text of the Icon
	 */
	public String getText() {
		return text;
	}

	/**
	*  Paint the icons of this compound icon at the specified location
	*
	*  @param c The component to which the icon is added
	*  @param g the graphics context
	*  @param x the X coordinate of the icon's top-left corner
	*  @param y the Y coordinate of the icon's top-left corner
	*/
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setFont(getFont());
		g2.setColor(getForeground());
		FontMetrics fm = g2.getFontMetrics();

		if (layout == Layout.HORIZONTAL) {
			g2.translate(x, y + fm.getAscent());
			g2.drawString(text, padding, 0);
		} else if (layout == Layout.VERTICAL) {
			int offsetY = fm.getAscent() - fm.getDescent() + padding;
			int incrementY = fm.getHeight() - fm.getDescent();

			for (int i = 0; i < text.length(); i++) {
				int offsetX = Math.round((getIconWidth() - stringWidths[i]) / 2.0f);
				g2.drawString(strings[i], x + offsetX, y + offsetY);
				offsetY += incrementY;
			}
		}
	}

	//
	//  Implement the PropertyChangeListener interface
	//
	public void propertyChange(PropertyChangeEvent e) {
		//  Handle font change when using the default font

		if (font == null) {
			calculateIconDimensions();
		}
	}

	/**
	 *  Set the Font to be used for rendering the text
	 *
	 *  @param font  the Font to be used for rendering the text
	 */
	public void setFont(Font font) {
		this.font = font;

		calculateIconDimensions();
	}

	/**
	 *  Set the foreground Color to be used for rendering the text
	 *
	 *  @param foreground  the foreground Color to be used for rendering the text
	 */
	public void setForeground(Color foreground) {
		this.foreground = foreground;
		component.repaint();
	}

	/**
	 *  By default the size of the Icon is based on the size of the rendered
	 *  text. You can specify some padding to be added to the start and end
	 *  of the text when it is rendered.
	 *
	 *  @param padding  the padding amount in pixels
	 */
	public void setPadding(int padding) {
		this.padding = padding;

		calculateIconDimensions();
	}

	/**
	 *  Set the text to be rendered on the Icon
	 *
	 *  @param text  the text to be rendered on the Icon
	 */
	public void setText(String text) {
		this.text = text;

		calculateIconDimensions();
	}
}
