SymmetricsDS Configurator
=========================

## How to use
After checkout and install with maven:
```
mvn clean install
```  
You can run it with following command inside the root of checked out folder of project:
```
$JAVA_HOME/bin/java \
-Dfile.encoding=UTF-8 \
-jar ~/.m2/repository/com/mcreations/symmetricds/symmetricds-configurator/1.0-SNAPSHOT/symmetricds-configurator-1.0-SNAPSHOT-jar-with-dependencies.jar \
-s ./src/test/resources -f .*.vm -d /tmp/ -u jdbc:mysql://localhost:13306/mydb -n myuser -p mypass
```
After that you can see the rendred files in /tmp/ folder:
```
cat /tmp/env-vers.test
cat /tmp/tables.test
``` 
These files are the rendered versions of src/test/resources/*.test.vm files which they are in the source of project:
```
cat src/test/resources/env-vers.test.vm
cat src/test/resources/tables.test.vm
```