def call(String imageName, String buildNumber) {
    stage("Docker Build") {
        sh "docker build -t ${imageName}:v${buildNumber} ."
        sh "docker images"
    }
}
