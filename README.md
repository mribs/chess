# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


SequenceURl (editable)
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkEkSYCkm+hi7jSyhGQyTJTvpc6eXeS7CjAYoSm6MpymW7xKjegUOkmvrOl2G7ulubmCsO-L1IecgoM+8Tnpe15ZYulTLgGa4BsV6XJe2Omlg54oZKoAGYA1IHVLp+lrBRVHoJpXXwsmyCpjAuH4aMPXGpe-VoHRTaeN4fj+F4KDoDEcSJGtG0Ob4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMfWIeg6HwpUDX1E9SHtWZGEWVZNn2Pt9lCftTlqC527uVSt7ZUYKDcMel5FS+2gBbyQXlSFGQzBANDVaj8ivZho3YeNeHDAJoOnjAEAAGYwPYSoAPyNgxS3Mf4KLrv42Dihq-FojAADiSoaIdFmScLl03UzcyPbNz2FFpH6-aWX0vR1-0utZaKizm9l60q4MklDmWw-SM16tASAAF4oBiBUlRbArBfUYUcD0cDxCgIAahRNu23k8ala72spQVgcoOuGITcz6MLq7WP1MLTK6zktMM6ABD43LahxRjCXhwyYtm4lDywv6ecaFrkultXxPl6JOEUw4pf0Yxy0BBwADsbhOCgTgxBGwRwFxABs8AToY+uGEUpNiUlEmllJHSy0qCvwUrWZ5wAckqg3L9pau1CMFGQEhax7wfP2V29HaWTruXorPGJwNPs8m5DGUeYXlvPygV+185gJ33PeEKj5GYViinnAAktIAuidOqdhyh-CsKJWoICwD-VWd9ajvyPDPdB-4sG3y-MgmoLw4HSEPomEaJQwAt0mjAEY1CFrsyYitSwCMbKbE2kgBIYBuF9ggHwgAUhAcUIsKz+GSKANU88GFHSGidJozIZI9DzpvSi29RjYAQMAbhUA4AQBslAK+Sp4G0L+u9E+VtEjPVUgYoxJizEWLmFYsh5kl6PxSgAK0kWgV+ATxSfxQISCGrk6q-0TvUEJaAMTUNAZjIU9Q6binXHnWU8pqG9SqvowxtBQ4UKsvE2eMAMFtRwbYvBMAynEMwYBNWFDdJ52scNSozdybMNYTfDuHMVpeEMfwwRQz5SIGDLAYA2B9GEGDmgGAijzDKOXidM6F0ro3WMI3GpX4crcDwF-LxNiH5WRAAcqAGIomdnNvFf+FyrmILAW7GA5zJlGG0HoAwTygrFyMAUMQ1SK57P+WgMQtcfH+g4ACxu9CxoTTZpgIAA
