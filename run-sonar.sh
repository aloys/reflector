#!/usr/bin/env bash

mvn sonar:sonar \
  -Dsonar.organization=aloys-github \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=a0fac7539bc5cf6c607d820375e12900a60fd713 \
  -s ./settings_local.xml
