export version=1.5.3
if [ "$#" -gt 0 ] ; then
	export version="$1"
fi

echo "Installing Dropbox SDK version $version"

wget https://www.dropbox.com/static/developers/dropbox-java-sdk-$version.zip
unzip dropbox-java-sdk*
cd dropbox-java-sdk*

mvn \
	install:install-file \
	-Dfile=lib/dropbox-java-sdk-$version.jar \
	-DgroupId=com.dropbox.sdk \
	-DartifactId=dropbox-client \
	-Dversion=$version \
	-Dpackaging=jar \
	-DpomFile=pom.xml

rm -fr dropbox-java-sdk*