# Set up Twitch OAuth

## Create Application
- Head over to https://www.twitch.tv/kraken/oauth2/clients/new to create a new Application.
- Fill in a Name, and a Redirect URI. The redirect URI should be [YOUR_SERVER]/auth/callback. 
If you want to test it locally on port 8000 e.g, the URI should be http://localhost:8000/auth/callback
Select chatbot as application category and accept the tos. Now the application is registered. Please note
your Client ID and Secret and fill it in the Config.
