### Requirements

* JDK 1.8+
* Maven
* Modern web browser

### Build
run `mvn clean install` at the top level of this project. This will build and install 
all dependent modules for the project.

### Running
This project involves two separate executables: PDF conversion and a web interface.

##### Starting PDF conversion module
```
cd demo-pdf
mvn spring-boot:run
```
The PDF conversion module includes ActiveMQ running in embedded mode.

#### Starting web module
```
cd demo-web
mvn spring-boot:run
```
The web module listens on port 8080. Click [here](http://localhost:8080) to start using the application.