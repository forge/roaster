Roaster - The only Java source parser library you'll ever need
===============================================================

image:https://img.shields.io/github/workflow/status/forge/roaster/GitHub%20CI?logo=GitHub&style=for-the-badge["Actions Status", link="https://github.com/forge/roaster/actions"]
image:http://img.shields.io/:license-EPL-blue.svg?style=for-the-badge["License", link="https://www.eclipse.org/legal/epl-v10.html"]
image:https://img.shields.io/github/tag/forge/roaster?logo=apache-maven&label=Maven%20Central&style=for-the-badge["Maven Central", link="https://search.maven.org/artifact/org.jboss.forge.roaster/roaster-jdt"]
image:https://img.shields.io/badge/JVM-11--17-brightgreen.svg?style=for-the-badge&logo=Java?style=for-the-badge["Supported JVM Versions"]

Roaster (formerly known as java-parser) is a library that allows easy parsing and formatting of java source files. 
Roaster introduces a fluent interface to manipulate Java source files, like adding fields, methods, annotations and so on.

Installation
============

* If you are using Maven, add the following dependencies to your project: 

```xml
<properties>
  <version.roaster>2.26.0.Final</version.roaster>
</properties>

<dependency>
  <groupId>org.jboss.forge.roaster</groupId>
  <artifactId>roaster-api</artifactId>
  <version>${version.roaster}</version>
</dependency>
<dependency>
  <groupId>org.jboss.forge.roaster</groupId>
  <artifactId>roaster-jdt</artifactId>
  <version>${version.roaster}</version>
  <scope>runtime</scope>
</dependency>
```

* Otherwise, download and extract (or build from sources) the most recent http://search.maven.org/#search|ga|1|a%3A%22roaster-distribution%22[distribution] containing the Roaster distribution and command line tools

Usage
=====

CLI
---
Execute roaster by running the following script (add these to your $PATH for convenience):

[source]
----
bin/roaster     (Unix/Linux/OSX)
bin/roaster.bat (Windows)
----

Options described here:

[source,cmd]
----
$ roaster -h

Usage: roaster [OPTION]... FILES ... 
The fastest way to build applications, share your software, and enjoy doing it. 

-c, --config [CONFIG_FILE]
	 specify the path to the Eclipse code format profile (usually found at '$PROJECT/.settings/org.eclipse.jdt.core.prefs') 

-r, --recursive
	 format files in found sub-directories recursively 

FILES... 
	 specify one or more space-separated files or directories to format 

-h, --help
	 display this help and exit 
----

Java Parser API
---------------

Example:
```java
Roaster.parse(JavaClassSource.class, "public class HelloWorld {}");
```

Java Source Code Generation API
-------------------------------

Roaster provides a fluent API to generate java classes. Here an example:

```java
final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
javaClass.setPackage("com.company.example").setName("Person");

javaClass.addInterface(Serializable.class);
javaClass.addField()
  .setName("serialVersionUID")
  .setType("long")
  .setLiteralInitializer("1L")
  .setPrivate()
  .setStatic(true)
  .setFinal(true);

javaClass.addProperty(Integer.class, "id").setMutable(false);
javaClass.addProperty(String.class, "firstName");
javaClass.addProperty("String", "lastName");

javaClass.addMethod()
  .setConstructor(true)
  .setPublic()
  .setBody("this.id = id;")
  .addParameter(Integer.class, "id");
```

Will produce:

```java
package com.company.example;

import java.io.Serializable;

public class Person implements Serializable {

   private static final long serialVersionUID = 1L;
   private final Integer id;
   private String firstName;
   private String lastName;

   public Integer getId() {
      return id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public Person(Integer id) {
      this.id = id;
   }
}
```

Java Source Code Modification API
---------------------------------

Of course it is possible to mix both approaches (parser and writer) to modify Java code programmatically:

```java
JavaClassSource javaClass = 
  Roaster.parse(JavaClassSource.class, "public class SomeClass {}");
javaClass.addMethod()
  .setPublic()
  .setStatic(true)
  .setName("main")
  .setReturnTypeVoid()
  .setBody("System.out.println(\"Hello World\");")
  .addParameter("java.lang.String[]", "args");
System.out.println(javaClass);
```

JavaDoc creation and parsing
----------------------------
 
Here is an example on how to add JavaDoc to a class:

```java
JavaClassSource javaClass = 
  Roaster.parse(JavaClassSource.class, "public class SomeClass {}");
JavaDocSource javaDoc = javaClass.getJavaDoc();

javaDoc.setFullText("Full class documentation");
// or 
javaDoc.setText("Class documentation text");
javaDoc.addTagValue("@author","George Gastaldi");

System.out.println(javaClass);
```

Formatting the Java Source Code
-------------------------------

Roaster formats the Java Source Code by calling the `format()` method:

```java
String javaCode = "public class MyClass{ private String field;}";
String formattedCode = Roaster.format(javaCode);
System.out.println(formattedCode);
```

Parsing the java unit 
----------------------

The link:http://docs.oracle.com/javase/specs/jls/se7/html/jls-7.html#jls-7.3[Java Language Specification] allows you to define multiple classes in the same .java file. Roaster supports parsing the entire unit by calling the parseUnit() method:

```java
String javaCode = "public class MyClass{ private String field;} public class AnotherClass {}";

JavaUnit unit = Roaster.parseUnit(javaCode);

JavaClassSource myClass = unit.getGoverningType();
JavaClassSource anotherClass = (JavaClassSource) unit.getTopLevelTypes().get(1);
```


Validate Code Snippets
----------------------

Roaster validates Java snippets and reports as Problem objects by calling the `validateSnippet()` method: 

Example:
```java
List<Problem> problem = Roaster.validateSnippet("public class HelloWorld {}");
// problem.size() == 0

List<Problem> problem = Roaster.validateSnippet("public class MyClass {");
// problem.size() == 1 containing a new Problem("Syntax error, insert \"}\" to complete ClassBody", 21, 21, 1)

```

Building from sources
=====================

Just run `mvn clean install` to build the sources


Issue tracker
=============

File an issue in our https://github.com/forge/roaster/issues[GitHub Issue Tracker]


Get in touch
============

Roaster uses the same forum and mailing lists as the http://forge.jboss.org/[JBoss Forge] project. See the http://forge.jboss.org/community[JBoss Forge Community] page.

* https://developer.jboss.org/en/forge[User forums]
* https://developer.jboss.org/en/forge/dev[Developer forums]


Related / Similar projects
==========================

For the writer part:

* https://github.com/square/javapoet[square/javapoet]


License
=======
http://www.eclipse.org/legal/epl-v10.html[Eclipse Public License - v 1.0]
