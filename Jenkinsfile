// def COLOR_MAP = [
//     'SUCCESS': 'good',
//     'FAILURE': 'danger'
// ]

// pipeline{
//     agent any
//     tools{
//         git 'git-1.9'
//         maven 'M3'  
//     }
//     environment{
//         DOCKER_USER = credentials('docker-username')
//         DOCKER_PASS = credentials('docker-password')
//     }
//     stages{
//         // stage("Dependancy check"){
//         //     steps{
//         //         sh "mvn dependency-check:check"
//         //         dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
//         //     }
//         // }
//         stage("build app"){
//             steps{
//                 sh "mvn package install"
//             }
//         }
//         stage("archive app"){
//             steps{
//                 archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
//             }
//         }
//         stage("docker build") {
//             steps {
//                 sh "docker build -t diaaqassem1/app-java:v${BUILD_NUMBER} ."
//                 sh "docker images"
//             }
//         }
//         stage("docker push") {
//             steps {
//                 sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
//                 sh "docker push diaaqassem1/app-java:v${BUILD_NUMBER}"
//             }
//         }
//     }

//   post {
//         always {
//             echo 'Slack Notifications.'
//             slackSend channel: '#devops',
//                       color: COLOR_MAP[currentBuild.currentResult],
//                       message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \nMore info at: ${env.BUILD_URL}"
//         }
//         success {
//             echo 'Deployment completed successfully!'
//         }
//         failure {
//             echo 'Deployment failed.'
//         }
//     }
// }
// // Test Trigger



// Shared Lib
@Library('shared-lib')
pipeline {
    agent any
    tools {
        git 'git-1.9'
        maven 'M3'
    }
    environment {
        DOCKER_USER = credentials('docker-username')
        DOCKER_PASS = credentials('docker-password')
        IMAGE_NAME = 'diaaqassem1/app-java'
    }
    stages {
        stage('Build') {
            steps {
                mvn.javaBuild("package install")
            }
        }
        stage('Archive') {
            steps {
                archiveApp()
            }
        }
        stage('Docker Build') {
            steps {
                dockerBuild(IMAGE_NAME, env.BUILD_NUMBER)
            }
        }
        stage('Docker Push') {
            steps {
                dockerPush(IMAGE_NAME, env.BUILD_NUMBER, env.DOCKER_USER, env.DOCKER_PASS)
            }
        }
    }
    post {
        always {
            slackNotify(env.JOB_NAME, env.BUILD_NUMBER, env.BUILD_URL, currentBuild.currentResult)
        }
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}
