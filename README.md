# JSON_DB
This is a NoSQL implementation of a local database for android. It uses the JSON APIs amongst other standard Android APIs. It can be used to setup a quick NoSQL database in android. It has the same advantages as any other NoSQL database. It is easy to setup and to get started. 

Getting started Guide 
Installation 

To use the library in yoUr project , just import the bin/json_db.jar file into your class path.
however , you could also add the project as an external Library in eclipse by adding it under the libraries in your project configuration.

Usage
just like any other database , one can store and retrieve data.
To store data say a set of devices, the following code  is used
    Context context = this ;
    String datakey = "DEVICES";
    JSON_DB myDb = new JSON_DB(context ,datakey);
    // here you can loop through a set of data and populate an arraylist
  String[] mdevices = {};
    ArrayList<String> myList= new ArrayList<String>();
    for(String dev1 : mdevices){
         myList.add(dev1); 
    }
    myDb.put(mdevices).save(); //this lines saves the data 
    
To retrieve the data, all you need is to get a reference to your database handler and call get on ther object.
   JSON_DB mDB = new JSON_DB (context , datakey );
   ArrayList mylist =mDB.get();
   
   Here , the ArrayList object contains a list of all the devices stored in it.
