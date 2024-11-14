// pipeline {
//     agent any

//     tools {
//         // Specify the JDK and Maven installations
//         jdk 'java' // Replace with your JDK installation name
//         maven 'maven' // Replace with your Maven installation name.....
//     }

//     environment {
//         SONAR_TOKEN = credentials('sonar-token') // Use Jenkins credentials for SonarQube token
//     }

//     stages {
//         stage('Checkout') {
//             steps {
//                 echo 'Checking out code from the repository...'
//                 git 'https://github.com/parthmadhvani/DalHousing.git' // Uncomment this line when ready to use
//             }
//         }
        
//         stage('Build') {
//             steps {
//                 echo 'Building the project using Maven...'
//                 // sh 'mvn clean install' // Uncomment this line when ready to use
//             }
//         }

//         stage('Test') {
//             steps {
//                 echo 'Running unit tests...'
//                 // sh 'mvn test' // Uncomment this line when ready to use
//             }
//         }

//         stage('SonarQube Analysis') {
//             steps {
//                 echo 'Performing SonarQube analysis...'
//                 script {
//                     sh "mvn sonar:sonar -Dsonar.projectKey=DalHousing -Dsonar.login=${SONAR_TOKEN}"
//                 }
//             }
//         }
        
//         stage('Deploy') {
//             steps {
//                 echo 'Deploying application...'
//                 // Add your deployment commands here (if applicable)
//             }
//         }
//     }

//     post {
//         // Actions to perform after the pipeline runs
//         always {
//             echo 'Cleaning up...'
//         }
//         success {
//             echo 'Build succeeded!'
//         }
//         failure {
//             echo 'Build failed!'
//         }
//     }
// }.

pipeline {
    agent any

    tools {
        maven 'maven'  
        hudson.plugins.sonar.SonarRunnerInstallation 'sonar'  
    }

    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'sonar' 
                    withSonarQubeEnv('sonar') {  
                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=DalHousing -Dsonar.sources=src -Dsonar.host.url=http://192.168.4.30:9000 -Dsonar.login=squ_da16511a0007e20ed0aa5904449f810d6d9560af"
                    }
                }
            }
        }
    }
}

