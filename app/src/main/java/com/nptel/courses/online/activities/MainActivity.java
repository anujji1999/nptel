package com.nptel.courses.online.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.nptel.courses.online.MyApplication;
import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.R;
import com.nptel.courses.online.ViewModels.MainActivityViewModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private Map<String, String> courseMap;
    private String selectedCourseID;
    private String selectedCourseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!MyApplication.courseId.equals("00")) {
            Intent intent = new Intent(MainActivity.this, CourseActivity.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDynamicLinks
                .getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        if (pendingDynamicLinkData != null) {
                            Uri deepLink = pendingDynamicLinkData.getLink();
                            String videoID = deepLink.toString().replace("https://youtu.be/", "");
                            if (videoID.length() > 15)
                                return;
                            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                            intent.putExtra("playlistID", "shared");
                            intent.putExtra("videoID", videoID);
                            startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Dynamic Link", "getDynamicLink:onFailure", e);
                    }
                });

        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getCourseList().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                courseMap = new LinkedHashMap<>();
                for (Course course : courses) {
                    courseMap.put(course.getCourseID(), course.getCourseName());
                }
                if (courseMap.size() > 0) {
                    findViewById(R.id.plusButton).setEnabled(true);
                    ((MaterialButton) findViewById(R.id.plusButton)).setText(R.string.tap_here_to_explore);
                }
            }
        });

    }

    public void showCourseChooser() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setCancelable(true);
        alert.setTitle("Select a Course (can be changed later)");
        alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (selectedCourseID != null) {
                    setCourse(selectedCourseID, selectedCourseName);
                    dialogInterface.cancel();
                } else
                    Toast.makeText(MainActivity.this, "Please select a course first", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                selectedCourseID = null;
            }
        });
        alert.setSingleChoiceItems(courseMap.values().toArray(new CharSequence[0]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                selectedCourseID = courseMap.keySet().toArray(new String[0])[position];
                selectedCourseName = courseMap.values().toArray(new String[0])[position];
            }
        });
        alert.show();
    }
    public void setCourse(String courseID, String courseName) {
        Intent intent = new Intent(MainActivity.this, CourseActivity.class);
        intent.putExtra("courseID", courseID);
        intent.putExtra("courseName", courseName);
        startActivity(intent);
        MyApplication.courseId = courseID;
        MyApplication.courseName = courseName;
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString("courseID", courseID)
                .putString("courseName", courseName)
                .apply();
        finish();
    }

    public void onClick(View view) {
        showCourseChooser();
    }

}