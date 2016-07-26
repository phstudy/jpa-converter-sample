jpa-converter-sample [![Build Status](https://travis-ci.org/phstudy/jpa-converter-sample.svg?branch=master)](https://travis-ci.org/phstudy/jpa-converter-sample)
=========

This project demos how to convert PostgreSQL ```hstore``` datatype to Java ```Map<String, String>``` object for different JPA Implementations, including Hibernate and EclipseLink.

This project was tested on
* PostgreSQL 9.5.0-1
* Hibernate 5.2.1.Final
* EclipseLink 2.6.3


If your PostgreSQL does not have the ```hstore``` datatype, you can create the ```hstore``` datatype by the following sql command: ```CREATE EXTENSION hstore;```
