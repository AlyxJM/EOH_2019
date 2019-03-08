package com.example.eoh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.widget.TextView;

public class SongMenu extends AppCompatActivity {
    private static String songURL;
    private static int resid;

    public static MediaPlayer trackPlayer;
    public static MediaPlayer recordingPlayer;

    private Context context = this;


    private ListView listView;
    private Typeface listFont;

//    ArrayList<String> songNames = new ArrayList<String>();
    String[] songNamesArray = {"Adventure of a Lifetime", "Blank Space",
            "Chandelier", "Delicate", "Don't", "Fergalicious", "Firework", "Girls Like You",
            "God is a Woman", "High Hopes", "I Like It", "I Like Me Better",
            "Sing", "Taki Taki", "Tell Me Something I Don't Know", "Thank You, Next",
            "Without Me", "Yellow"};

    String[] artists = {"Coldplay", "Taylor Swift", "Sia", "Taylor Swift", "Ed Sheeran",
            "Fergie", "Katy Perry", "Maroon 5", "Ariana Grande", "Panic at the Disco", "Cardi B",
            "lauv", "Ed Sheeran", "DJ Snake", "Selena Gomez", "Ariana Grande",
            "Halsey", "Coldplay"};

    int[] residIDS = {R.raw.adventure_of_a_lifetime, R.raw.blank_space, R.raw.chandelier,
            R.raw.delicate, R.raw.dont, R.raw.fergalicious, R.raw.firework, R.raw.girls_like_you, R.raw.god_is_a_woman,
            R.raw.high_hopes, R.raw.i_like_it, R.raw.i_like_me_better, R.raw.sing, R.raw.taki_taki,
            R.raw.tell_me_something_i_dont_know, R.raw.thank_you_next, R.raw.without_me, R.raw.yellow};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_menu);

        //trackPlayer.reset();
        //recordingPlayer.reset();
        //listFont = (Typeface) Typeface.createFromAsset(getAssets(), String.valueOf(R.font.Amatic_Bold));

//        initializeList();
//        songNamesArray = new String[songNames.size()];
//        songNamesArray = songNames.toArray(songNamesArray);
        listView=(ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,songNamesArray)
//        {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent){
//                TextView item = (TextView) super.getView(position,convertView,parent);
//                item.setTypeface(listFont);
//                item.setTextColor(Color.parseColor("#FF3E80F1"));
//                item.setTypeface(item.getTypeface(), Typeface.BOLD);
//                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
//                return item;
//            }
//        }
        ;

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (trackPlayer != null) trackPlayer.reset();
                if (recordingPlayer != null) recordingPlayer.reset();
                songURL = "https://api.lyrics.ovh/v1/" + artists[i] + "/" + songNamesArray[i];
                resid = residIDS[i];
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        // Register the ListView  for Context menu
        //registerForContextMenu(listView);
    }

//    public static MediaPlayer getTrackPlayer() {
//        return trackPlayer;
//    }
//
//    public static MediaPlayer getRecordingPlayer() {
//        return recordingPlayer;
//    }

    public static int getResid() {
        return resid;
    }

    public static String getURL() {
        return songURL;
    }

    //adds the song names of all the files in the raw directory
//    private void initializeList() {
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

//   }
}