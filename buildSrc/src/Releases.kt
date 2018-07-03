const val RELEASE_USER = "hendraanggrian"
const val RELEASE_ARTIFACT = "packr-gradle-plugin"
val RELEASE_GROUP = "com.$RELEASE_USER.${RELEASE_ARTIFACT.substringBefore('-')}"
const val RELEASE_VERSION = "0.1"
const val RELEASE_DESC = "Start making native distributions for your JAR"
const val RELEASE_WEBSITE = "https://github.com/$RELEASE_USER/$RELEASE_ARTIFACT"

val bintrayUserEnv = System.getenv("BINTRAY_USER")
val bintrayKeyEnv = System.getenv("BINTRAY_KEY")
