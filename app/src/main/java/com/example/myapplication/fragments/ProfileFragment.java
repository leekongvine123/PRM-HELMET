package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {
    private EditText nameEditText, emailEditText, phoneEditText, addressEditText;
    private Button editButton, logoutButton, changePasswordButton;
    private boolean isEditMode = false;

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private DatabaseHelper databaseHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseHelper = new DatabaseHelper(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        initializeViews(view);
        setupListeners();
        loadUserData();

        return view;
    }

    private void initializeViews(View view) {
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        editButton = view.findViewById(R.id.editButton);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);
        logoutButton = view.findViewById(R.id.logoutButton);
    }
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_password_dialog, null);
        builder.setView(dialogView);

        TextInputEditText currentPasswordInput = dialogView.findViewById(R.id.currentPasswordInput);
        TextInputEditText newPasswordInput = dialogView.findViewById(R.id.newPasswordInput);
        TextInputEditText confirmNewPasswordInput = dialogView.findViewById(R.id.confirmNewPasswordInput);

        builder.setTitle("Change Password")
                .setPositiveButton("Change", null) // Set to null initially
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();


        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                String currentPassword = currentPasswordInput.getText().toString().trim();
                String newPassword = newPasswordInput.getText().toString().trim();
                String confirmPassword = confirmNewPasswordInput.getText().toString().trim();

                if (validatePasswordInputs(currentPassword, newPassword, confirmPassword)) {
                    changePassword(currentPassword, newPassword, dialog);
                }
            });
        });

        dialog.show();
    }

    private boolean validatePasswordInputs(String currentPassword, String newPassword, String confirmPassword) {
        if (currentPassword.isEmpty()) {
            showError("Please enter your current password");
            return false;
        }

        if (newPassword.isEmpty()) {
            showError("Please enter a new password");
            return false;
        }

        if (newPassword.length() < 8) {
            showError("New password must be at least 8 characters long");
            return false;
        }
        if(!isPasswordStrong(newPassword)){
            showError("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("New passwords do not match");
            return false;
        }

        return true;
    }
    private boolean isPasswordStrong(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        return pattern.matcher(password).matches();
    }
    private void changePassword(String currentPassword, String newPassword, AlertDialog dialog) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> {

                        user.updatePassword(newPassword)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        showSuccess("Password updated successfully");

                                    } else {
                                        showError("Failed to update password: " + task.getException().getMessage());
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        showError("Current password is incorrect");
                    });
        }
    }

    private void showError(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccess(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
    private void setupListeners() {
        editButton.setOnClickListener(v -> handleEditAction());
        changePasswordButton.setOnClickListener(v -> showChangePasswordDialog());
        logoutButton.setOnClickListener(v -> logout());
    }

    private void handleEditAction() {
        if (!isEditMode) {

            enableEditMode(true);
        } else {

            if (validateInputs()) {
                saveUserData();
            }
        }
    }

    private void enableEditMode(boolean enable) {
        isEditMode = enable;
        nameEditText.setEnabled(enable);
        phoneEditText.setEnabled(enable);
        addressEditText.setEnabled(enable);

        emailEditText.setEnabled(false);

        editButton.setText(enable ? "Save Changes" : "Edit Profile");

        if (enable) {
            nameEditText.requestFocus();
            showSoftKeyboard(nameEditText);
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (nameEditText.getText().toString().trim().isEmpty()) {
            nameEditText.setError("Name cannot be empty");
            isValid = false;
        }

        if (phoneEditText.getText().toString().trim().isEmpty()) {
            phoneEditText.setError("Phone cannot be empty");
            isValid = false;
        }

        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Address cannot be empty");
            isValid = false;
        }

        return isValid;
    }

    private void saveUserData() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();
            User updatedUser = new User(
                    nameEditText.getText().toString().trim(),
                    email,
                    phoneEditText.getText().toString().trim(),
                    addressEditText.getText().toString().trim()
            );

            try {
                int updated = databaseHelper.updateUserByGmail(updatedUser);
                if (updated>0) {
                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    enableEditMode(false);
                } else {
                    Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("ProfileFragment", "Error updating user data", e);
                Toast.makeText(getActivity(), "Error updating profile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadUserData() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();
            User user = databaseHelper.getUserByEmail(email);

            if (user != null) {
                nameEditText.setText(user.getName());
                emailEditText.setText(user.getEmail());
                phoneEditText.setText(user.getPhone());
                addressEditText.setText(user.getAddress());
            } else {
                Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                Log.e("ProfileFragment", "User data not found in the database");
            }
        } else {
            Toast.makeText(getActivity(), "No logged-in user", Toast.LENGTH_SHORT).show();
            Log.e("ProfileFragment", "FirebaseUser is null");
        }
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void logout() {
        mAuth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getActivity(), "Successfully logged out", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Logout failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
