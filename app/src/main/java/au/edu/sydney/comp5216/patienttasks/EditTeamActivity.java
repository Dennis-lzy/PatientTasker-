package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditTeamActivity extends AppCompatActivity implements TeamMemberViewAdapter.ItemClickListener{
    TeamMemberViewAdapter adapter;
    ArrayList<String> membersUID;
    ArrayList<String> membersEmail = new ArrayList<String>();
    private static final String TAG = "EditTeamActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String teamName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        //Change title
        setTitle(getString(R.string.team_management));
        TextView teamTV = findViewById(R.id.text_team_name);
        teamName = getIntent().getStringExtra("name");
        teamTV.setText(teamName);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_team_members);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeamMemberViewAdapter(this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //database query (async) for fetching team members list (UID)
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    DocumentReference docRef = db.collection("Teams").document(teamName);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    membersUID =(ArrayList<String>) document.getData().get(
                                            "userList");
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    Log.d(TAG, String.valueOf(document.getData().get("userList")));
                                    //Need to show emails instead so need to query emails from uid
                                    convertUidToEmail();
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    //convert UID into email
    public void convertUidToEmail(){
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    DocumentReference docRef = db.collection("user").document("UIDtoEmail");
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    for (String uid: membersUID){
                                        if (document.get(uid) != null){
                                            String email = (String) document.get(uid);
                                            membersEmail.add(email.replace(",", "."));
                                        }
                                    }
                                    adapter.teamMembers.addAll(membersEmail);
                                    adapter.notifyDataSetChanged();
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    Log.d(TAG, String.valueOf(document.getData().get("userList")));
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClickInvite(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter email to invite");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString();
                inviteMember(email);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void inviteMember(final String email){
        //Firebase cant tolerate '.', so have to replace it to a comma ','
        final String emailReplaced = email.replace(".", ",");
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    DocumentReference docRef = db.collection("user").document("emailToUID");
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if (document.get(emailReplaced) != null){
                                        String uid = (String) document.get(emailReplaced);
                                        adapter.teamMembers.add(email);
                                        //Add uid to team collection
                                        db.collection("Teams").document(teamName).update(
                                                "userList", FieldValue.arrayUnion(uid));
                                        //Add team to user's teamlist
                                        db.collection("user").document(uid).update(
                                                "teamList", FieldValue.arrayUnion(teamName));
                                        Toast.makeText(EditTeamActivity.this, "User added",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(EditTeamActivity.this, "Email not registered",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    Log.d(TAG, String.valueOf(document.getData().get("userList")));
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void onClickLeave(View v){
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        leaveTeam();
                    }
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditTeamActivity.this);
        builder.setTitle("Confirm leaving team")
                .setMessage("Leave the team? This is cannot be reversed")
                .setPositiveButton("Confirm", discardButtonClickListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void leaveTeam(){
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();
                    db.collection("Teams").document(teamName).update("userList",
                            FieldValue.arrayRemove(uid));
                    db.collection("user").document(uid).update("teamList",
                            FieldValue.arrayRemove(teamName));
                    Intent data = new Intent();
                    data.putExtra("deletedTeam", teamName);
                    setResult(RESULT_OK, data);
                    finish();
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}