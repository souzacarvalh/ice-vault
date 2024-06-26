
# IceVault

Welcome to IceVault Services

The IceVault is a safe place where you can keep your sensitive information.

## How does it work?

The vault is designed to safely store the user's data using encryption methods for the data in-transit and at rest. When a new user is created (what can be performed only by admin users), the vault assigns to him a new pair of RSA keys (public/private) of 2048 bits which will be linked to this user and can be only used by him. Whenever this user successfully logs in the system using his personal credentials, a new JWT token containing his public key is returned to him for performing the next the data encryption/decryption operations - while his private key never leaves the server. 

When he creates a new "secret" (sensitive data), the system will actually encrypt the data using AES encryption with a random passphrase, and will encrypt this key with the user's personal RSA public key, in the front, before sending to the server for storage. When the data arrives in the server, the passphase is decrypted and encrypted again, but now using the user's private key and finally, it is store in the database. 

When the user wants to retrieve the secret and check it as plain text, the server will send him the encrypted data which can be only descrypted having the corresponding key.

To avoid any interception on the wire, the HTTPS should always be enabled in production servers with the proper signed certificates.

# Features

- User and Secret Management over REST API
- Spring Security based authentication and authorization (OAuth2 + JWT)
- Secret Hybrid Encryption/Decryption with Assymetric RSA (public/private) + AES (16Bit Random Passphrase)
- Users Password protected 'at rest' by BCrypt algorithm (Salted)
- Cloud Native Application

## How do I run it?

Execute 'gradle bootRun' it will start up a dev server on http://localhost:9191.
To change the port add --server.port=<SERVER_PORT> to the start command

# Endpoints

 **- Authorization**

 ***- OAuth Token:***

Method:GET
Resource:/oauth/tokengrant_type=password&username=username&password=password
Headers: 'Basic aWNlZmlyZTppY2VmaXJl'

**User Management**

This resource is only available for authorized ADMIN users. You can use one of the following for testing:

Username: admin
Password: 12345

Username felipec
Password: 12345

**- Create User:** 

Method: POST
Resource: api/user
Request Body:

    {
    "username": "joe",
    	"password": "12345",
    	"roles": [
    		"ROLE_ENCRYPT"
    	]
    }

Available Roles: ROLE_ADMIN, ROLE_ENCRYPT

***- Get User***

Method:GET
Resource: /api/user/{username}

***- List Users***

Method:GET
Resource: /api/user

***- Delete Users***

Method:DELETE
Resource: /api/user/{username}

**Secrets API**

This resource is only available for authorized ADMIN and ENCRYPT users.

***- Create a new Secret***

Method: POST
Resource: api/secret
Request Body:

    {
    	"userId":2, 	 
    	"data":"U2FsdGVkX18+nOrttjthjdL/fznqMMuAxeUi/TRTYRT",
    	"passphrase":"MuiSM7u5Cx5OfpMSARpsOm/RW1g+gC9P5d5rXI5fHbp=="
    }
    
***- List Secrets***

Method:GET
Resource: /api/secret?userId=userId

## Dev Requirements

JDK8 and Gradle 

### Who do I talk to? ###

Felipe F. de Souza Carvalho | souzacarvalh@gmail.com
