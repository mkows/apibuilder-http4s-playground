# apibuilder http4s playground

Code useful for test locally changes to [apibuilder-generator](https://github.com/apicollective/apibuilder-generator).  

Apibuilder project: [michal/playground-api](https://app.apibuilder.io/michal/playground-api/latest).  

Code generated locally (off apibuilder-generator feature branch) as per [apibuilder/DEVELOPER.md](https://github.com/apicollective/apibuilder/blob/master/DEVELOPER.md).  

Scope:  
- clients: http4s 0.15, 0.17, 0.18, 0.20
- server: http4s 0.20

Run with:

```
cd playground-server-020
sbt run
```

and then for each client subproject:
```
cd playground-client-020
sbt run
```
