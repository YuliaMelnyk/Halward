package com.example.halward.addActivity;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.halward.R;
import com.example.halward.homePage.HomeActivity;
import com.example.halward.model.Habit;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHabitFragment extends Fragment {

    Context mContext;

    View view;
    private EditText mTitle, mDescription, mDuration;

    private FirebaseFirestore mFirestore;
    private StorageReference mStorageRef;
    private FirebaseAuth auth;


    private Switch mSwitch;
    private Button mButton;

    private ImageButton mMonday, mTuesday,
            mWednesday, mThursday, mFriday, mSaturday, mSunday;

    private ImageButton mPhoto;

    private Bitmap image;

    private Habit mHabit;

    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private String habitImage;

    FrameLayout mView;

    ProgressBar progressBar;

    //private TextView textProgress;   ---for text  % Progress Bar

    public AddHabitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_habit, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mView = (FrameLayout) view.findViewById(R.id.view_progressBar);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mPhoto = (ImageButton) view.findViewById(R.id.back_home);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(intent);

            }
        });

        mSwitch = (Switch) view.findViewById(R.id.switch_desc);

        mTitle = (EditText) view.findViewById(R.id.add_title_ed);

        mDescription = (EditText) view.findViewById(R.id.add_description_ed);
        mButton = (Button) view.findViewById(R.id.add_button);

        mDuration = (EditText) view.findViewById(R.id.add_duration);
        hideKeyboardFrom(getContext(), view);

        mPhoto = (ImageButton) view.findViewById(R.id.add_photo);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mMonday = (ImageButton) view.findViewById(R.id.monday);
        mMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMonday.setBackgroundResource(R.drawable.monday);
            }
        });
        mTuesday = (ImageButton) view.findViewById(R.id.tuesday);
        mTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTuesday.setBackgroundResource(R.drawable.tuesday);
            }
        });
        mWednesday = (ImageButton) view.findViewById(R.id.wednesday);
        mWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWednesday.setBackgroundResource(R.drawable.wensday);
            }
        });
        mThursday = (ImageButton) view.findViewById(R.id.thursday);
        mThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThursday.setBackgroundResource(R.drawable.tuesday);
            }
        });
        mFriday = (ImageButton) view.findViewById(R.id.friday);
        mFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFriday.setBackgroundResource(R.drawable.friday);
            }
        });

        mSaturday = (ImageButton) view.findViewById(R.id.saturday);
        mSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaturday.setBackgroundResource(R.drawable.saturday);
            }
        });
        mSunday = (ImageButton) view.findViewById(R.id.sunday);
        mSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSunday.setBackgroundResource(R.drawable.saturday);
            }
        });


// save to FireBase
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CollectionReference habits = mFirestore.collection("habits");
                final String titleName = mTitle.getText().toString();
                final String descHabit = mDescription.getText().toString();
                final String durTime = mDuration.getText().toString();


                if (imageUri != null) {
                mView.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                putFile().addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);
                        //textProgress.setText((int)progress+"%");     ---   for text  % Progress Bar

                    }
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return task.getResult().getStorage().getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        //progressBar.setVisibility(View.INVISIBLE);

                        Uri downloadUrl = task.getResult();
                        habitImage = downloadUrl.toString();

                        if (!TextUtils.isEmpty(titleName) && !TextUtils.isEmpty(descHabit) && !TextUtils.isEmpty(durTime) && !TextUtils.isEmpty(habitImage)) {

                            Habit habit = new Habit();
                            habit.setName(titleName);
                            habit.setDescription(descHabit);
                            habit.setStartDate(new Date());
                            habit.setDuration(Integer.parseInt(durTime));
                            habit.setImage(habitImage);
                            habit.setActive(true);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            //Getting current date
                            Calendar cal = Calendar.getInstance();

                            //Number of Days to add
                            cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(durTime));
                            //Date after adding the days to the current date
                            String endDate = sdf.format(cal.getTime());
                            Date date1= null;
                            try {
                                date1 = new SimpleDateFormat("yyyy/MM/dd").parse(endDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            habit.setEndDate(date1);

                            habits.add(habit).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent myIntent = new Intent(getActivity(), HomeActivity.class);
                                    getActivity().startActivity(myIntent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("NoInsertaHabit", e.getStackTrace().toString());
                                }
                            });

                        }
                        else {
                            Toast.makeText(mContext, "Fill all the fields", Toast.LENGTH_SHORT).show();
                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("NoInsertaHabit", e.getStackTrace().toString());
                    }
                });
                 } else {
                   if (!TextUtils.isEmpty(titleName) && !TextUtils.isEmpty(descHabit) && !TextUtils.isEmpty(durTime)){
                       Habit habit = new Habit();
                       habit.setName(titleName);
                       habit.setDescription(descHabit);
                       habit.setStartDate(new Date());
                       habit.setDuration(Integer.parseInt(durTime));
                       habit.setActive(true);
                       habit.setImage("https://firebasestorage.googleapis.com/v0/b/halward-2932c.appspot.com/o/default.png?alt=media&token=a71f5ff6-01e5-4574-bc32-726fa8f65bd1");

                       SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                       //Getting current date
                       Calendar cal = Calendar.getInstance();

                       //Number of Days to add
                       cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(durTime));
                       //Date after adding the days to the current date
                       String endDate = sdf.format(cal.getTime());
                       Date date1= null;
                       try {
                           date1 = new SimpleDateFormat("yyyy/MM/dd").parse(endDate);
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }
                       habit.setEndDate(date1);

                       habits.add(habit).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                           @Override
                           public void onSuccess(DocumentReference documentReference) {
                               Intent myIntent = new Intent(getActivity(), HomeActivity.class);
                               getActivity().startActivity(myIntent);
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                               Log.e("NoInsertaHabit", e.getStackTrace().toString());
                           }
                       });
                   } else {
                  Toast.makeText(mContext, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
                }
            }
        });

        return view;
    }

    private StorageTask<UploadTask.TaskSnapshot> putFile() {
        StorageReference ref = mStorageRef.child(System.currentTimeMillis() + "." + getExtenion(imageUri));

        return ref.putFile(imageUri)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        String t = exception.getMessage();
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    private String getExtenion(Uri uri) {
        if (uri == null) {
            return null;
        }
        ContentResolver cr = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            mPhoto.setImageURI(imageUri);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
