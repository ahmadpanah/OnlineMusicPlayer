package ir.shariaty.onlinemusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.gif.StreamGifDecoder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;

    DatabaseReference mref;

    TextView SongName, SongArtist;
    MediaPlayer mediaPlayer;


    // make new Flag for Play
    boolean play = true;
    ImageView Play, Pause, Prev, Next;

    Integer currentSongIndex = 0;

    SeekBar seekBar;
    TextView Pass, Due;

    Handler handler;

    String out, out2;

    Integer totalTime;

    ImageView Heart, Repeat;

    boolean RepeatSong = false;

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

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        Pass = (TextView) findViewById(R.id.tv_pass);
        Due = (TextView) findViewById(R.id.tv_due);

        handler = new Handler();

        Play = (ImageView) findViewById(R.id.play);
        Pause = (ImageView) findViewById(R.id.pause);
        Prev = (ImageView) findViewById(R.id.prev);
        Next = (ImageView) findViewById(R.id.next);

        Heart = (ImageView) findViewById(R.id.heart);
        Repeat = (ImageView) findViewById(R.id.repeat);


        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // add all image url in list
                    imageurls.add(ds.child("imageurl").getValue(String.class));

                    // add all Song Name and Artist Name in our list

                    songnames.add(ds.child("songname").getValue(String.class));
                    songartists.add(ds.child("songartist").getValue(String.class));

                    // add song urls
                    songurls.add(ds.child("songurl").getValue(String.class));
                }
                for (int i = 0; i < imageurls.size(); i++) {
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

                // store value of index here

                currentSongIndex = viewPager2.getCurrentItem();

            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSongIndex < songurls.size() - 1) {
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    currentSongIndex = 0;
                }
                viewPager2.setCurrentItem(currentSongIndex);
                init(currentSongIndex);
            }
        });

        Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSongIndex > 0) {
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    currentSongIndex = songurls.size() - 1;
                }
                viewPager2.setCurrentItem(currentSongIndex);
                init(currentSongIndex);
            }
        });

        Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String like = snapshot.child(String.valueOf(currentSongIndex + 1)).child("like").getValue(String.class);
                        if (like.equals("0")) {
                            Heart.setImageResource(R.drawable.heart);
                            mref.child(String.valueOf(currentSongIndex + 1)).child("like").setValue("1");
                        } else {
                            Heart.setImageResource(R.drawable.heart2);
                            mref.child(String.valueOf(currentSongIndex + 1)).child("like").setValue("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        Repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String repeat = snapshot.child(String.valueOf(currentSongIndex + 1)).child("repeat").getValue(String.class);
                        if (repeat.equals("0")) {
                            Repeat.setImageResource(R.drawable.repeat);
                            mref.child(String.valueOf(currentSongIndex + 1)).child("repeat").setValue("1");
                            RepeatSong = true;
                            RepeatSong();
                        } else {
                            Repeat.setImageResource(R.drawable.repeat2);
                            mref.child(String.valueOf(currentSongIndex + 1)).child("repeat").setValue("0");
                            RepeatSong = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void init(int currentItem) {

        try {
            if (mediaPlayer.isPlaying())
                mediaPlayer.reset();
        } catch (Exception e) {
        }

        Pause.setVisibility(View.VISIBLE);
        Play.setVisibility(View.INVISIBLE);
        play = true;

        SongName.setText(songnames.get(currentItem));
        SongArtist.setText(songartists.get(currentItem));

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String like = snapshot.child(String.valueOf(currentSongIndex + 1)).child("like").getValue(String.class);
                String repeat = snapshot.child(String.valueOf(currentSongIndex + 1)).child("repeat").getValue(String.class);

                if (like.equals("0")) {
                    Heart.setImageResource(R.drawable.heart2);
                } else {
                    Heart.setImageResource(R.drawable.heart);
                }

                if (repeat.equals("0")) {
                    Repeat.setImageResource(R.drawable.repeat2);
                    RepeatSong = false;
                } else {
                    Repeat.setImageResource(R.drawable.repeat);
                    RepeatSong = true;

                    RepeatSong();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songurls.get(currentItem));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    initializeSeekBar();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void RepeatSong() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (RepeatSong) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void initializeSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
        seekBar.setProgress(mCurrentPosition);

        MainActivity.this.runOnUiThread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);

                    out = String.format("%02d:%02d", seekBar.getProgress() / 60, seekBar.getProgress() % 60);
                    Pass.setText(out);
                }
                handler.postDelayed(this, 1000);
            }
        });

        totalTime = mediaPlayer.getDuration() / 1000;
        out2 = String.format("%02d:%02d", totalTime / 60, totalTime % 60);
        Due.setText(out2);

    }

    public void playpausebutton(View v) {
        if (play) {
            play = false;
            Pause.setVisibility(View.INVISIBLE);
            Play.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
        } else {
            play = true;
            Pause.setVisibility(View.VISIBLE);
            Play.setVisibility(View.INVISIBLE);
            mediaPlayer.start();
        }
    }
}