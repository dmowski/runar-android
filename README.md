# Runar android application

## Prerequisites

1. Install Java (https://adoptopenjdk.net/)
2. Install Gradle (https://gradle.org/install/)
3. Install Android Studio (https://developer.android.com/studio/preview)
4. Install DB Browser for SQLite (https://sqlitebrowser.org/dl/)

## Setup

1. Open project in Android Studio
2. Build
3. Create new devise (Tools/Devise Manage/Create device)
4. Run devise (Button play in Devise Manage)ы
5. Run application (Run/Run "app")

## Project file structure (Global view)

| Folder               | File                | Description                                                                                                                                                           |
| -------------------- | ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| /app/src/main/res    |                     | resource catalog                                                                                                                                                      |
| /app/src/main/assets |                     | asset catalog                                                                                                                                                         |
| /app/src/main/       | AndroidManifest.xml | contains the preliminary information required to launch the application, such as the name, version, icons, permissions, registers all activity classes, services, etc |

## Project file structure (Source code: /app/src/main/java/com/tnco/runar/)

| Folder       | File           | Description                                                                                                                                                                                           |
| ------------ | -------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ./analytics  |                | contains classes which support send ivents for google analytic                                                                                                                                        |
| ./data       |                | contains database classes                                                                                                                                                                             |
| ./emuns      |                | contains enums of our project                                                                                                                                                                         |
| ./feature    |                | this package contains features of our project                                                                                                                                                         |
| ./model      |                | contains models of our project                                                                                                                                                                        |
| ./receivers  |                | contains a broadcast receiver, is a component that enables the system to deliver events to the app outside of a regular user flow, allowing the app to respond to system-wide broadcast announcements |
| ./repository |                | contains files that implement the logic of getting data from database and web service, and caching them.                                                                                              |
| ./services   |                | contains components which are needed to perform long-term operations in the background                                                                                                                |
| ./ui         |                | contains classes of UI elements as fragments, activitys and other                                                                                                                                     |
| ./util       |                | consist of supportive methods and classes                                                                                                                                                             |
| ./           | App.kt         | main bootstrap file                                                                                                                                                                                   |
| ./           | RunarLogger.kt | contains logs of project                                                                                                                                                                              |
