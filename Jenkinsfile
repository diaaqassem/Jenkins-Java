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
// //         success {
// //             echo 'Deployment completed successfully!'
// //         }
// //         failure {
// //             echo 'Deployment failed.'
// //         }
// //     }
// // }
// // // Test Trigger



// // Shared Lib
// @Library('shared-lib')_

// pipeline {
//     agent any
//     tools {
//         git 'git-1.9'
//         maven 'M3'
//     }
//     environment {
//         DOCKER_USER = credentials('docker-username')
//         DOCKER_PASS = credentials('docker-password')
//         IMAGE_NAME = 'diaaqassem1/app-java'
//     }
//     stages {
//         stage('Build') {
//             steps {
//                 script {
//                     def mavenBuild = new org.iti.mvn()
//                     mavenBuild.javaBuild("package install")
//                 }
//             }
//         }
//         stage('Archive') {
//             steps {
//                 script {
//                     def archiveApp = new org.iti.archiveApp()
//                     archiveApp.archive()
//                 }
//             }
//         }
//         stage('Docker Build') {
//             steps {
//                 script {
//                     def dockerBuildApp = new org.iti.dockerBuild()
//                     dockerBuildApp.dockerBuild(IMAGE_NAME, env.BUILD_NUMBER)
//                 }
//             }
//         }
//         stage('Docker Push') {
//             steps {
//                 dockerPush(IMAGE_NAME, env.BUILD_NUMBER, env.DOCKER_USER, env.DOCKER_PASS)
//             }
//         }
//     }
//     post {
//         always {
//                script {
//                     def slackNotify = new org.iti.slackNotify()
//                     slackNotify.slack(env.JOB_NAME, env.BUILD_NUMBER, env.BUILD_URL, currentBuild.currentResult)
//                 }
//         }
//         success {
//             echo 'Deployment completed successfully!'
//         }
//         failure {
//             echo 'Deployment failed.'
//         }
//     }
// }




// using ECR For final Project

@Library('shared-lib')_

pipeline {

    agent { 
        label 'worker' 
        }

    tools {
        git 'git-1.9'
        maven 'M3'
    }
    environment {
        AWS_REGION = "us-east-1"
        ECR_REPO = 'iti'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        IMAGE_NAME = "485492729952.dkr.ecr.us-east-1.amazonaws.com/${ECR_REPO}"
    }
    stages {
        stage('Build') {
            steps {
                script {
                    def mavenBuild = new org.iti.mvn()
                    mavenBuild.javaBuild("package install")
                }
            }
        }
        stage('Archive') {
            steps {
                script {
                    def archiveApp = new org.iti.archiveApp()
                    archiveApp.archive()
                }
            }
        }
        stage('ECR Login') {
            steps {
                script {
                    sh """
                    aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${IMAGE_NAME}
                    """
                }
            }
        }
        stage('Docker Build & Push to ECR') {
            steps {
                script {
                    def dockerBuildApp = new org.iti.dockerBuild()
                    dockerBuildApp.dockerBuild(IMAGE_NAME, IMAGE_TAG)
                    def dockerPushApp = new org.iti.dockerPush()
                    dockerPushApp.dockerPush(IMAGE_NAME, IMAGE_TAG)
                }
            }
        }
        // stage('Deploy to Kubernetes') {
        //     steps {
        //         withCredentials([file(credentialsId: env.KUBECONFIG_CRED, variable: 'KUBECONFIG_FILE')]) {
        //             sh """
        //             export KUBECONFIG=${KUBECONFIG_FILE}

        //             kubectl apply -f - <<EOF
        //             apiVersion: apps/v1
        //             kind: Deployment
        //             metadata:
        //               name: app-java
        //               labels:
        //                 app: app-java
        //             spec:
        //               replicas: 2
        //               selector:
        //                 matchLabels:
        //                   app: app-java
        //               template:
        //                 metadata:
        //                   labels:
        //                     app: app-java
        //                 spec:
        //                   containers:
        //                   - name: app-java
        //                     image: ${IMAGE_NAME}:${IMAGE_TAG}
        //                     ports:
        //                     - containerPort: 8080
        //             EOF

        //             kubectl apply -f - <<EOF
        //             apiVersion: v1
        //             kind: Service
        //             metadata:
        //               name: app-java-service
        //             spec:
        //               selector:
        //                 app: app-java
        //               ports:
        //               - protocol: TCP
        //                 port: 80
        //                 targetPort: 8080
        //               type: LoadBalancer
        //             EOF
        //             """
        //         }
        //     }
        // }
    }
    post {
        always {
            script {
                def slackNotify = new org.iti.slackNotify()
                slackNotify.slack(env.JOB_NAME, env.BUILD_NUMBER, env.BUILD_URL, currentBuild.currentResult)
            }
        }
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}