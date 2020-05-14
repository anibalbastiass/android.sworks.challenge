# Randuser App: Random User app for meet random user and contact them!

[![CircleCI](https://circleci.com/gh/anibalbastiass/android.sworks.challenge.svg?style=shield)](https://circleci.com/gh/anibalbastiass/android.sworks.challenge)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.3.72-blue.svg)](https://kotlinlang.org)
[![AGP](https://img.shields.io/badge/AndroidStudio-3.6.3-blue?style=flat)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-5.6.4-blue?style=flat)](https://gradle.org)

> Challenge app for Android Engineer technical test, very useful and funny app for apply Android Architecture components.

This project is a technical test for apply Android Enginner job. 
Goal: 

- Consume Random User API for show a list of user with endless pagination
- Save and delete favorite users showing into a horizontal scroll view
- Typing in a search bar for find suggested users and go forward to user details.
- Show a user detail with photo a main information
- Save and delete favorite in detail photo
- Save contact user into Smartphone contacts

## Steps

- Frontend (Android):
  - From a personal project with a solid, scalable and clean architecture for Android apps (CLEAN + MVVM), begins complete features for this project.
  - This project contains 2 flavors: Dummy and Prod. Dummy reads a JSON file for local interaction and Prod request to API.

This project contains the following milestones
 
- Build a scalable and stable architecture for Android App: MVVM + CLEAN Architecture
- SOLID principles + pattern designs
- Using Koin for Dependency Injection
- Apply Android Architecture Components: Navigation, View Model, Live Data, Databinding, Room
- Use Android X dependencies
- Using modularization for decouple dependencies
- Keep clean code and use minimal dependencies

- Navigation:
This app contains a single activity with a Navigation Controller with some Fragments: `UserFragment` and `UsersDetailFragment`

- ViewModel:
This component is useful for inject by Koin and implement use cases for connect domain data

- Databinding and Observables:
These are the ones in charge of refreshing data for UI throw Observable vars.

- Unit Testing
Implement test for ViewModel throw captors, mocks for check stats for LiveData too.

- Continuous Integration
With Github Actions feature (Top tab), this repository is processing throw CI.

## Screenshots
 
![User List](https://raw.githubusercontent.com/anibalbastiass/android.sworks.challenge/feature/testing-and-documentation/screenshots/Screenshot_1589130104.png)
![User List with favorites](https://raw.githubusercontent.com/anibalbastiass/android.sworks.challenge/feature/testing-and-documentation/screenshots/Screenshot_1589130116.png)
![User List with suggestions](https://raw.githubusercontent.com/anibalbastiass/android.sworks.challenge/feature/testing-and-documentation/screenshots/Screenshot_1589130125.png)
![User Detail](https://raw.githubusercontent.com/anibalbastiass/android.sworks.challenge/feature/testing-and-documentation/screenshots/Screenshot_1589130133.png)
![Add Contact](https://raw.githubusercontent.com/anibalbastiass/android.sworks.challenge/feature/testing-and-documentation/screenshots/Screenshot_1589130152.png)


