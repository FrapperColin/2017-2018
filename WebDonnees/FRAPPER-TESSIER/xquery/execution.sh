#!/bin/bash

echo "Execution de XQuery"
time java -cp saxon9ee.jar net.sf.saxon.Query -q:distanciel.xquery -o:distanciel.xml
echo "********"

echo "Fin des executions"

read