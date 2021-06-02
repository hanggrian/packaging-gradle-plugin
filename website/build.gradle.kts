plugins {
    `git-publish`
}

gitPublish {
    repoUri.set(RELEASE_GITHUB)
    branch.set("gh-pages")
    contents.from("../$RELEASE_ARTIFACT/build/dokka/html")
}

tasks {
    gitPublishCopy {
        dependsOn(":$RELEASE_ARTIFACT:dokkaHtml")
    }
}