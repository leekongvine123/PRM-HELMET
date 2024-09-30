package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.model.Helmet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelmetDetailActivity extends AppCompatActivity {

    private ImageView helmetImageView, backBtn;
    private TextView helmetNameTextView, helmetPriceTextView, helmetDescriptionTextView;
    private LinearLayout colorOptionsLayout;
    private RadioGroup sizeRadioGroup;

    private List<String> availableColors;
    private Map<String, List<String>> colorToSizeMap;
    private DatabaseHelper dbHelper;
    private String selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helmet_detail);

        dbHelper = new DatabaseHelper(this);

        helmetImageView = findViewById(R.id.productImageView);
        helmetNameTextView = findViewById(R.id.productNameTextView);
        helmetPriceTextView = findViewById(R.id.priceTextView);
        helmetDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        colorOptionsLayout = findViewById(R.id.colorOptionsLayout);
        sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
        backBtn = findViewById(R.id.backButton);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String productCode = bundle.getString("productCode");
            String helmetName = bundle.getString("helmetName");
            String helmetPrice = bundle.getString("helmetPrice");
            String helmetDescription = bundle.getString("helmetDescription");
            String imageUrl = bundle.getString("helmetImageUrl");

            helmetNameTextView.setText(helmetName);
            helmetPriceTextView.setText(helmetPrice);
            helmetDescriptionTextView.setText(helmetDescription);

            Glide.with(this).load(imageUrl).into(helmetImageView);

            // Fetch helmets by product code to get all colors and sizes
            List<Helmet> helmets = dbHelper.getHelmetsByProductCode(productCode);

            // Organize helmets by color and size
            colorToSizeMap = new HashMap<>();
            for (Helmet helmet : helmets) {
                String color = helmet.getColor();
                String size = helmet.getSize();

                if (!colorToSizeMap.containsKey(color)) {
                    colorToSizeMap.put(color, new ArrayList<>());
                }
                colorToSizeMap.get(color).add(size);
            }

            availableColors = new ArrayList<>(colorToSizeMap.keySet());

            // Load available colors and sizes
            loadAvailableColors();
        }

        backBtn.setOnClickListener(v-> back());

    }

    private void back() {
        Intent intent = new Intent(this, HomeFragment.class); // Replace with your previous activity class
        startActivity(intent); // Start the previous activity
        finish();
    }

    private void loadAvailableColors() {
        colorOptionsLayout.removeAllViews(); // Clear existing color options

        for (String color : availableColors) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    70, // width in pixels
                    70  // height in pixels
            );
            params.setMargins(0, 0, 15, 0); // Set margin to the right
            View colorView = new View(this);
            colorView.setLayoutParams(params); // Apply layout parameters

            // Determine the background color based on the available colors
            int backgroundColor;

            switch (color.toLowerCase()) {
                case "black":
                    backgroundColor = Color.BLACK;
                    break;
                case "red":
                    backgroundColor = Color.RED;
                    break;
                case "blue":
                    backgroundColor = Color.BLUE;
                    break;
                case "yellow":
                    backgroundColor = Color.YELLOW;
                    break;
                // Add more cases for additional colors
                default:
                    backgroundColor = Color.GRAY; // Fallback color
                    break;
            }

            // Create the LayerDrawable with rounded corners and a black border
            colorView.setBackground(createLayerDrawable(backgroundColor,1));

            colorView.setOnClickListener(v -> {
                selectedColor =color.toLowerCase();
                resetColorViews(); // Reset the styles of all views
                // Highlight the selected color with a heavier border
             //   colorView.setBackground(createLayerDrawable(backgroundColor)); // Keep the same background but reset the view
                onColorSelected(color,colorView); // Your method to handle color selection
            });

            colorOptionsLayout.addView(colorView);
        }

        if (!availableColors.isEmpty()) {
          //  onColorSelected(availableColors.get(0), null);  // Pre-select the first color
        }
    }


    private LayerDrawable createLayerDrawable(int backgroundColor,int option) {
        // Create the background shape with rounded corners
        float[] radii = new float[8]; // 8 values for the 4 corners (top-left, top-right, bottom-right, bottom-left)
        Arrays.fill(radii, 35); // Set radius for each corner, change this value for more or less rounding

        // Create the background shape
        RoundRectShape backgroundShape = new RoundRectShape(radii, null, null);
        ShapeDrawable backgroundDrawable = new ShapeDrawable(backgroundShape);
        backgroundDrawable.getPaint().setColor(backgroundColor); // Set the background color

        // Create the border shape with rounded corners
        ShapeDrawable borderDrawable = new ShapeDrawable(new RoundRectShape(radii, null, null));
        borderDrawable.getPaint().setColor(Color.BLACK); // Set the border color to black
        switch (option)
        {
            case 1:
                borderDrawable.setPadding(0, 0, 0, 0); // Set padding for the border
                break;
            case 2:
                borderDrawable.setPadding(5, 5, 5, 5); // Set padding for the border
        }

        // Create the LayerDrawable with the border and background
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{borderDrawable, backgroundDrawable});

        return layerDrawable;
    }


    private void resetColorViews() {
        // Loop through all child views in colorOptionsLayout
        for (int i = 0; i < colorOptionsLayout.getChildCount(); i++) {
            View colorView = colorOptionsLayout.getChildAt(i);

            // Reset the color view to its original style (with the light border)
            // Assuming you have a method to get the original color based on your availableColors list
            String colorName = availableColors.get(i);
            int originalColor;

            switch (colorName.toLowerCase()) {
                case "black":
                    originalColor = Color.BLACK;
                    break;
                case "red":
                    originalColor = Color.RED;
                    break;
                case "blue":
                    originalColor = Color.BLUE;
                    break;
                case "green":
                    originalColor = Color.GREEN;
                    break;
                // Add more cases for additional colors
                default:
                    originalColor = Color.GRAY; // Fallback color
                    break;
            }

            // Reset background with light border
            colorView.setBackground(createLayerDrawable(originalColor,1));
        }
    }


    private void onColorSelected(String color,View colorView) {
        // Logic for handling what happens when a color is selected
        // For example, you could update a selected color variable
        selectedColor = color; // Assuming you have a variable to store the currently selected color
        sizeRadioGroup.removeAllViews();
        List<String> availableSizes = colorToSizeMap.get(selectedColor);
        for (String size : availableSizes) {
            RadioButton sizeButton = new RadioButton(this);
            sizeButton.setText(size);
            sizeRadioGroup.addView(sizeButton);
        }
        // Update any other UI elements that might be affected by the color change
        // For example, change the preview of the selected color
        updateColorPreview(selectedColor,colorView);
    }

    private void updateColorPreview(String color, View colorView) {
        // Implementation to update any UI element with the selected color
        // For example, if you have a preview layout:
        int previewColor;

        switch (color.toLowerCase()) {
            case "black":
                previewColor = Color.BLACK;
                break;
            case "red":
                previewColor = Color.RED;
                break;
            case "blue":
                previewColor = Color.BLUE;
                break;
            case "green":
                previewColor = Color.GREEN;
                break;
            // Add more cases for additional colors
            default:
                previewColor = Color.GRAY; // Fallback color
                break;
        }

        // Assuming you have a preview view in your layout
        colorView.setBackground(createLayerDrawable(previewColor,2));
    }


    private int getColorFromName(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red":
                return getResources().getColor(R.color.red);
            case "blue":
                return getResources().getColor(R.color.blue);
            case "yellow":
                return getResources().getColor(R.color.yellow);
            default:
                return getResources().getColor(R.color.black);
        }
    }
}

