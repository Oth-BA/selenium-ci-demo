pipeline {
    agent any

    environment {
        IMAGE_NAME = "selenium-ci-demo"
    }

    stages {

        stage('Checkout') {
            steps {
                // Jenkins automatically checks out your repo here
                // This stage makes it explicit and visible in the UI
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running Selenium tests inside Docker...'
                sh '''
                    docker run --rm \
                        -v ${WORKSPACE}/test-results:/app/target \
                        ${IMAGE_NAME}
                '''
            }
        }

        stage('Publish Results') {
            steps {
                echo 'Publishing test results...'
                junit 'test-results/surefire-reports/*.xml'
            }
        }
    }

    post {
        success {
            echo 'Pipeline passed — all tests green.'
        }
        failure {
            echo 'Pipeline failed — check test results above.'
        }
        always {
            // Clean up the Docker image after every run
            sh 'docker rmi ${IMAGE_NAME} || true'
        }
    }
}