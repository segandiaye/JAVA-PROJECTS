# SIMPLE DEVOIR DE MAISON

Simple devoir de maison.

## Prérequis

- Avoir un `JDK/OPENJDK` intallé qui supporte les versions récentes de JAVA surtout les lambda expression, ici j'ai utilisé la version `openjdk 11.0.11`
- Avoir les fichiers JARs `junit.jar` et `hamcrest-core-1.jar` dans votre CLASSPATH si vous utilisez un `IDE` sinon si vous tester en ligne commande comme moi rassurez-vous de l'avoir dans `TESTS_TECHNIQUES/lib`.

**NB :** Dans ce projet les tests sont faits en ligne de commande, si vous utilisez un IDE exemple eclipse ayez les classes dans le dossier `src` de votre projet et les fichiers JARs dans `TESTS_TECHNIQUES/lib`.

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

- classes/: contient les classes compilées(`.class`).
- java/: contient les classes Java(`.java`).
- lib/: contient les fichiers JARs.

## TESTS AVEC LA CLASSE MAIN

```sh
cd TESTS_TECHNIQUES/ # pour se positionner au repertoire de base
javac -d classes -sourcepath java/ -cp lib/junit-4.12.jar: java/MyList.java java/TestWithMain.java java/JUnitTests.java java/TestUnitRunner.java # Pour compiler tout le necessaire
java -cp classes TestWithMain # Pour exécuter
```

Si tout est OK vous devriez avoir comme suite sur votre SHELL :

```Java
[[1, 2], [3, 4], [5]]
[[1, 2, 3], [4, 5]]
[[1], [2], [3], [4], [5]]
```

**NB :** Si vous êtes curieux vous pouvez éditer la classe TestWithMain pour voir son contenu.

## TESTER AVEC Junit

Assurez-vous d'avoir les fichiers JARs Junit et hamcrest-core-1 dans `TESTS_TECHNIQUES/lib`.

```sh
cd TESTS_TECHNIQUES/ # pour se positionner au repertoire de base
javac -d classes -sourcepath java/ -cp lib/junit-4.12.jar: java/MyList.java java/TestWithMain.java java/JUnitTests.java java/TestUnitRunner.java # Pour compiler tout le necessaire
java -cp classes:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar TestUnitRunner # pour exécuter
```

Si tout va bien vous devriez avoir sur votre console SHELL les messages ci-dessous sinon vous aurez aussi les détails sur ce qui n'a pas passer:

```sh
Test exécuté: 1, Échoué: 0
Est-ce que tout est OK? : true
```
