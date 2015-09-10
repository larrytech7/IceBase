# ICEBASE
Read the project wiki <a href="https://github.com/larrytech7/IceBase/wiki/ICEBASE-version-1.0.1">HERE</a> to get full concrete information.<br />
Basic details can be found here for a really quick start but full documentation is on the wiki and site pages.

Getting started Guide Installation
<br/>
To use the library in your project , just import the bin/json_db.jar file into your class path. You could also add the project as an external Library in eclipse by adding it under the libraries in your project configuration.
<br/>
Usage just like any other database , one can store and retrieve data. To store data, say a set of devices, the following code is used. <br /><br/>
<code>
Context context = this ;

String datakey = "DEVICES"; 

JSON_DB_EXTENDED myDb = new JSON_DB_EXTENDED(context ,datakey);

ArrayList myList= new ArrayList();
    for(String dev1 : mdevices){
      myList.add(dev1);
      }
      myDb.put(mdevices).save(); //this lines saves the data

</code>

To retrieve the data, all you need is to get a reference to your database handler and call get on the object.
<br />
<code>
JSON_DB_EXTENDED mDB = new JSON_DB_EXTENDED(context , datakey );
ArrayList mylist =mDB.get();
</code>
<br />

Here , the mylist object contains a list of all the devices stored in it.
