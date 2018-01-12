@ECHO OFF

echo Checking for MongoDB Server v.3.4 installation...
IF EXIST C:\Program Files\MongoDB\Server\3.4\bin (
echo MongoDB found. Skipping installation...
REM GOTO SKIP_MONGODB
)

echo MongoDB NOT found.

echo [MONGO] Step 1 out of X: Downloading installation file
IF EXIST mongodb-win32-x86_64-2008plus-ssl-3.6.2-rc0-signed.msi (
echo MongoDB installation file already found. Skipping download...
GOTO SKIP_MONGODB_DOWNLOAD
)
powershell Invoke-WebRequest http://downloads.mongodb.org/win32/mongodb-win32-x86_64-2008plus-ssl-3.6.2-rc0-signed.msi -OutFile mongodb-win32-x86_64-2008plus-ssl-3.6.2-rc0-signed.msi
echo [MONGO] Step 1 out of X completed.
:SKIP_MONGODB_DOWNLOAD

echo [MONGO] Step 2 out of 8: Running installation
msiexec.exe /i mongodb-win32-x86_64-2008plus-ssl-3.6.2-rc0-signed.msi INSTALLLOCATION="C:\Program Files\MongoDB\Server\3.6.1\" ADDLOCAL="all" /QB-!
echo [MONGO] Step 2 out of 8 completed.

echo [MONGO] Step 3 out of 8: Starting instance
mkdir c:\mongodata
start "mongodb_insecure" "C:\Program Files\MongoDB\Server\3.6.1\bin\mongod.exe" --dbpath c:\mongodata
ping localhost >nul
echo [MONGO] Step 3 out of 8 completed.

echo [MONGO] Step 4 out of 8: Creating admin user
"C:\Program Files\MongoDB\Server\3.6.1\bin\mongo.exe" admin --eval "db.createUser({user: \"root\", pwd: \"s3cur3p4ssw0rd\", roles:[\"root\"]})"
echo [MONGO] Step 4 out of 8 completed

echo [MONGO] Step 5 out of 8: Stopping mongodb
TASKKILL /FI "WINDOWTITLE eq mongodb_insecure"
echo [MONGO] Step 5 out of 8: Stopping mongodb

echo [MONGO] Step 6 out of 8: Starting instance (with AUTH)
start "mongodb_secure" "C:\Program Files\MongoDB\Server\3.6.1\bin\mongod.exe" --dbpath c:\mongodata --auth
ping localhost >nul
echo [MONGO] Step 6 out of 8 completed.

echo [MONGO] Step 7 out of 8: Creating twasi user
"C:\Program Files\MongoDB\Server\3.6.1\bin\mongo.exe" --authenticationDatabase admin -u root -p s3cur3p4ssw0rd twasidb --eval "db.createUser({user: \"twasi\", pwd: \"anothers3cur3p4ssw0rd\", roles:[\"readWrite\"]})"
echo [MONGO] Step 7 out of 8 completed

echo [MONGO] Step 8 out of 8: Stopping mongodb
TASKKILL /FI "WINDOWTITLE eq mongodb_secure"
echo [MONGO] Step 8 out of 8: Stopping mongodb

echo ----------------------------------------------------
echo MongoDB was installed successfully.
echo Credentials for twasi.yml:
echo user: twasi
echo password: anothers3cur3p4ssw0rd
echo database: twasidb
echo ----------------------------------------------------
echo If you need to access the database otherwise (Robo 3T), the credentials are:
echo user: admin
echo password: s3cur3p4ssw0rd
echo login database: admin
echo ----------------------------------------------------

:SKIP_MONGODB

echo Twasi-Core should be installed.
pause



