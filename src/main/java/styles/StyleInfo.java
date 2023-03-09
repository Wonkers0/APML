package main.java.styles;

import javax.swing.*;
import java.awt.*;
public class StyleInfo {
    public Insets margin;
    public Insets padding;
    public Color color;
    public HORIZONTAL_ALIGNMENT textAlignment;

    public enum HORIZONTAL_ALIGNMENT{
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VERTICAL_ALIGNMENT{
        BOTTOM,
        CENTER,
        TOP
    }

    public static int getHorizontalAlignment(JFrame frame, Component component, HORIZONTAL_ALIGNMENT horizontalAlignment){
        return switch(horizontalAlignment) {
          case LEFT -> 0;
          case CENTER -> frame.getWidth() / 2 - component.getWidth() / 2;
          case RIGHT -> frame.getWidth() - component.getWidth();
        };
    }

    public StyleInfo(Insets margin, Insets padding, Color color, HORIZONTAL_ALIGNMENT textAlignment){
        this.margin = margin;
        this.padding = padding;
        this.color = color;
        this.textAlignment = textAlignment;
    }
}
