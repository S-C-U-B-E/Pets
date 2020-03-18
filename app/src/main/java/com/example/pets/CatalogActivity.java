/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pets.data.PetsContract.PetsEntry;
import com.example.pets.EditorActivity;
import com.example.pets.R;
import com.example.pets.data.PetsContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    private Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PetsEntry._ID,
                PetsEntry.COLUMN_PETS_NAME,
                PetsEntry.COLUMN_PETS_BREED,
                PetsEntry.COLUMN_PETS_GENDER,
                PetsEntry.COLUMN_PETS_WEIGHT };

        try {
             cursor = getContentResolver().query(
                    PetsEntry.CONTENT_URI,   // The content URI of the words table
                    projection,             // The columns to return for each row
                    null,                   // Selection criteria
                    null,                   // Selection criteria
                    null);                  // The sort order for the returned rows
        }catch (Exception e){
            Toast.makeText( this ,"UNKNOWN URI PROVIDED ;" + e.toString(), Toast.LENGTH_LONG).show();
        }

        TextView displayView = (TextView) findViewById(R.id.text_view_pet);


        if(cursor != null) {

            try {

                // Perform a query on the provider using the ContentResolver.
                // Use the {@link PetEntry#CONTENT_URI} to access the pet data.

                // Create a header in the Text View that looks like this:
                //
                // The pets table contains <number of rows in Cursor> pets.
                // _id - name - breed - gender - weight
                //
                // In the while loop below, iterate through the rows of the cursor and display
                // the information from each column in this order.
                displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
                displayView.append(PetsEntry._ID + " - " +
                        PetsEntry.COLUMN_PETS_NAME + " - " +
                        PetsEntry.COLUMN_PETS_BREED + " - " +
                        PetsEntry.COLUMN_PETS_GENDER + " - " +
                        PetsEntry.COLUMN_PETS_WEIGHT + "\n");


                // Figure out the index of each column
                int idColumnIndex = cursor.getColumnIndex(PetsContract.PetsEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PETS_NAME);
                int breedColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PETS_BREED);
                int genderColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PETS_GENDER);
                int weightColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PETS_WEIGHT);

                // Iterate through all the returned rows in the cursor
                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentBreed = cursor.getString(breedColumnIndex);
                    int currentGender = cursor.getInt(genderColumnIndex);
                    int currentWeight = cursor.getInt(weightColumnIndex);
                    // Display the values from each column of the current row in the cursor in the TextView
                    displayView.append(("\n" + currentID + " - " +
                            currentName + " - " +
                            currentBreed + " - " +
                            currentGender + " - " +
                            currentWeight));
                }
            } finally {
                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
                cursor.close();
            }
        }
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertPet() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PetsEntry.COLUMN_PETS_NAME, "Toto");
        values.put(PetsEntry.COLUMN_PETS_BREED, "Terrier");
        values.put(PetsEntry.COLUMN_PETS_GENDER, PetsEntry.GENDER_MALE);
        values.put(PetsEntry.COLUMN_PETS_WEIGHT, 7);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        try{
        Uri newUri = getContentResolver().insert(PetsEntry.CONTENT_URI, values);
        }catch(Exception e){
            Toast.makeText( this ,"UNKNOWN URI PROVIDED ;" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
