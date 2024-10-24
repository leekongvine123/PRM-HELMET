package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.database_helper.DatabaseHelper;
import com.example.myapplication.model.ColorInfo;
import com.example.myapplication.model.Helmet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelmetDetailActivity extends AppCompatActivity {

    private ImageView helmetImageView, backBtn;
    private TextView helmetNameTextView, helmetPriceTextView, helmetDescriptionTextView, stockTextView;
    private LinearLayout colorOptionsLayout;
    private RadioGroup sizeRadioGroup;
    private Button addToCartButton;

    private List<String> availableColors;
    private Map<String, ColorInfo> colorToSizeMap;
    private DatabaseHelper dbHelper;
    private String selectedColor;
    private String selectedSize;
    private String generalPrice;

    private int helmetID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helmet_detail);

        dbHelper = new DatabaseHelper(this);

        helmetImageView = findViewById(R.id.productImageView);
        helmetNameTextView = findViewById(R.id.productNameTextView);
        helmetPriceTextView = findViewById(R.id.priceTextView);
        helmetDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        stockTextView = findViewById(R.id.stockTextView);
        colorOptionsLayout = findViewById(R.id.colorOptionsLayout);
        sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
        addToCartButton = findViewById(R.id.addToCartButton);
        backBtn = findViewById(R.id.backButton);
        // paypal


        // Initially disable Add to Cart button and set a different color for disabled state
        addToCartButton.setEnabled(false);
        addToCartButton.setBackgroundColor(Color.GRAY); // Set gray color when disabled

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            helmetID = getIntent().getIntExtra("helmetID", -1); // Retrieve helmetID from the intent
            if (helmetID == -1) {
                Toast.makeText(this, "Error: helmetID not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            String productCode = bundle.getString("productCode");
            String helmetName = bundle.getString("helmetName");
            Double helmetPrice = bundle.getDouble("helmetPrice");
            String helmetDescription = bundle.getString("helmetDescription");
            String imageUrl = bundle.getString("helmetImageUrl");

            helmetNameTextView.setText(helmetName);
            helmetDescriptionTextView.setText(helmetDescription);

            Glide.with(this).load(imageUrl).into(helmetImageView);

            // Fetch helmets by product code to get all colors and sizes
            List<Helmet> helmets = dbHelper.getHelmetsByProductCode(productCode);

            colorToSizeMap = new HashMap<>();
            double helmetLowestPrice = Double.MAX_VALUE; // Initialize to max value
            double helmetHighestPrice = Double.MIN_VALUE; // Initialize to min value

            for (Helmet helmet : helmets) {
                String color = helmet.getColor();
                String size = helmet.getSize();
                String imgUrl = helmet.getImageUrl();
                double price = helmet.getPrice(); // Assuming price is part of the helmet
                int stock = helmet.getStock(); // Assuming stock is part of the helmet

                // Update lowest and highest price
                if (price < helmetLowestPrice) {
                    helmetLowestPrice = price;
                }
                if (price > helmetHighestPrice) {
                    helmetHighestPrice = price;
                }

                // Create a SizeStock instance for the current size
                ColorInfo.SizeStock sizeStock = new ColorInfo.SizeStock(size, stock,price);

                // If the color does not exist in the map, create a new ColorInfo instance
                if (!colorToSizeMap.containsKey(color)) {
                    List<ColorInfo.SizeStock> sizeStocks = new ArrayList<>();
                    sizeStocks.add(sizeStock);
                    ColorInfo colorInfo = new ColorInfo(sizeStocks, imgUrl);
                    colorInfo.setColor(color);
                    colorToSizeMap.put(color, colorInfo);
                } else {
                    // Add the sizeStock to the existing ColorInfo instance
                    ColorInfo colorInfo = colorToSizeMap.get(color);

                    // Check if the size already exists for this color
                    boolean sizeExists = false;
                    for (ColorInfo.SizeStock existingSizeStock : colorInfo.getSizeStocks()) {
                        if (existingSizeStock.getSize().equals(size)) {
                            // If size exists, update the stock (optional)
                            existingSizeStock.setStock(existingSizeStock.getStock() + stock); // Accumulate stock
                            sizeExists = true;
                            break;
                        }
                    }

                    // If the size does not exist, add it
                    if (!sizeExists) {
                        colorInfo.getSizeStocks().add(sizeStock);
                    }
                }
            }

// Update price text view
            generalPrice ="$" + helmetLowestPrice + " - $" + helmetHighestPrice;
            helmetPriceTextView.setText(generalPrice);



            availableColors = new ArrayList<>(colorToSizeMap.keySet());
            // Load available colors and sizes
            loadAvailableColors();
        }

        backBtn.setOnClickListener(v-> back());

       addToCartButton.setOnClickListener(v -> addToCart());

       //addToCartButton.setOnClickListener(v -> getPayment());

    }

    private void back() {
        Intent intent = new Intent(HelmetDetailActivity.this, MainActivity.class); // Go back to MainActivity
        intent.putExtra("navigateToHome", true); // Optional: Pass a flag to indicate that HomeFragment should be loaded
        startActivity(intent); // Start the MainActivity
        finish(); // Close the current activity
    }

    private void loadAvailableColors() {
        colorOptionsLayout.removeAllViews(); // Clear existing color options

        for (String color : availableColors) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    70, // width in pixels
                    70  // height in pixels
            );
            params.setMargins(0, 0, 15, 0); // Set margin to the right
            View colorView = new View(HelmetDetailActivity.this);
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
                case "green":
                    backgroundColor = Color.GREEN;
                    break;
                // Add more cases for additional colors
                default:
                    backgroundColor = Color.GRAY; // Fallback color
                    break;
            }

            // Create the LayerDrawable with rounded corners and a black border
            colorView.setBackground(createLayerDrawable(backgroundColor,1));

            colorView.setOnClickListener(v -> {
                selectedColor = color.toLowerCase();
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
                case "yellow":
                    originalColor = Color.GREEN;
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


    private void onColorSelected(String color, View colorView) {
        selectedColor = color;
        sizeRadioGroup.removeAllViews();

        // Get the ColorInfo for the selected color
        ColorInfo colorInfo = colorToSizeMap.get(selectedColor);
        List<ColorInfo.SizeStock> sizeStocks = colorInfo.getSizeStocks();
        String imageUrl = colorInfo.getImageUrl();

        helmetPriceTextView.setText(generalPrice);

        // Load the image based on the selected color
        Glide.with(this).load(imageUrl).into(helmetImageView);

        // Set the price
        stockTextView.setText("Unselected Sizes");
        // Populate size options
        for (ColorInfo.SizeStock sizeStock : sizeStocks) {
            RadioButton sizeButton = new RadioButton(HelmetDetailActivity.this);
            sizeButton.setText(sizeStock.getSize());
            sizeRadioGroup.addView(sizeButton);

            // Add click listener for size selection
            sizeButton.setOnClickListener(v -> {
                selectedSize = sizeStock.getSize();
                // Check stock for the selected size
                int stock = sizeStock.getStock();

                // Update stock display (optional)
                stockTextView.setText("Stock: " + stock);
                helmetPriceTextView.setText("$" + sizeStock.getPrice());

                // Enable or disable the Add to Cart button based on stock
                addToCartButton.setEnabled(stock > 0);

                checkSelections();
            });
        }

        // Disable Add to Cart button if no sizes are available
        boolean hasAvailableSize = sizeStocks.stream().anyMatch(sizeStock -> sizeStock.getStock() > 0);
        addToCartButton.setEnabled(hasAvailableSize);

        updateColorPreview(selectedColor, colorView);
    }


    private void checkSelections() {
        if (selectedColor != null && selectedSize != null) {
            addToCartButton.setEnabled(true);
            addToCartButton.setBackgroundColor(getResources().getColor(R.color.teal_200));
        } else {
            addToCartButton.setEnabled(false);
            addToCartButton.setBackgroundColor(Color.GRAY);
        }
    }

    private void addToCart() {
        // Ensure both color and size are selected
        if (selectedColor == null || selectedSize == null) {
            Toast.makeText(HelmetDetailActivity.this, "Please select both color and size", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add item to cart
        dbHelper.addItemToCart(
                helmetID,
                helmetNameTextView.getText().toString(),
                selectedSize,
                selectedColor,
                1, // Default quantity is 1
                Double.parseDouble(helmetPriceTextView.getText().toString().replace("$", "")),
                1
        );
        Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
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

}

