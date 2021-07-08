

<h1 align="center">LoKi</h1></br>
<p align="center"> 
🔒 Add a passcode lock feature to your android applications hassle-free.
</br>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=26"><img alt="API" src="https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/joelromanpr/LoKi/actions"><img alt="Build Status" src="https://github.com/joelromanpr/LoKi/workflows/Android%20CI/badge.svg"/></a> 


## Download
Go to the [Releases](https://github.com/joelromanpr/LoKi/releases) to download the demo APK.

## Screenshots
<p align="center">
<img src="/showcase/demo_1.gif" width="32%"/>
<img src="/showcase/demo_2.gif" width="32%"/>
<img src="/showcase/demo_3.gif" width="32%"/>
</p>

## Including in your project
[![Maven Central](https://img.shields.io/maven-central/v/io.github.joelromanpr/loki.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.joelromanpr/loki)

### Gradle 
Add below codes to your **root** `build.gradle` file (not your module build.gradle file).
```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```
And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
    implementation "io.github.joelromanpr:loki:1.0.0"
}
```

## Usage

### LoKi
Here is a basic example of implementing a `LoKi` workflow. <br>
Any activity that you wish to protect with a passcode must inherit a ```LokiActivity```

```kotlin
class MainActivity : LokiActivity() {}
```
To prompt the user to setup their passcode for the first time call the ```launch``` function within ```PasscodeSetupActivity```  
```kotlin
PasscodeSetupActivity.launch(this@MainActivity)
```

It is common to show the "enable passcode" state with a view such as a ```Switch```  from your app Settings screen. Here is one way to approach it:
```kotlin
class MainActivity : LokiActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
		findViewById<SwitchCompat>(R.id.enable_pin).apply {
            this.isChecked = Loki.isEnabled(this@MainActivity)
            this.setOnCheckedChangeListener { _, isChecked ->
                when (isChecked) {
                    true -> PasscodeSetupActivity.launch(this@MainActivity)
                    else -> Loki.disable(this@MainActivity)
                }
            }
        }
    }
}
```


### Loki Customization
You can customize certain aspects of LoKi by calling ```overwriteConfig``` on the ```Loki``` singleton  object. You  want to most likely do this from your Application class since it serves as the first entrypoint to it.
```kotlin
class DemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Loki.overwriteConfig(
            LokiConfig(
                appName = "LoKi Demo",
                maxAttempts = 1,
                passcodePinActiveCircleColor = android.R.color.holo_blue_dark
            )
        )
    }
}
```

## Internals
* Loki will place your app in a "disable" state if the user fails the passcode unlock workflow. Currently users must wait 1 minute before trying again
* LoKi's ```PasscodeManager``` is in charge of encoding (using a salt) the user's passcode
* Behavior might change on newer versions. Make sure to reference docs. 


## Additional 🎈
LoKi is a fast-track way of adding a passcode lock to your applications, you can also use it in production apps, however it was designed for quick experimentation and allow teams to satisfy a passcode lock need as quick as possible. Feel free to contribute and submit ideas on how it can be improved!


## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/joelromanpr/LoKi/stargazers)__ for this repository. :star: <br>
And __[follow](https://github.com/joelromanpr)__ me for my next creations! 🤩

# License
```xml
Copyright 2021 joelromanpr (Joel R. Sosa)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
