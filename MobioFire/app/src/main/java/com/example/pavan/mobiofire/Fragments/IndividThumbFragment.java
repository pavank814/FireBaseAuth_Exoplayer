package com.example.pavan.mobiofire.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pavan.mobiofire.Activities.Individual_thumb_Activity;
import com.example.pavan.mobiofire.Adapter.listviewAdapter;
import com.example.pavan.mobiofire.DatabaseHandler.DatabaseHandler;
import com.example.pavan.mobiofire.DatabaseHandler.thumbs_model;
import com.example.pavan.mobiofire.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;

import kotlin.jvm.internal.Intrinsics;

import static com.google.android.exoplayer2.util.Util.SDK_INT;
import static com.google.android.exoplayer2.util.Util.getUserAgent;


public class IndividThumbFragment extends Fragment {
    Context mContext;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String url=" ";
    SimpleExoPlayer player = null;
    PlayerView playerView=null;
    Boolean shouldAutoPlay= true;
    DefaultTrackSelector trackSelector=null;
    TrackGroupArray lastSeenTrackGroupArray=null;
    DataSource.Factory  mediaDataSourceFactory;
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    Boolean playWhenReady = false;
    Integer currentWindow= 0;
    Long playbackPosition = Long.valueOf(0);
    ProgressBar progressBar= null;
    ImageView ivHideControllerButton=null;
    Integer id=0;
    ListView thumb_list_video_player;
    DatabaseHandler DatabaseHandler;


    public IndividThumbFragment(Context applicationContext, String url, Integer id) {
        this.mContext=applicationContext;
        this.url=url;
        this.id=id;

    }


    @Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ind_thnmb_fragment, container, false);
        sharedpreferences = mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        playerView=(PlayerView) view.findViewById(R.id.player_view);
        progressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
        ivHideControllerButton=(ImageView) view.findViewById(R.id.exo_controller);
        thumb_list_video_player=(ListView)view.findViewById(R.id.thumb_list_video_player);

        DatabaseHandler=new DatabaseHandler(mContext);
        thumb_list_video_player.setAdapter(new listviewAdapter(mContext, DatabaseHandler," "));
        shouldAutoPlay = true;
        mediaDataSourceFactory = new DefaultDataSourceFactory(mContext, getUserAgent(mContext, "mediaPlayerSample"),
                (TransferListener<? super DataSource>) bandwidthMeter);

        return view;
    }

    public void onStart() {
        super.onStart();

        if (SDK_INT > 23) initializePlayer();
    }

    public void onResume() {
        super.onResume();

        if (SDK_INT <= 23 || player == null) initializePlayer();
    }

    public void onPause() {
        super.onPause();

        if (SDK_INT <= 23) releasePlayer();
    }

    public void onStop() {
        super.onStop();

        if (SDK_INT > 23) releasePlayer();
    }

    private final void initializePlayer() {
        this.playerView.requestFocus();
        com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.Factory videoTrackSelectionFactory = new com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.Factory(this.bandwidthMeter);
        this.trackSelector = new DefaultTrackSelector((com.google.android.exoplayer2.trackselection.TrackSelection.Factory)videoTrackSelectionFactory);
        this.lastSeenTrackGroupArray = (TrackGroupArray)null;
        this.player = ExoPlayerFactory.newSimpleInstance(mContext, (TrackSelector)this.trackSelector);
        this.playerView.setPlayer((Player)this.player);
        SimpleExoPlayer var10000 = this.player;
        if (this.player == null) {
            Intrinsics.throwNpe();
        }

        SimpleExoPlayer var2 = var10000;
        var2.addListener((Player.EventListener)(new PlayerEventListener()));
        var2.setPlayWhenReady(this.shouldAutoPlay);
        ExtractorMediaSource.Factory var6 = new ExtractorMediaSource.Factory(this.mediaDataSourceFactory);
//        DataSource.Factory var10002 = this.mediaDataSourceFactory;
        if (this.mediaDataSourceFactory == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mediaDataSourceFactory");
        }

//        var6.<init>(var10002);
        ExtractorMediaSource mediaSource = var6.createMediaSource(Uri.parse(this.url));
        boolean haveStartPosition = this.currentWindow != -1;
        if (haveStartPosition) {
            var10000 = this.player;
            if (this.player == null) {
                Intrinsics.throwNpe();
            }
            if(sharedpreferences.contains(String.valueOf(id))){
                this.playbackPosition= Long.valueOf(sharedpreferences.getInt(String.valueOf(id),0));
            }else{
                this.playbackPosition= Long.valueOf(0);
            }

            var10000.seekTo(this.currentWindow, this.playbackPosition);
        }

        var10000 = this.player;
        if (this.player == null) {
            Intrinsics.throwNpe();
        }

        var10000.prepare((MediaSource)mediaSource, !haveStartPosition, false);
        this.updateButtonVisibilities();
        this.ivHideControllerButton.setOnClickListener((View.OnClickListener)(new View.OnClickListener() {
            public final void onClick(View it) {
                playerView.hideController();
            }
        }));
    }
    private final void releasePlayer() {
        if (this.player != null) {
            this.updateStartPosition();
//            SimpleExoPlayer var10001 = this.player;
            if (this.player == null) {
                Intrinsics.throwNpe();
            }

            this.shouldAutoPlay = this.player.getPlayWhenReady();
            SimpleExoPlayer var10000 = this.player;
            if (this.player == null) {
                Intrinsics.throwNpe();
            }

            this.player.release();
            this.player = (SimpleExoPlayer)null;
            this.trackSelector = (DefaultTrackSelector)null;
        }

    }
    private final void updateStartPosition() {
        SimpleExoPlayer var10000 = this.player;
        if (this.player == null) {
            Intrinsics.throwNpe();
        }
        if(player.getPlaybackState()==4){
            editor.putInt(String.valueOf(id), 0);
            editor.commit();
        }else {
            editor.putInt(String.valueOf(id), (int) this.player.getCurrentPosition());
            editor.commit();
        }
        this.playbackPosition = this.player.getCurrentPosition();
        this.currentWindow = this.player.getCurrentWindowIndex();
        this.player.setPlayWhenReady(this.player.getPlayWhenReady());
    }
    private final void updateButtonVisibilities() {

        if (this.player != null) {
            DefaultTrackSelector var10000 = this.trackSelector;
            if (this.trackSelector == null) {
                Intrinsics.throwNpe();
            }

            MappingTrackSelector.MappedTrackInfo var5 = var10000.getCurrentMappedTrackInfo();
            if (var5 != null) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = var5;
                int i = 0;
                Intrinsics.checkExpressionValueIsNotNull(mappedTrackInfo, "mappedTrackInfo");

                for(int var3 = mappedTrackInfo.getRendererCount(); i < var3; ++i) {
                    TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
                    if (trackGroups.length != 0) {
                        SimpleExoPlayer var6 = this.player;
                        if (this.player == null) {
                            Intrinsics.throwNpe();
                        }
                    }
                }

            }
        }
    }
    private final class PlayerEventListener extends Player.DefaultEventListener {
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case 1:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    progressBar.setVisibility(View.GONE);
                    break;
                case 4:
                    editor.putInt(String.valueOf(id), 0);
                    editor.commit();
                    Intent mIntent = new Intent(mContext, Individual_thumb_Activity.class);
                    thumbs_model tm= DatabaseHandler.getdata_based_on_id(id+1);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mIntent.putExtra("url", tm.getUrl());
                    mIntent.putExtra("id", tm.getId());
                    mContext.startActivity(mIntent);
            }

            updateButtonVisibilities();
        }

        public void onTracksChanged(@org.jetbrains.annotations.Nullable TrackGroupArray trackGroups, @org.jetbrains.annotations.Nullable TrackSelectionArray trackSelections) {
            updateButtonVisibilities();
            if (trackGroups != lastSeenTrackGroupArray) {
                DefaultTrackSelector var10000 = trackSelector;
                if (var10000 == null) {
                    Intrinsics.throwNpe();
                }

                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = var10000.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null && mappedTrackInfo.getTypeSupport(2) == 1) {
                    Toast.makeText(mContext ,(CharSequence) "Error unsupported track", Toast.LENGTH_LONG).show();
                }

                lastSeenTrackGroupArray = trackGroups;
            }

        }
    }

}
