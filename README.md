# hmusket
K-mer spectrum corrector based on Hadoop

## Requisites

* Java Develpment Environment (JDK) version 1.6 or above
* Make sure you have a working Apache Maven distribution version 3 or above
* A version of hsp (Hadoop Sequence Parser) in your Maven repository
    * Do you need help? [Check this out](https://github.com/rreye/hsp)
* Hadoop 2.8.0 or above
* g++ (development version: 6.3.0)
* GNU Make (development version: 4.1)

## Authors

* **Luis Lorenzo Mosquera** (https://github.com/luislorenzom)
* **Roberto R. Éxposito** (http://gac.udc.es/~rreye)
* **Jorge González Domínguez** (http://gac.udc.es/~jgonzalezd)

## Do you want to compile hmusket from scrath?
* Clone this project
* Generate the header file for java side call (resource/make.sh)
* Configure in src/main/native/MakeFile.common where is musket source files. 
	* Don't you have a musket source code copy? [Download it](http://musket.sourceforge.net/homepage.htm)  
* Also in src/main/native execute **make** to compile musket and create the shared library
* Once you have the shared library created copy it from lib folder (root folder) and paste it in $HADOOP\_HOME/lib/native
* Finally compile with Maven hmusket **mvn clean package**

## How to run hmusket?
TODO


## License

This software is distributed as free software and is publicity available under the GPLv3 license (see the [LICENSE](LICENSE) file for more details)
