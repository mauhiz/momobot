@echo off
mvn -Dmaven.artifact.threads=15 -U dependency:sources clean compile