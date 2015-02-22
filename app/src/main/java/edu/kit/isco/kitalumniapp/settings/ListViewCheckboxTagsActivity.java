package edu.kit.isco.kitalumniapp.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import edu.kit.isco.kitalumniapp.R;
import edu.kit.isco.kitalumniapp.adapter.CheckboxTagAdapter;
import edu.kit.isco.kitalumniapp.dbObjects.DataAccessTag;
import edu.kit.isco.kitalumniapp.gcm.ServerUtilities;


public class ListViewCheckboxTagsActivity extends Activity {

    CheckboxTagAdapter dataAdapter = null;
    private String[] tagsStringArray;
    //Array list of tags
    ArrayList<Tag> tagList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    public static final String PROPERTY_REG_ID = "registration_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        Button saveButton = (Button)findViewById(R.id.saveButton);
        sharedPreferences = getSharedPreferences(ListViewCheckboxTagsActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DataAccessTag> checkedTagList= new ArrayList<DataAccessTag>();
                for(int i = 0;i< tagList.size(); i++){
                    if(sharedPreferences.getBoolean(tagList.get(i).getName(),true)){
                        checkedTagList.add(tagList.get(i));
                    }
                }
                ServerUtilities updater = new ServerUtilities();
                updater.updateUser(getApplicationContext(),checkedTagList,sharedPreferences.getString(PROPERTY_REG_ID,""));

            }
        });
        //Generate list View from ArrayList
        displayListView();
    }

    private void displayListView() {
        tagsStringArray = getResources().getStringArray(R.array.tags);


        tagList.add(new Tag(tagsStringArray[0]));
        tagList.add(new Tag(tagsStringArray[1]));
        tagList.add(new Tag(tagsStringArray[2]));
        tagList.add(new Tag(tagsStringArray[3]));
        tagList.add(new Tag(tagsStringArray[4]));
        tagList.add(new Tag(tagsStringArray[5]));
        tagList.add(new Tag(tagsStringArray[6]));
        tagList.add(new Tag(tagsStringArray[7]));
        tagList.add(new Tag(tagsStringArray[8]));
        tagList.add(new Tag(tagsStringArray[9]));
        tagList.add(new Tag(tagsStringArray[10]));
        tagList.add(new Tag(tagsStringArray[11]));
        tagList.add(new Tag(tagsStringArray[12]));
        tagList.add(new Tag(tagsStringArray[13]));
        tagList.add(new Tag(tagsStringArray[14]));

        //create an ArrayAdaptar from the String Array
        dataAdapter = new CheckboxTagAdapter(this, tagList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }
}