import javax.swing.*;
import java.awt.*;

public class Element {
	public static int stackHeightPx = 0;
	public String tag;
	public String contents;
	
	public Element(String tag, String contents){
		this.tag = tag;
		this.contents = contents;
	}
	
	public void render(JFrame frame){
		frame.add(switch(tag){
			case "txt" -> {
				JLabel label = new JLabel(contents);
				label.setVerticalAlignment(0);
				label.setAlignmentY(stackHeightPx);
				
				stackHeightPx += 50;
				yield label;
			}
			default -> null;
		});
		SwingUtilities.updateComponentTreeUI(frame);
	}
	
	@Override
	public String toString() {
		return "<" + tag + ">" + contents + "</" + tag + ">";
	}
}