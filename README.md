# twasi-core
Twasi Core is the Core Application of the Twasi Twitchbot.
[![Build Status](https://travis-ci.org/Twasi/twasi-core.svg?branch=master)](https://travis-ci.org/Twasi/twasi-core)

## Contents
Twasi Core is the main controller. It starts up it's sub connect plugins (e.g. the communicator to twitch or youtube) and loads the feature plugins. Furthermore it manages the configuration and database access.

## How to run
The easiest way is to run it using Docker.

You can also run it as maven java application. Just issue `mvn clean compile assembly:single` to generate your jar file. Just run it with `java -jar target/TwasiCore-1.0-SNAPSHOT-jar-with-dependencies.jar`. It will start up and generate default directories and configurations.


