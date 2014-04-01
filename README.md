jpa-converter-sample [![Build Status](https://travis-ci.org/phstudy/jpa-converter-sample.svg?branch=master)](https://travis-ci.org/phstudy/jpa-converter-sample)
=========

This project demos how to convert PostgreSQL ```hstore``` datatype to Java ```Map<String, String>``` object.

This project was tested on
* PostgreSQL 9.3
* Hibernate 4.3.1
* EclipseLink 2.5.2-M1


If your PostgreSQL does not have the ```hstore``` datatype, you can create the ```hstore``` datatype by the following sql command: ```CREATE EXTENSION hstore;```
