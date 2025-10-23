.# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

(https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQB6bpKwDyARgM4pQBuFmADsFGEiEvRjAMoXVQ7b2NmrYOwASIgCYIK2AArBSpCADNlfSDGASJMAK7lYCCAHMkaADQwjxiLrCXkpMKU0IEMY8AC2KF2ZgSwGDAxAoofADuKMSafmgwxChgUegwytAwIMjoYJpoOgY8fNjk1PFQKACOukgVOsq6aOBIEGikxVExwLRILgAWwNQwZrR2LpIwtmCjznwVpk4UmiAgtuykuisoKDoZysBICHwSSBLs9IrD8SheB+5aFZe3AJ6JfGgQMLdgYEsRA7kANZmXbKGAUKAZJC5CpgXRQNqaAK+EBQVhMIY+RTAYzhXDWfwNJpMVqWfQUNDeFB5HQXUgRaC0hTkHT+KG5faHcb5GCw+GIiYQjJY0g4lAAOhgfGAdj6MEggPQ4wqvKS-J28oBmRECWpdI1oRAgKumupDThFT4ACsIP5PD4YPq4nlNLL5RBFfEJv8go6EMBnktVkZYBNCktTnwkF5aNBguwUKA5VlmaraI8ci5WtSom5kcozBrVk0ULRchMJBBfMMsLhBradKsJNSJLpaMgQEEWmg+OhO7QNv7ftgzOkoN9uzA+qc8XgYJ1ND00o1mq1huN59FwflY2ZcnQGEwWGxcuJ8tJeAfBMeRLkuFRmOEBEfhOwACJBYAAQS2ikwgWCUJyACYhMEwHIoGeUh6BYNBjBgAAGcUAE4wOMSE2wAYgAFgAZgADgAJmQkAYEw5BjD6MBiAQXRqQAJRQBYwCgLtWkwRBUHYABaAA+ThuAoAAuGAAG1ZEIDgABUAF0YGwckoAAHTQABvAAiRTKR8dShPUmB1MsdS6QZKAJF0-TDIMm47gsgyAF9MHvHgYD4mAzykYSYA0rSqTsqzjOZUzzL0gyjJsw5-McjyL1c-jnMfET5l6X4oAACkY5iKEY6pfDAABKJzuEfOKYA-YIfxAXxSBE3EwAAVQMNLfJ8Qryu-X8XDciRiCEgAxEEYEaih2oSZ49AMbTwmICpgGNYbYAiaE5Ra6lgAQWaJHGlAAA8UvadrKuq1y3ISqqRIW9qiofKqTviwSoBEr8NsTLapLm9AAFEdqqstu2uyhHwAHm4w7OqE0cIABpZuLczichE7CENwlSNJFMVdIMz6oEhR6JopKlXE28bgk9dTHPQCRMBmxNjQAIS0VVcqcTAwaqy5Yfum6UBEoVeDO6lOYEqgvIZiQcropxvt+kke2cmG4eyDBEYQhDUfU9HcUx9TsdxkSxYlvLyfA-J1D6Chq2AKwIFyFQ0jueFqSmGYs3iMBzY8ZbdBid3fTQbYdA0RJllWRpfnqaBqdm+a0EBD4IjQPqHctNnjqFgXeZxyOBbu4WeAu2P47QaXS1l6HYCF+HlbEgBWVW5NUgzNZQbXdegES0ubUhUXRSc7b5-L9Ip03U4507ivOmA0F0NwAZKtzR5q1FE1+BbmoMdq2s-I6Of4nqhK-bQho3z859uhft-B5eghQL9ZTSmV3c31nL-Zrq996w+dDvp-T5zoWYpeSlIAiu48RZ4x8pNPyMARKWSMo-PoUkPToH0rAhy5dc5VzACJQiqt1arVQQZMKBkEFILJjAohxtKZoQwrQTC0ARC4jIn1OAn03xwC-DAAAMiYMwHEla5DAfnMSElpJyRKNVbsqNVqWBMoyRy5c+IgKEo3TSUCdJ6QCnIsyFkh6YBAXxDOBI0BpR4aYNAhsnCFQFnxReQk6prxkY6IKjIt4VU6nxfeA0eSXU-GNfGCIqSyJcWZMCNM5owAAOqQjgsfAmDoMjyEUMFM+KAQag1ftVIS09Z7yygOkjOABJJo0AKjgDMWYEuf12JYJBnkoSdcELeQ1tVDGek2542KasHGKBwABKmpMWA2jzLDyplHWmMAxZM0llgReucM7gizvzCegshGiy0JYsAVSy55MwQIlWas1ItOxFrWBHT9YbKqDMqhptcDuwthuIwtswScloiqZ2YxJhuw9qYd23tNR+wDu6XUIc1jhzSJHcJMc44QATknQ4jsX7uLfvMlZmdcapNReAguMKE7bP+rsyu+za7128k3VppysZLI7l3Hu1T4j9yWYPGAoywIC3SXY3xwRUm2MyYoISxjHEn2CG4jqb9PGfyPj-PoV0QEFLRRUixvgZ5YHLuk5RkD4kt1CoZdSpDkFoCivwriYB5XYtwYczVgSNFwL1bKMhKCdWsswOhWwdD0LbHiORJAlEwAetSGYqYxqcilXqWJN8n0uGfSkp9cR5BFBSMpPag1ijeLKP1Z6fRkgLyGLRdYKYD9k2emsSs3lyKsn5rsIW92Dq0CFShVcSg61TiuhrSm9lsM7E5KOHks1gNzrFKbcgCQ0qcjMDYsXH6pd-q1O4vU0SSMACMclUbkpOdqqles9BJvdtAJAAAvHYejqENvqoXWFidk7hDmenNFiyMX-zWXjM9uLJ0ywJQ9PZJqRKiUaQ3NdopKU62pTATuKI0T0smGCAex6R58tIBKoSb4UDSF+K2xBBq0iQi8P0qkqSOXweyTPI4crYYZ27Wq2GyiKOzrnQ9ISFqVIUeoS62hmE6hkR6LHbhKUYAAHEqTtCwaG+jYk+PRvEfaFAKkM3oFTemotcmDG8SMSlATWJq0YeLTy3idi6rSs07W0VO8EO8S8YNLlVtiDjVkz2cZESzBDpbbZ-DnbCMUd7WRtFg7m0jtlGOzsst8U1IEX24SABIBdCFl1kuOYBjdwGt2NAQXuw95kWUmzGae89cKr1IrFWnIRiV73ZxWVi4RL6i7Bblp+ol36SUIX-XFtpm726gdpRB2WUGStQGZc6jtGTy38tWjpuxjgwDqd8IZg1xmPFmd6t4nQqipOkExqJDSUnClvkxouwiuFsJGX+NCFAC0pq6KMjRUAgJTt+U0UZKTAA5Kkmj7IwBkq96z6Ha2ucGwV-ly3BNrY21SLbO29sHYMkd1e6jtXqQCpdo0N2bUBUe89uHr33tZvPBQPtiUAdYiB+pTb22RK7f24d6c0OtXnYMgj67MOadE6pE9nwL23uOU89xDVBkVuE+J2D8nkPKcnYZ3d2n-pEei7h-d5naPybs+DRgMLj1RIWpXWpHngOf3A58KD0n4OKfHaR7D+HEv6fU7F0znwLOebo4Vyx1jbrMISAAOzIQQigJpmFPq4TgKwgAbPAWaaHJtSmE0+n9ojZLySkzJxTaBUao58Ao+T2bhK2csKpJPKBopp6gLm7mQlr6-Em2lbP5hbMlu5mWv7NUHFNUr-lkziHFtxKgKNT7I2G2Od8199tKyCNDZqh5h6uOB1oCc35+5GBx1BanfSxXpqvPYqizF1RzdW4geS7KVLR6MsnujpMxmFRmazPg+V4rfNMW3uxUf8WVy8rVYwXVhGMAkaWua0B85d-Nk3Ky4fyrC9eFN5a9c-G-ftHmHra-CPIaHLN9addiQlRWerX9UldfClBLb-MDbuTrPuaDJlWDMZAbOxEbGxXTQjYvFAUvSbZ+ReRDL+fjKkWVPPMfHmbPSjLnPPFRK3FAG3Y2WjedNXGAVdHgvXGAMnbCffU2R3LCQMNwWFMiCiKiOQowCIGAAAKVtHiFD0wjp0XxE1v3Enqmj2wFjx3S03QFRnbADAoDgAgBDEsGJw5weiUS4Izw0msMDCgDsJDF1X51z2x3zxUzRRtDMFL3cM8NsPsOgHMCcKrwgJrxM3sSSDXkbzIL0ySFL2J1m3FXmyEgLB8WFSsxs3j3s2NB72HT70zQ7S7WI0oyKQn181HRn0C27Cf1nXqSRhRiOQ33aRAx80qOaKPAnT-zAjoLyIKJ0FD0+2JzKJgAGJbVmJqPczqM8waMn0m3aNCzo2xS6PVl6MSzawWKmJB221ZQbSmRPxmSb06gv0niv0fS5mEQNgfylnnx2Vq2QNf3f32IwM3y3ReNP1GLmMANywRRTjAKK3uKWWgKeK8lBPgIXyQP4iwR-T-Vi16NazxmwLpS60ZVxj60yzZQHzcyHyEkUiumIMIyk2YMCNYKEhoL-lHyoy4MZO5QEPo0YzUmdVdSwnQgDEUJ9Son5PGkQETFDFoHbFn0nH0JgNEgjSjRjXER6lT0CKEmbFQ1vjcDSkKmUwzg1KSC1IQB1NG0IwNN+GemNJyOqkQ3NKNPXgoFIAr3jydJW3iOBlJNryI1nlIx2IgO9J7WZM4LVJo22PqS5NUkcgbQ6R+zsQeKDIzgRK2JNWVyElQMawxL+LORAxxNwLXHxOgEJOoWoSAA)

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
