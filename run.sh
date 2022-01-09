mvn clean install \
	&& \
	sed -i '$ d' index.html && date >> index.html

git add .
git commit && git push origin main

