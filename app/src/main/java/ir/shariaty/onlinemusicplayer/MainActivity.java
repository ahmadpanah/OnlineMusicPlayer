package ir.shariaty.onlinemusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;

    DatabaseReference mref;

    TextView SongName, SongArtist;
    MediaPlayer mediaPlayer;

    ArrayList<String> imageurls = new ArrayList<>();
    ArrayList<String> songnames = new ArrayList<>();
    ArrayList<String> songartists = new ArrayList<>();
    ArrayList<String> songurls = new ArrayList<>();

    List<SliderItems> sliderItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewpagerimageslider);

        mref = FirebaseDatabase.getInstance().getReference();

        SongName = (TextView) findViewById(R.id.songname);
        SongArtist = (TextView) findViewById(R.id.songartist);

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    // add all image url in list
                    imageurls.add(ds.child("imageurl").getValue(String.class));

                    // add all Song Name and Artist Name in our list

                    songnames.add(ds.child("songname").getValue(String.class));
                    songartists.add(ds.child("songartist").getValue(String.class));

                    // add song urls
                    songurls.add(ds.child("songurl").getValue(String.class));
                }
                for (int i=0;i<imageurls.size();i++)
                {
                    sliderItems.add(new SliderItems(imageurls.get(i)));
                }

                viewPager2.setAdapter(new SliderAdapter(sliderItems));

                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);

                viewPager2.setOffscreenPageLimit(3);
                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        page.setScaleY(1);
                    }
                });

                viewPager2.setPageTransformer(compositePageTransformer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);


                // make new Function
                init(viewPager2.getCurrentItem());
            }
        });

    }

    private void init(int currentItem) {

        try {
            if (mediaPlayer.isPlaying())
                mediaPlayer.reset();
        } catch (Exception e) {}

        // Now Set Text to our Text View with Help Array List
        SongName.setText(songnames.get(currentItem));
        SongArtist.setText(songartists.get(currentItem));

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songurls.get(currentItem));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        }
        catch (Exception e) {e.printStackTrace();}

    }

    public void playpausebutton(View v) {

    }
}