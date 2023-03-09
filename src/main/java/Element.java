package main.java;

import main.java.styles.StyleInfo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Element {
	public static int stackHeightPx;
	public String tag;
	public String contents;
	public StyleInfo styleInfo;

	public final Insets ZERO_INSETS = new Insets(0,0,0,0);
	
	public Element(String tag, String contents){
		this.tag = tag;
		this.contents = contents;

		HashMap<String, StyleInfo> defaultStyleInfo = new HashMap<>(){{
			put("txt", new StyleInfo(ZERO_INSETS, new Insets(5, 0, 5, 0), Color.BLACK, StyleInfo.HORIZONTAL_ALIGNMENT.LEFT));
		}};

		styleInfo = defaultStyleInfo.getOrDefault(tag, null);
	}
	
	public void render(JFrame frame){
		Component component = switch(tag){
			case "txt" -> {
				JLabel label = new JLabel(contents);

				Dimension size = label.getPreferredSize();
				label.setBounds(StyleInfo.getHorizontalAlignment(frame, label, styleInfo.textAlignment) + styleInfo.margin.left - styleInfo.margin.right,
						stackHeightPx + styleInfo.margin.top,
						size.width + styleInfo.padding.left + styleInfo.padding.right,
						size.height + styleInfo.padding.bottom + styleInfo.padding.top);

				label.setForeground(styleInfo.color);
				yield label;
			}
			case "style" -> {
				Main.readStyleSheet(contents);
				yield null;
			}
			case "title" -> {
				frame.setTitle(contents);
				yield null;
			}
			default -> null;
		};

		if(component != null){
			frame.add(component);
			updateStackHeight(component);
			SwingUtilities.updateComponentTreeUI(frame);
		}
	}

	private void updateStackHeight(Component c){
		stackHeightPx += c.getHeight() + styleInfo.margin.bottom;
	}
	
	@Override
	public String toString() {
		return "<" + tag + ">" + contents + "</" + tag + ">";
	}
}