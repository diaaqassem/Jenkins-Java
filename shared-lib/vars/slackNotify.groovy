def call(String jobName, String buildNumber, String buildUrl, String buildResult) {
    def COLOR_MAP = [
        'SUCCESS': 'good',
        'FAILURE': 'danger'
    ]

    slackSend channel: '#devops',
              color: COLOR_MAP[buildResult],
              message: "*${buildResult}:* Job ${jobName} build ${buildNumber} \nMore info at: ${buildUrl}"
}
