package ngo.music.player.adapters;

import android.content.Context;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.CategoryManager;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.R;
import ngo.music.player.ViewHolder.CompositionViewHolder;
import ngo.music.player.boundary.MusicPlayerMainActivity;
import ngo.music.player.boundary.fragment.abstracts.ListItemsInCompositionListFragment;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;

public abstract class CategoryListAdapter extends ArrayAdapter<String>
		implements Constants.Models {
	private static final int NUM_ITEM_IN_ONE_CATEGORY = 5;
	protected ArrayList<String> categories;
	Context context;
	int resource;
	CompositionViewHolder holder = null;
	private View v;
	private int type = -1;
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
	 *
	 * @return if the category can be editted or not
	 */
	protected abstract boolean setCanEdit();

	/**
	 * @return if the category item can be delete or not
	 */
	protected abstract boolean setCanDelete();

	/**
	 * @return type of Category in Constant.Category
	 */
	protected abstract int setType();

	/**
	 * get categories (songs and title) in list of item sets
	 * 
	 * @return categories
	 */
	protected ArrayList<String> getCategories() {
		return new ArrayList<String>();
//		return CategoryController.getInstance(type).getCategoryString();
	}

	/**
	 * get item from specific categories
	 * 
	 * @param cat
	 *            : category
	 * @return list of songs
	 */
	protected Song[] getSongsFromCat(String cat) {
		System.out.println ("GET SONGS FROM CATE");

//		return new ArrayList<Song>();
		JSONObject[] array = ((CategoryManager) ModelManager.getInstance(type)).getSongsFromCategory(cat);
		Song[] result = new Song[array.length];
		for (int i = 0; i < result.length; i++) {
			try {
				result[i] = (Song) ModelManager.getInstance(OFFLINE).get(array[i].getString("id"));
			} catch (JSONException e) {
				continue;
			}
		}
		return result;
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
	 * @param
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
				holder.editText.getLayoutParams().height = Helper.dpToPx(20, context);
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
//					ModelManager.getInstance(type).updateTitle((String) holder.items[0].getText(),holder.editText.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Helper.makeToastTake(e.getMessage(), context);
					return;
				}
				holder.items[0].setVisibility(View.VISIBLE);
				holder.editText.getLayoutParams().height = Helper.dpToPx(20, context);
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
//							CategoryController.getInstance(type)
//									.removeCategory(cat);
							break;
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
	



}
