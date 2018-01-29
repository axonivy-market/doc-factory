@Library('ivy-jenkins-shared-libraries') _

pipeline {
  agent {
    docker {
      image 'maven:3.5.2-jdk-8'
    }
  }
  stages {
    stage('build') {
      steps {
        script {
          maven cmd: 'clean install'
        }
      }
    }
  }
}
