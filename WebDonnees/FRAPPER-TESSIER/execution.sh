#!/bin/bash

echo "Execution de XSLT"
time java -jar saxon9he.jar -s:mondial.xml -xsl:xslt_mondial.xsl -o:exo_xslt.xml
echo "********"
echo "Execution de SAX"
time php sax_mondial_dom.php
echo "********"
echo "Execution de DOM sans Xpath"
time php dom.php
echo "********"
echo "Execution de DOM avec Xpath"
time php dom_xpath.php
echo "********"
echo "Execution de XML Reader / XML Writer"
time php xml_reader_writer.php
echo "********"
echo "Execution de JDOM sans filtre"
time java -jar sans_filtre.jar
echo "********"
echo "Execution de JDOM avec filtre"
time java -jar avec_filtre.jar

echo "Fin des executions"

read

