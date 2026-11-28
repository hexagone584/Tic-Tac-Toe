#!/bin/bash

case "$1" in
    "init")
        StatTable=$2
        printf -- "Trial Number,Who Won,Menace Starting,Number of Turns\n" > "$StatTable"
        echo "Menace vs Random"
        ;;
    "play")
        ActualBoard=$2
        Placement=$3
        player=$4

        while [[ "$placementSuccess" !=  "Success" ]]; do
            #random bot
            randomPlacement=$(( $RANDOM % 9 + 1 ))
            placementSuccess=$(java "$Placement" "$randomPlacement" "$player" "$ActualBoard")
            if [[ "$placementSuccess" != "Success" ]]; then
                continue
            fi
        done
        ;;
esac