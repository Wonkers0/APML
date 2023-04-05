package main.java.styles;

import java.awt.*;
import java.util.HashMap;

public class StyleRule {
    public String rule;
    public Object value;

    public StyleRule(String rule, String strVal){
        this.rule = rule;
        this.value = parseValueFromString(strVal);
    }

    public Object parseValueFromString(String strVal){
        HashMap<String, String> colors = new HashMap<>(){{
           put("red", "#e02828");
           put("blue", "#3528e0");
           put("green", "#30ab3e");
           put("yellow", "#edd03e");
           put("orange", "#eda73e");
           put("purple", "#c73eed");
           put("black", "#000000");
           put("white", "#ffffff");
           put("gray", "#696969");
        }};

        System.out.println(rule);

        if(colors.containsKey(strVal)) return Color.decode(colors.get(strVal));
        else if(rule.equals("TextAlign")) return StyleInfo.HORIZONTAL_ALIGNMENT.valueOf(strVal.toUpperCase());
        else return strVal;
    }
}
