plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

sourceSets {
    main {
        java.srcDirs += File("src/main/kotlin/")
    }
}