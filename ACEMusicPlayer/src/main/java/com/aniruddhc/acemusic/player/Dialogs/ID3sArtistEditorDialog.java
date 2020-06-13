/*
 * Copyright (C) 2014 Saravan Pantham
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aniruddhc.acemusic.player.Dialogs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aniruddhc.acemusic.player.R;
import com.aniruddhc.acemusic.player.DBHelpers.DBAccessHelper;
import com.aniruddhc.acemusic.player.Helpers.TypefaceHelper;

public class ID3sArtistEditorDialog extends DialogFragment {

	private Context mContext;
	private static Activity parentActivity;
	private DialogFragment dialogFragment;
	private View rootView;
	private EditText titleEditText;
	private EditText artistEditText;
	private EditText albumEditText;
	private EditText albumArtistEditText;
	private EditText genreEditText;
	private EditText producerEditText;
	private EditText yearEditText;
	private EditText trackEditText;
	private EditText trackTotalEditText;
	private EditText commentsEditText;

	private boolean titleEdited = false;
	private boolean artistEdited = false;
	private boolean albumEdited = false;
	private boolean albumArtistEdited = false;
	private boolean genreEdited = false;
	private boolean producerEdited = false;
	private boolean yearEdited = false;
	private boolean trackEdited = false;
	private boolean commentEdited = false;
	
	private String ARTIST;
	
	private ArrayList<String> titlesList = new ArrayList<String>();
	private ArrayList<String> artistsList = new ArrayList<String>();
	private ArrayList<String> albumsList = new ArrayList<String>();
	private ArrayList<String> albumArtistsList = new ArrayList<String>();
	private ArrayList<String> genresList = new ArrayList<String>();
	private ArrayList<String> producersList = new ArrayList<String>();
	private ArrayList<String> yearsList = new ArrayList<String>();
	private ArrayList<String> trackNumbersList = new ArrayList<String>();
	private ArrayList<String> totalTracksList = new ArrayList<String>();
	private ArrayList<String> commentsList = new ArrayList<String>();
	private ArrayList<String> songURIsList = new ArrayList<String>();
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mContext = getActivity();
		parentActivity = getActivity();
		dialogFragment = this;
		
		//Get the artist name.
		ARTIST = getArguments().getString("ARTIST");
		
		rootView = (View) parentActivity.getLayoutInflater().inflate(R.layout.fragment_edit_id3_artist_album_dialog, null);

		TextView[] textViews = new TextView[] {
			rootView.findViewById(R.id.edit_title_text),
			rootView.findViewById(R.id.edit_artist_text),
			rootView.findViewById(R.id.edit_album_text),
			rootView.findViewById(R.id.edit_album_artist_text),
			rootView.findViewById(R.id.edit_genre_text),
			rootView.findViewById(R.id.edit_producer_text),
			rootView.findViewById(R.id.edit_year_text),
			rootView.findViewById(R.id.edit_track_text),
			rootView.findViewById(R.id.text_of),
			rootView.findViewById(R.id.edit_comment_text),
		};

		EditText[] editTexts = new EditText[] {
			titleEditText = rootView.findViewById(R.id.edit_title_field),
			artistEditText = rootView.findViewById(R.id.edit_artist_field),
			albumEditText = rootView.findViewById(R.id.edit_album_field),
			albumArtistEditText = rootView.findViewById(R.id.edit_album_artist_field),
			genreEditText = rootView.findViewById(R.id.edit_genre_field),
			producerEditText = rootView.findViewById(R.id.edit_producer_field),
			yearEditText = rootView.findViewById(R.id.edit_year_field),
			trackEditText = rootView.findViewById(R.id.edit_track_field),
			trackTotalEditText = rootView.findViewById(R.id.edit_track_total_field),
			commentsEditText = rootView.findViewById(R.id.edit_comment_field),
		};

		CheckBox[] checkBoxes = new CheckBox[] {
			rootView.findViewById(R.id.title_checkbox),
			rootView.findViewById(R.id.artist_checkbox),
			rootView.findViewById(R.id.album_checkbox),
			rootView.findViewById(R.id.album_artist_checkbox),
			rootView.findViewById(R.id.genre_checkbox),
			rootView.findViewById(R.id.producer_checkbox),
			rootView.findViewById(R.id.year_checkbox),
			rootView.findViewById(R.id.track_checkbox),
			rootView.findViewById(R.id.comment_checkbox),
		};

		ArrayList[] arrayLists = new ArrayList[] {
			titlesList,
			artistsList,
			albumsList,
			albumArtistsList,
			genresList,
			producersList,
			yearsList,
			trackNumbersList,
			totalTracksList,
			commentsList,
		};

		boolean[] booleans = new boolean[] {
			titleEdited,
			artistEdited,
			albumEdited,
			albumArtistEdited,
			genreEdited,
			producerEdited,
			yearEdited,
			trackEdited,
			commentEdited,
		};

		for (TextView tv: textViews) {
			tv.setTypeface(TypefaceHelper.getTypeface(parentActivity, "RobotoCondensed-Light"));
			tv.setPaintFlags(tv.getPaintFlags() | Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
		}

		for (EditText et: editTexts) {
			et.setTypeface(TypefaceHelper.getTypeface(parentActivity, "RobotoCondensed-Light"));
			et.setPaintFlags(et.getPaintFlags() | Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
			//Disable all EditTexts by default.
			et.setEnabled(false);
		}

		for (CheckBox cb: checkBoxes) {
			//Keep all the fields locked by default.
			cb.setEnabled(false);
		}

		//Register click registers on each checkbox.
		for (int i = 0; i < checkBoxes.length; i++) {
			int finalI = i;
			checkBoxes[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
				booleans[finalI] = isChecked;
				editTexts[finalI].setEnabled(isChecked);
			});
		}

		if (ARTIST!=null) {
			songURIsList = getAllSongsByArtist(ARTIST);
			
			//Populate the ArrayLists with the song tags.
			try {
				getSongTags(songURIsList);
			} catch (CannotReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReadOnlyFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAudioFrameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < arrayLists.length; i++) {
				boolean same = checkIfAllElementsEqual(arrayLists[i]);
				setEditorFields(same, arrayLists[i], editTexts[i]);
			}

		}
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Set the dialog title.
        builder.setTitle(R.string.edit_tags);
        builder.setView(rootView);
        builder.setPositiveButton(R.string.save, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dialogFragment.dismiss();
				AsyncSaveArtistTagsTask asyncSaveArtistTagsTask = new AsyncSaveArtistTagsTask(getActivity(), getActivity());
				asyncSaveArtistTagsTask.execute();
        	
			}
	        
        });
        
        builder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
        	
        });

        return builder.create();
			
	}
	
	public static ArrayList<String> getAllSongsInAlbum(String albumName, String artistName) {
		ArrayList<String> songURIsList = new ArrayList<String>();
		
		DBAccessHelper dbHelper = new DBAccessHelper(parentActivity);
		
		//Escape any rogue apostrophes.
		if (albumName.contains("'")) {
			albumName = albumName.replace("'", "''");
		}
		
		if (artistName.contains("'")) {
			artistName = artistName.replace("'", "''");
		}
		
		String selection = DBAccessHelper.SONG_ALBUM + "=" + "'" + albumName + "'" + " AND "
						 + DBAccessHelper.SONG_ARTIST + "=" + "'" + artistName + "'" + " AND "
						 + DBAccessHelper.SONG_SOURCE + "<>" + "'GOOGLE_PLAY_MUSIC'";
		
		String[] projection = { DBAccessHelper._ID, DBAccessHelper.SONG_FILE_PATH };
		
		Cursor cursor = dbHelper.getWritableDatabase().query(DBAccessHelper.MUSIC_LIBRARY_TABLE, 
															 projection,
															 selection, 
															 null, 
															 null, 
															 null, 
															 null);
		
		cursor.moveToFirst();
		
		if (cursor.getCount()!=0) {
			
			songURIsList.add(cursor.getString(1));
			
			while (cursor.moveToNext()) {
				songURIsList.add(cursor.getString(1));
				
			}
			
		}
		
		cursor.close();
		
		return songURIsList;
		
	}
	
	public static ArrayList<String> getAllSongsByArtist(String artistName) {
		ArrayList<String> songURIsList = new ArrayList<String>();
		
		DBAccessHelper dbHelper = new DBAccessHelper(parentActivity);
		
		//Escape any rogue apostrophes.
		if (artistName.contains("'")) {
			artistName = artistName.replace("'", "''");
		}
		
		String selection = DBAccessHelper.SONG_ARTIST + "=" + "'" + artistName + "'"
						 + " AND " + DBAccessHelper.SONG_SOURCE + "<>" + "'GOOGLE_PLAY_MUSIC'";
		
		String[] projection = { DBAccessHelper._ID, DBAccessHelper.SONG_FILE_PATH };
		
		Cursor cursor = dbHelper.getWritableDatabase().query(DBAccessHelper.MUSIC_LIBRARY_TABLE, 
															 projection,
															 selection, 
															 null, 
															 null, 
															 null, 
															 null);
		
		cursor.moveToFirst();
		
		if (cursor.getCount()!=0) {
			
			songURIsList.add(cursor.getString(1));
			
			while (cursor.moveToNext()) {
				songURIsList.add(cursor.getString(1));
				
			}
			
		}
		
		cursor.close();
		
		return songURIsList;
		
	}
	
	//This method loops through all the songs and saves their tags into ArrayLists.
	public void getSongTags(ArrayList<String> dataURIsList) throws CannotReadException, 
																   IOException, 
																   TagException, 
																   ReadOnlyFileException, 
																   InvalidAudioFrameException {
		
		for (int i=0; i < dataURIsList.size(); i++) {
			
			try {
				File file = new File(dataURIsList.get(i));
				AudioFile audioFile = AudioFileIO.read(file);
				
				titlesList.add(audioFile.getTag().getFirst(FieldKey.TITLE));
				artistsList.add(audioFile.getTag().getFirst(FieldKey.ARTIST));
				albumsList.add(audioFile.getTag().getFirst(FieldKey.ALBUM));
				albumArtistsList.add(audioFile.getTag().getFirst(FieldKey.ALBUM_ARTIST));
				genresList.add(audioFile.getTag().getFirst(FieldKey.GENRE));
				producersList.add(audioFile.getTag().getFirst(FieldKey.PRODUCER));
				yearsList.add(audioFile.getTag().getFirst(FieldKey.YEAR));
				trackNumbersList.add(audioFile.getTag().getFirst(FieldKey.TRACK));
				totalTracksList.add(audioFile.getTag().getFirst(FieldKey.TRACK_TOTAL));
				commentsList.add(audioFile.getTag().getFirst(FieldKey.COMMENT));
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
	}
	
	//This method goes through the inputted ArrayList and checks if all its elements are the same.
	public static boolean checkIfAllElementsEqual(ArrayList<String> list) {
		
		if (list.size()!=0) {
			String firstElement = list.get(0);
			
			for (int i=0; i < list.size(); i++) {
				if (!firstElement.equals(list.get(i))) {
					return false;
				}
				
			}
			
			return true;
		}
		
		return false;
		
	}
	
	//This method sets the specified EditText values based on the boolean parameter.
	public static void setEditorFields(boolean allElementsSame, ArrayList<String> list, EditText editText) {
		
		if (allElementsSame==true) {
			editText.setText(list.get(0));
		} else {
			editText.setText(R.string.varies_by_song);
		}
		
	}
	
	class AsyncSaveArtistTagsTask extends AsyncTask<String, String, String> {

		private Context mContext;
		private Activity mActivity;
		private ProgressDialog pd;
		private int i = 0;
		
		private String songTitle;
		private String songArtist;
		private String songAlbum;
		private String songAlbumArtist;
		private String songComposer;
		private String songProducer;
		private String songTrackNumber;
		private String songTrackTotals;
		private String songComments;
		private String songYear;
		
		public AsyncSaveArtistTagsTask(Context context, Activity activity) {
			mContext = context;
			mActivity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			pd = new ProgressDialog(mActivity);
			pd.setTitle(R.string.saving_artist_info);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setIndeterminate(false);
			pd.setMax(songURIsList.size());
			pd.show();
			
			//Retrieve the strings from the EditText fields.
			if (titleEdited) {
				songTitle = titleEditText.getText().toString();	
				songTitle = songTitle.replace("'", "''");
			} else {
				songTitle = null;
			}
			
			if (artistEdited) {
				songArtist = artistEditText.getText().toString();
				songArtist = songArtist.replace("'", "''");
			} else {
				songArtist = null;
			}
			
			if (albumEdited) {
				songAlbum = albumEditText.getText().toString();
				songAlbum = songAlbum.replace("'", "''");
			} else {
				songAlbum = null;
			}
			
			if (albumArtistEdited) {
				songAlbumArtist = albumArtistEditText.getText().toString();
				songAlbumArtist = songAlbumArtist.replace("'", "''");
			} else {
				songAlbumArtist = null;
			}
			
			if (genreEdited) {
				songComposer = genreEditText.getText().toString();
				songComposer = songComposer.replace("'", "''");
			} else {
				songComposer = null;
			}
			
			if (producerEdited) {
				songProducer = producerEditText.getText().toString();
				songProducer = songProducer.replace("'", "''");
			} else {
				songProducer = null;
			}
			
			if (trackEdited) {
				songTrackNumber = trackEditText.getText().toString();
				songTrackNumber = songTrackNumber.replace("'", "''");
				songTrackTotals = trackTotalEditText.getText().toString();
				songTrackTotals = songTrackTotals.replace("'", "''");
			} else {
				songTrackNumber = null;
				songTrackTotals = null;
			}
			
			if (commentEdited) {
				songComments = commentsEditText.getText().toString();
				songComments = songComments.replace("'", "''");
			} else {
				songComments = null;
			}
			
			if (yearEdited) {
				songYear = yearEditText.getText().toString();
				songYear = songYear.replace("'", "''");
			} else {
				songYear = null;
			}
			
		}
		
		@Override
		protected String doInBackground(String... arg0) {

			//Create DB instances.
			DBAccessHelper dbHelper = new DBAccessHelper(mContext.getApplicationContext());
			
			for (i=0; i < songURIsList.size(); i++) {
				publishProgress(new String[] {});
				File file = null;
				try {
					file = new File(songURIsList.get(i));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				AudioFile audioFile = null;
				
				try {
					audioFile = AudioFileIO.read(file);
				} catch (CannotReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TagException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReadOnlyFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidAudioFrameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Escape any rogue apostrophes.
				String uri = songURIsList.get(i);
				
				if (uri!=null) {
					if (uri.contains("'")) {
						uri = uri.replace("'", "''");
					}
					
				} else {
					continue;
				}

				Tag tag = audioFile.getTag();
				
				if (tag!=null) {
					String whereClause = DBAccessHelper.SONG_FILE_PATH + "=" + "'" + uri + "'";
					ContentValues values = new ContentValues();
					
					if (titleEdited==false) {
						//Don't do anything here. The user didn't change the title.
					} else {
						try {
							tag.setField(FieldKey.TITLE, songTitle);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						values.put(DBAccessHelper.SONG_TITLE, songTitle);
						
					}
					
					if (albumEdited==false) {
						//Don't do anything here. The user didn't change the album.
					} else {
						try {
							tag.setField(FieldKey.ALBUM, songAlbum);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						values.put(DBAccessHelper.SONG_ALBUM, songAlbum);
						
					}
					
					if (artistEdited==false) {
						//Don't do anything here. The user didn't change the artist.
					} else {
						try {
							tag.setField(FieldKey.ARTIST, songArtist);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						values.put(DBAccessHelper.SONG_ARTIST, songArtist);
						
					}
					
					if (albumArtistEdited==false) {
						//Don't do anything here. The user didn't change the album artist.
					} else {
						try {
							tag.setField(FieldKey.ALBUM_ARTIST, songAlbumArtist);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						values.put(DBAccessHelper.SONG_ALBUM_ARTIST, songAlbumArtist);
						
					}
					
					if (genreEdited==false) {
						//Don't do anything here. The user didn't change the genre.
					} else {
						try {
							tag.setField(FieldKey.GENRE, songComposer);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					if (producerEdited==false) {
						//Don't do anything here. The user didn't change the producer.
					} else {
						try {
							tag.setField(FieldKey.PRODUCER, songProducer);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					if (yearEdited==false) {
						//Don't do anything here. The user didn't change the year.
					} else {
						try {
							tag.setField(FieldKey.YEAR, songYear);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						values.put(DBAccessHelper.SONG_YEAR, songYear);
						
					}
					
					if (trackEdited==false) {
						//Don't do anything here. The user didn't change the track number.
					} else {
						try {
							tag.setField(FieldKey.TRACK, songTrackNumber);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						values.put(DBAccessHelper.SONG_TRACK_NUMBER, songTrackNumber);
						
					}
					
					try {
						tag.setField(FieldKey.TRACK_TOTAL, songTrackTotals);
					} catch (KeyNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FieldDataInvalidException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchElementException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (commentEdited==false) {
						//Don't do anything here. The user didn't change the comments.
					} else {
						try {
							tag.setField(FieldKey.COMMENT, songComments);
						} catch (KeyNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FieldDataInvalidException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchElementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					try {
						audioFile.commit();
					} catch (CannotWriteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//Write the values to the DB.
					try {
						dbHelper.getWritableDatabase().update(DBAccessHelper.MUSIC_LIBRARY_TABLE, 
															  values, 
															  whereClause, 
															  null);

						dbHelper.close();
						dbHelper = null;
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else {
					Toast.makeText(mContext, R.string.unable_to_edit_artist_tags, Toast.LENGTH_SHORT).show();
				}
				
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			pd.setProgress(i);
			String message = mContext.getResources().getString(R.string.saving_song_info_for) + " " + titlesList.get(i) + ".";
			pd.setMessage(message);
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
			
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//Delete all the contents of the ArrayLists.
			clearArrayLists();
			dialogFragment.dismiss();
			Toast.makeText(parentActivity, R.string.changes_saved, Toast.LENGTH_SHORT).show();
			
		}
		
	}


	public void clearArrayLists() {
		if (titlesList!=null) {
			titlesList.clear();
			titlesList = null;
		}
		
		if (artistsList!=null) {
			artistsList.clear();
			artistsList = null;
		}
		
		if (albumsList!=null) {
			albumsList.clear();
			albumsList = null;
		}
		
		if (albumArtistsList!=null) {
			albumArtistsList.clear();
			albumArtistsList = null;
		}
		
		if (genresList!=null) {
			genresList.clear();
			genresList = null;
		}
		
		if (producersList!=null) {
			producersList.clear();
			producersList = null;
		}
		
		if (yearsList!=null) {
			yearsList.clear();
			yearsList = null;
		}
		
		if (trackNumbersList!=null) {
			trackNumbersList.clear();
			trackNumbersList = null;
		}
		
		if (totalTracksList!=null) {
			totalTracksList.clear();
			totalTracksList = null;
		}
		
		if (commentsList!=null) {
			commentsList.clear();
			commentsList = null;
		}
		
		if (songURIsList!=null) {
			songURIsList.clear();
			songURIsList = null;
		}
		
		/*if (songSourcesList!=null) {
			songSourcesList.clear();
			songSourcesList = null;
		}
		
		if (songIdsList!=null) {
			songIdsList.clear();
			songIdsList = null;
		}*/
		
	}
	
}
