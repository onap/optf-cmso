OOF CMSO Delivery
======================

OOF CMSO is made up of 6 docker containers depoloyed via OOM

 #. CMSO Service - Java server (Jersey)
 #. CMSO Database Initialization - Java wrapper invoking Liquibase schema management scripts
 #. MariaDB - OOM Common mariadb-galera server
 #. CMSO Optimizer - Java server (Jersey)
 #. CMSO Ticket Management - Java server (Jersey)
 #. CMSO Topology - Java server (Jersey)

The OOM Helm Charts are located here:

https://gerrit.onap.org/r/gitweb?p=oom.git;a=tree;f=kubernetes/oof/charts/oof-cmso;h=2cbb8cd2178077575cd03a4dfb359b2c941ba2db;hb=HEAD

OOF CMSO Delivery Diagram
------------------------------

.. image:: ./diagrams/DeliveryOOM.png

