# NewsApiSample

### Description
This app is a sample Android app that integrates with the [News API](https://newsapi.org/).
It has low complexity but uses (or tries to use) some good Android development practices.

### Architecture
This app is using an MVVM approach, and it uses the following main libraries:
- [Retrofit](https://square.github.io/retrofit/) (REST API requests)
- [Gson](https://github.com/google/gson) (for Json serialization/deserialization)
- [coil](https://github.com/coil-kt/coil) (image loading)
- [Result](https://github.com/kittinunf/Result) (for a railway oriented response handling)
- [Koin](https://github.com/InsertKoinIO/koin) (for Dependency Injection)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) (for ViewModel layer)
- [MockK](https://mockk.io/) (for mocking / testing)

### Dependency Management
The dependency management organization is made using Kotlin and the `buildSrc` approach, inspired by this [article](https://proandroiddev.com/gradle-dependency-management-with-kotlin-94eed4df9a28).

This allows better logical organization of dependencies and versions, and enables autocomplete and easy lookup (i.e.: using `Ctrl+Click`).

The main downside is that Android Studio no longer warns you when new versions of your dependencies is available, but this feature is added back by using this [gradle-versions-plugin](https://github.com/ben-manes/gradle-versions-plugin).

The plugin adds the `dependencyUpdates` gradle task, that generates a report with useful information about you dependencies versions.

### Configuration
To run the project, you will need to get an [API Key from News API](https://newsapi.org/register).
Then you need to create a file called `newsapi.properties` at the **root** location of this project, with the following content:

```groovy
NEWS_API_KEY="<your_key_here>"
```

That's it. You should be able to properly run this project.
