package org.example;

//
//in this code we connect to the test project in firebase, write/read some data to firestore
//

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GetDocument {

    public static Firestore authenticationGoogleClient(String FireStoreName)
            throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId(FireStoreName)
                .build();
        FirebaseApp.initializeApp(options);

        return FirestoreClient.getFirestore();
    }

    public static Firestore authenticationServiceAccount(String FireStoreName)
            throws IOException {
        // Use a service account. go to settings of firebase -> service accounts -> generate new private keys
        String currentDirectory = System.getProperty("user.dir");

        FileInputStream serviceAccount =
//                new FileInputStream(currentDirectory + "/serviceAccountKey.json");
                new FileInputStream("/home/alex/IdeaProjects/FirestoreTest/serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://test-8dd59-default-rtdb.europe-west1.firebasedatabase.app")
                .build();

        FirebaseApp.initializeApp(options);

        return FirestoreClient.getFirestore();
    }

    public static void getData(Firestore db)
            throws ExecutionException, InterruptedException {

        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection("users").get();
// ...
// query.get() blocks on response
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("First: " + document.getString("first"));
            if (document.contains("middle")) {
                System.out.println("Middle: " + document.getString("middle"));
            }
            System.out.println("Last: " + document.getString("last"));
            System.out.println("Born: " + document.getLong("born"));
        }
    }

    public static void writeCities(Firestore db)
            throws ExecutionException, InterruptedException {

        CollectionReference cities = db.collection("cities");
        List<ApiFuture<WriteResult>> futures = new ArrayList<>();
        futures.add(
                cities
                        .document("SF")
                        .set(
                                new City(
                                        "San Francisco",
                                        "CA",
                                        "USA",
                                        false,
                                        860000L,
                                        Arrays.asList("west_coast", "norcal"))));
        futures.add(
                cities
                        .document("LA")
                        .set(
                                new City(
                                        "Los Angeles",
                                        "CA",
                                        "USA",
                                        false,
                                        3900000L,
                                        Arrays.asList("west_coast", "socal"))));
        futures.add(
                cities
                        .document("DC")
                        .set(
                                new City(
                                        "Washington D.C.", null, "USA", true, 680000L,
                                        Arrays.asList("east_coast"))));
        futures.add(
                cities
                        .document("TOK")
                        .set(
                                new City(
                                        "Tokyo", null, "Japan", true, 9000000L,
                                        Arrays.asList("kanto", "honshu"))));
        futures.add(
                cities
                        .document("BJ")
                        .set(
                                new City(
                                        "Beijing",
                                        null,
                                        "China",
                                        true,
                                        21500000L,
                                        Arrays.asList("jingjinji", "hebei"))));
// (optional) block on operation
        ApiFutures.allAsList(futures).get();
        //there is also update method for this which does  not overwrite the whole database
    }


public static void writeUsers(Firestore db) throws ExecutionException, InterruptedException {
    DocumentReference docRef = db.collection("users777").document("aturing");
// Add document data with an additional field ("middle")
    Map<String, Object> data = new HashMap<>();
    data.put("first", "Alan");
    data.put("middle", "Mathison");
    data.put("last", "Turing");
    data.put("born", 1912);

    ApiFuture<WriteResult> result = docRef.set(data);
    System.out.println("Update time : " + result.get().getUpdateTime());
}
    public static void listen(Firestore db) {
        //basically this thing makes a query, takes snapshot and tells you a difference
        DocumentReference docRef = db.collection("cities").document("SF");
        docRef.addSnapshotListener(
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            System.out.println("Current data: " + snapshot.getData());
                        } else {
                            System.out.print("Current data: null");
                        }
                    }
                });
        while (true) {
            try {
                Thread.sleep(1000); // Sleep to avoid high CPU usage
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}