plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

val buildDirLocation = File(System.getProperty("user.home"), "android-builds/${rootProject.name}")
allprojects {
    layout.buildDirectory.set(File(buildDirLocation, name))
}

