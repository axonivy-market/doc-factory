pipeline {
  agent any

  triggers {
    pollSCM 'H/5 * * * *'
    cron '0 20 * * *'
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '2'))
  }

  stages {
    stage('build') {
      steps {
        script {
          docker.withRegistry('', 'docker.io') {
            docker.image('axonivy/build-container:web-1.0').inside {
              def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
              maven cmd: "clean ${phase} -Dmaven.test.failure.ignore=true " +
                "-Dproject-build-plugin-version=9.1.0 -Divy.compiler.warnings=false "
            }
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
          docker.withRegistry('', 'docker.io') {
            def currentVersion = 'dev';
            currentVersion = getCurrentVersion();
            docker.image('axonivy/build-container:read-the-docs-1.2').inside {
              sh "make -C /doc-build html BASEDIR='${env.WORKSPACE}/doc-factory/doc' VERSION='${currentVersion}'"
            }
            archiveArtifacts 'doc-factory/doc/build/html/**/*'
            recordIssues tools: [eclipse(), sphinxBuild()], unstableTotalAll: 1
        
            echo 'deploy doc'
            docker.image('maven:3.6.3-jdk-11').inside {            
              def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
              maven cmd: "-f doc-factory/doc/pom.xml clean ${phase}"
            }
            archiveArtifacts 'doc-factory/doc/target/*.zip'
          }
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
