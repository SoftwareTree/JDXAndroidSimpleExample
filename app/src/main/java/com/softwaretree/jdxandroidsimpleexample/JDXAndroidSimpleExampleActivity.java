package com.softwaretree.jdxandroidsimpleexample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import com.softwaretree.jdx.JDXHelper;
import com.softwaretree.jdx.JDXSetup;
import com.softwaretree.jdxandroid.DatabaseAndJDX_Initializer;
import com.softwaretree.jdxandroid.Utils;
import com.softwaretree.jdxandroidsimpleexample.model.ClassA;
import com.softwaretree.jx.JXUtilities;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This project exemplifies how JDXA ORM and associated utilities can be used to easily develop 
 * an Android application that exchanges data of domain model objects with an SQLite database.
 * In particular, this project demonstrates the following:
 * <p>
 * 1) How an ORM Specification (mapping) for domain model classes can be defined textually using
 * simple statements.  The mapping is specified in a text file \res\raw\simple_example.jdx identified 
 * by the resource id R.raw.simple_example.
 * <p>
 * 2) Use of {@link AppSpecificJDXSetup} and {@link DatabaseAndJDX_Initializer} classes to easily: 
 *   <br>&nbsp;&nbsp;&nbsp;&nbsp;  a) create the underlying database, if not already present. 
 *   <br>&nbsp;&nbsp;&nbsp;&nbsp;  b) create the schema (tables and constraints) corresponding to the JDXA ORM specification 
 *      every time the application runs.  See setForceCreateSchema(true) in {@link AppSpecificJDXSetup#initialize()}. 
 * <p>
 * 3) Examples of how just a few lines of object-oriented code incorporating JDX APIs 
 * can be used to easily interact with relational data.  This avoids tedious and 
 * time-consuming coding/maintenance of low-level SQL statements.  
 * <p> 
 * 4) Example of how a {@link JDXHelper} facade object can be used to interact with relational
 * data using simpler methods.    
 * <p>  
 * 5) Examples of how details of an object or a list of objects can be added in JDX log and 
 * how that output can be collected in a file and then displayed in a scrollable TextBox.
 * <p>
 * @author Damodar Periwal
 */
public class JDXAndroidSimpleExampleActivity extends Activity {
    JDXSetup jdxSetup = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.setTitle(getResources().getString(R.string.activity_title));
        
        TextView tvJDXLog = (TextView) findViewById(R.id.tvJDXLog);
        tvJDXLog.setMovementMethod(new ScrollingMovementMethod());

        try {
            AppSpecificJDXSetup.initialize();  // must be done before calling getInstance()
            jdxSetup = AppSpecificJDXSetup.getInstance(this);

            // Use a JDXHelper object to easily configure capturing of JDX log output 
            JDXHelper jdxHelper = new JDXHelper(jdxSetup);
            String jdxLogFileName = getFilesDir() + "jdx.log";
            jdxHelper.setJDXLogging(jdxLogFileName);

            useJDXORM(jdxHelper);
            
            jdxHelper.resetJDXLogging();
           
            // Show the captured JDX log on the screen
            tvJDXLog.setText(Utils.getTextFileContents(jdxLogFileName));

        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Exception: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            cleanup();
            return;
        }
    }

    /**
     * Do the necessary cleanup.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanup();
    }
    
    private void cleanup() {
        AppSpecificJDXSetup.cleanup(); // Do this when the application is exiting.
        jdxSetup = null;
    }
    
    /**
     * Shows some simple examples of using JDXA ORM APIs to exchange object data with a relational database.
     * <p>
     * @param jdxHelper A helper object to further simplify interactions with the JDXA ORM system.
     * @throws Exception
     */
    private void useJDXORM(JDXHelper jdxHelper) throws Exception {
        if (null == jdxHelper) {
            return;
        }
        
        String aClassName = ClassA.class.getName();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
        dfm.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        // First delete all existing ClassA objects from the database.
        JXUtilities.log("\n-- First deleting all the existing ClassA objects from the database --\n"); 
        jdxHelper.delete2(aClassName, null);

        // Create and save a new ClassA object with aId=1
        JXUtilities.log("\n-- Creating and saving three ClassA objects (A1, A2, and A3) in the database --\n");
        ClassA aObject = new ClassA(1, "A1", dfm.parse("1981-01-01"), true, (float) 1.1);
        jdxHelper.insert(aObject, false);

        // Create and save a new ClassA object with aId=2
        aObject = new ClassA(2, "A2", dfm.parse("1982-02-02"), false, (float) 2.2);
        jdxHelper.insert(aObject, false);
        
        // Create and save a new ClassA object with aId=3
        aObject = new ClassA(3, "A3", dfm.parse("1983-03-03"), false, (float) 3.3);
        jdxHelper.insert(aObject, false);

        // Retrieve all the ClassA objects from the database
        JXUtilities.log("\n-- getObjects for all the ClassA objects --\n");
        List queryResults = jdxHelper.getObjects(aClassName, null);
        JXUtilities.printQueryResults(queryResults);
        
        // Retrieve all the ClassA objects from the database in the descending order of aId
        JXUtilities.log("\n-- getObjects for all the ClassA objects in the descending order of aId --\n");
        queryResults = jdxHelper.getObjects(aClassName, "ORDER BY aId DESC");
        JXUtilities.printQueryResults(queryResults);
        
        // Retrieve all the ClassA objects from the database with a search condition (predicate)
        JXUtilities.log("\n-- getObjects for all the ClassA objects with a search condition of aFloat > 1.5 --\n");
        queryResults = jdxHelper.getObjects(aClassName, "aFloat > 1.5");
        JXUtilities.printQueryResults(queryResults);
        
        // Retrieve all the ClassA objects from the database with a search condition (predicate) and in a certain order
        JXUtilities.log("\n-- getObjects for all the ClassA objects with a search condition of aFloat > 1.5 --\n");
        queryResults = jdxHelper.getObjects(aClassName, "aFloat > 1.5 ORDER BY aDate DESC");
        JXUtilities.printQueryResults(queryResults);

        // Retrieve the ClassA object with aId=2
        JXUtilities.log("\n-- getObjectById for aId=2 --\n");
        aObject = (ClassA) jdxHelper.getObjectById(aClassName, "aId=2", false, null);       
        JXUtilities.printObject(aObject, 0, null);

        // Change some attributes of the retrieved object and update it in the database 
        aObject.setABoolean(true);
        aObject.setAFloat((float) 2.22);
        jdxHelper.update(aObject, false);

        // Retrieve the updated ClassA object with aId=2
        JXUtilities.log("\n-- getObjectById for aId=2 after updating the object --\n"); 
        aObject = (ClassA) jdxHelper.getObjectById(aClassName, "aId=2", false, null);       
        JXUtilities.printObject(aObject, 0, null);
        
        // Delete the just updated object with aId=2
        JXUtilities.log("\n-- Deleting the object with aId=2 --\n");
        jdxHelper.delete(aObject, false);
        
        // Retrieve all the objects
        JXUtilities.log("\n-- getObjects for all the ClassA objects --\n");
        queryResults = jdxHelper.getObjects(aClassName, null);        
        JXUtilities.printQueryResults(queryResults);   

        return;
    }
}