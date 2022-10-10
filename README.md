# Runar android application

### How to start

#### Prerequisites

1. Install Java (https://adoptopenjdk.net/)
2. Install Gradle (https://gradle.org/install/)
3. Install Android Studio (https://developer.android.com/studio/preview)
4. Install DB Browser for SQLite (https://sqlitebrowser.org/dl/)

#### Setup

1. Open project in Android Studio
2. Build
3. Create new devise (Tools/Devise Manage/Create device)
4. Run devise (Button play in Devise Manage)
5. Run application (Run/Run "app")

#### Project structure
1. manifests(contains the preliminary information required to launch the application, such as the name, version, icons, permissions, registers all activity classes, services, etc)
2. java  
  -analytics(contains classes which support send ivents for google analytic)  
  -data(contains database classes)  
  -emuns(contains enums of our project)  
  -feature(this package contains features of our project)  
  -model(contains models of our project)  
  -receivers(contains a broadcast receiver, is a component that enables the system to deliver events to the app outside of a regular user flow, allowing the app to respond to system-wide broadcast announcements)  
  -repository(contains files that implement the logic of getting data from database and web service, and caching them.)  
  -services(contains components which are needed to perform long-term operations in the background)  
  -ui(contains classes of UI elements as fragments, activitys and other)  
  -util(consist of supportive methods and classes)  
  -App  
  -RunarLogger(contains logs of project)  
3. assets(asset catalog)  
4. res(resource catalog)  
