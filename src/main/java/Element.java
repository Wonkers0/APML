package main.java;

import main.java.styles.StyleInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import static main.java.styles.StyleInfo.currentStyleSheet;

public class Element {
	public static int stackHeightPx;
	public String tag;
	public String contents;
	public StyleInfo styleInfo;

	
	public Element(String tag, String contents){
		this.tag = tag;
		this.contents = contents;

		styleInfo = new StyleInfo(tag);
	}
	
	public void render(JFrame frame){
		if(currentStyleSheet != null) styleInfo.applyStylesheet(tag, currentStyleSheet);

		Component component = switch(tag){
			case "txt" -> {
				JLabel label = new JLabel(contents);

				Dimension size = label.getPreferredSize();
				label.setBounds(StyleInfo.getHorizontalAlignment(frame, label, styleInfo.TextAlign) + styleInfo.MarginLeft,
						stackHeightPx + styleInfo.MarginTop,
						size.width + styleInfo.PaddingLeft + styleInfo.PaddingRight,
						size.height + styleInfo.PaddingBottom + styleInfo.PaddingTop);
				System.out.println(styleInfo.MarginTop);

				label.setForeground(styleInfo.TextColor);
				label.setOpaque(true);
				yield label;
			}
			case "img" -> {
				Image image;
				try {
					URL url = new URL(contents);
					image = ImageIO.read(url);
				}
				catch (IOException e) {
					throw new RuntimeException("Image URL does not point to an image");
				}

				int width = styleInfo.Width == null ? image.getWidth(null) : styleInfo.Width;
				int height = styleInfo.Height == null ? image.getHeight(null) : styleInfo.Height;

				BufferedImage bufferedImage = resizeImage(toBufferedImage(image), width, height);
				JLabel lblImage = new JLabel(new ImageIcon(bufferedImage));
				lblImage.setBounds(styleInfo.MarginLeft, stackHeightPx + styleInfo.MarginTop, bufferedImage.getWidth(), bufferedImage.getHeight());
				yield lblImage;
			}
			case "style" -> {
				currentStyleSheet = Main.readStyleSheet(contents);
				yield null;
			}
			case "title" -> {
				frame.setTitle(contents);
				yield null;
			}
			default -> null;
		};

		if(component != null){
			if(styleInfo.BackgroundColor != null) component.setBackground(styleInfo.BackgroundColor);

			frame.add(component);
			updateStackHeight(component);
			SwingUtilities.updateComponentTreeUI(frame);
		}
	}


	// https://www.baeldung.com/java-resize-image
	BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();
		return resizedImage;
	}


	// https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
	public static BufferedImage toBufferedImage(Image img)
	{
		if (img instanceof BufferedImage)
		{
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bufferedImage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bufferedImage;
	}

	private void updateStackHeight(Component c){
		stackHeightPx += c.getHeight() + styleInfo.MarginBottom + styleInfo.MarginTop;
	}
	
	@Override
	public String toString() {
		return "[" + tag + "]" + contents + "[#" + tag + "]";
	}
}