#!/usr/bin/env bash
sudo apt-get install -y software-properties-common
sudo add-apt-repository -y ppa:webupd8team/java && apt-get update
echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections
echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 seen true" | debconf-set-selections
sudo apt-get install -y oracle-java8-installer
sudo apt install -y oracle-java8-set-default