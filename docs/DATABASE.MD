# Twasi Database

## Installing MongoDB, setting up authentication
Twasi uses MongoDB as Database. You need to set up a MongoDB Server to either use Twasi locally, or contribute to the development.

- Install MongoDB for your operating system. Head over to https://www.mongodb.com/download-center#community for official downloads.
- Start MongoDB without authentication. This should be the default after the first-time installation.
- Log in to MongoDB and issue this commands to create a root user (don't forget to change the password):

```
use admin
db.createUser({user: "root", pwd: "s3cur3p4ssw0rd", roles:["root"]})
```
- Stop the database and start it with authorization enabled (provide the "--auth" parameter or set authorization to true if using a config file)
- Now you should be able to login using user "root" and your password and algorithm SCRAM-SHA1

## Creating an account for Twasi
Next you have to create an account for Twasi to access your database. You could use root, but root is bad and Twasi isn't. It's a better security practice to create an own account.

- First connect to the database using the root account you created before (if you aren't anymore).
- Select the database and create a database user for the bot, which is allowed to read and write to the database:
```
use twasidb
db.createUser({user: "twasi", pwd: "anothers3cur3p4ssw0rd", roles:["readWrite"]})
```

Now the account should be created. You can now fill in the information into the twasi.yml config file. No further installation or setup has to be done - Collections are created automatically on data insert.
