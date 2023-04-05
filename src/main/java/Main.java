package main.java;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.styles.Selector;
import main.java.styles.StyleRule;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setLayout(null);

        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Element[] elements = getElements("./src/main/java/files/browserData.APML");
        for(Element element : elements) element.render(frame);
    }
    
    private static String getFileContents(String path){
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            ArrayList<String> lines = new ArrayList<>();

            while (myReader.hasNextLine()) lines.add(myReader.nextLine());
            myReader.close();
            
            return String.join("", lines);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + path);
            return null;
        }
    }
    
    private static Element[] getElements(String filePath) {
        Pattern pattern = Pattern.compile("\\[.+?(?=]).+?(?=\\[)\\[#.+?(?=])]");
        
        String fileContents = getFileContents(filePath);
        if(fileContents == null) return new Element[0];
        
        Matcher matcher = pattern.matcher(fileContents);
        
        ArrayList<Element> foundElements = new ArrayList<>();
        while(matcher.find()) foundElements.add(getElementFromString(matcher.group()));
        return foundElements.toArray(Element[]::new);
    }
    
    private static Element getElementFromString(String str) {
        Pattern pattern = Pattern.compile("(?<=\\[)([^/]*?)(?=])");
        Matcher matcher = pattern.matcher(str);
        
        if(!matcher.find()) throw new IllegalArgumentException("Cannot find tag in string: " + str);
        String tag = matcher.group();
        
        pattern = Pattern.compile("(?<=])(.*?)(?=\\[)");
        matcher = pattern.matcher(str);
        String contents = matcher.find() ? matcher.group() : "";
        
        return new Element(tag, contents);
    }

    public static Selector getSelectorFromString(String str){
        Matcher tempMatcher = Pattern.compile("^.*?(?=\\()").matcher(str);
        if(!tempMatcher.find()) throw new IllegalArgumentException("Cannot parse selector tag from given string");
        String tag = tempMatcher.group();

        tempMatcher = Pattern.compile("(?<=\\{)(.*?)(?=\\})").matcher(str);
        if(!tempMatcher.find()) throw new IllegalArgumentException("Cannot parse selector rules from given string");

        StyleRule[] rules = getStyleRulesFromString(tempMatcher.group());
        return new Selector(tag, rules);
    }

    public static StyleRule[] getStyleRulesFromString(String str){
        ArrayList<StyleRule> result = new ArrayList<>();

        for(String rule : str.split(";")){
            String[] splitRule = rule.split(":");
            result.add(new StyleRule(splitRule[0], splitRule[1]));
        }
        return result.toArray(new StyleRule[0]);
    }

    public static Selector[] readStyleSheet(String sheetName){
        String fileContents = getFileContents("./src/main/java/files/" + sheetName);
        if(fileContents == null) return null;

        Pattern pattern = Pattern.compile(".+?(?=\\()\\(.*?(?=\\))\\)\\{.*?(?=\\})}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fileContents.replaceAll(" |\n", ""));

        ArrayList<Selector> foundSelectors = new ArrayList<>();
        while(matcher.find()) foundSelectors.add(getSelectorFromString(matcher.group()));

        return foundSelectors.toArray(new Selector[0]);
    }
}