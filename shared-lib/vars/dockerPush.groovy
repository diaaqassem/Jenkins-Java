def call(String imageName, String buildNumber, String dockerUser, String dockerPass) {
    stage("Docker Push") {
        sh "echo ${dockerPass} | docker login -u ${dockerUser} --password-stdin"
        sh "docker push ${imageName}:v${buildNumber}"
    }
}
