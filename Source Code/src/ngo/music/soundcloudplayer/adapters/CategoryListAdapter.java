package ngo.music.soundcloudplayer.adapters;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.ViewHolder.CompositionViewHolder;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.boundary.fragment.abstracts.ListItemsInCompositionListFragment;
import ngo.music.soundcloudplayer.controller.CategoryController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.OfflineSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

public abstract class CategoryListAdapter extends ArrayAdapter<String>
		implements Constants.Categories {
	private View v;
	private int type = -1;
	Context context;
	int resource;
	protected ArrayList<String> categories;
	CompositionViewHolder holder = null;
	private boolean canDelete;
	private boolean canEdit;

	public CategoryListAdapter(Context context, int resource) {
		super(context, resource);
		type = setType();
		canDelete = setCanDelete();
		canEdit = setCanEdit();
		this.categories = getCategories();
		this.context = context;
		this.resource = resource;

	}

	/**
	 * 
	 * @return if the category can be editted or not
	 */
	protected abstract boolean setCanEdit();

	/**
	 * 
	 * @return if the category item can be delete or not
	 */
	protected abstract boolean setCanDelete();

	/**
	 * 
	 * @return type of Category in Constant.Category
	 */
	protected abstract int setType();

	public static CategoryListAdapter createNewInstance(int type) {
		// TODO Auto-generated method stub
		
		switch (type) {
		case PLAYLIST:
			return new PlaylistAdapter(MusicPlayerMainActivity.getActivity()
					.getApplicationContext(), R.layout.list_view);
		case ALBUM:
			return new AlbumAdapter(MusicPlayerMainActivity.getActivity()
					.getApplicationContext(), R.layout.list_view);
		case ARTIST:
			return new ArtistAdapter(MusicPlayerMainActivity.getActivity()
					.getApplicationContext(), R.layout.list_view);
		case SC_PLAYLIST:

			return new SCPlaylistAdapter(MusicPlayerMainActivity.getActivity()
					.getApplicationContext(), R.layout.list_view);

		case SC_SEARCH_PLAYLIST:
			return new SCPlaylistSearchAdapter(MusicPlayerMainActivity
					.getActivity().getApplicationContext(), R.layout.list_view);

		default:
			return null;
		}

	}

	public static CategoryListAdapter getInstance(int type) {

		switch (type) {
		case PLAYLIST:
			if (PlaylistAdapter.instance == null) {
				createNewInstance(type);
			}
			return PlaylistAdapter.instance;
		case ALBUM:
			if (AlbumAdapter.instance == null) {
				createNewInstance(type);
			}
			return AlbumAdapter.instance;

		case SC_PLAYLIST:
			if (SCPlaylistAdapter.instance == null) {
				createNewInstance(type);
			}
			return SCPlaylistAdapter.instance;
		case SC_SEARCH_PLAYLIST:
			if (SCPlaylistSearchAdapter.instance == null) {
				createNewInstance(type);
			}
			return SCPlaylistSearchAdapter.instance;

		case ARTIST:
			if (ArtistAdapter.instance == null) {
				createNewInstance(type);
			}
			return ArtistAdapter.instance;

		default:
			return null;
		}
	}

	/**
	 * get categories (songs and title) in list of item sets
	 * 
	 * @return categories
	 */
	protected ArrayList<String> getCategories() {
		return CategoryController.getInstance(type).getCategoryString();
	}

	/**
	 * get item from specific categories
	 * 
	 * @param cat
	 *            : category
	 * @return list of songs
	 */
	protected ArrayList<Song> getSongsFromCat(String cat) {
		System.out.println ("GET SONGS FROM CATE");
		try {
			return new getSongFromCategoryBackground(cat, type).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Song>();
		//return CategoryController.getInstance(type).getSongFromCategory(cat);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.category_view, parent, false);

			holder = new CompositionViewHolder(NUM_ITEM_IN_ONE_CATEGORY, v);
			v.setTag(holder);
		} else {
			holder = (CompositionViewHolder) v.getTag();
		}

		setLayoutInformation(holder, categories.get(position), v);

		return v;
	}

	/**
	 * @param position
	 * @param v
	 */
	public void setLayoutInformation(final CompositionViewHolder holder,
			String catString, View v) {

		String[] catData = getSongsTitleFromCat(catString);

		/*
		 * Set song titles
		 */
		for (int i = 0; i < holder.items.length; i++) {
			if (i == 0) {
				holder.items[i].setText(catData[0]); // playlist name
			} else {

				String content = "" + i + ". " + catData[i];
				holder.items[i].setText(content);
			}

		}

		/**
		 * Menu
		 */
		setMenuLayout(holder);
		/**
		 * Edit Text
		 */
		holder.editText.setVisibility(View.INVISIBLE);
		/**
		 * Clear Button
		 */
		holder.clearBtn.setVisibility(View.INVISIBLE);
		holder.clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.items[0].setVisibility(View.VISIBLE);
				holder.editText.setVisibility(View.INVISIBLE);
				holder.editText.getLayoutParams().height = BasicFunctions.dpToPx(20, context);
				holder.editBtn.setVisibility(View.VISIBLE);
				holder.clearBtn.setVisibility(View.INVISIBLE);
				holder.submitBtn.setVisibility(View.INVISIBLE);
			}
		});
		/**
		 * submit Button
		 */
		setSubmitButton(holder);
	}

	/**
	 * @param holder
	 */
	private void setSubmitButton(final CompositionViewHolder holder) {
		holder.submitBtn.setVisibility(View.INVISIBLE);
		holder.submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					CategoryController.getInstance(type).updateTitle((String) holder.items[0].getText(),holder.editText.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					BasicFunctions.makeToastTake(e.getMessage(), context);
					return;
				}
				holder.items[0].setVisibility(View.VISIBLE);
				holder.editText.getLayoutParams().height = BasicFunctions.dpToPx(20, context);
				holder.editText.setVisibility(View.VISIBLE);
				holder.editBtn.setVisibility(View.VISIBLE);
				holder.clearBtn.setVisibility(View.INVISIBLE);
				holder.submitBtn.setVisibility(View.INVISIBLE);
			}
		});
		if (canEdit) {
			holder.editBtn.setVisibility(View.VISIBLE);

		} else {
			holder.editBtn.setVisibility(View.GONE);
		}
		/**
		 * Edit Btn
		 */
		holder.editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.editText.setVisibility(View.VISIBLE);
				holder.items[0].setVisibility(View.INVISIBLE);
				holder.editText.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
				
				holder.editText.setText(holder.items[0].getText());
				holder.editText.requestFocus();
				holder.editText.setSelection(holder.editText.getText().length());
				holder.clearBtn.setVisibility(View.VISIBLE);
				holder.submitBtn.setVisibility(View.VISIBLE);
				holder.editBtn.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * @param holder
	 */
	private void setMenuLayout(final CompositionViewHolder holder) {
		holder.menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(MusicPlayerMainActivity
						.getActivity(), holder.menu);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(
						R.menu.composition_list_item_menu, popup.getMenu());
				if (!canDelete) {
					popup.getMenu().findItem(R.id.composition_list_item_delete)
							.setVisible(false);
				}
				// registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						String cat = (String) holder.items[0].getText();
						// TODO Auto-generated method stub
						switch (arg0.getItemId()) {
						case R.id.composition_list_item_shows:

							ListItemsInCompositionListFragment.createInstance(
									getSongsFromCat(cat), cat, type).show(
									MusicPlayerMainActivity.getActivity()
											.getSupportFragmentManager(),
									"Show songs in cate");
							break;
						case R.id.composition_list_item_delete:
							CategoryController.getInstance(type)
									.removeCategory(cat);
						default:
							break;
						}

						return false;
					}
				});

				popup.show(); // showing popup menu
			}
		});
	}

	private String[] getSongsTitleFromCat(String catString) {
		// TODO Auto-generated method stub
		String[] temp = catString.split(String.valueOf('\1'));
		String[] result = new String[holder.items.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = "";
			if (i < temp.length) {
				result[i] = temp[i];
			}
		}
		return result;
	}

	/**
	 * get only title of category
	 */
	@Override
	public String getItem(int position) {
		String[] temp = categories.get(position).split(String.valueOf('\1'));
		return temp[0];
	}

	/**
	 * get title and content of a category
	 */
	public String getWholeItem(int position) throws IndexOutOfBoundsException {
		return categories.get(position);
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	public void update() {
		categories = getCategories();
		this.notifyDataSetChanged();
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public int getAdapterType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	private class getSongFromCategoryBackground extends AsyncTask<String, String, ArrayList<Song>>{

		int type;
		int position;
		String category;
		
		
		public getSongFromCategoryBackground(String cat, int type) {
			// TODO Auto-generated constructor stub
			this.type = type;
			this.category = cat;
		}
		
		@Override
		protected ArrayList<Song> doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			ArrayList<Song> songs = new ArrayList<Song>();
			try {
				songs = CategoryController.getInstance(type).getSongFromCategory(category);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return songs;
			}
			
			return songs;
		}
		
		
		
	}


}
