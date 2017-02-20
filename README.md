SymmetricsDS Configurator
=========================
## Introduction
We needed a simple tool which would allow us to replace values of
environment variables and topological sorted table names of a database in
files and would work as a Java library and as a command line tool.

We use the [Velocity Renderer](https://github.com/m-creations/velocity-renderer) library which use
[Apache Velocity](http://velocity.apache.org/) as template engine.

## How to use
After checkout, build it with maven:
```
mvn clean install
```
You can run it with following command inside the project root folder:
```
$JAVA_HOME/bin/java \
-Dfile.encoding=UTF-8 \
-jar target/symmetricds-configurator-standalone.jar \
-s ./src/test/resources -f .*.vm -d ./target/ -u jdbc:mysql://localhost:13306/mydb -n myuser -p mypass
```
After that you can see the rendred configurations files at the ./target/ folder:
```
cat ./target/env-vers.test
cat ./target/tables.test
```
To see the templates, from which previou files were generated:
```
cat src/test/resources/env-vers.test.vm
cat src/test/resources/tables.test.vm
```
