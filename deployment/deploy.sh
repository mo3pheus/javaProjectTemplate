#!/usr/bin/env bash

java -jar target/news.service-1.0-SNAPSHOT-shaded.jar --debug.logging true --news.frequency.hours 2 --extra.argument randomString
