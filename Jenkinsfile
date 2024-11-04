pipeline {
    agent any

    tools {
        // Specify the JDK and Maven installations
        jdk 'java' // Replace with your JDK installation name
        maven 'maven' // Replace with your Maven installation name.....
    }

    environment {
        SONAR_TOKEN = credentials('sonar-token') // Use Jenkins credentials for SonarQube token
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from the repository...'
                // git 'https://github.com/your-repo/your-java-project.git' // Uncomment this line when ready to use
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the project using Maven...'
                // sh 'mvn clean install' // Uncomment this line when ready to use
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                // sh 'mvn test' // Uncomment this line when ready to use
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Performing SonarQube analysis...'
                script {
                    sh "mvn sonar:sonar -Dsonar.projectKey=DalHousing -Dsonar.login=${SONAR_TOKEN}"
                }
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                // Add your deployment commands here (if applicable)
            }
        }
    }

    post {
        // Actions to perform after the pipeline runs
        always {
            echo 'Cleaning up...'
        }
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
