sed -i "s/LIVE/$GITHUB_SHA/g" pom.xml
mvn -q -B -s maven-settings.xml clean compile assembly:single deploy
mvn help:evaluate -Dexpression=project.version -q -DforceStdout