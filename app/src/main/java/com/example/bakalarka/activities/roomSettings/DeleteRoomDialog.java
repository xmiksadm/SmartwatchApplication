package com.example.bakalarka.activities.roomSettings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.bakalarka.R;
import com.example.bakalarka.activities.overview.OverviewAllRoomsActivity;
import com.example.bakalarka.data.room.RoomController;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeleteRoomDialog extends DialogFragment {

    @NonNull
    public static DeleteRoomDialog newInstance(int roomId) {
        DeleteRoomDialog fragment = new DeleteRoomDialog();

        Bundle bundle = new Bundle();
        bundle.putInt("roomId", roomId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int roomId = getArguments().getInt("roomId");
        builder.setMessage(R.string.delete_room)
                .setPositiveButton(R.string.delete, (dialog, id) -> {

                    RoomController roomController = new RoomController();
                    roomController.removeRoom(roomId);
                    Intent intent = new Intent(this.getContext(), OverviewAllRoomsActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                    // FIRE ZE MISSILES!
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                });
        return builder.create();
    }
}
