# CCD Annotations

CCD Annotations API

### Building
__Dependencies__    
- CCD DB v0.6.0-anno
- CCD OAuth2 v1.2.0

__Build__   
`./gradlew clean build`

__Install__    
`./gradlew install`

CCD Annotations is now installed to your Maven Local repository.

### Running
You may edit the `application.properties` file in `src/main/resources/` to change the server settings and select a database (HSQLDB or MySQL).

`./gradlew bootrun`

CCD Annotations is now running on port 8000.
