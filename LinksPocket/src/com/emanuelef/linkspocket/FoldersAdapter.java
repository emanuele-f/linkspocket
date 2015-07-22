package com.emanuelef.linkspocket;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.ViewHolder> {
	private PocketDB mDB;
	private onFolderClickListener mListener;
	private RecyclerView mRecyclerView;
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView mFolderName;
		public ViewHolder(View itemView) {
			super(itemView);
			mFolderName = (TextView) itemView;
		}
		
	}
	
	public static interface onFolderClickListener {
		public void onFolderClick(int folderid);
	}
	
	public FoldersAdapter(Context context, RecyclerView rview) {
		mDB = PocketDB.getPocketDB(context);
		mListener = (onFolderClickListener) context;
		mRecyclerView = rview;
	}

	@Override
	public int getItemCount() {
		Cursor cursor = mDB.getFolders();
		if (cursor != null)
			return cursor.getCount();
		return 0;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Cursor cursor = mDB.getFolders();
		
		if (cursor == null)
			return;
		
		if (cursor.moveToPosition(position)) {
			int cName = cursor.getColumnIndex(PocketDB.FOLDERS_COLUMN_NAME);
			
			String foldname = cursor.getString(cName);
			holder.mFolderName.setText(foldname);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		TextView tv = new TextView(parent.getContext());
		ViewHolder vholder = new ViewHolder(tv);
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int pos = mRecyclerView.getChildAdapterPosition(view);
				Cursor cursor = mDB.getFolders();
				
				if (cursor != null && cursor.moveToPosition(pos)) {
					int cID = cursor.getColumnIndex(PocketDB.FOLDERS_COLUMN_ID);
					mListener.onFolderClick(cursor.getInt(cID));
				}
			}
		});
		return vholder;
	}
}