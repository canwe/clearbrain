ClearBrain
==========

ClearBrain is an **experimental** Todo list manager in JavaEE.
It should not be used until the v1.0 is released.

Test it
-------

* Use postgreSQL and create a database named "clearbrain" and another one named "clearbrain-test"
* Modify ~/src/main/resources/META-INF/database.properties
* Don't forget to change rememberme.key in the database.properties file in a production environment. It is used for recognizing rememberme tokens
* Launch mvn package
* Put the .war file from the target folder in your application server
* You should add the mime type text/css for .less files in your application server configuration.

Screenshot
----------

<img src="http://nilhcem.github.com/screenshots/clearbrain.png" width="840" height="380" />
