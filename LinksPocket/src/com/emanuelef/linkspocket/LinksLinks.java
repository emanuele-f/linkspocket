package com.emanuelef.linkspocket;

import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

public class LinksLinks extends Activity{
	private RecyclerView mLinksRecycler;
	private int mFolderID;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mFolderID = intent.getIntExtra(LinksFolders.EXTRA_FOLDER_ID, 0);
		
		setContentView(R.layout.activity_links_links);
		mLinksRecycler = (RecyclerView) findViewById(R.id.linksview);
		LayoutManager lm = new LinearLayoutManager(this);
		final LinksAdapter adapter = new LinksAdapter(this, mFolderID);
		mLinksRecycler.setHasFixedSize(true);
		mLinksRecycler.setLayoutManager(lm);
		mLinksRecycler.setAdapter(adapter);
		
		// Swype to delete and undo support
		final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener = new SwipeToDismissTouchListener<>(
            new RecyclerViewAdapter(mLinksRecycler),
            new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                @Override
                public boolean canDismiss(int position) {
                    return true;
                }

                @Override
                public void onDismiss(RecyclerViewAdapter view, int position) {
                    adapter.removeLinkAt(position);
                }
        });
		
		mLinksRecycler.setOnTouchListener(touchListener);
		mLinksRecycler.setOnScrollListener((RecyclerView.OnScrollListener)touchListener.makeScrollListener());
		mLinksRecycler.addOnItemTouchListener(new SwipeableItemClickListener(this,
	        new OnItemClickListener() {
	            @Override
	            public void onItemClick(View view, int position) {
	                if (view.getId() == R.id.txt_delete) {
	                    touchListener.processPendingDismisses();
	                } else if (view.getId() == R.id.txt_undo) {
	                    touchListener.undoPendingDismiss();
	                } else { // R.id.txt_data
	                    openLinkAt(position);
	                }
	            }
	        }));
	}
	
	private void openLinkAt(int position) {
		Cursor cursor = PocketDB.getPocketDB(this).getLinks(mFolderID);
		
		if (cursor != null && cursor.moveToPosition(position)) {
			int cUri = cursor.getColumnIndex(PocketDB.LINKS_COLUMN_VALUE);
			Uri uri = Uri.parse(cursor.getString(cUri));
			
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(uri);
			startActivity(intent);
		}
	}
}