#!/usr/bin/env bash

rm -rf output
mkdir -p output/train/
mkdir -p output/test/

JAR=target/neural-nets-1.0-SNAPSHOT.jar

java -cp $JAR com.palenica.david.GenerateImages
