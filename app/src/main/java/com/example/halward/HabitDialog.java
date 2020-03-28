package com.example.halward;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * @author yuliiamelnyk on 25/03/2020
 * @project Halward
 */
public class HabitDialog extends AppCompatDialogFragment {
private EditText mEditTextDate;
private EditText mEditTextDuration;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_habit, null);

        mEditTextDate = (EditText) view.findViewById(R.id.startDateDialog);
        mEditTextDuration = (EditText) view.findViewById(R.id.durationET);

        builder.setView(view)
                .setTitle(R.string.dialog_title)
                .setNegativeButton(R.string.terminate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.activate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return  builder.create();
    }
}
