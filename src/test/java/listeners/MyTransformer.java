package listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;


public class MyTransformer implements IAnnotationTransformer {

    // Do not worry about calling this method as testNG calls it behind the scenes before EVERY method (or test).
    // It will disable single tests, not the entire suite like SkipException

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod){

        String enabled = null;
        if (annotation.getGroups()[0].equals("part1")) {
            enabled = System.getProperty("part1");
            System.out.println("part1 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("part2")) {
            enabled = System.getProperty("part2");
            System.out.println("part2 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("part3")) {
            enabled = System.getProperty("part3");
            System.out.println("part3 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("part4")) {
            enabled = System.getProperty("part4");
            System.out.println("part4 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("part5")) {
            enabled = System.getProperty("part5");
            System.out.println("part5 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("part6")) {
            enabled = System.getProperty("part6");
            System.out.println("part6 = " + enabled);
        }
        if (annotation.getGroups()[0].equals("part7")) {
            enabled = System.getProperty("part7");
            System.out.println("part7 = " + enabled);
        }
        if (enabled!=null) {
            annotation.setEnabled(enabled.equals("true"));
        }
    }
}
