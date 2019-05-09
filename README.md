## waiter
Low level (non-http), light and fast µ-server.

### About waiter
Waiter is a server not a framework, so provides some common functionality ready to use.
- It is written based on non-blocking and asynchronous networking principles.
- It has its own rules and protocol, [MessagePack](https://msgpack.org/index.html) is used for serialization.
- You can think of it as a RPC tool, you only call some remote procedure with your input data, and it will return back to you with appropriate output.

#### How to call procedures?
You can call provided procedures with the following structure using [MessagePack](https://msgpack.org/index.html):

```
{
    "proc": "generate_secure_random_number",
    "atts": [
        {"token" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"},
        {"client_ip" : "127.0.0.1"}
    ],
    "iput": {
        "from": 111111,
        "to": 999999
    }
}
```
- proc (Required): Procedure name.
- atts (Optional): An array of key-value pair attributes.
- iput (Optional): Input data that must be sent to procedure.

#### What about response?
The server will respond to your call with the following structure, again using [MessagePack](https://msgpack.org/index.html):

```
{
    "sful": true,
    "atts": [
            {"server_ip" : "127.0.0.1"}
    ],
    "oput": 243459
}
```
- sful: Determines whether the procedure call was successful or not. Possible values are `true` and `false`.
- atts (Optional): An array of key-value pair attributes.
- errs (Optional): An array of errors occurred when procedure call was not successful.
    - Array elements are an object with two fields, `code` and `message`.
- oput (Optional): Output data (if any) that received from procedure call.

### Prerequisites to develop and test this project
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
