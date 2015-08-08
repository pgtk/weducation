#!/bin/sh

GF_HOME=$1
DOMAIN=$2
DIR_NAME="config"

if [ -d $GF_HOME ]; then
  CONFIG_PATH="$GF_HOME/glassfish/domains/$DOMAIN/$DIR_NAME"
  if [ -d $CONFIG_PATH ]; then
    echo "Removing old $DIR_NAME directory..."
    rm -rf "$DIR_NAME"
    echo "Making symbolic link..."
    ln -s "$CONFIG_PATH" "./$DIR_NAME"
  else
    echo "Path $CONFIG_PATH is not correct!\nCheck your Glassfish installation directory."
    echo "\nUsage: $0 /path/to/glassfish/installation/directory domain_name\n"
    exit 1
  fi
fi

