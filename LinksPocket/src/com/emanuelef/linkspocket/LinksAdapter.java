package com.emanuelef.linkspocket;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.ViewHolder> {
	private PocketDB mDB;
	private int mFolderID;
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView mLinkName;
		public ViewHolder(View itemView) {
			super(itemView);
			mLinkName = (TextView) itemView.findViewById(R.id.txt_data);
		}
		
	}
	
	public LinksAdapter(Context context, int folderid) {
		mDB = PocketDB.getPocketDB(context);
		mFolderID = folderid;
	}

	@Override
	public int getItemCount() {
		Cursor cursor = mDB.getLinks(mFolderID);
		if (cursor != null)
			return cursor.getCount();
		return 0;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Cursor cursor = mDB.getLinks(mFolderID);
		
		if (cursor == null)
			return;
		
		if (cursor.moveToPosition(position)) {
			int cTitle = cursor.getColumnIndex(PocketDB.LINKS_COLUMN_TITLE);
			
			String title = cursor.getString(cTitle);
			holder.mLinkName.setText(title);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		FrameLayout layout = (FrameLayout)(inflater.inflate(R.layout.link_item, parent, false));
		
		ViewHolder vholder = new ViewHolder(layout);
		return vholder;
	}

	public void removeLinkAt(int position) {
		Cursor cursor = mDB.getLinks(mFolderID);
		
		if (cursor != null && cursor.moveToPosition(position)) {
			int cID = cursor.getColumnIndex(PocketDB.LINKS_COLUMN_ID);
			mDB.removeLink(cursor.getInt(cID));
		}
	}
}