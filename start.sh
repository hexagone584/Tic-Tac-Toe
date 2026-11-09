#!/bin/bash

# initialization
clear
echo "Welcome to Tic-Tac-Toe!"

turns=0
player="X"

# restarts the board
echo "board" > board.txt
for i in {1..9}
do
	if !(( "$i" % 3 )); then
		echo "-" >> board.txt
	else
		echo -n "-" >> board.txt
		echo -n " " >> board.txt
	fi
done

#main logic
while [ "$turns" -lt 9 ]; do
	echo "pick a location (or enter 0 for help)"
	cat board.txt
	read location
	if [ "$location" = 0 ]; then
		clear
		echo "Below is the board and the numbered position"
		echo "board"
		for i in {1..9}
		do
			if !(( "$i" % 3 )); then
				echo "$i"
			else
				echo -n "$i"
				echo -n " "
			fi
		done
	elif [[ "$location" == [1-9] ]]; then
		placementSuccess=$(java placement.java "$location" "$player")
		if [[ $placementSuccess != "Success" ]]; then
			echo "Invalid Placement!"
			continue
		fi

		if [[ $player == "X" ]]; then
			player="O"
		else
			player="X"
		fi
		(( turns++ ))
	else
		echo "not a valid input!"
	fi
done
