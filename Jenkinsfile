node {
    stage('SCM') {
        checkout scm
    }
    stage('SonarQube Analysis') {
        def mvn = tool 'maven' 
        dir('backend') {  
            withSonarQubeEnv('sonar') { 
                sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=DalHousing -Dsonar.projectName='DalHousing'"
            }
        }
    }
}

