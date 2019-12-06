sed -i "s/LIVE/$GITHUB_SHA/g" pom.xml
mvn -q -B -s maven-settings.xml clean compile assembly:single deploy

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

# supports #major, #minor, #patch (anything else will be 'minor')
case "$log" in
    *#major* ) new=$(semver bump major $t);;
    *#patch* ) new=$(semver bump patch $t);;
    *#minor* ) new=$(semver bump minor $t);;
esac

# get current commit hash
commit=$(git rev-parse HEAD)

git tag -a $new -m "new version $new" $commit

git show $new