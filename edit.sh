if [ $# -eq 0 ]
then
	vi src/main/java/com/vanillabreeze/
else
	vi src/main/java/com/vanillabreeze/$1.java
fi
