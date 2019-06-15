@file:Suppress("Unused", "MayBeConstant", "MemberVisibilityCanBePrivate")

internal object Version {
    // Project
    const val ANDROID_GRADLE = "3.4.1"
    const val GOOGLE_SERVICES = "4.2.0"
    const val FIREBASE = "1.2.0"
    const val FABRIC = "1.29.0"

    // Kotlin
    const val KOTLIN = "1.3.31"
    const val COROUTINES = "1.2.1"

    // Android
    const val MATERIAL = "1.1.0-alpha07"
    const val APP_COMPAT = "1.1.0-beta01"
    const val PREFERENCE = "1.1.0-beta01"
    const val CONSTRAINT_LAYOUT = "2.0.0-beta1"
    const val LIFECYCLE = "2.2.0-alpha01"
    const val BROWSER = "1.0.0"

    // Android KTX
    const val CORE_KTX = "1.2.0-alpha02"
    const val PREFERENCE_KTX = "1.1.0-beta01"
    const val FRAGMENT_KTX = "1.1.0-beta01"
    const val VIEWMODEL_KTX = "2.2.0-alpha01"
    const val LIFECYCLE_KTX = "2.2.0-alpha01"
    const val LIVEDATA_KTX = "2.2.0-alpha01"

    // Firebase
    const val FIREBASE_CORE = "16.0.9"
    const val FIREBASE_PERF = "17.0.2"
    const val FIREBASE_CRASHLYTICS = "2.10.1"

    // View
    const val KRUMBS_VIEW = "1.1.2"
    const val AZTEC_EDITOR = "1.3.27"
    const val FAST_ADAPTER = "4.0.0"
    const val FAB_OPTIONS = "1.2.0"
    const val SIMPLE_SEARCH_VIEW = "0.1.4"
    const val STATEFUL_LAYOUT = "2.0.8"

    // Util
    const val PUFFER_DB = "1.0.0"
    const val KOIN = "2.0.1"
    const val EIFFEL = "4.1.0"
    const val TIMBER = "4.7.1"
    const val KEYBOARD_LISTENER = "1.0.0"
    const val PRETTY_TIME = "4.0.2.Final"
    const val PDF_GENERATOR = "1.2"
    const val ASSENT = "2.3.0"
    const val LEAK_CANARY = "2.0-alpha-2"

    // Forks
    const val MATERIAL_DIALOGS = "master-SNAPSHOT"
    const val NUMBER_SLIDING_PICKER = "develop-SNAPSHOT"

    // Test
    const val JUNIT = "4.12"
    const val ESPRESSO = "3.2.0"
    const val RUNNER = "1.2.0"
}

object ProjectLib {
    const val ANDROID_GRADLE = "com.android.tools.build:gradle:${Version.ANDROID_GRADLE}"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:${Version.GOOGLE_SERVICES}"
    const val FIREBASE = "com.google.firebase:firebase-plugins:${Version.FIREBASE}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}"
    const val FABRIC = "io.fabric.tools:gradle:${Version.FABRIC}"

    val all = setOf(ANDROID_GRADLE, GOOGLE_SERVICES, FIREBASE, KOTLIN, FABRIC)
}

object ModuleLib {
    // Kotlin
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.KOTLIN}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"

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
    const val VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.VIEWMODEL_KTX}"
    const val LIFECYCLE_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.LIFECYCLE_KTX}"
    const val LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.LIVEDATA_KTX}"

    // Firebase
    const val FIREBASE_CORE = "com.google.firebase:firebase-core:${Version.FIREBASE_CORE}"
    const val FIREBASE_PERF = "com.google.firebase:firebase-perf:${Version.FIREBASE_PERF}"
    const val FIREBASE_CRASHLYTICS = "com.crashlytics.sdk.android:crashlytics:${Version.FIREBASE_CRASHLYTICS}"

    // Quality
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Version.LEAK_CANARY}"

    // View
    const val KRUMBS_VIEW = "com.github.adrielcafe:krumbsview:${Version.KRUMBS_VIEW}"
    const val AZTEC_EDITOR = "com.github.wordpress-mobile.WordPress-Aztec-Android:aztec:${Version.AZTEC_EDITOR}"
    const val FAST_ADAPTER_CORE = "com.mikepenz:fastadapter:${Version.FAST_ADAPTER}"
    const val FAST_ADAPTER_UI = "com.mikepenz:fastadapter-extensions-ui:${Version.FAST_ADAPTER}"
    const val FAST_ADAPTER_UTILS = "com.mikepenz:fastadapter-extensions-utils:${Version.FAST_ADAPTER}"
    const val FAB_OPTIONS = "com.github.joaquimley:faboptions:${Version.FAB_OPTIONS}"
    const val SIMPLE_SEARCH_VIEW = "com.github.Ferfalk:SimpleSearchView:${Version.SIMPLE_SEARCH_VIEW}"
    const val STATEFUL_LAYOUT = "cz.kinst.jakub:android-stateful-layout-base:${Version.STATEFUL_LAYOUT}"
    const val KEYBOARD_LISTENER = "com.github.ravindu1024:android-keyboardlistener:${Version.KEYBOARD_LISTENER}"
    const val PRETTY_TIME = "org.ocpsoft.prettytime:prettytime:${Version.PRETTY_TIME}"
    const val PDF_GENERATOR = "com.uttampanchasara.pdfgenerator:pdfgenerator:${Version.PDF_GENERATOR}"
    const val ASSENT = "com.afollestad:assent:${Version.ASSENT}"

    // Util
    const val PUFFER_DB = "com.github.adrielcafe.pufferdb:core:${Version.PUFFER_DB}"
    const val KOIN_CORE = "org.koin:koin-core:${Version.KOIN}"
    const val KOIN_VIEW_MODEL = "org.koin:koin-androidx-viewmodel:${Version.KOIN}"
    const val EIFFEL = "com.github.etiennelenhart:eiffel:${Version.EIFFEL}"
    const val TIMBER = "com.jakewharton.timber:timber:${Version.TIMBER}"

    // Forks
    // https://github.com/adrielcafe/material-dialogs
    const val MATERIAL_DIALOGS_CORE = "com.github.adrielcafe.material-dialogs:core:${Version.MATERIAL_DIALOGS}"
    const val MATERIAL_DIALOGS_INPUT = "com.github.adrielcafe.material-dialogs:input:${Version.MATERIAL_DIALOGS}"
    const val MATERIAL_DIALOGS_FILES = "com.github.adrielcafe.material-dialogs:files:${Version.MATERIAL_DIALOGS}"
    // https://github.com/adrielcafe/NumberSlidingPicker
    const val NUMBER_SLIDING_PICKER = "com.github.adrielcafe:NumberSlidingPicker:${Version.NUMBER_SLIDING_PICKER}"

    val kotlin = setOf(KOTLIN, COROUTINES)
    val android = setOf(MATERIAL, APP_COMPAT, PREFERENCE, CONSTRAINT_LAYOUT, LIFECYCLE, BROWSER)
    val androidKtx = setOf(CORE_KTX, PREFERENCE_KTX, FRAGMENT_KTX, VIEWMODEL_KTX, LIFECYCLE_KTX, LIVEDATA_KTX)
    val firebase = setOf(FIREBASE_CORE, FIREBASE_PERF, FIREBASE_CRASHLYTICS)
    val view = setOf(KRUMBS_VIEW, AZTEC_EDITOR, FAST_ADAPTER_CORE, FAST_ADAPTER_UI, FAST_ADAPTER_UTILS, FAB_OPTIONS,
        SIMPLE_SEARCH_VIEW, STATEFUL_LAYOUT, MATERIAL_DIALOGS_CORE, MATERIAL_DIALOGS_INPUT, KEYBOARD_LISTENER,
        PRETTY_TIME, PDF_GENERATOR, ASSENT, MATERIAL_DIALOGS_FILES, NUMBER_SLIDING_PICKER)
}

object TestLib {
    // Unit testing
    const val JUNIT = "junit:junit:${Version.JUNIT}"

    // Instrumentation testing
    const val RUNNER = "androidx.test:runner:${Version.RUNNER}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${Version.ESPRESSO}"

    val unitTest = setOf(JUNIT)
    val instrumentationTest = setOf(RUNNER, ESPRESSO)
}
