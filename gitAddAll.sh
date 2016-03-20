#!/bin/bash

paths=("core/src/*" "core/pom.xml" "data/*" "membernet-utils/src/*" "membernet-utils/pom.xml" "ui/src/*" "ui/pom.xml" "pom.xml" "gitAddAll.sh")

for item in ${paths[*]}
do
	printf "git add --all %s\n" $item
	git add --all $item
done