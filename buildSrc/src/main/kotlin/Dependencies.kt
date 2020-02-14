@file:Suppress("Unused", "MayBeConstant", "MemberVisibilityCanBePrivate")

private object Version {
    // Gradle plugins
    const val ANDROID_GRADLE = "3.5.3"
//    const val GOOGLE_SERVICES = "4.3.3"
//    const val FIREBASE = "2.0.0"

    // Kotlin
    const val KOTLIN = "1.3.61"
    const val COROUTINES = "1.3.3"

    // Android
    const val MATERIAL = "1.1.0"
    const val APP_COMPAT = "1.1.0"
    const val CONSTRAINT_LAYOUT = "2.0.0-beta4"
    const val RECYCLER_VIEW = "1.1.0"
    const val VIEW_PAGER_2 = "1.0.0"
    const val ACTIVITY_KTX = "1.1.0"
    const val CORE_KTX = "1.2.0"
    const val LIFECYCLE_KTX = "2.2.0"

    // Firebase
//    const val FIREBASE_CORE = "17.2.2"
//    const val FIREBASE_PERF = "19.0.5"
//    const val FIREBASE_CRASHLYTICS = "17.0.0-beta01"

    // Data
    const val OBJECT_BOX = "2.5.0"

    // DI
    const val KOIN = "2.0.1"

    // UI
    const val KRUMBS_VIEW = "1.1.2"
    const val FASTADAPTER = "4.1.2"

    // Test
    const val JUNIT = "4.13"
    const val RUNNER = "1.2.0"
    const val ESPRESSO = "3.2.0"
}

object ProjectDep {
    const val ANDROID_GRADLE = "com.android.tools.build:gradle:${Version.ANDROID_GRADLE}"
//    const val GOOGLE_SERVICES = "com.google.gms:google-services:${Version.GOOGLE_SERVICES}"
//    const val FIREBASE = "com.google.firebase:firebase-plugins:${Version.FIREBASE}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}"
    const val OBJECT_BOX = "io.objectbox:objectbox-gradle-plugin:${Version.OBJECT_BOX}"

    val all = setOf(ANDROID_GRADLE, KOTLIN, OBJECT_BOX)
}

object ModuleDep {
    // Kotlin
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.KOTLIN}"
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES}"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"

    // Android
    const val MATERIAL = "com.google.android.material:material:${Version.MATERIAL}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Version.APP_COMPAT}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINT_LAYOUT}"
    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Version.RECYCLER_VIEW}"
    const val VIEW_PAGER_2 = "androidx.viewpager2:viewpager2:${Version.VIEW_PAGER_2}"
    const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${Version.ACTIVITY_KTX}"
    const val CORE_KTX = "androidx.core:core-ktx:${Version.CORE_KTX}"
    const val LIFECYCLE_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.LIFECYCLE_KTX}"

    // Firebase
//    const val FIREBASE_CORE = "com.google.firebase:firebase-core:${Version.FIREBASE_CORE}"
//    const val FIREBASE_PERF = "com.google.firebase:firebase-perf:${Version.FIREBASE_PERF}"
//    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics:${Version.FIREBASE_CRASHLYTICS}"

    // Data
    const val OBJECT_BOX = "io.objectbox:objectbox-android:${Version.OBJECT_BOX}"
    const val OBJECT_BOX_BROWSER = "io.objectbox:objectbox-android-objectbrowser:${Version.OBJECT_BOX}"
    const val OBJECT_BOX_KTX = "io.objectbox:objectbox-kotlin:${Version.OBJECT_BOX}"

    // DI
    const val KOIN = "org.koin:koin-core:${Version.KOIN}"
    const val KOIN_ANDROID = "org.koin:koin-android:${Version.KOIN}"
    const val KOIN_ANDROID_VIEWMODEL = "org.koin:koin-androidx-viewmodel:${Version.KOIN}"

    // UI
    const val KRUMBS_VIEW = "com.github.adrielcafe:krumbsview:${Version.KRUMBS_VIEW}"
    const val FASTADAPTER = "com.mikepenz:fastadapter:${Version.FASTADAPTER}"
    const val FASTADAPTER_UTILS = "com.mikepenz:fastadapter-extensions-utils:${Version.FASTADAPTER}"

    val kotlin = setOf(KOTLIN, COROUTINES_CORE)
    val android = setOf(COROUTINES_ANDROID, MATERIAL, APP_COMPAT, CONSTRAINT_LAYOUT, RECYCLER_VIEW, VIEW_PAGER_2, ACTIVITY_KTX, CORE_KTX, LIFECYCLE_KTX)
    val ui = setOf(KRUMBS_VIEW, FASTADAPTER, FASTADAPTER_UTILS)
}

object TestDep {
    // Unit testing
    const val JUNIT = "junit:junit:${Version.JUNIT}"

    // Instrumentation testing
    const val RUNNER = "androidx.test:runner:${Version.RUNNER}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${Version.ESPRESSO}"

    val unitTest = setOf(JUNIT)
    val instrumentationTest = setOf(RUNNER, ESPRESSO)
}
