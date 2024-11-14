// node {
//   stage('SCM') {
//     checkout scm
//   }
//   stage('SonarQube Analysis') {
//     def mvn = tool 'maven';
//     withSonarQubeEnv() {
//       sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=DalHousing -Dsonar.projectName='DalHousing' -Dsonar.sources=src -Dsonar.host.url=http://192.168.4.30:9000 -Dsonar.login=squ_da16511a0007e20ed0aa5904449f810d6d9560af"
//     }
//   }
// }

//                sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=DalHousing -Dsonar.projectName='DalHousing' -Dsonar.sources=src -Dsonar.host.url=http://192.168.4.30:9000 -Dsonar.login=squ_da16511a0007e20ed0aa5904449f810d6d9560af"


node {
    stage('SCM') {
        checkout scm
    }
    stage('SonarQube Analysis') {
        def mvn = tool 'maven'  // Replace 'maven' with your configured Maven tool name
        dir('backend') {  // Change to the directory containing your pom.xml
            withSonarQubeEnv('sonar') {  // Replace 'SonarQube' with your configured SonarQube server name
                sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=DalHousing -Dsonar.projectName='DalHousing'"
            }
        }
    }
}



// pipeline {
//     agent any

//     tools {
//         maven 'maven'  
//         hudson.plugins.sonar.SonarRunnerInstallation 'sonar'  
//     }

//     stages {
//         stage('SCM') {
//             steps {
//                 checkout scm
//             }
//         }

//         stage('Build') {
//             steps {
//                 sh 'mvn clean install'
//             }
//         }

//         stage('SonarQube Analysis') {
//             steps {
//                 script {
//                     def scannerHome = tool 'sonar' 
//                     withSonarQubeEnv('sonar') {  
//                         sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=DalHousing -Dsonar.sources=src -Dsonar.host.url=http://192.168.4.30:9000 -Dsonar.login=squ_da16511a0007e20ed0aa5904449f810d6d9560af"
//                     }
//                 }
//             }
//         }
//     }
// }

