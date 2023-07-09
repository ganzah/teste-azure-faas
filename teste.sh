#!/bin/bash

S1=$(tail -n +2 $1 | tr -d '\n')
S2=$(tail -n +2 $2 | tr -d '\n')

if [ -z "$4" ] || [ -z "$5" ] || [ -z "$6" ] 
then
    GAP=-1
    MATCH=1
    MISMATCH=-1
else 
    GAP=$4
    MATCH=$5
    MISMATCH=$6
fi

curl -H 'Content-type: application/json' -X POST $3 -d "{\"seq1\": \"$S1\", \"seq2\": \"$S2\", \"gap\": $GAP, \"match\": $MATCH, \"mismatch\": $MISMATCH}"