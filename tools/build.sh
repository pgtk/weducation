#!/bin/bash

##############################################################################################
# Script for automatically building project and deploy to glassfish under Linux
# Created by Voronin Leonid
##############################################################################################

PROJECT="weducation"
MODULE="webui"
WARFILE="weducation-webui.war"
GIT="/usr/bin/git"
MAVEN="/opt/maven/bin/mvn"
GF_DIR="/opt/glassfish"
DOMAIN="pgtk"
DEPLOY_DIR="$GF_DIR/glassfish/domains/$DOMAIN/autodeploy/"
ASADMIN="$GF_DIR/bin/asadmin"
JAVA="/opt/java"


echo "Building $PROJECT"
export JAVA_HOME="$JAVA"
export JRE_HOME="$JAVA/jre"
ROOT=`pwd`
cd $PROJECT
PROJECT_ROOT=`pwd`
$GIT checkout stable
$GIT pull origin stable
$MAVEN clean package
RC=$?;

if [[ $RC -eq 0 ]]; then
  echo "Build $PROJECT success!"
  # Deploy to Glassfish
  APP="$PROJECT_ROOT/$MODULE/target/$WARFILE"
  if [[ -r "$APP" ]]; then
    echo "Deploing $APP  to glassfish..."
    $ASADMIN --user admin --passwordfile "$ROOT/asadmin" deploy --force=true $APP
#    if [[ -d $DEPLOY_DIR ]]; then
#      cp -u "$APP" $DEPLOY_DIR
#    else
#      echo "Directory $DEPLOY_DIR does not exist!"
#      exit 1;
    fi
  fi
  exit 0;
else
  echo "Build $PROJECT failed (code $RC)!"
  exit $RC;
fi
exit 0