FROM ubuntu:latest

MAINTAINER Mark Silvis "marksilvis@pitt.edu"

# Install dependencies
RUN \
  apt-get update -y && \
  apt-get install -y \
  git

ENV DEBIAN_FRONTEND noninteractive

# Install Java
RUN \
  apt-get install software-properties-common -y && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update -y && \
  echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-intaller

# Define JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Fetch CCD repos
RUN \
  mkdir -p lib/db lib/security/oauth2 && \
  git clone -b v0.6.0-anno git://github.com/bd2kccd/ccd-db.git lib/db/ && \
  git clone -b v1.1.1 git://github.com/bd2kccd/ccd-security-oauth2.git lib/security/oauth2

# Build
ONBUILD ./gradlew build

# Run
ONBUILD ./gradlew bootRun