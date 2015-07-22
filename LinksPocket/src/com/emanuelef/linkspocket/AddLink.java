package com.emanuelef.linkspocket;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddLink extends Activity implements OnClickListener{
	TextView mLabel;
	EditText mTitle;
	EditText mTarget;
	Spinner mFolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addlink);
		mLabel = (TextView) findViewById(R.id.addlink_label);
		mTitle = (EditText) findViewById(R.id.linktitle);
		mTarget = (EditText) findViewById(R.id.linktarget);
		mFolder = (Spinner) findViewById(R.id.foldersel);
		
		// Spinner setup
		Cursor cursor = PocketDB.getPocketDB(this).getFolders();
		String [] from = new String [] {PocketDB.FOLDERS_COLUMN_NAME};
		int [] to = new int [] {android.R.id.text1};
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, from, to);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		mFolder.setAdapter(adapter);
		
		((Button)findViewById(R.id.addlink_confirm)).setOnClickListener(this);
		parseIntent(getIntent());
	}
	
	private void parseIntent(Intent intent) {
		if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.hasExtra(Intent.EXTRA_TEXT)) {
			String text = intent.getStringExtra(Intent.EXTRA_TEXT);
			mLabel.setText(text);
			
			// Try to fill in fields
			int pos = text.indexOf("http");
			if (pos >= 0) {
				mTitle.setText(text.subSequence(0, pos));
				mTarget.setText(text.subSequence(pos, text.length()));
			}
		}
	}

	@Override
	public void onClick(View button) {
		if (button.getId() == R.id.addlink_confirm) {
			// Do add link
			PocketDB db = PocketDB.getPocketDB(this);
			int position = mFolder.getSelectedItemPosition();
			Cursor cursor = db.getFolders();
			if (cursor!=null && cursor.moveToPosition(position)) {
				int cfid = cursor.getColumnIndex(PocketDB.FOLDERS_COLUMN_ID);
				int folderid = cursor.getInt(cfid);
				db.addLink(mTitle.getText().toString(), mTarget.getText().toString(), folderid);
			
				// done
				Toast toast = Toast.makeText(this, "Link added", Toast.LENGTH_SHORT);
				toast.show();
				this.finish();
			}
		}
	}

}