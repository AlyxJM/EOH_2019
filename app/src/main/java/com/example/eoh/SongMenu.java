package com.example.eoh;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class SongMenu extends AppCompatActivity {
    private static String songURL;
    private static int resid;

    public static int getResid() {
        return resid;
    }

    public static String getURL() {
        return songURL;
    }


    ListView listView;
    private Context context = this;
//    ArrayList<String> songNames = new ArrayList<String>();
    String[] songNamesArray = {"Adventure of a Lifetime", "Blank Space",
            "Chandelier", "Delicate", "Don't", "Firework", "Girls Like You",
            "God is a Woman", "High Hopes", "I Like It", "I Like Me Better",
            "Sing", "Taki Taki", "Tell Me Something I Don't Know", "Thank You, Next",
            "Without Me", "Yellow"};

    String[] artists = {"Coldplay", "Taylor Swift", "Sia", "Taylor Swift", "Ed Sheeran",
            "Katy Perry", "Maroon 5", "Ariana Grande", "Panic at the Disco", "I Like It",
            "lauv", "Ed Sheeran", "DJ Snake", "Selena Gomez", "Ariana Grande",
            "Halsey", "Coldplay"};

    int[] residIDS = {R.raw.adventure_of_a_lifetime, R.raw.blank_space, R.raw.chandelier,
            R.raw.delicate, R.raw.dont, R.raw.firework, R.raw.girls_like_you, R.raw.god_is_a_woman,
            R.raw.high_hopes, R.raw.i_like_it, R.raw.i_like_me_better, R.raw.sing, R.raw.taki_taki,
            R.raw.tell_me_something_i_dont_know, R.raw.thank_you_next, R.raw.without_me, R.raw.yellow};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_menu);
//        initializeList();
        listView=(ListView)findViewById(R.id.listView);

//        songNamesArray = new String[songNames.size()];
//        songNamesArray = songNames.toArray(songNamesArray);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,songNamesArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                songURL = "https://api.lyrics.ovh/v1/" + artists[i] + "/" + songNamesArray[i];
                resid = residIDS[i];
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        // Register the ListView  for Context menu
        //registerForContextMenu(listView);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.setHeaderTitle("Select The Action");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId()==R.id.call){
            Toast.makeText(getApplicationContext(),"calling code",Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId()==R.id.sms){
            Toast.makeText(getApplicationContext(),"sending sms code",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }

    //adds the song names of all the files in the raw directory
    private void initializeList() {
//        System.out.println("starting initialize list");
//        File rawDir = new File("C:\\Users\\Kimi\\AndroidStudioProjects\\EOH_2019\\app\\src\\main\\res\\raw");
//        if (rawDir != null) {
//            File [] songFiles = rawDir.listFiles();
//            if (songFiles != null) {
//                for (File songFile : songFiles) {
//                    songNames.add(songFile.getName());
//                }
//            }
//        }
//        songNames.add("fml");
//        System.out.println("finished with initialize list");

    }
}