pipeline {
  agent any

  triggers {
    cron '0 20 * * *'
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '2'))
  }

  stages {
    stage('build') {
      steps {
        script {
          def random = (new Random()).nextInt(10000000)
          def networkName = "build-" + random
          def seleniumName = "selenium-" + random
          def ivyName = "ivy-" + random
          sh "docker network create ${networkName}"
          try {
            docker.image("selenium/standalone-firefox:3").withRun("-e START_XVFB=false --shm-size=2g --name ${seleniumName} --network ${networkName}") {
              docker.build('maven', ".").inside("--name ${ivyName} --network ${networkName}") {
                def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
                maven cmd: "clean ${phase} -Dmaven.test.failure.ignore=true -Divy.compiler.warnings=false -Dtest.engine.url=http://${ivyName}:8080 -Dselenide.remote=http://${seleniumName}:4444/wd/hub"
              }
            }
          } finally {
            sh "docker network rm ${networkName}"
          }
          archiveArtifacts '**/target/*.iar'
          archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
          junit '**/target/*-reports/**/*.xml'
        }
      }
    }

    stage('doc') {
      steps {
        script {
          def currentVersion = 'dev';
          currentVersion = getCurrentVersion();
          docker.image('axonivy/build-container:read-the-docs-2').inside {
            sh "make -C /doc-build html BASEDIR='${env.WORKSPACE}/doc' VERSION='${currentVersion}'"
          }
          archiveArtifacts 'doc/build/html/**/*'
          recordIssues tools: [eclipse(), sphinxBuild()], unstableTotalAll: 1
        
          echo 'deploy doc'
          docker.image('maven:3.6.3-jdk-11').inside {            
            def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
            maven cmd: "-f doc/pom.xml clean ${phase}"
          }
          archiveArtifacts 'doc/target/*.zip'
        }
      }
    }
  }
}

def getCurrentVersion() {
  docker.image('maven:3.6.3-jdk-11').inside { 
    def cmd = "mvn -f maven/pom.xml  help:evaluate -Dexpression=revision -q -DforceStdout"
    def value = sh (script: cmd, returnStdout: true)
    echo "version is $value"
    if (value == "null object or invalid expression") {
      throw new Exception("could not evaluate maven revision property");
    }
    return value
  }
}
