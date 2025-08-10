def call() {
    stage("Build App") {
        sh "mvn package install"
    }
}
