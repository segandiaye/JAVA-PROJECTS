# SIMPLE HOMEWORK

Simple homework.

## Prerequisites

- Have a `JDK/OPENJDK` installed which supports recent versions of JAVA especially lambda expressions, here I used the version` openjdk 11.0.11`
- Have the `junit.jar` and` hamcrest-core-1.jar` JAR files in your CLASSPATH if you use an `IDE` otherwise if you test in command line like me, be sure to have it in` TESTS_TECHNIQUES/lib`.

**NB:** In this project the tests are done on command line, if you are using an eclipse IDE example have the classes in the `src` folder of your project and the JARs files in the CLASSPATH of your project.

## ARCHITECTURE DU PROJET

```sh
TESTS_TECHNIQUES
├── classes
│   ├── JUnitTests.class
│   ├── MyList.class
│   ├── TestUnitRunner.class
│   └── TestWithMain.class
├── java
│   ├── JUnitTests.java
│   ├── MyList.java
│   ├── TestUnitRunner.java
│   └── TestWithMain.java
├── lib
│   ├── hamcrest-core-1.3.jar
│   └── junit-4.12.jar
└── README.MD
```

- classes/: contains the compiled classes (`.class`).
- java/: contains the Java classes (`.java`).
- lib/: contains JARs files.

## TESTS WITH THE MAIN CLASS

```sh
cd TESTS_TECHNIQUES/# to go to the base directory
javac -d classes -sourcepath java/-cp lib/junit-4.12.jar: java/MyList.java java/TestWithMain.java java/JUnitTests.java java/TestUnitRunner.java # To compile all the necessary
java -cp classes TestWithMain # To run
```

If everything is OK you should have the following on your SHELL:

```Java
[[1, 2], [3, 4], [5]]
[[1, 2, 3], [4, 5]]
[[1], [2], [3], [4], [5]]
```

**NB:** If you are curious you can edit the TestWithMain class to see its content.

## TEST WITH Junit

Make sure you have the Junit and hamcrest-core-1 JARs in `TESTS_TECHNIQUES/lib`.

```sh
cd TESTS_TECHNIQUES/# to go to the base directory
javac -d classes -sourcepath java/-cp lib/junit-4.12.jar: java/MyList.java java/TestWithMain.java java/JUnitTests.java java/TestUnitRunner.java # To compile all the necessary
java -cp classes: lib/junit-4.12.jar: lib/hamcrest-core-1.3.jar TestUnitRunner # to run
```

If everything is OK you should have the messages below on your SHELL console otherwise you will also have the details on what went wrong:

```sh
Test run: 1, Fail: 0
Is everything OK? : true
```
