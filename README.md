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
  -analytics(contains app measurement solution, available at no charge, that provides insight on app usage and user engagement)  
  -data(contains database classes)  
  -emuns(contains enums of our project)  
  -feature(this package contains features of our project)  
  -model(contains entities of database)  
  -receivers(contains components that responds to system-wide broadcast announcements)  
  -repository(contains classes which isolate the data and other parts of the application)  
  -services(contains components which are needed to perform long-term operations in the background)  
  -ui(contains classes of UI elements as fragments, activitys and other)  
  -util(consist of supportive methods and classes)  
  -App  
  -RunarLogger(contains logs of project)  
3. assets(asset catalog)  
4. res(resource catalog)  
