# Training Advanced Java

## Archive Aggregator
For this training, the proposed approach is to learn with a concrete use case.
The codename for the project is "Archive Aggregator". The application will be a (very useful) tool to:  
 
 * Upload Zip archives  
 * Aggregate those archives (Unzip all archives, and re-compress them together into one big archive)

We propose to build a simple web application with a REST-API allowing those operations to be made.
For now, no persistence except for in-memory is required.

## Introduction
During this training, we will study typical use cases for some advanced features in Java.

Here is a non-exhaustive list of some of the things we will talk about:

 * How to find the right balance between environment-properties and profiles.  
 * How to better separate your spring / spring-mvc bean declarations, and how to use Spring Boot
 * How to provide exhaustive exception handling
 * How to use Java 8 CompletableFutures to implement multi-threaded paradigms
 * How to use ServletFilters
 * How to insert Aspects with Spring AOP
 * How to use JDK Proxies
 * How to use Reflection and Proxying to implement runtime "plugins"
 * How to use Mongo Driver and Jongo
 
## Environment

### 1. Prerequisites

* [java8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [tomcat8](http://tomcat.apache.org/)
* [An IDE](http://www.jetbrains.com/idea/)

### 2. Build the project

```sh
mvn clean install
```

### 3. Import the project into your IDE

It's a maven project, just import it as such.

### 4. Setup Directories

The Archive Aggregator default setup folder is: /opt/archive-aggregator. Default user is archive-aggregator, default group is archive-aggregator
**Warning: replace the setup folder, user, and group values with your own value if relevant.**

Create main project folders
```sh
#Engine
AA_USER=user        #CHANGE HERE  
AA_GROUP=group      #CHANGE HERE  
AA_SETUP_DIR=/opt/archive-aggregator
sudo mkdir -p $AA_SETUP_DIR/resources  
sudo mkdir -p $AA_SETUP_DIR/tmp
sudo mkdir -p $AA_SETUP_DIR/server  
sudo chown $AA_USER:$AA_GROUP -R $AA_SETUP_DIR  
sudo chmod 755 $AA_SETUP_DIR  
```

Copy a brand new tomcat 8 installation into $AA_SETUP_DIR/server folder.

In $AA_SETUP_DIR/server/conf directory, edit file catalina.properties and set the shared.loader to the following:
**Warning: replace the setup dir with your own value if relevant.**

```sh
AA_SETUP_DIR=/opt/archive-aggregator #CHANGE HERE
shared.loader=$AA_SETUP_DIR/conf 
```

### 5. Environment configuration

Make a copy of **archive-aggregator-webapp.properties** (in training-advanced-java/src/main/resources) to **env-**archive-aggregator-webapp.properties in $AA_SETUP_DIR/conf.  
* Override all "override with env-designmyapp-engine.properties" fields.  
Make a copy of logback.xml (in training-advanced-java/conf) to $AA_SETUP_DIR/conf.

### 6. Testing

A nodejs based bot has been created to facilitate the app testing.  
Install node, then browse to training-advanced-java/bot folder.  

 * Create a directory named **archives**  
 * Insert 12 dummy zip files named fileX.zip (where X is 1 to 12)  
 * Test with the following command: **node client**