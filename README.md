# Stargazer [![Build Status](https://travis-ci.org/dcampogiani/Stargazers.svg?branch=master)](https://travis-ci.org/dcampogiani/Stargazers)

Show who starred a repo using [GitHub Api](https://developer.github.com/v3/activity/starring/#list-stargazers)

<img src="https://github.com/dcampogiani/Stargazers/blob/master/demo.gif?raw=true" width="250"> 

## Try it

* [Download](https://github.com/dcampogiani/Stargazers/blob/master/app/release/Stargazers.apk?raw=true)
* [Live Demo](https://appetize.io/app/m838v2p4yaqnutbcd2dtb5wefr)

## Building
To build a debug version, run this from the root of the project:

    ./gradlew app:assembleDebug
    
    
## Testing

To run the Unit Tests run the following command from the root of the project:

	./gradlew app:testDebugUnitTest

## Languages, libraries and tools used

* [Kotlin](https://kotlinlang.org/)
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html)
* [Dagger](https://google.github.io/dagger/)
* [Glide](https://github.com/bumptech/glide)
* [Retrofit](http://square.github.io/retrofit/) with [Coroutine Adapter](https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter)
* [JUnit](http://junit.org/junit4/)
* [Mockito-Kotlin](https://github.com/nhaarman/mockito-kotlin)
