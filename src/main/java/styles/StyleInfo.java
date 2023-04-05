package main.java.styles;

import main.java.Main;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class StyleInfo implements Cloneable {
    private static final Selector[] defaultStyleSheet = Main.readStyleSheet("defaultStyles.APSS");
    public static Selector[] currentStyleSheet = null;
    public Integer MarginTop, MarginBottom, MarginLeft, MarginRight;
    public Integer PaddingTop, PaddingBottom, PaddingLeft, PaddingRight;
    public Integer Width,Height;
    public Color TextColor;
    public Color BackgroundColor;
    public HORIZONTAL_ALIGNMENT TextAlign;

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

    public StyleInfo(String tag){
        assert defaultStyleSheet != null;
        applyStylesheet(tag, defaultStyleSheet);
    }

    public void applyStylesheet(String tag, Selector[] styleSheet){
        for(Selector selector : styleSheet)
            if(selector.tag.equals(tag) || selector.tag.equals("*")) applySelectorRules(selector);
    }

    private void applySelectorRules(Selector selector){
        for(StyleRule styleRule : selector.rules){
            try {
                Field field = this.getClass().getField(styleRule.rule);
                field.setAccessible(true);

                System.out.println("Applying rule: " + styleRule.rule);
                System.out.println(styleRule.value.toString());

                if(field.getType() == Integer.class && styleRule.value instanceof String)
                    field.set(this, Integer.parseInt((String) styleRule.value));
                else field.set(this, styleRule.value);

                System.out.println("After: " + field.get(this).toString());
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("No such style value: " + styleRule.rule);
            } catch (IllegalAccessException ignored) {}
            // Shouldn't happen because field is set to accessible
        }
    }

//    /**
//     *
//     * @param a The first stylesheet
//     * @param b The 2nd stylesheet, will override a
//     * @return The result of merging the 2 StyleInfos
//     * @author me (whatever that means)
//     */
//    public static StyleInfo mergeStyleInfos(StyleInfo a, StyleInfo b){
//        if(b == null) return a;
//
//        Field[] fields = a.getClass().getDeclaredFields();
//        StyleInfo result = a.clone();
//
//        for (Field field : fields) {
//            field.setAccessible(true);
//            try {
//                if (field.get(a) == null && field.get(b) != null) field.set(result, field.get(b));
//            } catch (IllegalAccessException ignored) {}
//        }
//
//        return a;
//    }

    @Override
    public StyleInfo clone() { // Warning: Shallow copy
        try {
            return (StyleInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
