# ICEBASE
Read the project wiki <a href="https://github.com/larrytech7/IceBase/wiki/ICEBASE-version-1.0.1">HERE</a> to get full concrete information.<br />
Basic details can be found here for a really quick start but full documentation is on the wiki and site pages.

#<h4>Getting started Guide Installation</h4>
<br/>
<p>
To use the library in your Android project, just import the bin/json_db.jar file into your class path. You could also add the project as an external Library in eclipse by adding it under the libraries in your project configuration.
<br/>
Just like any other database , your application can manipulate(CRUD) data seamlessly without much effort. To store data, say a set of devices, the following code is used.
</p>
--------
Context context = this ; //application or component context

String datakey = "DEVICES"; //application wide key. most often declared final and static

JSON_DB_EXTENDED myDb = new JSON_DB_EXTENDED(context ,datakey);

List<Object> myList = new ArrayList<>();
    for(Object dev1 : mdevices){
      myList.add(dev1); 
      }
      myDb.put(mdevices).save(); //this lines saves the data
//alternatively, on could do myDb.insert(myList) if no other data objects need to be added to the current schema at the moment.
--------

To retrieve the data, all you need is to get a reference to your database handler and call get on the object.
<br/>
-----
ArrayList<Object> mylist = myDb.get();
-----

Here , the mylist object contains a list of all the devices previously stored in it or is empty if nothing was found.

<br/>
Note , to save binary data say for example images, the data type is a little tricky to configure. Let us consider we have a bitmap built from an image
we would have to convert it to a compressed byte array as follows before adding/inserting it as a value to the database
Note how your bitmap is compressed using the PNG lossless photo conversion format so we don't run into issues 
with space availability and pixel loss or scrambled.
<br/>
-----
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mybitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        //to now add it as a value or property of some entity, we do
        person.add(Base64.encodeToString(bytes, Base64.NO_WRAP));
------
<br/><br/>
To retrieve the image, it is normal to re-convert the base64 encoded string back to a a byte array from which the bitmap can be extracted.

------
		//note that 'profile' is a result data set from fetching a single object from the data store like myDb.get(itemid);
		
		byte[] imgbyte = Base64.decode((String) profile.get(position of the image in the dataset), Base64.NO_WRAP);

        ByteArrayInputStream imageStream = new ByteArrayInputStream(imgbyte);
        Bitmap image = BitmapFactory.decodeStream(imageStream);
------

You can now use your bitmap to set whatever image you want.

License
-------

    Copyright 2013-2014 Akah Larry H N

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


---
