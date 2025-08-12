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

        stage('Deploy to Argo') {
            steps {
                sh '''
                ls
                rm -rf Java-App-ArgoCD
                
                git clone git@github.com:diaaqassem/Java-App-ArgoCD.git
                git checkout fp
                pwd
                ls
                cd Java-App-ArgoCD
                pwd
                ls
                sed -i "s|image: .*|image: ${IMAGE_NAME}:v${IMAGE_TAG}|" deployment.yml

                git add .
                git commit -m "update image"
                git push
                '''
                }
            }
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

// test 