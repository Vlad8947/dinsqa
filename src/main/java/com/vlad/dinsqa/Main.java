package com.vlad.dinsqa;

import org.testng.TestNG;

public class Main {

    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{UserTest.class});
        testNG.setOutputDirectory("./reports/testng");
        testNG.run();
    }

}
