pipeline{
    agent any
    options {
        disableConcurrentBuilds()
        timeout(time: 1, unit: "HOURS")
    }
    environment {
        MVN_SETTING_PROVIDER = "3ec57b41-efe6-4628-a6c7-8be5f1c26d77"
        COMPONENT = "projet-isa-devops-20-team-b-20-entities"
    }
    stages {
        stage("Compile") {
            steps {
                configFileProvider([configFile(fileId: MVN_SETTING_PROVIDER, variable: "MAVEN_SETTINGS")]) {
					echo "Compile module"
					sh "mvn -s $MAVEN_SETTINGS clean compile"
                }
            }
        }
		stage("Tests") {
			steps {
				echo "Unit tests module"
				sh "mvn test"
			}
		}
        stage('Mutations') {
            steps {
                echo 'PiTest Mutation'
                sh 'mvn org.pitest:pitest-maven:mutationCoverage'
            }
        }
		stage("Deploy") {
			steps {
				configFileProvider([configFile(fileId: MVN_SETTING_PROVIDER, variable: "MAVEN_SETTINGS")]) {
                    echo "Deployment into artifactory"
                    sh "mvn -s $MAVEN_SETTINGS deploy"
				}
			}
		}
        stage("Check Dependencies") {
            when {
                expression { env.GIT_BRANCH == 'develop' }
            }
            steps {
                build job: 'projet-isa-devops-20-team-b-20-drone-delivery/develop', parameters: [booleanParam(name: 'UPDATE_BUILD', value: true)], wait: false
            }
        }
        stage('Sonarqube') {
            steps {
                withSonarQubeEnv('Sonarqube_env') {
                    echo 'Sonar Analysis'
                    sh 'mvn package sonar:sonar -Dsonar.pitest.mode=reuseReport'
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate true
                }
                echo 'passed'
            }
        }
        stage('Update snapshot dependencies') {
            when { not { branch 'master' } }
            steps {
                script {
                    def components = ['projet-isa-devops-20-team-b-20-shipment-component']
                    for (int i = 0; i < components.size(); ++i) {
                        echo "Check dependency on ${components[i]}"
                        build job: "${components[i]}/develop",
                            parameters: [string(name: 'DEPENDENCY', value: "${COMPONENT}"),
                            string(name: 'VERSION', value: 'snapshot')],
                            propagate: false,
                            wait: false
                    }
                }
            }
        }
    }
    post{
        always {
            archiveArtifacts artifacts: 'target/**/*', fingerprint: true
            junit 'target/surefire-reports/*.xml'
            echo '======== pipeline archived ========'
        }
        success {
            slackSend(
            channel: 'projet-isa-devops-ci',
            notifyCommitters: true,
            failOnError: true,
            color: 'good',
            token: env.SLACK_TOKEN,
            message: 'Job: ' + env.JOB_NAME + ' with buildnumber ' + env.BUILD_NUMBER + ' was successful',
            baseUrl: env.SLACK_WEBHOOK)
            echo "======== pipeline executed successfully ========"
        }
        failure {
            slackSend(
            channel: 'projet-isa-devops-ci',
            notifyCommitters: true,
            failOnError: true,
            color: 'danger',
            token: env.SLACK_TOKEN,
            message: 'Job: ' + env.JOB_NAME + ' with buildnumber ' + env.BUILD_NUMBER + ' was failed',
            baseUrl: env.SLACK_WEBHOOK)
            echo "======== pipeline execution failed========"
        }
    }
}
