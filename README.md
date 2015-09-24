# ICEBASE
Read the project wiki <a href="https://github.com/larrytech7/IceBase/wiki/ICEBASE-version-1.0.1">HERE</a> to get full concrete information.<br />
Basic details can be found here for a really quick start but full documentation is on the wiki and site pages.

Getting started Guide Installation
<br/>
To use the library in your Android project, just import the bin/json_db.jar file into your class path. You could also add the project as an external Library in eclipse by adding it under the libraries in your project configuration.
<br/>
Just like any other database , your application can manipulate(CRUD) data seamlessly without much effort. To store data, say a set of devices, the following code is used. <br /><br/>
<code>
Context context = this ; //application or component context

String datakey = "DEVICES"; //application wide key. most often declared final and static

JSON_DB_EXTENDED myDb = new JSON_DB_EXTENDED(context ,datakey);

List<Object> myList = new ArrayList<>();
    for(Object dev1 : mdevices){
      myList.add(dev1); 
      }
      myDb.put(mdevices).save(); //this lines saves the data
//alternatively, on could do myDb.insert(myList) if no other data objects need to be added to the current schema at the moment.
</code>

To retrieve the data, all you need is to get a reference to your database handler and call get on the object.
<br />
<code>

ArrayList<Object> mylist = myDb.get();
</code>
<br />

Here , the mylist object contains a list of all the devices previously stored in it or is empty if nothing was found.

<br/>
Note , to save binary data say for example images, the data type is a little tricky to configure. Let us consider we have a bitmap built from an image
we would have to convert it to a compressed byte array as follows before adding/inserting it as a value to the database
Note how your bitmap is compressed using the PNG lossless photo conversion format so we don't run into issues 
with space availability and pixel loss or scrambled.
<br/>
<code>
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mybitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        //to now add it as a value or property of some entity, we do
        person.add(Base64.encodeToString(bytes, Base64.NO_WRAP));
</code>
<br/>
To retrieve the image, it is normal to re-convert the base64 encoded string back to a a byte array from which the bitmap can be extracted.

<code>
		//not that 'profile' is a result data set from fetching a single object from the data store like myDb.get(itemid);
		
		byte[] imgbyte = Base64.decode((String) profile.get(position of the image in the dataset), Base64.NO_WRAP);

        ByteArrayInputStream imageStream = new ByteArrayInputStream(imgbyte);
        Bitmap image = BitmapFactory.decodeStream(imageStream);
</code>

You can now use your bitmap to set whatever image you want.
