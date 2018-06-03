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
* Upgrade Commons-cli version (1.2 --> 1.4) in hadoop/share/hadoop/common (TODO: improve this)

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
* Additionally you have to change your common-cli library in your hadoop cluster
    * Copy the commons-cli-1.4 (from .m2/) to $HADOOP\_HOME/share/hadoop/common/lib 
* Finally compile with Maven hmusket **mvn clean package**

## How to run hmusket?
### flags cheat sheet
usage: hmusket -fileIn <filePath> -fileOut <filePath> -fileType <a/q>
       [-inorder] [-k <int/unit>] [-lowercase] [-maxbuff <int>] [-maxerr
       <int>] [-maxiter <int>] [-maxtrim <int>] [-minmulti <int>] [-multik
       <bool>] [-o <str>] [-omulti <str>] [-p <int>] [-zlib <int>]
-------------------------------------------------------------------
 -fileIn <filePath>    File where there are the sequences
 -fileOut <filePath>   File where there want to save the output
 -fileType <a/q>       File type <a> for FASTA files and <q> for FASTQ
                       files
 -k <int/unit>         Specify two paramters: k-mer size and estimated
                       total number of k-mers for this k-mer size)
 -lowercase            Write corrected bases in lowercase, default=0
 -maxbuff <int>        Capacity of message buffer for each worker,
                       default=1024
 -maxerr <int>         Maximal number of mutations in any region of length
                       #k, default=4
 -maxiter <int>        Maximal number of correcting iterations per k-mer
                       size, default=2
 -maxtrim <int>        Maximal number of bases that can be trimmed,
                       default=0
 -minmulti <int>       Minimum multiplicty for correct k-mers [only
                       applicable when not using multiple k-mer sizes],
                       default=0
 -multik <bool>        Enable the use of multiple k-mer sizes, default=0
 -o <str>              The single output file name
 -omulti <str>         Prefix of output file names, one input
                       corresponding one output
 -p <int>              Number of threads [>=2], default=2
 -zlib <int>           Zlib-compressed output, default=0

### Some runs examples

```bash
# Single-end dataset
user@host:~$ hadoop jar Hmusket-1.0.jar es.udc.gac.hmusket.HMusket -fileIn ~/datasets/single-end.fastq -fileOut output1 -fileType q

# Pair-end dataset
user@host:~$ hadoop jar Hmusket-1.0.jar es.udc.gac.hmusket.HMusket -fileIn  ~/datasets/pair-end_1.fasta ~/datasets/pair-end_2.fasta -fileOut output2 -fileType a -p 4
```

## License

This software is distributed as free software and is publicity available under the GPLv3 license (see the [LICENSE](LICENSE) file for more details)
