package net.mauhiz.board.impl.common.gui.rotation;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;

import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.MonitoredRunnable;
import net.mauhiz.util.NamedRunnable;
import net.mauhiz.util.PerformanceMonitor;

import org.apache.commons.lang.StringUtils;

public class RotatingJButton extends JButton {

	class ButtonDecorator extends MonitoredRunnable {

		public ButtonDecorator() {
			super("Button Decorator", "Decorated button[flip=" + flip + "] with text: " + iconText);
		}

		@Override
		protected ExecutionType getExecutionType() {
			return ExecutionType.PARALLEL_CACHED;
		}

		@Override
		public void mrun() {
			Icon ti = getTextIcon();
			final Icon realIcon = flip ? getRotatedIcon(ti) : ti;

			if (getIcon() != realIcon) {
				new NamedRunnable("Set Icon") {

					@Override
					protected ExecutionType getExecutionType() {
						return ExecutionType.GUI_ASYNCHRONOUS;
					}

					@Override
					protected void trun() {
						setIcon(realIcon);
					}
				}.launch(null);
			}
		}
	}

	class ButtonEnabler extends MonitoredRunnable {
		private final boolean enabled;

		ButtonEnabler(boolean enabled) {
			super("Button Toggler");
			this.enabled = enabled;
		}

		@Override
		protected ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public String getLogMsg() {
			return "Button " + (enabled ? "enabled" : "disabled");
		}

		@Override
		public void mrun() {
			superEnable(enabled);
		}
	}

	static final Map<String, TextIcon> ICON_CACHE = new HashMap<String, TextIcon>();
	static final Map<Icon, RotatedIcon> ROTATION_CACHE = new HashMap<Icon, RotatedIcon>();
	boolean flip;

	String iconText = "";

	@Override
	public void addActionListener(ActionListener l) {
		super.addActionListener(l);
		if (!isEnabled()) {
			setEnabled(true);
		}
	}

	RotatedIcon getRotatedIcon(Icon normal) {
		synchronized (normal) {
			RotatedIcon reverse = ROTATION_CACHE.get(normal);

			if (reverse == null) {
				reverse = new RotatedIcon(normal, Rotation.UPSIDE_DOWN);
				ROTATION_CACHE.put(normal, reverse);
			}

			return reverse;
		}
	}

	Icon getTextIcon() {
		synchronized (iconText.intern()) {
			TextIcon ti = ICON_CACHE.get(iconText);

			if (ti == null) {
				PerformanceMonitor pm = new PerformanceMonitor();
				ti = new HorizontalTextIcon(RotatingJButton.this, iconText);
				ICON_CACHE.put(iconText, ti);
				pm.perfLog("Text Icon '" + iconText + "' created");
			}

			return ti;
		}
	}

	@Override
	public void removeActionListener(ActionListener l) {
		super.removeActionListener(l);
		if (getActionListeners().length == 0) {
			setEnabled(false);
		}
	}

	@Override
	public void repaint() {
		PerformanceMonitor pm = new PerformanceMonitor(50, 100);
		super.repaint();
		pm.perfLog("Repainted: " + this);
	}

	@Override
	public void revalidate() {
		PerformanceMonitor pm = new PerformanceMonitor(50, 100);
		super.revalidate();
		pm.perfLog("Revalidated: " + this);
	}

	@Override
	public void setEnabled(boolean enabled) {
		new ButtonEnabler(enabled).launch(null);
	}

	public void setText(String iconText, boolean flip) {
		if (StringUtils.equals(iconText, this.iconText)) {
			if (this.flip == flip) {
				return;
			}

			if (StringUtils.isBlank(this.iconText)) {
				return;
			}
		}
		this.flip = flip;
		this.iconText = iconText;
		new ButtonDecorator().launch(null);

	}

	void superEnable(boolean enabled) {
		super.setEnabled(enabled);
	}

	@Override
	public void validate() {
		PerformanceMonitor pm = new PerformanceMonitor(50, 100);
		super.validate();
		pm.perfLog("Validated: " + this);
	}
}
