HOW TO CONFIGURE THE PROJECT
 
path: /hbase-crud/src/main/resources/
add core-site.xml from /etc/hbase/conf.cloudera.hbase/
add hbase-site.xml from /etc/hbase/conf.cloudera.hbase/
 
Building and Running
  
Build
To build the application it is required to have this installed:
Java 9
Maven 3.x
Then just run this:
mvn clean install assembly:single
  
Run
$ su <user>
$ cd /home/<user>
$ chmod 770 ./hbase/hbase-crud-0.0.1-SNAPSHOT-jar-with-dependencies.jar
$ chown <user>:<user> ./hbase/hbase-crud-0.0.1-SNAPSHOT-jar-with-dependencies.jar
$ kinit -kt /etc/security/keytabs/<user>.keytab <user>
$ java -jar ./hbase-crud-0.0.1-SNAPSHOT-jar-with-dependencies.jar


Result
*** HBase is running. ***

*** Create/Insert - BEGIN ***
OK
*** Create/Insert - END ***

*** Read/Select - BEGIN ***
1
        shop
                category
                        shoes, 1674459300795
                color
                        black, 1674459300856
                price_eu
                        44.50, 1674459300872
                product
                        productA, 1674459300836
                sex
                        m, 1674459300864
                size_eu
                        42, 1674459300845
2
        shop
                category
                        shoes, 1674459300879
                color
                        white, 1674459300916
                price_eu
                        40.50, 1674459300931
                product
                        productA, 1674459300892
                sex
                        m, 1674459300924
                size_eu
                        42, 1674459300904
*** Read/Select - End ***

*** Update - BEGIN ***
OK

*** Read/Select - BEGIN ***
1
        shop
                category
                        shoes, 1674459300968
                color
                        black, 1674459301013
                price_eu
                        42.50, 1674459301029
                product
                        productA, 1674459300997
                sex
                        m, 1674459301022
                size_eu
                        42, 1674459301006
2
        shop
                category
                        shoes, 1674459300879
                color
                        white, 1674459300916
                price_eu
                        40.50, 1674459300931
                product
                        productA, 1674459300892
                sex
                        m, 1674459300924
                size_eu
                        42, 1674459300904
*** Read/Select - End ***
*** Update - End ***

*** Delete - BEGIN ***
OK
*** Delete - End ***

1st step: Create/Insert
 ________________________________
/		        /t1 (version 1)	/
|_______________|_______________|
|row_key	    |cf:cq6		    |
|_______________|_______________|
|1		        |44.50          |
|_______________|_______________|
|2		        |40.50          |
|_______________|_______________|

2nd step: Update
   ________________________________
  /		          /t1 (version 1) /
 /_______________/_______________/
/		        /t2 (version 2)	/
|_______________|_______________|
|row_key	    |cf:cq6		    |
|_______________|_______________|
|1		        |42.50          |
|_______________|_______________|
|2		        |40.50          |
|_______________|_______________|