def gate = ""
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
                catchError(buildResult: "SUCCESS", stageResult: "FAILURE") {
                    timeout(time: 1, unit: "HOURS") {
                        waitForQualityGate true
                    }
                }
            }
            post{
                success {
                    script {
                        gate = "\n - Quality gate was successful"
                    }
                }
                failure {
                    script {
                        gate = "\n - Quality gate was failed"
                    }
                }
            }
        }
        stage('Update snapshot dependencies') {
            environment {
                CURRENT_VERSION = '''${sh(
                                    returnStdout: true,
                                    script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout"
                                )}'''
            }
            when { not { branch 'master' } }
            steps {
                script {
                    def components = ['projet-isa-devops-20-team-b-20-shipment-component','projet-isa-devops-20-team-b-20-schedule-component','projet-isa-devops-20-team-b-20-drone-park-component','projet-isa-devops-20-team-b-20-warehouse-component']
                    for (int i = 0; i < components.size(); ++i) {
                        echo "Check dependency on ${components[i]}"
                        build job: "${components[i]}/develop",
                            parameters: [string(name: 'DEPENDENCY', value: "${COMPONENT}"),
                            string(name: 'VERSION', value: "${CURRENT_VERSION}"),
                            string(name: 'TYPE', value: 'snapshot')],
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
            message: 'Job: ' + env.JOB_NAME + ' with buildnumber ' + env.BUILD_NUMBER + ' was successful' + gate,
            baseUrl: env.SLACK_WEBHOOK)
            echo "======== pipeline executed successfully ========"
            sh 'mvn versions:commit'
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
            sh 'mvn versions:revert'
        }
    }
}
