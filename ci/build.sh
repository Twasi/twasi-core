# get current version
t=$(git describe --tags `git rev-list --tags --max-count=1`)

# if we have a blank tag, get all commits
if [ -z "$t" ]
then
  log=$(git log --pretty=oneline)
  t=0.0.0
# otherwise, get commits since last tag
else
  log=$(git log $t..HEAD --pretty=oneline)
fi

echo Version before: $t

# supports #major, #minor, #patch (anything else will be ignored)
case "$log" in
    *#major* ) new=$(bash ci/semver.sh bump major $t);;
    *#patch* ) new=$(bash ci/semver.sh bump patch $t);;
    *#minor* ) new=$(bash ci/semver.sh bump minor $t);;
esac

if [ -z "$new" ]
then
  echo No new version found, not tagging anything. Build version will be: $t-unstable
  new=$t-unstable
else
  git config --global user.email "info@twasi.net"
  git config --global user.name "Twasi Team"
  git tag -a $new -m "new version $new"
fi

mvn versions:set -DnewVersion=$new
mvn -q -B --no-transfer-progress -s maven-settings.xml clean compile assembly:single deploy

# Tag only after build completed so we do not have a possibly broken version
git push "https://${GITHUB_ACCOUNT}:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git" --tags;