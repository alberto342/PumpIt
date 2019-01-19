package fitness.albert.com.pumpit.Model;

import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collections;
import java.util.List;

public class FireBaseInit {
    public static FireBaseInit fireBaseInit = null;
    public FirebaseUser user;

    public List<AuthUI.IdpConfig> providers = Collections.singletonList(

            new AuthUI.IdpConfig.GoogleBuilder().build());

    public FirebaseStorage storage;

    public StorageReference storageRef;
    public FirebaseFirestore db;
    private Context context;


    private FireBaseInit(Context context){
        this.context=context;

        defineTheFireBaseDataBase();
        defineTheFireBaseUser();
        defineTheStorageReferance();
    }

    private void defineTheStorageReferance() {
        storage = FirebaseStorage.getInstance();
        storageRef=storage.getReference();
    }

    private void defineTheFireBaseUser() {
        user= FirebaseAuth.getInstance().getCurrentUser();
    }

    // rest the firebase settings
    private void defineTheFireBaseDataBase() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }



    public static FireBaseInit getInstance(Context context)
    {
        if (fireBaseInit == null)
            fireBaseInit = new FireBaseInit(context);

        return fireBaseInit;
    }

}
