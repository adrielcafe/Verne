object LibVersion {
    // Project
    internal const val androidGradle = "3.4.0-rc02"
    internal const val googleServices = "4.2.0"
    internal const val firebasePlugin = "1.1.5"
    internal const val fabric = "1.27.1"
    internal const val gradleVersions = "0.20.0"

    // Kotlin
    internal const val kotlin = "1.3.21"
    internal const val coroutines = "1.1.1"
    internal const val androidCoroutineScopes = "1.0.1"

    // Android
    internal const val material = "1.1.0-alpha04"
    internal const val appCompat = "1.1.0-alpha03"
    internal const val preference = "1.1.0-alpha04"
    internal const val constraintLayout = "2.0.0-alpha3"
    internal const val lifecycle = "2.1.0-alpha03"
    internal const val browser = "1.0.0"

    // Android KTX
    internal const val coreKtx = "1.1.0-alpha05"
    internal const val preferenceKtx = "1.1.0-alpha04"
    internal const val fragmentKtx = "1.1.0-alpha05"

    // Firebase
    internal const val firebaseCore = "16.0.8"
    internal const val firebasePerf = "16.2.4"

    // Quality
    internal const val instabug = "8.1.2"
    internal const val crashlytics = "2.9.9"
    internal const val leakCanary = "1.6.3"

    // View
    internal const val krumbsView = "1.1.2"
    internal const val fastAdapter = "3.3.1"
    internal const val fabOptions = "1.2.0"
    internal const val simpleSearchView = "0.1.3"
    internal const val numberSlidingPicker = "1.0.2"
    internal const val statefulLayout = "2.0.8"

    // Util
    internal const val koin = "2.0.0-rc-1"
    internal const val eiffel = "4.1.0"
    internal const val mmkv = "1.0.18"
    internal const val timberKt = "1.5.1"
    internal const val keyboardListener = "1.0.0"
    internal const val prettyTime = "4.0.2.Final"
    internal const val pdfGenerator = "1.1"
    internal const val assent = "2.2.3"

    // Forks
    internal const val aztecEditor = "develop-SNAPSHOT"
    internal const val materialDialogs = "master-SNAPSHOT"

    // Test
    internal const val junit = "4.12"
    internal const val espresso = "3.1.1"
    internal const val runner = "1.1.1"
}

object ProjectLib {
    const val androidGradle = "com.android.tools.build:gradle:${LibVersion.androidGradle}"
    const val googleServices = "com.google.gms:google-services:${LibVersion.googleServices}"
    const val firebasePlugin = "com.google.firebase:firebase-plugins:${LibVersion.firebasePlugin}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${LibVersion.kotlin}"
    const val fabric = "io.fabric.tools:gradle:${LibVersion.fabric}"
    const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${LibVersion.gradleVersions}"
}

object AppLib {
    // Kotlin
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${LibVersion.kotlin}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibVersion.coroutines}"
    const val androidCoroutineScopesAppCompat = "com.github.adrielcafe.androidcoroutinescopes:appcompat:${LibVersion.androidCoroutineScopes}"
    const val androidCoroutineScopesViewModel = "com.github.adrielcafe.androidcoroutinescopes:viewmodel:${LibVersion.androidCoroutineScopes}"

    // Android
    const val material = "com.google.android.material:material:${LibVersion.material}"
    const val appCompat = "androidx.appcompat:appcompat:${LibVersion.appCompat}"
    const val preference = "androidx.preference:preference:${LibVersion.preference}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${LibVersion.constraintLayout}"
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${LibVersion.lifecycle}"
    const val browser = "androidx.browser:browser:${LibVersion.browser}"

    // Android KTX
    const val coreKtx = "androidx.core:core-ktx:${LibVersion.coreKtx}"
    const val preferenceKtx = "androidx.preference:preference-ktx:${LibVersion.preferenceKtx}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${LibVersion.fragmentKtx}"

    // Firebase
    const val firebaseCore = "com.google.firebase:firebase-core:${LibVersion.firebaseCore}"
    const val firebasePerf = "com.google.firebase:firebase-perf:${LibVersion.firebasePerf}"

    // Quality
    const val instabug = "com.instabug.library:instabug:${LibVersion.instabug}"
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${LibVersion.crashlytics}"
    const val leakCanaryNoOp = "com.squareup.leakcanary:leakcanary-android-no-op:${LibVersion.leakCanary}"
    const val leakCanaryAndroid = "com.squareup.leakcanary:leakcanary-android:${LibVersion.leakCanary}"
    const val leakCanarySupportFragment = "com.squareup.leakcanary:leakcanary-support-fragment:${LibVersion.leakCanary}"

    // View
    const val krumbsView = "com.github.adrielcafe:krumbsview:${LibVersion.krumbsView}"
    const val fastAdapter = "com.mikepenz:fastadapter:${LibVersion.fastAdapter}"
    const val fastAdapterCommons = "com.mikepenz:fastadapter-commons:${LibVersion.fastAdapter}"
    const val fastAdapterExtensions = "com.mikepenz:fastadapter-extensions:${LibVersion.fastAdapter}"
    const val fabOptions = "com.github.joaquimley:faboptions:${LibVersion.fabOptions}"
    const val simpleSearchView = "com.github.Ferfalk:SimpleSearchView:${LibVersion.simpleSearchView}"
    const val numberSlidingPicker = "com.github.sephiroth74:NumberSlidingPicker:${LibVersion.numberSlidingPicker}"
    const val statefulLayout = "cz.kinst.jakub:android-stateful-layout-base:${LibVersion.statefulLayout}"

    // Util
    const val koinCore = "org.koin:koin-core:${LibVersion.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${LibVersion.koin}"
    const val eiffel = "com.github.etiennelenhart:eiffel:${LibVersion.eiffel}"
    const val mmkv = "com.tencent:mmkv:${LibVersion.mmkv}"
    const val timberKt = "com.github.ajalt:timberkt:${LibVersion.timberKt}"
    const val keyboardListener = "com.github.ravindu1024:android-keyboardlistener:${LibVersion.keyboardListener}"
    const val prettyTime = "org.ocpsoft.prettytime:prettytime:${LibVersion.prettyTime}"
    const val pdfGenerator = "com.uttampanchasara.pdfgenerator:pdfgenerator:${LibVersion.pdfGenerator}"
    const val assent = "com.afollestad:assent:${LibVersion.assent}"

    // Forks
    // https://github.com/adrielcafe/AztecEditor-Android
    const val aztecEditor = "com.github.adrielcafe.AztecEditor-Android:aztec:${LibVersion.aztecEditor}"
    // https://github.com/adrielcafe/material-dialogs
    const val materialDialogsCore = "com.github.adrielcafe.material-dialogs:core:${LibVersion.materialDialogs}"
    const val materialDialogsInput = "com.github.adrielcafe.material-dialogs:input:${LibVersion.materialDialogs}"
    const val materialDialogsFiles = "com.github.adrielcafe.material-dialogs:files:${LibVersion.materialDialogs}"
}

object TestLib {
    const val junit = "junit:junit:${LibVersion.junit}"
    const val espresso = "androidx.test.espresso:espresso-core:${LibVersion.espresso}"
    const val runner = "androidx.test:runner:${LibVersion.runner}"
}