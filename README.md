# Cryptographic Debugging Agent
An attempt to create Java agent using [ByteBuddy](http://bytebuddy.net/) for debugging the JDK's cryptographic classes.

## Building
Assuming Maven 3 and Java 7 installed properly:

    mvn clean compile assembly:single

## Running
On Windows, to run the application with the agent in place:

    run {text-to-encrypt} [AES-key]

* The requried `text-to-encrypt` must have a length that is a multiple of 16 bytes.
* The optional `AES-key` must be 16-bytes in length.

To run the application without the agent:

    run-noagent {text-to-encrypt} [AES-key]
