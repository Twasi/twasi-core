# Twasi Database

## Installing MongoDB
Twasi uses MongoDB as Database. You need to set up a MongoDB Server to either use Twasi locally, or contribute to the development.

- Install MongoDB for your Operating System. Head over to [https://www.mongodb.com/download-center#community] for official downloads.
- Start mongodb without authentication. This should be the default after the first-time installation.
- Log in to MongoDB and issue this commands to create a root user (don't forget to change the password):
`
    use admin
    db.createUser({user: "root", pwd: "s3cur3p4ssw0rd", roles:["root"]})
`
- Stop the database and start it with authorization enabled (provide the "--auth" parameter or set authorization to true if using a config file)
- Now you should be able to login using user "root" and your password an algorithm SCRAM-SHA1