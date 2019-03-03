#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }
    def isMaster = env.BRANCH_NAME

    gitlabCommitStatus('build') {
        docker.image('jhipster/jhipster:latest').inside('-u root -e MAVEN_OPTS="-Duser.home=./"') {

            stage('clean') {
                sh "chmod +x mvnw"
                sh "./mvnw clean"
            }

            stage('install tools') {
                sh "./mvnw com.github.eirslett:frontend-maven-plugin:install-node-and-npm -DnodeVersion=v10.15.0 -DnpmVersion=6.4.1"
            }

            stage('npm install') {
                sh "./mvnw com.github.eirslett:frontend-maven-plugin:npm"
            }

            stage('backend tests') {
                try {
                    sh "./mvnw test"
                } catch (err) {
                    throw err
                } finally {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }

            stage('frontend tests') {
                try {
                    sh "./mvnw com.github.eirslett:frontend-maven-plugin:npm -Dfrontend.npm.arguments='run test'"
                } catch (err) {
                    throw err
                } finally {
                    junit '**/target/test-results/TESTS-*.xml'
                }
            }
            if (isMaster) {
                stage('package and deploy') {
                    sh "./mvnw com.heroku.sdk:heroku-maven-plugin:2.0.5:deploy -DskipTests -Pprod -Dheroku.buildpacks=heroku/jvm -Dheroku.appName=flatterservermonolith"
                    archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
                }
            }
        }
    }
}
