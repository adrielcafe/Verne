object Version {
    // Project
    internal const val ANDROID_GRADLE = "3.4.0-rc03"
    internal const val GOOGLE_SERVICES = "4.2.0"
    internal const val FIREBASE = "1.2.0"
    internal const val FABRIC = "1.28.1"

    // Kotlin
    internal const val KOTLIN = "1.3.21"
    internal const val COROUTINES = "1.1.1"
    internal const val COROUTINES_SCOPES = "1.0.1"

    // Android
    internal const val MATERIAL = "1.1.0-alpha05"
    internal const val APP_COMPAT = "1.1.0-alpha04"
    internal const val PREFERENCE = "1.1.0-alpha04"
    internal const val CONSTRAINT_LAYOUT = "2.0.0-alpha4"
    internal const val LIFECYCLE = "2.1.0-alpha04"
    internal const val BROWSER = "1.0.0"

    // Android KTX
    internal const val CORE_KTX = "1.1.0-alpha05"
    internal const val PREFERENCE_KTX = "1.1.0-alpha04"
    internal const val FRAGMENT_KTX = "1.1.0-alpha06"

    // Firebase
    internal const val FIREBASE_CORE = "16.0.8"
    internal const val FIREBASE_PERF = "16.2.5"

    // Quality
    internal const val INSTABUG = "8.2.1"
    internal const val CRASHLYTICS = "2.9.9"
    internal const val LEAK_CANARY = "1.6.3"

    // View
    internal const val KRUMBS_VIEW = "1.1.2"
    internal const val AZTEC_EDITOR = "1.3.24"
    internal const val FAST_ADAPTER = "3.3.1"
    internal const val FAB_OPTIONS = "1.2.0"
    internal const val SIMPLE_SEARCH_VIEW = "0.1.3"
    internal const val STATEFUL_LAYOUT = "2.0.8"

    // Util
    internal const val PUFFER_DB = "1.0.0"
    internal const val KOIN = "2.0.0-rc-3"
    internal const val EIFFEL = "4.1.0"
    internal const val TIMBER = "4.7.1"
    internal const val KEYBOARD_LISTENER = "1.0.0"
    internal const val PRETTY_TIME = "4.0.2.Final"
    internal const val PDF_GENERATOR = "1.2"
    internal const val ASSENT = "2.2.3"

    // Forks
    internal const val MATERIAL_DIALOGS = "master-SNAPSHOT"
    internal const val NUMBER_SLIDING_PICKER = "develop-SNAPSHOT"

    // Test
    internal const val JUNIT = "4.12"
    internal const val ESPRESSO = "3.1.1"
    internal const val RUNNER = "1.1.1"
}

object ProjectLib {
    const val ANDROID_GRADLE = "com.android.tools.build:gradle:${Version.ANDROID_GRADLE}"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:${Version.GOOGLE_SERVICES}"
    const val FIREBASE = "com.google.firebase:firebase-plugins:${Version.FIREBASE}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}"
    const val FABRIC = "io.fabric.tools:gradle:${Version.FABRIC}"
}

object ModuleLib {
    // Kotlin
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.KOTLIN}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"
    const val COROUTINES_SCOPES_APP_COMPAT =
        "com.github.adrielcafe.androidcoroutinescopes:appcompat:${Version.COROUTINES_SCOPES}"
    const val COROUTINES_SCOPES_VIEW_MODEL =
        "com.github.adrielcafe.androidcoroutinescopes:viewmodel:${Version.COROUTINES_SCOPES}"

    // Android
    const val MATERIAL = "com.google.android.material:material:${Version.MATERIAL}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Version.APP_COMPAT}"
    const val PREFERENCE = "androidx.preference:preference:${Version.PREFERENCE}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINT_LAYOUT}"
    const val LIFECYCLE = "androidx.lifecycle:lifecycle-extensions:${Version.LIFECYCLE}"
    const val BROWSER = "androidx.browser:browser:${Version.BROWSER}"

    // Android KTX
    const val CORE_KTX = "androidx.core:core-ktx:${Version.CORE_KTX}"
    const val PREFERENCE_KTX = "androidx.preference:preference-ktx:${Version.PREFERENCE_KTX}"
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Version.FRAGMENT_KTX}"

    // Firebase
    const val FIREBASE_CORE = "com.google.firebase:firebase-core:${Version.FIREBASE_CORE}"
    const val FIREBASE_PERF = "com.google.firebase:firebase-perf:${Version.FIREBASE_PERF}"

    // Quality
    const val INSTABUG = "com.instabug.library:instabug:${Version.INSTABUG}"
    const val CRASHLYTICS = "com.crashlytics.sdk.android:crashlytics:${Version.CRASHLYTICS}"
    const val LEAK_CANARY_NO_OP = "com.squareup.leakcanary:leakcanary-android-no-op:${Version.LEAK_CANARY}"
    const val LEAK_CANARY_ANDROID = "com.squareup.leakcanary:leakcanary-android:${Version.LEAK_CANARY}"
    const val LEAK_CANARY_FRAGMENT = "com.squareup.leakcanary:leakcanary-support-fragment:${Version.LEAK_CANARY}"

    // View
    const val KRUMBS_VIEW = "com.github.adrielcafe:krumbsview:${Version.KRUMBS_VIEW}"
    const val AZTEC_EDITOR = "com.github.wordpress-mobile.WordPress-Aztec-Android:aztec:${Version.AZTEC_EDITOR}"
    const val FAST_ADAPTER = "com.mikepenz:fastadapter:${Version.FAST_ADAPTER}"
    const val FAST_ADAPTER_COMMONS = "com.mikepenz:fastadapter-commons:${Version.FAST_ADAPTER}"
    const val FAST_ADAPTER_EXTENSIONS = "com.mikepenz:fastadapter-extensions:${Version.FAST_ADAPTER}"
    const val FAB_OPTIONS = "com.github.joaquimley:faboptions:${Version.FAB_OPTIONS}"
    const val SIMPLE_SEARCH_VIEW = "com.github.Ferfalk:SimpleSearchView:${Version.SIMPLE_SEARCH_VIEW}"
    const val STATEFUL_LAYOUT = "cz.kinst.jakub:android-stateful-layout-base:${Version.STATEFUL_LAYOUT}"

    // Util
    const val PUFFER_DB = "com.github.adrielcafe.pufferdb:core:${Version.PUFFER_DB}"
    const val KOIN_CORE = "org.koin:koin-core:${Version.KOIN}"
    const val KOIN_VIEW_MODEL = "org.koin:koin-androidx-viewmodel:${Version.KOIN}"
    const val EIFFEL = "com.github.etiennelenhart:eiffel:${Version.EIFFEL}"
    const val TIMBER = "com.jakewharton.timber:timber:${Version.TIMBER}"
    const val KEYBOARD_LISTENER = "com.github.ravindu1024:android-keyboardlistener:${Version.KEYBOARD_LISTENER}"
    const val PRETTY_TIME = "org.ocpsoft.prettytime:prettytime:${Version.PRETTY_TIME}"
    const val PDF_GENERATOR = "com.uttampanchasara.pdfgenerator:pdfgenerator:${Version.PDF_GENERATOR}"
    const val ASSENT = "com.afollestad:assent:${Version.ASSENT}"

    // Forks
    // https://github.com/adrielcafe/material-dialogs
    const val MATERIAL_DIALOGS_CORE = "com.github.adrielcafe.material-dialogs:core:${Version.MATERIAL_DIALOGS}"
    const val MATERIAL_DIALOGS_INPUT = "com.github.adrielcafe.material-dialogs:input:${Version.MATERIAL_DIALOGS}"
    const val MATERIAL_DIALOGS_FILES = "com.github.adrielcafe.material-dialogs:files:${Version.MATERIAL_DIALOGS}"
    // https://github.com/adrielcafe/NumberSlidingPicker
    const val NUMBER_SLIDING_PICKER = "com.github.adrielcafe:NumberSlidingPicker:${Version.NUMBER_SLIDING_PICKER}"
}

object TestLib {
    const val JUNIT = "junit:junit:${Version.JUNIT}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${Version.ESPRESSO}"
    const val RUNNER = "androidx.test:runner:${Version.RUNNER}"
}
