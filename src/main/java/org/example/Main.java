package org.example;

//
//in this code we connect to the test project in firebase, write/read some data to firestore
//

import com.google.cloud.firestore.Firestore;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

//what we want in the end// something changed in the base (probably listener or by some action we run a job) -> take it -> write to s3 or also firestore
public class Main {

    public static void main(String[] args)
            throws IOException, ExecutionException, InterruptedException {
        //authenticate
//        Firestore db = GetDocument.authenticationGoogleClient("test-8dd59");
        Firestore db = GetDocument.authenticationServiceAccount("test-8dd59");
//        GetDocument.listen(db);

//        GetDocument.writeCities(db);

//        GetDocument.writeUsers(db);

        GetDocument.getData(db);
    }
}
