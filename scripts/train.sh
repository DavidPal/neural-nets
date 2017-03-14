#!/usr/bin/env bash

rm -rf output

JAR=target/neural-nets-1.0-SNAPSHOT.jar

java -cp $JAR com.palenica.david.TrainModel
