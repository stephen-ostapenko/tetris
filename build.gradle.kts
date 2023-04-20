plugins {
    id("java")
}

group = "osss.tetris"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("org.jogamp.gluegen:gluegen:2.3.1")
    implementation("org.jogamp.gluegen:gluegen-rt:2.3.1")
    implementation("org.jogamp.gluegen:gluegen-rt-main:2.3.1")

    implementation("org.jogamp.jogl:jogl:2.3.1:")
    implementation("org.jogamp.jogl:jogl-all:2.3.1")
    implementation("org.jogamp.jogl:jogl-main:2.3.1")
    implementation("org.jogamp.jogl:jogl-all-main:2.3.1")

    implementation("org.jogamp.jogl:jogl:2.3.1:natives-windows-amd64")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-windows-amd64")
    implementation("org.jogamp.jogl:jogl:2.3.1:natives-windows-i586")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-windows-i586")

    implementation("org.jogamp.jogl:jogl:2.3.1:natives-linux-amd64")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-linux-amd64")
    implementation("org.jogamp.jogl:jogl:2.3.1:natives-linux-i586")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-linux-i586")
    implementation("org.jogamp.jogl:jogl:2.3.1:natives-linux-armv6")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-linux-armv6")
    implementation("org.jogamp.jogl:jogl:2.3.1:natives-linux-armv6hf")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-linux-armv6hf")

    implementation("org.jogamp.jogl:jogl:2.3.1:natives-solaris-amd64")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-solaris-amd64")
    implementation("org.jogamp.jogl:jogl:2.3.1:natives-solaris-i586")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-solaris-i586")

    implementation("org.jogamp.jogl:jogl:2.3.1:natives-macosx-universal")
    implementation("org.jogamp.jogl:jogl-all:2.3.1:natives-macosx-universal")

    // implementation("org.jogamp.jogl:jogl-all-noawt:2.3.1")
    // implementation("org.jogamp.jogl:jogl-all-noawt-main:2.3.1")

    implementation("org.jogamp.jogl:newt:2.3.1")
    implementation("org.jogamp.jogl:newt-main:2.3.1")

    implementation("org.jogamp.jogl:nativewindow:2.3.1")
    implementation("org.jogamp.jogl:nativewindow-main:2.3.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}