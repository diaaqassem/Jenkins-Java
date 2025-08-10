def call() {
    stage("Archive App") {
        archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
    }
}
