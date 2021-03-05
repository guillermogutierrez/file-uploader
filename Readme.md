# How to start the service

1. Ensure the run.sh script has execution permissions
2. Execute run.sh
The script clean and create the package using maven, copy all requried files into the dependency folder, build a docker image and run the docker image
- mvnw clean package 
- mkdir -p target/dependency 
- (cd target/dependency; jar -xf ../*.jar) 
- docker build -t guillermo/multifile-uploader . 
- docker run -p 8080:8080  guillermo/multifile-uploader

# How the service works
Multiple files can be uploaded in the same request /multiple-upload. All files will be stored in a dedicated folder and a ZIP containing all files is created. The service will respond with the ID of that folder and a list of all files processed.
The Zip file can be retrieved by sending a new request to /download/{ID}

# How to test the service
1. Upload files
POST to http://localhost:8080/multiple-upload files to be included in the body using the "files" key and configured as form-data

Respond
```
{
    "id": "7ace1a5c-76ba-49e8-9ddc-d68e7b34d3ef",
    "fileOutcomes": [
        {
            "fileName": "Ryanair Refund Application Form.pdf.zip",
            "resultCode": "FILE_STORED"
        },
        {
            "fileName": "Guillermo CV 2020.pdf",
            "resultCode": "FILE_STORED"
        },
        {
            "fileName": "2020_06_08_YourTalkTalkBill.pdf",
            "resultCode": "FILE_STORED"
        }
    ]
}
```

2. Download ZIP file
GET to http://localhost:8080/download/{fileKey} where fileKey is the id value provided by the Upload service