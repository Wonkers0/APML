package main.java.styles;

public class Selector {
    public String tag;
    public StyleRule[] rules;

    public Selector(String tag, StyleRule[] rules){
        this.tag = tag;
        this.rules = rules;
    }
}
