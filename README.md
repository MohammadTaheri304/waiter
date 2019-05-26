## Waiter
Simple, light and fast µ-server.

### About
Waiter is a server not a framework, so provides some common services ready to use.
- It is written based on asynchronous and non-blocking networking principles.
- It has its own rules and protocol, [MessagePack](https://msgpack.org/index.html) is used for serialization.
- Like an RPC tool; Call some remote procedure with your input data, and it will return back to you with appropriate output.

### Prerequisites to develop and test
- JDK 11 (JavaSE 11)
- Gradle 5.3+

### How to build?
Check prerequisites, then run:
```
./gradlew clean installDist
```

### How to test?
```
./gradlew clean test
```

### Javadoc
To generate javadoc, run:
```
./gradlew javadoc
```

### Contributing
To contribute on this project please see [CONTRIBUTING](CONTRIBUTING.md).

### Contributors
List of Contributors on [CONTRIBUTORS](CONTRIBUTORS.md).
