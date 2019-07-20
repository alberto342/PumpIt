package fitness.albert.com.pumpit.model;

import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FireBaseInit {
    public static FireBaseInit fireBaseInit = null;
    public FirebaseUser user;
    public List<AuthUI.IdpConfig> providers = Collections.singletonList(
            new AuthUI.IdpConfig.GoogleBuilder().build());
    public FirebaseStorage storage;
    public StorageReference storageRef;
    public DocumentReference docRef;
    public FirebaseFirestore db;
    private Context context;
    public PrefsUtils prefsUtils = new PrefsUtils();


    public FireBaseInit(Context context){
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
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getEmailRegister() {

        String email = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        return email;
    }

    public void setIntoPrefs() {
        prefsUtils.createSharedPreferencesFiles(context, UserRegister.SharedPreferencesFile);
        prefsUtils.saveData("userEmail", getEmailRegister());
    }

    // rest the firebase settings
    private void defineTheFireBaseDataBase() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }


    public static FireBaseInit getInstance(Context context) {
        if (fireBaseInit == null)
            fireBaseInit = new FireBaseInit(context);

        return fireBaseInit;
    }

    public String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

}
