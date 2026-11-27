#!/bin/bash
wantsToQuit=0
#main logic
while [[ "$wantsToQuit" -eq 0 ]]; do
	# initialization
	clear
	echo "Welcome to Tic-Tac-Toe!"
	echo "you will play with an AI called MENACE (it goes first)"
	echo "good luck"
	echo 

	turns=0
	player="X"
	winVerification="N"

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

	while [ "$turns" -lt 9 ]; do
		#menace will play first
		if [[ "$player" == "X" ]]; then
			if [[ "$turns" -eq 0 ]]; then
				menacePlacement=$(java MENACE.java "$(cat board.txt)" "start")
			else
				menacePlacement=$(java MENACE.java "$(cat board.txt)" "play")
			fi
			
			menacePlacementStatus=$(java Placement.java "$menacePlacement" "$player")
			player="O"
		else
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
				placementSuccess=$(java Placement.java "$location" "$player")
				if [[ $placementSuccess != "Success" ]]; then
					echo "Invalid Placement!"
					continue
				fi
				player="X"
			else
				echo "not a valid input!"
			fi
		fi

		winVerification=$(java WinVerify.java)

		case "$winVerification" in
			"X")
				cat board.txt
				echo "X Wins!"
				menacePlacement=$(java MENACE.java "$(cat board.txt)" "learn")
				break;;
			"O")
				cat board.txt
				echo "O Wins!"
				menacePlacement=$(java MENACE.java "$(cat board.txt)" "learn")
				break;;
			"T")
				cat board.txt
				echo "Tie!"
				menacePlacement=$(java MENACE.java "$(cat board.txt)" "learn")
				break;;
			"N");;
			"L")
				echo "dear god what have you done"
				break;;
		esac

		(( turns++ ))
	done

	echo "Do you want to play again? (Y or N)"
	response="L"

	while [[ "$response" != "Y" || "$response" != "N" ]]; do
		read response
		if [[ "$response" == "Y" || "$response" == "y" ]]; then
			wantsToQuit=0
			break
		elif [[ "$response" == "N" || "$response" == "n" ]]; then
			wantsToQuit=1
			echo "Thanks for playing!"
			break
		else
			echo "Not an option!"
			echo "Do you want to play again? (Y or N)"
		fi
	done
done