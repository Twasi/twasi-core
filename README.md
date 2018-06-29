# Twasi-Core
Twasi Core is the Core Application of the Twasi Twitchbot. It hosts all the other plugins, manages the connection to the interface (Twitch IRC) and manages data persistence.

[![Build Status](https://travis-ci.org/Twasi/twasi-core.svg?branch=master)](https://travis-ci.org/Twasi/twasi-core)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=net.twasi%3ATwasiCore&metric=alert_status)](https://sonarcloud.io/dashboard?id=net.twasi%3ATwasiCore)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=net.twasi%3ATwasiCore&metric=coverage)](https://sonarcloud.io/dashboard?id=net.twasi%3ATwasiCore)
[![Duplicated lines](https://sonarcloud.io/api/project_badges/measure?project=net.twasi%3ATwasiCore&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=net.twasi%3ATwasiCore)
[![Lines of code](https://sonarcloud.io/api/project_badges/measure?project=net.twasi%3ATwasiCore&metric=ncloc)](https://sonarcloud.io/dashboard?id=net.twasi%3ATwasiCore)

## Contents
Twasi-Core is the main application. It can be started an will then automatically create the initial folder structure and create default config files. After that, you have to adapt those files and install all the plugins you need. After that, you can start it again. It will connect to the configured MongoDB server, load all plugins in the 'plugins' directory and start the webinterface.

These features are beeing implemented or planned into twasi-core. See the feature-plugin list for a complete list of options:
- Reliable, simple and small API for plugins
- Multilanguage (In first place English and German. For other languages is community support needed)
- WebAPI interface where you can control the bot over the network (e.g. an online panel) and register new users without asking them for their Twitch OAuth token.

## Feature Plugins
The following list of feature plugins are planned:

| Plugin Name | Description | Author |
| --- | --- | --- |
| [commands](https://github.com/Twasi/twasi-commands) | Create, edit, delete and execute custom commands. Powerful in combination with twasi-variables | Twasi team |
| [common-variables](https://github.com/Twasi/common-variables) | Nice to have variables | Twasi-team |
| [tokens](https://github.com/Twasi/twasi-tokens) | Award your viewers for their participation in your stream | Twasi-team |
| [songrequest](https://github.com/Twasi/twasi-songrequest) | Let your viewers request their music. This plugin also provides an API for the player | Twasi-team |
| [message-logger](https://github.com/Twasi/twasi-message-logger) | Log messages in the database and access them via WebAPI including pagination. Also collect some neat chat statistics. | Twasi-team |
| [stream-stats](https://github.com/Twasi/twasi-stream-stats) | Collect statistics for your stream and access them via WebAPI. You can generate some pretty awesome charts with this data! | Twasi-team |
| [timed-messages](https://github.com/Twasi/twasi-timed-messages) | Repetitive write messages in the chat | Twasi-team |
| [raffles](https://github.com/Twasi/twasi-raffles) | Giveaways and other tools to give your viewers something back. Integrates and depends on twasi-variables. | Twasi-team |

All these plugins listed here are also available in our hosted version. Have you written your own plugin and want to have it featured here and to easy use it in the hosted version? Let us now! [Mail](mailto://info@twasi.net)

## Compile from source
Twasi-Core is a java maven application. Just issue `mvn clean compile assembly:single` to generate your jar file. Copy it to an own directory (it will create some files) and issue `java -jar TwasiCore-1.0-SNAPSHOT-jar-with-dependencies.jar` in it. It will fail to start (no connection to database), but create a config file (twasi.yml). Edit the config files and adapt all values to your needs. Learn [here](https://github.com/Twasi/twasi-core/blob/master/docs/DATABASE.MD) how to set up and initialize the database. Learn [here](https://github.com/Twasi/twasi-core/blob/master/docs/TWITCH_OAUTH_SETUP.MD) how to set up the authentication with Twitch to use the /auth endpoint.

## Download precompiled version
Later you will find the latest release in the release tab on Github. Sadly, there are no releases during our instable development version. But you can download a snapshot of the latest changes from our artifactory repository [here](https://artifactory.twasi.net/artifactory/list/libs-snapshot-local/net/twasi/TwasiCore/1.0-SNAPSHOT/)
