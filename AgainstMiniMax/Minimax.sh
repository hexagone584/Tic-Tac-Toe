#!/bin/bash

case "$1" in
    "init")
        StatTable=$2
        printf -- "Trial Number,Who Won,Menace Starting,Number of Turns\n" > "$StatTable"
        echo "Menace vs Minimax"
		javac -d . $(find . -name "MiniMaxBot.java")

        ;;
    "play")
        ActualBoard=$2
        Placement=$3
        player=$4

        while [[ "$minimaxPlacementStatus" !=  "Success" ]]; do
			#minimax
			minimaxPlacement=$(java "MiniMaxBot" "$(cat $ActualBoard)")
			minimaxPlacementStatus=$(java "$Placement" "$minimaxPlacement" "$player" "$ActualBoard")
			#this shouldn't happen, but it's a safe guard
			if [[ $minimaxPlacementStatus != "Success" ]]; then
				echo "minimax is playing wrong!"
				echo "currentBoard:"
				echo $ActualBoard
				exit 1
			fi
        done
        ;;
esac