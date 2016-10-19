package onkar.taglife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Graph extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static ListView listView;
    static ArrayList<Entry> entriesArrayList;
    static EntryAdapter myEntryAdapter;
    static Context context;
    static String theQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                entriesArrayList = new ArrayList<Entry>();
                query = query.replace(" ","%20");
                Graph.theQuery = query;
                new GetEntriesAsyncTask().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Store the application context
        context = this;
        //Define the listview
        listView = (ListView) findViewById(R.id.listView);
        // You will get the entries for initilization here, for now crete them manually
        //TODO: 17.10.2016 Write an AsyncTask getting entries from server and store them in entriesArrayList
        /*for(int i=0; i<10; i++){
            Entry entry = new Entry(0,"Hello I m entry number "+i);
            entriesArrayList.add(entry);
        }*/
        //After storing entries in the arraylist, set and notify our adapter for the entries
        /*Graph.myEntryAdapter = new EntryAdapter(Graph.context,Graph.entriesArrayList);
        Graph.listView.setAdapter(Graph.myEntryAdapter);
        Graph.myEntryAdapter.notifyDataSetChanged();*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PostEntry.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

class EntryAdapter extends ArrayAdapter<Entry> {

    public EntryAdapter(Context context, ArrayList<Entry> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this
        Entry entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.entry_layout, parent, false);
        }
        // Lookup view for data population
        TextView entryText = (TextView) convertView.findViewById(R.id.entryTextView);
        //TextView answerText = (TextView) convertView.findViewById(R.id.textDown);
        // Populate the data into the template view using the data object
        entryText.setText(entry.getEntryText());
        //answerText.setText(question.answer);
        // Return the completed view to render on screen
        if(position%2 == 0){
            convertView.setBackgroundColor(Color.LTGRAY);
        }
        return convertView;
    }
}

class GetEntriesAsyncTask extends AsyncTask<String, Void, String> {
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            //String topicName = "fb";
            String link = "http://352.16mb.com/getEntriesByTopic.php?topicName="+Graph.theQuery;
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    //String is the answer comig from the server
    protected void onPostExecute(String result) {
        String myResult = "";
        try {
            JSONArray myServerResponse = new JSONArray(result);
            for (int i = 0; i < myServerResponse.length()-1; i++) {
                JSONObject obj = myServerResponse.getJSONObject(i);
                String entryText = obj.getString("e_text");
                int entryId = obj.getInt("e_id");
                Log.d("ID-TEXT-SIZE",entryId+":"+entryText+":"+Graph.entriesArrayList.size());
                Entry entry = new Entry(entryId,entryText);
                Graph.entriesArrayList.add(entry);


            }
            int size=Graph.entriesArrayList.size();
            ArrayList finalList = new ArrayList<Entry>();
            for(int i=0; i<size/2; i++){
                finalList.add(Graph.entriesArrayList.get(i));
            }
            Graph.myEntryAdapter = new EntryAdapter(Graph.context,finalList);
            Graph.listView.setAdapter(Graph.myEntryAdapter);
            Graph.myEntryAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}


