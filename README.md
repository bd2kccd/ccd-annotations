# CCD Annotations

CCD Annotations API

### Building
__Dependencies__    
- CCD DB v0.6.2
- CCD OAuth2 v1.3.1

All commands shown use the included Gradle wrapper executable. If you want to run a command with your system's Gradle installation, replace `./gradlew` with `gradle`. This option requires Gradle version 3.1 or greater.

If you are using Windows, there is batch script included (gradlew.bat). You may also use your system's Gradle installation following the requirements listed above.

__Build__    
To build CCD Annotations: `./gradlew clean build`.    
To update documentation: `./gradlew asciidoctor`.

__Install__    
To install CCD Annotations to your your local maven repository: `./gradlew install`

### Customizing
You may edit the `application.properties` file in `src/main/resources/` to change the server settings and select a database (HSQLDB or MySQL). Additionally, edit the appropriate properties file to change database settings.

### Running
To start the server with documentation, `./gradlew start`. This is a custom command that is identical to the following: `./gradlew build asciidoctor; java -jar build/libs/ccd-annotations-0.8.0.jar`.

## Other
To view a list of the other available tasks with description: `./gradlew tasks`.

