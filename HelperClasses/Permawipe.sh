#deletes all data in menace table, refreshes board, and position storage
# will delete the data in menacetable IN your current directory
# first argument just checks if you're conscious

ActualBoard=$(find ../HelperClasses -name "board.txt")
MenaceTable=$(find . -name "MenaceTable.csv")
PositionStorage=$(find ../HelperClasses -name "PositionStorage.csv")

exit 0
if [[ "$1" == "y" ]]; then
    echo "deleting..."

    #restarts the board
    echo "board" > "$ActualBoard"
    for i in {1..9}; do
        if (( "$i" % 3 == 0 )); then
            printf -- "-\n" >> "$ActualBoard"
        else
            printf -- "-" >> "$ActualBoard"
            printf -- " " >> "$ActualBoard"
        fi
    done

    #Deletes MenaceTable
    echo "board_number,1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19" > "$MenaceTable"

    #Deletes PositionStorage
    echo "" > "$PositionStorage"

    echo "deleted"
else
    echo "nuh uh"
    exit 0
fi
