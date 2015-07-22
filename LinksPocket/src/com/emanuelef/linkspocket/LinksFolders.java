package com.emanuelef.linkspocket;

import com.emanuelef.linkspocket.FoldersAdapter.onFolderClickListener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class LinksFolders extends Activity implements onFolderClickListener {
	public static final String EXTRA_FOLDER_ID = "folderid";
	private RecyclerView mFoldersRecycler;
	private PocketDB mDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_links_folders);
		
		mFoldersRecycler = (RecyclerView) findViewById(R.id.foldersview);
		LayoutManager lm = new LinearLayoutManager(this);
		FoldersAdapter adapter = new FoldersAdapter(this, mFoldersRecycler);
		mFoldersRecycler.setHasFixedSize(true);
		mFoldersRecycler.setLayoutManager(lm);
		mFoldersRecycler.setAdapter(adapter);
		
		mDB = PocketDB.getPocketDB(this);
	}
	
	private void newFolderDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Folder name");
		final EditText editor = new EditText(this);
		builder.setView(editor);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				createNewFolder(editor.getText().toString());
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface arg0, int arg1) {}
		});
		builder.show();
	}
	
	private void createNewFolder(String foldname) {
		mDB.createFolder(foldname);
		Toast toast = Toast.makeText(this, "Folder '" + foldname + "' created", Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.links_folders, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_new_folder) {
			newFolderDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFolderClick(int folderid) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_FOLDER_ID, folderid);
		intent.setComponent(new ComponentName(this, LinksLinks.class));
		startActivity(intent);
	}
}
