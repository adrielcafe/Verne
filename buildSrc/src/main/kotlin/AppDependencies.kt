object LibVersion {
    // Project
    internal const val ANDROID_GRADLE = "3.4.0-rc02"
    internal const val GOOGLE_SERVICES = "4.2.0"
    internal const val FIREBASE = "1.1.5"
    internal const val FABRIC = "1.27.1"
    internal const val GRADLE_VERSIONS = "0.20.0"

    // Kotlin
    internal const val KOTLIN = "1.3.21"
    internal const val COROUTINES = "1.1.1"
    internal const val COROUTINES_SCOPES = "1.0.1"

    // Android
    internal const val MATERIAL = "1.1.0-alpha05"
    internal const val APP_COMPAT = "1.1.0-alpha03"
    internal const val PREFERENCE = "1.1.0-alpha04"
    internal const val CONSTRAINT_LAYOUT = "2.0.0-alpha3"
    internal const val LIFECYCLE = "2.1.0-alpha03"
    internal const val BROWSER = "1.0.0"

    // Android KTX
    internal const val CORE_KTX = "1.1.0-alpha05"
    internal const val PREFERENCE_KTX = "1.1.0-alpha04"
    internal const val FRAGMENT_KTX = "1.1.0-alpha05"

    // Firebase
    internal const val FIREBASE_CORE = "16.0.8"
    internal const val FIREBASE_PERF = "16.2.4"

    // Quality
    internal const val INSTABUG = "8.2.0"
    internal const val CRASHLYTICS = "2.9.9"
    internal const val LEAK_CANARY = "1.6.3"

    // View
    internal const val KRUMBS_VIEW = "1.1.2"
    internal const val FAST_ADAPTER = "3.3.1"
    internal const val FAB_OPTIONS = "1.2.0"
    internal const val SIMPLE_SEARCH_VIEW = "0.1.3"
    internal const val NUMBER_SLIDING_PICKER = "1.0.2"
    internal const val STATEFUL_LAYOUT = "2.0.8"

    // Util
    internal const val KOIN = "2.0.0-rc-1"
    internal const val EIFFEL = "4.1.0"
    internal const val TIMBER_KT = "1.5.1"
    internal const val KEYBOARD_LISTENER = "1.0.0"
    internal const val PRETTY_TIME = "4.0.2.Final"
    internal const val PDF_GENERATOR = "1.2"
    internal const val ASSENT = "2.2.3"

    // Forks
    internal const val AZTEC_EDITOR = "develop-SNAPSHOT"
    internal const val MATERIAL_DIALOGS = "master-SNAPSHOT"

    // Test
    internal const val JUNIT = "4.12"
    internal const val ESPRESSO = "3.1.1"
    internal const val RUNNER = "1.1.1"
}

object ProjectLib {
    const val ANDROID_GRADLE = "com.android.tools.build:gradle:${LibVersion.ANDROID_GRADLE}"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:${LibVersion.GOOGLE_SERVICES}"
    const val FIREBASE = "com.google.firebase:firebase-plugins:${LibVersion.FIREBASE}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${LibVersion.KOTLIN}"
    const val FABRIC = "io.fabric.tools:gradle:${LibVersion.FABRIC}"
    const val GRADLE_VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${LibVersion.GRADLE_VERSIONS}"
}

object AppLib {
    // Kotlin
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${LibVersion.KOTLIN}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibVersion.COROUTINES}"
    const val COROUTINES_SCOPES_APP_COMPAT = "com.github.adrielcafe.androidcoroutinescopes:appcompat:${LibVersion.COROUTINES_SCOPES}"
    const val COROUTINES_SCOPES_VIEW_MODEL = "com.github.adrielcafe.androidcoroutinescopes:viewmodel:${LibVersion.COROUTINES_SCOPES}"

    // Android
    const val MATERIAL = "com.google.android.material:material:${LibVersion.MATERIAL}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${LibVersion.APP_COMPAT}"
    const val PREFERENCE = "androidx.preference:preference:${LibVersion.PREFERENCE}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${LibVersion.CONSTRAINT_LAYOUT}"
    const val LIFECYCLE = "androidx.lifecycle:lifecycle-extensions:${LibVersion.LIFECYCLE}"
    const val BROWSER = "androidx.browser:browser:${LibVersion.BROWSER}"

    // Android KTX
    const val CORE_KTX = "androidx.core:core-ktx:${LibVersion.CORE_KTX}"
    const val PREFERENCE_KTX = "androidx.preference:preference-ktx:${LibVersion.PREFERENCE_KTX}"
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${LibVersion.FRAGMENT_KTX}"

    // Firebase
    const val FIREBASE_CORE = "com.google.firebase:firebase-core:${LibVersion.FIREBASE_CORE}"
    const val FIREBASE_PERF = "com.google.firebase:firebase-perf:${LibVersion.FIREBASE_PERF}"

    // Quality
    const val INSTABUG = "com.instabug.library:instabug:${LibVersion.INSTABUG}"
    const val CRASHLYTICS = "com.crashlytics.sdk.android:crashlytics:${LibVersion.CRASHLYTICS}"
    const val LEAK_CANARY_NO_OP = "com.squareup.leakcanary:leakcanary-android-no-op:${LibVersion.LEAK_CANARY}"
    const val LEAK_CANARY_ANDROID = "com.squareup.leakcanary:leakcanary-android:${LibVersion.LEAK_CANARY}"
    const val LEAK_CANARY_FRAGMENT = "com.squareup.leakcanary:leakcanary-support-fragment:${LibVersion.LEAK_CANARY}"

    // View
    const val KRUMBS_VIEW = "com.github.adrielcafe:krumbsview:${LibVersion.KRUMBS_VIEW}"
    const val FAST_ADAPTER = "com.mikepenz:fastadapter:${LibVersion.FAST_ADAPTER}"
    const val FAST_ADAPTER_COMMONS = "com.mikepenz:fastadapter-commons:${LibVersion.FAST_ADAPTER}"
    const val FAST_ADAPTER_EXTENSIONS = "com.mikepenz:fastadapter-extensions:${LibVersion.FAST_ADAPTER}"
    const val FAB_OPTIONS = "com.github.joaquimley:faboptions:${LibVersion.FAB_OPTIONS}"
    const val SIMPLE_SEARCH_VIEW = "com.github.Ferfalk:SimpleSearchView:${LibVersion.SIMPLE_SEARCH_VIEW}"
    const val NUMBER_SLIDING_PICKER = "com.github.sephiroth74:NumberSlidingPicker:${LibVersion.NUMBER_SLIDING_PICKER}"
    const val STATEFUL_LAYOUT = "cz.kinst.jakub:android-stateful-layout-base:${LibVersion.STATEFUL_LAYOUT}"

    // Util
    const val KOIN_CORE = "org.koin:koin-core:${LibVersion.KOIN}"
    const val KOIN_VIEW_MODEL = "org.koin:koin-androidx-viewmodel:${LibVersion.KOIN}"
    const val EIFFEL = "com.github.etiennelenhart:eiffel:${LibVersion.EIFFEL}"
    const val TIMBER_KT = "com.github.ajalt:timberkt:${LibVersion.TIMBER_KT}"
    const val KEYBOARD_LISTENER = "com.github.ravindu1024:android-keyboardlistener:${LibVersion.KEYBOARD_LISTENER}"
    const val PRETTY_TIME = "org.ocpsoft.prettytime:prettytime:${LibVersion.PRETTY_TIME}"
    const val PDF_GENERATOR = "com.uttampanchasara.pdfgenerator:pdfgenerator:${LibVersion.PDF_GENERATOR}"
    const val ASSENT = "com.afollestad:assent:${LibVersion.ASSENT}"

    // Forks
    // https://github.com/adrielcafe/AztecEditor-Android
    const val AZTEC_EDITOR = "com.github.adrielcafe.AztecEditor-Android:aztec:${LibVersion.AZTEC_EDITOR}"
    // https://github.com/adrielcafe/material-dialogs
    const val MATERIAL_DIALOGS_CORE = "com.github.adrielcafe.material-dialogs:core:${LibVersion.MATERIAL_DIALOGS}"
    const val MATERIAL_DIALOGS_INPUT = "com.github.adrielcafe.material-dialogs:input:${LibVersion.MATERIAL_DIALOGS}"
    const val MATERIAL_DIALOGS_FILES = "com.github.adrielcafe.material-dialogs:files:${LibVersion.MATERIAL_DIALOGS}"
}

object TestLib {
    const val JUNIT = "junit:junit:${LibVersion.JUNIT}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${LibVersion.ESPRESSO}"
    const val RUNNER = "androidx.test:runner:${LibVersion.RUNNER}"
}