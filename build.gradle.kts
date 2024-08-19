val releaseGroup: String by project
val releaseVersion: String by project

allprojects {
    group = releaseGroup
    version = releaseVersion
}
