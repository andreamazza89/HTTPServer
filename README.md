# HTTP Server

Small HTTP server implementation built as part of my apprenticeship at [8thLight](https://8thlight.com/).

To use this server, include it as a maven dependency to your project like so:

```
...
    <dependencies>
        <dependency>
            <groupId>com.andreamazzarella.http_server</groupId>
            <artifactId>HTTPserver</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>HTTPServer-mvn-repo</id>
            <url>https://raw.github.com/andreamazza89/HTTPServer/mvn-repo/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
...
```

You can now use the HTTPServer by initialising it with a port number and a middleware stack, then call `start` on it.

See [this repo](https://github.com/andreamazza89/CobServer) for an example usage of it.
