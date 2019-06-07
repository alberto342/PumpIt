package fitness.albert.com.pumpit.fragment.profile.AccountSettings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView btnEmail, btnPassword, btnDeleteAccount, btnLogout, tvMyEmail;
    private ImageView goToEmail, goToPassword;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String TAG = "AccountFragment";
    private boolean successDeleteData;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get current user
        getCurrentUser();

        init(view);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();


        btnClick();

//        //get current user
//        getCurrentUser();
    }


    private void init(View view) {
        btnDeleteAccount = view.findViewById(R.id.btn_delete_account);
        btnLogout = view.findViewById(R.id.btn_sign_out);

        btnEmail = view.findViewById(R.id.btn_email);
        goToEmail = view.findViewById(R.id.go_to_email);

        btnPassword = view.findViewById(R.id.btn_password);
        goToPassword = view.findViewById(R.id.go_to_password);

        tvMyEmail = view.findViewById(R.id.tv_my_email);

        String myEmail = getEmailRegister();
        tvMyEmail.setHint(myEmail);


    }

    private void btnClick() {
        btnDeleteAccount.setOnClickListener(onClickListener);
        btnLogout.setOnClickListener(onClickListener);
        btnEmail.setOnClickListener(onClickListener);
        goToEmail.setOnClickListener(onClickListener);
        btnPassword.setOnClickListener(onClickListener);
        goToPassword.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        Fragment changeEmailFragment = new ChangeEmailFragment();
        Fragment changePasswordFragment = new ChangePasswordFragment();

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sign_out:
                    signOut();
                    break;

                case R.id.btn_delete_account:
                    //alertDialog();
                    deleteAllData();
                    if(successDeleteData) {
                        reAuthenticateUser();
                        deleteAccount();
                    } else {
                        Log.d(TAG, "Error deleting data");
                    }
                    signOut();
                    break;

                case R.id.btn_email:
                    loadFragment(changeEmailFragment);
                    break;

                case R.id.go_to_email:
                    loadFragment(changeEmailFragment);
                    break;

                case R.id.tv_my_email:
                    loadFragment(changeEmailFragment);

                case R.id.btn_password:
                    loadFragment(changePasswordFragment);
                    break;

                case R.id.go_to_password:
                    loadFragment(changePasswordFragment);
                    break;
            }
        }
    };


    private void signOut() {
        auth.signOut();
    }

    private void getCurrentUser() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    signOut();
                }
            }
        };
    }


    private void reAuthenticateUser() {
// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated." + task.getException());
                    }
                });
    }



    //        private void UpdateData() {
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            DocumentReference contact = db.collection("users");
//            String NAME_KEY, EMAIL_KEY, PHONE_KEY;
//            contact.(NAME_KEY, "Kenny");
//            contact.update(EMAIL_KEY, "kenny@gmail.com");
//            contact.update(PHONE_KEY, "090-911-419")
//                    .addOnSuccessListener(new OnSuccessListener< Void >() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(getActivity(), "Updated Successfully",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
//    }

    private void deleteAccount() {
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
    }

    private void deleteAllData() {
        //getUserId
        FireBaseInit.getInstance(getActivity()).db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                try {
                                    if (getEmailRegister() != null && getEmailRegister().equals(document.getId())) {
                                        //deleteUser
                                        FireBaseInit.getInstance(getActivity()).db.collection("users")
                                                .document(document.getId())
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                successDeleteData = true;
                                                Log.d(TAG,"Data deleted!");
                                            }
                                        });
                                    } else {
                                        Log.w(TAG, "Error set documents.", task.getException());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }



    private String getEmailRegister() {
        String email;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
            Log.d(TAG, "GET EMAIL");
            return email;
        } else {
            return null;
        }
    }



    public void loadFragment(Fragment fragment) {
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void alertDialog() {

    }







}
