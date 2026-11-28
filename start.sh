#!/bin/bash
wantsToQuit=0
trials=1
# $1 - The folder's name
# $2 - if it's bot against bot, how many times?
#checking the args

if [[ "$#" -gt 2 ]]; then
	echo "wrong format!"
	echo "first arg: folder name"
	echo "second arg: IF doing bot vs bot, how many (positive integer) trials"
	exit 1
fi

if [[ ! -n $(find . -type d -name "$1") ]]; then
	echo "directory not found!"
	exit 1
fi 

if [[ ! "$2" =~ ^[0-9]+$ && "$#" -eq 2 ]]; then
	echo "you're doing bot vs bot, how many (positive integer) trials do you want?"
	exit 1
fi 

#should be in HelperClasses
MENACE=$(find ./HelperClasses -name "MENACE.java")
WinVerify=$(find ./HelperClasses -name "WinVerify.java")
Placement=$(find ./HelperClasses -name "Placement.java")
PositionStorage=$(find ./HelperClasses -name "PositionStorage.csv")
ActualBoard=$(find ./HelperClasses -name "board.txt")


#compile so we don't have to keep recompiling when using just 'java'
javac -d . $MENACE $WinVerify $Placement
MENACE="MENACE"
WinVerify="WinVerify"
Placement="Placement"

#deletes the compiled files in case we need to abort the script
trap "rm $MENACE.class; rm $WinVerify.class; rm $Placement.class; rm -f "MiniMaxBot.class"; exit 0" SIGINT 

#you can choose who is player O
#Should be folder specific
playerODirectory=$(find . -type d -name "$1")
playerO=$(find $playerODirectory -name "*.sh")
MenaceTable=$(find $playerODirectory -name "MenaceTable.csv")
StatTable=$(find $playerODirectory -name "stats.csv")
# wc -l $(find $playerODirectory -name "stats.csv")
# exit 0

$playerO "init" "$StatTable"

#main logic
while [[ "$wantsToQuit" -eq 0 ]]; do
	turns=1
	player="X"
	winVerification="N"

	# restarts the board
    printf "board\n" > "$ActualBoard"
	for i in {1..9}; do
		if (( "$i" % 3 == 0 )); then
			printf -- "-\n" >> "$ActualBoard"
		else
			printf -- "-" >> "$ActualBoard"
			printf -- " " >> "$ActualBoard"
		fi
	done

	while [ "$turns" -lt 10 ]; do
		#menace will play first
		if [[ "$player" == "X" ]]; then
			if [[ "$turns" -eq 1 ]]; then
				menacePlacement=$(java "$MENACE" "$(cat $ActualBoard)" "start" "$PositionStorage" "$MenaceTable")
				menaceStart=$menacePlacement
			else
				menacePlacement=$(java "$MENACE" "$(cat $ActualBoard)" "play" "$PositionStorage" "$MenaceTable")
			fi
			
			menacePlacementStatus=$(java "$Placement" "$menacePlacement" "$player" "$ActualBoard")
			#this shouldn't happen, but it's a safe guard
			if [[ $menacePlacementStatus != "Success" ]]; then
				echo "menace is playing wrong!"
				echo "currentBoard:"
				echo $ActualBoard
				exit 1
			fi
			player="O"
		else
			$playerO "play" "$ActualBoard" "$Placement" "O"
			player="X"
		fi

		winner=$(java "$WinVerify" "$ActualBoard")

		case "$winner" in
			"X" | "O" | "T")
				if [[ "$playerODirectory" == "./main" ]]; then
					cat "$ActualBoard"
					echo $winner "Wins!"
				fi
				
				printf "%d,%s,%s,%d\n" "$trials" "$winner" "$menaceStart" "$turns" >> "$StatTable"
				menacePlacement=$(java "$MENACE" "$(cat "$ActualBoard")" "learn" "$PositionStorage" "$MenaceTable")
				break;;
			"N");;
			"L")
				echo "dear god what have you done"
				break;;
		esac

		(( turns++ ))
	done
	
	case "$playerODirectory" in
		"./main")
			echo "Do you want to play again? (Y or N)"
			response="L"
			while [[ "$response" != "Y" || "$response" != "N" ]]; do
				read response
				if [[ "$response" == "Y" || "$response" == "y" ]]; then
					break
				elif [[ "$response" == "N" || "$response" == "n" ]]; then
					echo "Thanks for playing!"
					wantsToQuit=1
					break
				else
					echo "Not an option!"
					echo "Do you want to play again? (Y or N)"
				fi
			done
			;;
		"./AgainstMiniMax" | "./AgainstRandom")
			echo "trial" $trials "out of" $2 "complete"
			if [[ "$trials" == "$2" ]]; then
				echo "done"
				break;
			fi
			;;
	esac

	(( trials++ ))
done
rm $MENACE.class
rm $WinVerify.class
rm $Placement.class
rm -f "MiniMaxBot.class" #minmax is different because it has a unique java file