#!/bin/bash
case "$1" in
    "init")
        # initialization
        clear
        printf -- "Trial Number,Who Won,Menace Starting,Number of Turns\n" > "$2"
        echo "Welcome to Tic-Tac-Toe!"
        echo "you will play with an AI called MENACE (it goes first)"
        echo "good luck"
        echo 
        ;;
    "play")
        ActualBoard=$2
        Placement=$3
        player=$4

        while [[ "$placementSuccess" !=  "Success" ]]; do
            echo "pick a location (or enter 0 for help)"
            cat $ActualBoard
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
                placementSuccess=$(java "$Placement" "$location" "$player" "$ActualBoard")
                if [[ $placementSuccess != "Success" ]]; then
                    echo "Invalid Placement!"
                    continue
                fi
            else
                echo "not a valid input!"
            fi
        done
        ;;
esac