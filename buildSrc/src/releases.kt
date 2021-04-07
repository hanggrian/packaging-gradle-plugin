const val RELEASE_USER = "hendraanggrian"
const val RELEASE_GROUP = "com.$RELEASE_USER"
const val RELEASE_ARTIFACT = "packr"
const val RELEASE_VERSION = "0.1-SNAPSHOT"
const val RELEASE_DESCRIPTION = "Start making native distributions for your JAR"
const val RELEASE_URL = "https://github.com/$RELEASE_USER/$RELEASE_ARTIFACT"

fun getReleaseSourceUrl(project: String = RELEASE_ARTIFACT) =
    `java.net`.URL("$RELEASE_URL/tree/main/$project/src")