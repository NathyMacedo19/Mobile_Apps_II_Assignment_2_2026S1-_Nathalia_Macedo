# Simple App 77742 Nathalia Macedo

An Android app built with **Kotlin** and **Jetpack Compose** for the *Mobile Apps II* module (Assignment 2) at Dorset College Dublin.

The app shows four topics, each in its own tab: state and state hoisting in Compose, Backend-as-a-Service (BaaS) authentication with Firebase, Kotlin precondition functions, and custom exceptions.

## Features

### 1. State and state hoisting
- One button increments two counters at the same time.
- One counter uses `remember` and resets when the screen rotates. The other uses `rememberSaveable` and keeps its value.
- Both counters are declared in the parent composable (`MainAppScreen`) and passed to `StateSection` as parameters, along with an `onIncrement` callback.
- Two more counters are held at class level in `MainViewModel` and passed down in the same way.

### 2. BaaS authorisation (Firebase)
- Register and sign in with email and password.
- Anonymous sign-in.
- Sign-out.
- Firebase errors are caught and shown on screen.

### 3. Preconditions (Exceptions 1)
- `check()` stops an action when a light is not turned on (`IllegalStateException`).
- `require()` rejects a negative number entered by the user (`IllegalArgumentException`).
- Each exception is caught with `try/catch` and shown as a message, so the app does not crash.

### 4. Custom exception (Exceptions 2)
- `WordTooLongException` is thrown when a word is longer than 10 characters.
- The class is declared `open`, so it can be extended. `ExceededWordLimitException` is a subclass that also reports how many characters went over the limit.
- One `catch (e: WordTooLongException)` block handles both classes.

## Tech stack

| | |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | ViewModel with `StateFlow` |
| Backend | Firebase Authentication |
| Build | Gradle (Kotlin DSL) with a version catalogue |
| Min / target SDK | 28 / 37 |
| Tests | JUnit 4 |

## Project structure

```
app/src/main/java/com/example/simpleapp_77742nathalia/
├── MainActivity.kt                  # Entry point, tabs and hoisted state
├── data/baas/
│   └── BaasAuthManager.kt           # Firebase Authentication logic
├── exceptions/
│   └── WordTooLongException.kt      # Custom exception and subclass
└── ui/
    ├── components/                  # One composable per tab
    │   ├── StateSection.kt
    │   ├── BaasSection.kt
    │   ├── PreconditionsSection.kt
    │   └── CustomExceptionSection.kt
    ├── theme/                       # Colours, typography, theme
    └── viewmodel/
        └── MainViewModel.kt         # Class-level state and exception logic

app/src/test/.../AssignmentUnitTest.kt  # Unit tests
```

## Getting started

### Requirements
- Android Studio (a recent stable version)
- An emulator or device with **Google Play services**

### Firebase setup
The `google-services.json` file is **not** included in this repository. To run the app with your own Firebase project:

1. Create a project in the [Firebase console](https://console.firebase.google.com/).
2. Add an Android app with the package name `com.example.simpleapp_77742nathalia`.
3. Download `google-services.json` and place it in the `app/` folder.
4. In **Authentication > Sign-in method**, enable **Email/Password** and **Anonymous**.

### Run
1. Clone the repository and open it in Android Studio.
2. Let Gradle sync.
3. Run the `app` configuration on an emulator or device.

### Tests
Right-click `AssignmentUnitTest` and choose **Run**, or run:

```bash
./gradlew test
```

The tests cover the ViewModel counters, the `check()` and `require()` preconditions, and the custom exceptions. Firebase authentication is tested manually in the app and in the Firebase console.

## Use of AI

This assignment follows the Level 3 AI policy of the module.

- **Gemini agent in Android Studio** was used to write and modify the Firebase code in `BaasAuthManager.kt` and the Firebase entries in the Gradle files. This code is marked with comments in the source.
- **Claude** was used for guidance on the brief and the Firebase setup, for troubleshooting, and for reviewing the project. It did not write code for the app. This README was also drafted with Claude's help.

Full details and screenshots are in the assignment documentation.

## References

- Android Developers. *State and Jetpack Compose*. https://developer.android.com/develop/ui/compose/state
- Android Developers. *Where to hoist state*. https://developer.android.com/develop/ui/compose/state-hoisting
- Firebase. *Authenticate with Firebase using password-based accounts on Android*. https://firebase.google.com/docs/auth/android/password-auth
- JetBrains. *Kotlin exceptions*. https://kotlinlang.org/docs/exceptions.html

## Author

**Nathalia Figueredo Macedo** (77742)
BSc in Computing, Dorset College Dublin
