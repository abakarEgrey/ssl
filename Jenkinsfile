pipeline {
    agent any
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
                sh './build.sh'
            }
        }

        stage('deploy') {
            steps {
                echo 'deploy'
            }
        }
    }
}