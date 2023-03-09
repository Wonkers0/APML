package main.java;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.styles.Selector;

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
        Pattern pattern = Pattern.compile("<.+?(?=>).+?(?=<)<\\/.+?(?=>)>");
        
        String fileContents = getFileContents(filePath);
        if(fileContents == null) return new Element[0];
        
        Matcher matcher = pattern.matcher(fileContents);
        
        ArrayList<Element> foundElements = new ArrayList<>();
        while(matcher.find()) foundElements.add(getElementFromString(matcher.group()));
        return foundElements.toArray(Element[]::new);
    }
    
    private static Element getElementFromString(String str) {
        Pattern pattern = Pattern.compile("(?<=<)([^/]*?)(?=>)");
        Matcher matcher = pattern.matcher(str);
        
        if(!matcher.find()) throw new IllegalArgumentException("Cannot find tag in string: " + str);
        String tag = matcher.group();
        
        pattern = Pattern.compile("(?<=>)(.*?)(?=<)");
        matcher = pattern.matcher(str);
        String contents = matcher.find() ? matcher.group() : "";
        
        return new Element(tag, contents);
    }

    public static Selector getSelectorFromString(String str){
        return null;
    }

    public static Selector[] readStyleSheet(String sheetName){
        String fileContents = getFileContents("./src/main/java/files/" + sheetName);
        if(fileContents == null) return null;

        Pattern pattern = Pattern.compile(".+?(?=\\()\\(.*?(?=\\))\\)\\{.*?(?=\\})}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fileContents.replaceAll(" ", ""));

        ArrayList<Selector> foundSelectors = new ArrayList<>();
        while(matcher.find()) foundSelectors.add(getSelectorFromString(matcher.group()));

        return foundSelectors.toArray(new Selector[0]);
    }
}