pipeline {
    agent any
    tools {
      maven 'MAVEN_HOME'
    }
    stages {
        stage('checkout') {
            steps {
            //    checkout scm
                echo 'Hello World'
            }
        }

        stage('build and test') {
            steps {
                echo 'build and test'
                sh "chmod +x -R ${env.WORKSPACE}"
                sh './build.sh'
            }
        }

        stage('dockerize application') {
            steps {
                echo 'build application image'
                sh "chmod +x -R ${env.WORKSPACE}"
                sh './buildImage.sh'
            }
        }

        stage('deploy') {
            steps {
                echo 'deploy'
            }
        }
    }
}