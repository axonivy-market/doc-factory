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
          maven cmd: '-s settings.xml -Divy.engine.list.url=http://zugprobldmas/job/Trunk_All/lastSuccessfulBuild/ -Divy.engine.directory=/tmp/ivyEngine clean deploy'
        }
      }
    }
  }
}
