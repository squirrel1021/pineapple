package pineapple.bd.com.pineapple.media;

import android.app.Application;
import android.app.Service;

import com.devbrackets.android.exomedia.manager.EMPlaylistManager;
import com.devbrackets.android.exomedia.service.EMPlaylistService;


import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.media.entity.MediaItem;


public class PlaylistManager extends EMPlaylistManager<MediaItem> {

    @Override
    protected Application getApplication() {
        return PineApplication.mContext;
    }

    @Override
    protected Class<? extends Service> getMediaServiceClass() {
        return AudioService.class;
    }
}
