import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);

        Element[] elements = getElements("C:\\Users\\s324536\\IdeaProjects\\untitled\\src\\browserData.APML");
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
}