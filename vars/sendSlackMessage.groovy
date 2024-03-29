#!/usr/bin/env groovy

import groovy.json.JsonOutput

def sendSlackMessage() {
	jenkins_image = ":jenkins:"
	beer_image = ":beer:"
	long epoch = System.currentTimeMillis()/1000
	def BUILD_COLORS = ['SUCCESS': 'good', 'FAILURE': 'danger', 'UNSTABLE': 'danger', 'ABORTED': 'danger']
  
	def slack = JsonOutput.toJson(
    	[
            icon_emoji: jenkins_image,
            attachments: [[
              title: "Jenkins Job Alert - ${currentBuild.currentResult}",
              text:  "Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}  ${beer_image}\n Details at: ${env.BUILD_URL}console",
              fallback: "ICDC Jenkins Build",
              color: "${BUILD_COLORS[currentBuild.currentResult]}",
              footer: "icdc devops",
              ts: epoch,
              mrkdwn_in: ["footer", "title"],
           ]]
        ]
    )
    try {
        sh "curl -X POST -H 'Content-type: application/json' --data '${slack}'  '${env.SLACK_URL}'"
    } catch (err) {
        echo "${err} Slack notify failed"
    }
}

