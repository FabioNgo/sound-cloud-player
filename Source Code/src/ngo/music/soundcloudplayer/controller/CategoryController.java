package ngo.music.soundcloudplayer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.entity.Category;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.Constants;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class CategoryController implements Constants.Data, Constants,
		Constants.Categories {
	private int type;
	ArrayList<Category> categories = new ArrayList<Category>();
	protected int TAG_DATA_CHANGED = -1;
	protected int TAG_ITEM_CHANGED = -1;

	public CategoryController() {
		// TODO Auto-generated constructor stub
		// playlists = new ArrayMap<String, ArrayList<Song>>();

		try {
			categories = getCategories(true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MusicPlayerMainActivity.getActivity(),
					"You need login to add song to playlist", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MusicPlayerMainActivity.getActivity(),
					"You need login to add song to playlist", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
		// new getCategoriesAsync().execute();
		TAG_DATA_CHANGED = setTagDataChange();
		TAG_ITEM_CHANGED = setTagItemChange();
	}

	/**
	 * 
	 * @return type for sub class
	 */
	protected abstract int setType();

	/**
	 * 
	 * @return ITEM_IN_CATEGORY_CHANGED in Constants.Data
	 */
	protected abstract int setTagItemChange();

	/**
	 * 
	 * @return CATEGORY_CHANGED in Constants.Data
	 */
	protected abstract int setTagDataChange();

	/**
	 * Get list of Categories
	 * 
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */

	protected abstract ArrayList<Category> getCategoriesInBackGround(
			String... params);

	protected abstract void getCategoriesPostExcecute(
			ArrayList<Category> categories);

	private ArrayList<Category> getCategories(boolean needUpdate)
			throws InterruptedException, ExecutionException {
		if (needUpdate) {
			categories = new loadCategoryBackground().execute().get();
		}
		return categories;

	}

	/**
	 * Store categories, called when any data change in category
	 * 
	 * @throws IOException
	 */
	public abstract void storeCategories() throws IOException;

	/**
	 * Chec
	 */
	public void checkCategoryName(String name) throws Exception {
		for (Category category : categories) {
			if (category.getTitle().equals(name)) {
				throw new Exception("A playlist with the same name is existed");
			}
		}

		if (name.equals("")) {
			throw new Exception("A playlist cannot be created without a name");
		}
	}

	/**
	 * Create a category
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void newCategory(String name) throws Exception {
		// TODO Auto-generated method stub
		checkCategoryName(name);
		new CreateCategoryBackground().execute(name);

		UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	}

	/**
	 * 
	 * @param name
	 * @return false if cannot add Category, true if can add category
	 */
	public abstract Category createCategory(String name);

	public abstract void addSongsToCategory(String categoryName,
			ArrayList<Song> songs) throws Exception;

	public ArrayList<String> getCategoryName() {
		ArrayList<String> categoriesName = new ArrayList<String>();
		// System.out.println(categories);
		for (Category cate : categories) {
			categoriesName.add(cate.getTitle());
		}
		return categoriesName;
	}

	public ArrayList<Song> getSongFromCategory(String categoryName){
		for (Category category : categories) {
			if (category.getTitle().equals(categoryName)) {
				return category.getSongs();
			}
		}
		return new ArrayList<Song>();
	}

	// public ArrayList<Song> getSongFromCategory(String categoryName) {
	// for (Category category : categories) {
	// if (category.getTitle().equals(categoryName)) {
	// return category.getSongs();
	// }
	// }
	// return null;
	// }

	/**
	 * get category Name and songs Titles
	 * 
	 * @return
	 */
	@SuppressWarnings("finally")
	public ArrayList<String> getCategoryString() {
		// TODO Auto-generated method stub
		try {
			categories = getCategories(false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ArrayList<String> categoriesString = new ArrayList<String>();
			for (Category cate : categories) {

				categoriesString.add(cate.toString());
			}
			return categoriesString;
		}

	}

	// public void removeSongFromCate(Song song, String cate) {
	// // TODO Auto-generated method stub
	// for (Category category : categories) {
	// if (category.getTitle().equals(cate)) {
	// category.removeSong(song);
	// }
	// }
	// UIController.getInstance().updateUiWhenDataChanged(TAG_ITEM_CHANGED);
	// try {
	// storeCategories();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	/**
	 * Remove a song from a category
	 * 
	 * @param song
	 *            song want to be removed
	 * @param cate
	 *            name of category
	 */
	abstract public void removeSongFromCate(Song song, String cate);

	/**
	 * In order to remove online category, need to override this function
	 * 
	 * @param cate
	 *            :name of category
	 */
	// public void removeCategory(String cate) {
	// for (int i = 0; i < categories.size(); i++) {
	// if (categories.get(i).getTitle().equals(cate)) {
	// categories.remove(i);
	// break;
	// }
	// }
	// UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	// try {
	// storeCategories();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	abstract public void removeCategory(String cate);

	/**
	 * 
	 * @param type
	 *            from Constant.Categproes
	 * @return
	 */
	public static void createInstance(int type) {
		switch (type) {
		case PLAYLIST:
			PlaylistController.instance = new PlaylistController();
			return;
		case ALBUM:
			AlbumController.instance = new AlbumController();
			return;
		case ARTIST:
			ArtistController.instance = new ArtistController();
			return;
		case SC_PLAYLIST:
			SCMyPlaylistController.instance = new SCMyPlaylistController();
			return;
		case SC_SEARCH_PLAYLIST:
			SCPlaylistSearchController.instance = new SCPlaylistSearchController();
			return;
		default:
			return;
		}
	}

	public static CategoryController getInstance(int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case PLAYLIST:
			if (PlaylistController.instance == null) {
				createInstance(type);
			}
			return PlaylistController.instance;
		case ALBUM:
			if (AlbumController.instance == null) {
				createInstance(type);
			}
			return AlbumController.instance;
		case ARTIST:
			if (ArtistController.instance == null) {
				createInstance(type);
			}
			return ArtistController.instance;
		case SC_PLAYLIST:
			if (SCMyPlaylistController.instance == null) {
				createInstance(type);
			}
			return SCMyPlaylistController.instance;
		case SC_SEARCH_PLAYLIST:
			if (SCPlaylistSearchController.instance == null) {
				createInstance(type);
			}
			return SCPlaylistSearchController.instance;
		default:
			return null;
		}
	}

	// public void updateTitle(String oldName, String newName) throws Exception{
	// // TODO Auto-generated method stub
	// if("".equals(newName)){
	// throw new Exception("Playlist name cannot be empty");
	// }
	// for (Category category : categories) {
	// if(category.getTitle().equals(newName)){
	// throw new Exception("Playlist name exsited");
	// }
	// }
	// for (Category category : categories) {
	// if(category.getTitle().equals(oldName)){
	// category.setTitle(newName);
	// break;
	// }
	// }
	// storeCategories();
	// UIController.getInstance().updateUiWhenDataChanged(TAG_DATA_CHANGED);
	// }
	/**
	 * Update title of a playlist
	 * 
	 * @param oldName
	 *            old name
	 * @param newName
	 *            new name
	 * @throws Exception
	 */
	public abstract void updateTitle(String oldName, String newName)
			throws Exception;

	private class CreateCategoryBackground extends
			AsyncTask<String, String, Category> {

		@Override
		protected Category doInBackground(String... params) {
			// TODO Auto-generated method stub
			// SCPlaylist playlist = new SCPlaylist(params[0]);
			return createCategory(params[0]);

		}

		@Override
		protected void onPostExecute(final Category result) {
			// TODO Auto-generated method stub
			MusicPlayerMainActivity.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (result == null) {
						Toast.makeText(MusicPlayerMainActivity.getActivity(),
								"Cannot create playlist", Toast.LENGTH_LONG)
								.show();
					} else {

						categories.add(result);
						try {
							storeCategories();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Toast.makeText(
									MusicPlayerMainActivity.getActivity(),
									"Create unsuccessfully", Toast.LENGTH_LONG)
									.show();
						}
						Toast.makeText(MusicPlayerMainActivity.getActivity(),
								"Create successfully", Toast.LENGTH_LONG)
								.show();
						UIController.getInstance().updateUiWhenDataChanged(
								TAG_DATA_CHANGED);

					}

				}
			});

		}

	}

	protected class loadCategoryBackground extends
			AsyncTask<String, String, ArrayList<Category>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			categories.clear();
		}

		@Override
		protected ArrayList<Category> doInBackground(String... params) {
			// TODO Auto-generated method stub
			return getCategoriesInBackGround(params);
		}

		@Override
		protected void onPostExecute(ArrayList<Category> categories) {
			// TODO Auto-generated method stub
			getCategoriesPostExcecute(categories);
			return;
		}

	}
}
