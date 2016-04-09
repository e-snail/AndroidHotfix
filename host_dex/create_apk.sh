#/bin/sh

cd roff

#.java --> .class
javac HostDex.java 

#.class --> .dex
dx --dex --output=HostDex.dex HostDex.class

#.dex --> .apk
jar cvf host.apk HostDex.dex

#copy .apk to assert
mkdir -p ../../app/src/main/assets/
cp host.apk ../../app/src/main/assets/

cd ..

