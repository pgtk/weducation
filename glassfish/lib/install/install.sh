#!/bin/sh

GF_HOME=$1
DIR_NAME="applications"

if [ -d $GF_HOME ]; then
  APP_PATH="$GF_HOME/glassfish/lib/install/$DIR_NAME"
  if [ -d $APP_PATH ]; then
    echo "Removing old $DIR_NAME directory..."
    rm -rf "$DIR_NAME"
    echo "Making symbolic link..."
    ln -s "$APP_PATH" "./$DIR_NAME"
  else
    echo "Path $APP_PATH is not correct!\nCheck your Glassfish installation directory."
    echo "\nUsage: $0 /path/to/glassfish/installation/directory\n"
    exit 1
  fi
fi

