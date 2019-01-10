#!/bin/bash

init_emulator () {
    echo no | android create avd --force -n test -t android-22 --abi armeabi-v7a
    emulator -avd test -no-skin -no-audio -no-window &
    android-wait-for-emulator
    adb shell input keyevent 82 &
    adb shell setprop dalvik.vm.dexopt-flags v=n,o=v
}

DEPLOY_COMMAND="[ci deploy]"

# get current directory name
BASEDIR=`dirname $0`
# assumes that next gradle commands will execute in basedir/..
# put current dir in the stack and move current working dir to basedir/..
# stack -> ./scripts ---- /.../.../px-android/
command pushd "$BASEDIR/.." > /dev/null

# Get last 2 commit messages (1 is the merge, second last real commit) in a variable
if [ $TRAVIS_PULL_REQUEST != "false" ]
then
    LAST_GIT_COMMIT=$(git log -2 --pretty=%B)
    BRANCH_TO_TAG=$TRAVIS_PULL_REQUEST_BRANCH
else
    LAST_GIT_COMMIT=$(git log -1 --pretty=%B)
    BRANCH_TO_TAG=$TRAVIS_BRANCH
fi

if [[ $LAST_GIT_COMMIT == *$DEPLOY_COMMAND* ]] && [[ $BRANCH_TO_TAG == "release/"* ]]
then
    init_emulator
    # Load properties
    . gradle.properties
    # Running task and tagging
    ./gradlew :px-checkout:jacocoTestReport && ./gradlew -Pproduction publishAar -q && git remote add origin-travis https://$GITHUB_AUTH_TOKEN@github.com/mercadopago/px-android.git && git fetch origin-travis && git checkout -b $BRANCH_TO_TAG origin-travis/$BRANCH_TO_TAG && git tag -a $version_to_deploy -m "travis deployed version $version_to_deploy" && git push --tags
else
    ./gradlew :px-checkout:test && ./gradlew publishAar -q
fi
