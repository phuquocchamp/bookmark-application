## SPRING BOOT APPLICATION - BOOKMARK-APPLICATION 




### BUILD DOCKER IMAGE FROM MAVEN (CLOUD NATIVE BUILDPACKS)
[CLOUD NATIVE BUILDPACKS](https://docs.spring.io/spring-boot/reference/packaging/container-images/cloud-native-buildpacks.html).  

#### Step 1: Config Docker Image Name

**pom.xml**
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                // Config Docker Image name
                <image>
                    <name>phuquocchamp/bookmarker-api</name>
                </image>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```
#### Step 2: Build Docker Image

```bash
./mvnw spring-boot:build-image
```

**Build Image Successfully**
```text
[INFO] Successfully built image 'docker.io/phuquocchamp/bookmarker-api:latest'
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  39.397 s
[INFO] Finished at: 2024-10-24T00:26:53+07:00
[INFO] ------------------------------------------------------------------------
```

#### Step 3: Run Image
```bash
docker run -p 8080:8080 phuquocchamp/bookmaker-api
```